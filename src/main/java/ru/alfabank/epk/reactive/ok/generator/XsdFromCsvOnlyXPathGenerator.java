package ru.alfabank.epk.reactive.ok.generator;

import ru.alfabank.epk.reactive.ui.utils.UiUtils;
import ru.alfabank.epk.reactive.ui.xpath_comparator.ArrayNodeOnlyXPath;
import ru.alfabank.epk.reactive.ui.xpath_comparator.TreeTableGeneratorOnlyXPath;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class XsdFromCsvOnlyXPathGenerator implements XsdGenerator {

    private static final String LINE_SEPARATOR = System.lineSeparator();

    public Path xsdGenerate(Path csvPath, String namespace, String xmlnsUrl, Map<String, String> importNamespacesAndUrls) {
        TreeTableGeneratorOnlyXPath treeTableGenerator = new TreeTableGeneratorOnlyXPath();
        StringBuilder xsdBuilder = new StringBuilder();

        List<ArrayNodeOnlyXPath> xsdRoots = treeTableGenerator.generateNodesTree(csvPath);

        String xsdBegin = """
                <?xml version="1.0" encoding="UTF-8" standalone="no"?>
                <%s:schema xmlns:%s="http://www.w3.org/2001/XMLSchema" xmlns="%s" elementFormDefault="unqualified" targetNamespace="%s">
                
                """.formatted(namespace, namespace, xmlnsUrl, xmlnsUrl);
        xsdBuilder.append(xsdBegin);

        for (Map.Entry<String, String> importNamespaceAndUrl : importNamespacesAndUrls.entrySet()) {
            String importStr = """
                        <%s:import namespace="%s" schemaLocation="%s"/>
                    """.formatted(namespace, importNamespaceAndUrl.getKey(), importNamespaceAndUrl.getValue());
            xsdBuilder.append(importStr);
        }
        xsdBuilder.append(LINE_SEPARATOR);

        for (ArrayNodeOnlyXPath xsdRoot : xsdRoots) {
            xsdBuilder.append("    ").append("<")
                    .append(namespace)
                    .append(":element name=\"")
                    .append(generateNameAttr(xsdRoot))
                    .append("\" type=\"")
                    .append(generateTypeAttr(xsdRoot))
                    .append("\"/>").append(LINE_SEPARATOR);

            generateComplexTypeXsd(namespace, xsdRoot, xsdBuilder);
        }

        xsdBuilder.append("</").append(namespace).append(":schema>").append(LINE_SEPARATOR);

        String fileName = UiUtils.getFileName(csvPath);

        // и после вывод в файл
        return UiUtils.exportResultToFile("xsd__" + fileName, "xsd", xsdBuilder.toString());
    }

    private static void generateComplexTypeXsd(String namespace, ArrayNodeOnlyXPath parent, StringBuilder xsdBuilder) {
        if (parent.getChildCount() > 0) {
            List<ArrayNodeOnlyXPath> children = new ArrayList<>();
            for (int i = 0; i < parent.getChildCount(); i++) {
                children.add((ArrayNodeOnlyXPath) parent.getChildAt(i));
            }
            xsdBuilder.append(LINE_SEPARATOR);
            String complexTypeBegin = """
                    <%s:complexType name="%s">
                        <%s:sequence>
                """.formatted(namespace, generateTypeAttr(parent), namespace);
            xsdBuilder.append(complexTypeBegin);

            for (ArrayNodeOnlyXPath child : children) {

                String childXml = """
                            <%s:element name="%s" type="%s"/>
                """.formatted(
                        namespace,
                        generateNameAttr(child),
                        generateTypeAttr(child)
                );
                xsdBuilder.append(childXml);
            }

            String complexTypeEnd = """
                        </%s:sequence>
                    </%s:complexType>
                """.formatted(namespace, namespace);
            xsdBuilder.append(complexTypeEnd);

            for (ArrayNodeOnlyXPath child : children) {
                generateComplexTypeXsd(namespace, child, xsdBuilder);
            }
        }
    }


    private static String generateNameAttr(ArrayNodeOnlyXPath node) {
        return node.lastXPathElement();
    }

    private static String generateTypeAttr(ArrayNodeOnlyXPath node) {
        return node.getXPath().length == 1 ? node.getXPath()[0] :
                Arrays.stream(node.getXPath()).map(str ->
                    Character.toUpperCase(str.charAt(0)) + str.substring(1)
                ).collect(Collectors.joining());
    }

}

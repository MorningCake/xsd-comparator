package ru.alfabank.epk.reactive.ok.generator;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import ru.alfabank.epk.reactive.ui.utils.UiUtils;
import ru.alfabank.epk.reactive.ui.xsd_comparator.ArrayNode;
import ru.alfabank.epk.reactive.ui.xsd_comparator.TreeTableGenerator;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class XsdFromCsvGenerator implements XsdGenerator {

    private static final String LINE_SEPARATOR = System.lineSeparator();

    public Path xsdGenerate(Path csvPath, String namespace, String xmlnsUrl, Map<String, String> importNamespacesAndUrls) {
        TreeTableGenerator treeTableGenerator = new TreeTableGenerator();
        StringBuilder xsdBuilder = new StringBuilder();
        List<ArrayNode> xsdRoots = treeTableGenerator.generateNodesTree(csvPath);

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

        for (ArrayNode xsdRoot : xsdRoots) {
            xsdBuilder.append("    ").append("<")
                    .append(namespace)
                    .append(":element name=\"")
                    .append(xsdRoot.getValueAt(0))
                    .append("\" type=\"")
                    .append(generateTypeAttr((String) xsdRoot.getValueAt(1)))
                    .append("\"/>").append(LINE_SEPARATOR);

            generateComplexTypeXsd(namespace, xsdRoot, xsdBuilder);
        }

        xsdBuilder.append("</").append(namespace).append(":schema>").append(LINE_SEPARATOR);

        String fileName = UiUtils.getFileName(csvPath);

        // и после вывод в файл
        return UiUtils.exportResultToFile("xsd__" + fileName, "xsd", xsdBuilder.toString());
    }

    private static void generateComplexTypeXsd(String namespace, ArrayNode parent, StringBuilder xsdBuilder) {
        if (parent.getChildCount() > 0) {
            List<ArrayNode> children = new ArrayList<>();
            for (int i = 0; i < parent.getChildCount(); i++) {
                children.add((ArrayNode) parent.getChildAt(i));
            }
            xsdBuilder.append(LINE_SEPARATOR);
            String complexTypeBegin = """
                    <%s:complexType name="%s">
                        <%s:sequence>
                """.formatted(namespace, parent.getValueAt(1), namespace);
            xsdBuilder.append(complexTypeBegin);

            for (ArrayNode child : children) {

                String childXml = """
                            <%s:element name="%s" type="%s"%s%s/>
                """.formatted(
                        namespace,
                        child.getValueAt(0),
                        generateTypeAttr((String) child.getValueAt(1)),
                        generateOccursAttr((String) child.getValueAt(3), AttrType.MIN),
                        generateOccursAttr((String) child.getValueAt(4), AttrType.MAX)
                );
                xsdBuilder.append(childXml);
            }

            String complexTypeEnd = """
                        </%s:sequence>
                    </%s:complexType>
                """.formatted(namespace, namespace);
            xsdBuilder.append(complexTypeEnd);

            for (ArrayNode child : children) {
                generateComplexTypeXsd(namespace, child, xsdBuilder);
            }
        }
    }

    private static String generateTypeAttr(@Nullable String type) {
        return (Strings.isBlank(type)) ? "" : type;
    }

    private static String generateOccursAttr(@Nullable String occurs, AttrType type) {
        return (Strings.isBlank(occurs) || occurs.equals("1"))
                ? ""  // 1 - дефолтное значение, можно не указывать
                : type.getPattern().formatted(occurs);
    }

    @RequiredArgsConstructor
    private enum AttrType {
        MIN(" minOccurs=\"%s\""),
        MAX(" maxOccurs=\"%s\"");

        @Getter
        private final String pattern;
    }

}

package ru.alfabank.epk.reactive.ok.generator;

import org.jdesktop.swingx.JXTreeTable;
import ru.alfabank.epk.reactive.ui.utils.UiUtils;
import ru.alfabank.epk.reactive.ui.xsd_comparator.ArrayNode;
import ru.alfabank.epk.reactive.ui.xsd_comparator.TreeTableGenerator;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class XsdFromCsvGenerator {

    private static final String LINE_SEPARATOR = System.lineSeparator();

    public Path xsdGenerate(Path csvPath, String xmlnsUrl) {
        // TODO проверка что дерево строится - вынести в UI
        TreeTableGenerator treeTableGenerator = new TreeTableGenerator();
        StringBuilder xsdBuilder = new StringBuilder();

        List<ArrayNode> xsdRoots = treeTableGenerator.generateNodesTree(csvPath);
        /*

        1 - корень
    <xs:element name="GetListRequest" type="GetListRequest"/>

        2 - промежуточные и окончательные элементы
    <xs:complexType name="GetRequest">
        <xs:sequence>
            <xs:element name="inParams" type="EPKSystemTypeRequest"/>
            <xs:element name="data" type="EPKSubjectInfoGetData"/>
            <xs:element name="TechnicalManager" type="EPKSubjectInfoGetInParmsSubjectTechnicalManager" minOccurs="0" maxOccurs="1"/>
        </xs:sequence>
    </xs:complexType>

        */

        // TODO запилить форму UI (одиночная с чеком дерева и выводом xsd в файл)

        String xsdBegin = """
                <?xml version="1.0" encoding="UTF-8" standalone="no"?>
                <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns="%s"
                           elementFormDefault="unqualified" targetNamespace="%s">
                
                """.formatted(xmlnsUrl, xmlnsUrl);

        xsdBuilder.append(xsdBegin);

        for (ArrayNode xsdRoot : xsdRoots) {
            xsdBuilder.append("    ").append("<xs:element name=\"").append(xsdRoot.getValueAt(0))
                    .append(" type=").append(xsdRoot.getValueAt(1)).append("\"/>").append(LINE_SEPARATOR);

            generateComplexTypeXsd(xsdRoot, xsdBuilder);
        }

        xsdBuilder.append("</xs:schema>").append(LINE_SEPARATOR);

        String fileName = UiUtils.getFileName(csvPath);

        // и после вывод в файл
        return UiUtils.exportResultToFile("xsd__" + fileName, "xsd", xsdBuilder.toString());
    }

    private static void generateComplexTypeXsd(ArrayNode parent, StringBuilder xsdBuilder) {
        if (parent.getChildCount() > 0) {
            List<ArrayNode> children = new ArrayList<>();
            for (int i = 0; i < parent.getChildCount(); i++) {
                children.add((ArrayNode) parent.getChildAt(i));
            }
            xsdBuilder.append(LINE_SEPARATOR);
            String complexTypeBegin = """
                    <xs:complexType name="%s">
                        <xs:sequence>
                """.formatted(parent.getValueAt(1));
            xsdBuilder.append(complexTypeBegin);

            for (ArrayNode child : children) {
                String childXml = """
                            <xs:element name="%s" type="%s" minOccurs="%s" maxOccurs="%s"/>
                """.formatted(child.getValueAt(0), child.getValueAt(1),
                        child.getValueAt(3), child.getValueAt(4));
                xsdBuilder.append(childXml);
            }

            String complexTypeEnd = """
                        </xs:sequence>
                    </xs:complexType>
                """;
            xsdBuilder.append(complexTypeEnd);

            for (ArrayNode child : children) {
                generateComplexTypeXsd(child, xsdBuilder);
            }
        }
    }

}

package ru.alfabank.epk.reactive;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XSDToXPathConverter {

    public static void main(String[] args) throws Exception {
        // Парсинг XSD файла
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse("src/main/resources/UWSConsumerSubjectInfoGetInOutParms28.xsd");
        doc.normalizeDocument();

        // Поиск всех корневых элементов с атрибутом "name"
        NodeList elementsWithNameAttribute = doc.getElementsByTagName("xs:element");
        for (int i = 0; i < elementsWithNameAttribute.getLength(); i++) {
            Node element = elementsWithNameAttribute.item(i);
            Node nameAttr = element.getAttributes().getNamedItem("name");
            if (nameAttr != null) {
                // Начинаем обработку нового корня
                processNode(element, "");
            }
        }
        System.out.println("ALL");
    }

    private static void processNode(Node node, String parentPath) {
        // Получаем имя узла из атрибута "name"
        Node nameAttr = node.getAttributes().getNamedItem("name");
        String nodeName = "";
        if (nameAttr != null) {
            nodeName = nameAttr.getNodeValue();
        }

        // Формируем полный путь
        String fullPath = !parentPath.isEmpty() ? parentPath + "/" + nodeName : nodeName;

        // Выводим XPath путь, если он не пустой
        if (!fullPath.isEmpty()) {
            System.out.println(fullPath);
        }

        // Обрабатываем атрибуты узла
        NamedNodeMap attributes = node.getAttributes();
        if (attributes != null && attributes.getLength() > 0) {
            for (int i = 0; i < attributes.getLength(); i++) {
                Node attr = attributes.item(i);
                if (!attr.getNodeName().equals("name")) {
                    String attributeFullPath = fullPath + "@" + attr.getNodeName();
                    System.out.println(attributeFullPath);
                }
            }
        }

        // Продолжаем обход дочерних узлов
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                processNode(child, fullPath);
            }
        }
    }
}
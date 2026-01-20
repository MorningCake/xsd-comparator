package ru.alfabank.epk.reactive;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


public class XPathExporter {

    public static final String ROOT = "root";

    /** Имя файла корневой xsd-схемы в ресурсах, например client_app.xsd */
    private final String xsdName;
    /** Наименования корневых элементов для построения дерева (мб можно вычислить, хз) */
    private final Set<String> xsdRootsNames;

    /** Ключ - имя локального корня, например TARLSubjectInfoGetInParms для TARLSubjectInfoGetInParms/subjectSet */
    @Setter
    private Map<String, List<XPathElement>> nodeElements = new HashMap<>();


    public XPathExporter(String xsdName, Set<String> xsdRootsNames) {
        this.xsdName = xsdName;
        if (xsdRootsNames == null || xsdRootsNames.isEmpty())
            throw new RuntimeException("xsdRootsNames д.б. заполнен!");
        this.xsdRootsNames = xsdRootsNames;
    }

    /**
     * Генерация csv из xsd-схемы
     */
    public void xsdToCsv() throws IOException, SAXException, ParserConfigurationException {
        Path xsdPath = Path.of("src/main/resources/" + xsdName).toAbsolutePath();
        if (!Files.isReadable(xsdPath)) {
            throw new IOException("File is not readable: " + xsdPath);
        }
        // Парсинг XSD файла
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xsdPath.toString());

        // Начало рекурсивного обхода, формирование относительных xPath
        processNode(doc.getDocumentElement(), "");
        // Коррекция и выделение полных xPath
        xPathCorrection();
    }

    private void processNode(Node node, String parentPath) {
        // Получаем имя узла из атрибута "name"
        Node nameAttr = node.getAttributes().getNamedItem("name");
        Node typeAttr = node.getAttributes().getNamedItem("type");

        // если есть наследники схемы xs:element у которых тип xs:complexType - то это root (учесть имя)
        // если наследники схемы сразу xs:complexType - то они все root
        String fullPath;
        if (nameAttr != null) {
//            boolean isRoot = node.getParentNode().toString().contains(":schema");

            String nodeName = nameAttr.getNodeValue();
            boolean isRoot = xsdRootsNames.contains(nodeName);
            String nodeType = typeAttr != null ? typeAttr.getNodeValue() :
                    (isRoot ? "" : null);
            String nodeStr = (nodeType == null || nodeType.isEmpty()) ? nodeName : nodeName + "(" + nodeType + ")";
            fullPath = parentPath.isEmpty() ? nodeStr : parentPath + "/" + nodeStr;

            // Выводим XPath путь, если он не пустой и есть тип
            if (!fullPath.isEmpty() && nodeType != null) {
                // todo убрать после отладки
                System.out.println(fullPath);

                XPathElement xPathElement = XPathElement.builder()
                        .type(nodeType)
                        .name(nodeName)
                        .relPath(fullPath)
                        .isAbsRoot(isRoot)
                        .build();

                // вычисление ключа из fullPath
                String key = isRoot ? ROOT : fullPath.split("/")[0];

                if (nodeElements.containsKey(key)) {
                    nodeElements.get(key).add(xPathElement);
                } else {
                    List<XPathElement> elements = new ArrayList<>();
                    elements.add(xPathElement);
                    nodeElements.put(key, elements);
                }
            }
        } else { // не выводим узел, но оставляем для сохранения относительных путей
            fullPath = parentPath;
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

    private void xPathCorrection() {
        Optional<List<XPathElement>> rootsOptional = nodeElements.entrySet().stream()
                .filter(entry -> entry.getKey().equals(ROOT))
                .map(Map.Entry::getValue)
                .findFirst();

        if (rootsOptional.isEmpty())
            throw new RuntimeException("В схеме " + xsdName + " не найдены указанные корни! Генерация остановлена!");

        List<XPathElement> roots = rootsOptional.get();
        for (XPathElement root : roots) {
            root.setFullXpath(""); //todo
            elementsCorrection(root);
        }
    }

    private void elementsCorrection(XPathElement parent) {
        if (!nodeElements.containsKey(parent.getName())) {
            parent.setLast(true);
        } else {
            List<XPathElement> elements = nodeElements.get(parent.getName());
            for (XPathElement el : elements) {

            }
        }
    }

    private void generateCsv(String csvName) {

    }
}

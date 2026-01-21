package ru.alfabank.epk.reactive.ok;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


public class SaxXsdReader {

    public static final String PATH = "src/main/resources";

    public Path readXsd(String fileName, boolean isTreeFile, boolean isCsvFile, boolean isTreeLogs, boolean isCsvLogs,
                        boolean isHeader, String resultName)
            throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = spf.newSAXParser();
        XMLReader reader = sp.getXMLReader();
        SchemaSaxHandler schemaSaxHandler = new SchemaSaxHandler(isTreeFile, isCsvFile, isTreeLogs, isCsvLogs, isHeader, resultName);
        reader.setContentHandler(schemaSaxHandler);
        reader.parse(new InputSource(new FileInputStream(new File(PATH, fileName))));
        return schemaSaxHandler.getGeneratedCsvPath();
    }
}

@Getter
@Setter
class SchemaElement {

    private String name;
    private String type;
    private String minOccurs;
    private String maxOccurs;
    private List<SchemaElement> children;
    private Map<String, String> attributes = new HashMap<>();

    // getters and setters
}

@Getter
@Setter
class SchemaComplexType {

    private String name;
    private List<SchemaElement> children;
    private Map<String, String> attributes = new HashMap<>();

    public void addAttribute(String name, String type) {
        attributes.put(name, type);
    }
}

@RequiredArgsConstructor
@Getter @Setter
class SchemaSaxHandler extends DefaultHandler {

    private final boolean isTreeFile;
    private final boolean isCsvFile;
    private final boolean isTreeLogs;
    private final boolean isCsvLogs;
    private final boolean isHeader;
    private final String resultName;

    @Getter
    private Path generatedCsvPath;

    // temporary - always null when tag closes
    private String currentSimpleTypeName;
    private String currentSimpleTypeBaseType;
    private SchemaElement currentElement;
    private SchemaComplexType currentComplexType;
    private List<SchemaElement> currentSequence;

    private List<String> csvLines = new ArrayList<>();
    private List<String> treeLines = new ArrayList<>();

    // cumulative - will use the data when XML finishes
    private Map<String, String> simpleTypes = new HashMap<>();
    private Map<String, SchemaComplexType> complexTypes = new HashMap<>();
    private List<SchemaElement> rootElements = new ArrayList<>();

    @Override
    public void endDocument() throws SAXException {
        Path resultTreePath = generateResultPath(resultName, "txt");
        Path resultCsvPath = generateResultPath(resultName, "csv");
        rootElements.forEach(rootElement -> {
            makeTree(rootElement);

            if (isTreeFile || isTreeLogs) {


                if (isTreeLogs) printTree(rootElement, "");
                if (isTreeFile) {
                    exportResultToFile(resultTreePath, treeLines);
                    treeLines = new ArrayList<>();
                }
                System.out.println(System.lineSeparator());
            }

            if (isCsvLogs || isCsvFile) {
                if (isCsvLogs && isHeader) System.out.println(getCsvHeader());

//                if (isCsvFile && isHeader) csvLines.add(getCsvHeader());  //todo хэдеры в файл не катит ставить
                generateXPathCsv(rootElement, "", isCsvFile, isCsvLogs);
                if (isCsvFile) {
                    exportResultToFile(resultCsvPath, csvLines);
                    csvLines = new ArrayList<>();
                }
            }
        });
    }

    private static Path generateResultPath(String name, String format) {
        return Path.of("src/main/resources/generated/" + LocalDateTime.now() + "__" + name + "." + format).toAbsolutePath();
    }

    public void makeTree(SchemaElement element) {
        SchemaComplexType type = complexTypes.get(element.getType());
        if (type != null) {
            List<SchemaElement> children = type.getChildren();
            element.setChildren(children);

            for (SchemaElement child : children) {
                makeTree(child);
            }
            element.setAttributes(type.getAttributes());
        } else {
            if (simpleTypes.containsKey(element.getType())) {
                element.setType(simpleTypes.get(element.getType()));
            }
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        if (qName.contains("simpleType")) {
            currentSimpleTypeName = atts.getValue("name");
        }
        if (qName.contains("restriction")) {
            currentSimpleTypeBaseType = atts.getValue("base");
        }
        if (qName.contains("complexType")) {
            currentComplexType = new SchemaComplexType();
            currentComplexType.setName(atts.getValue("name"));
        }
        if (qName.contains("sequence")) {
            currentSequence = new ArrayList<>();
        }
        if (qName.contains("element")) {
            currentElement = new SchemaElement();
            currentElement.setName(atts.getValue("name"));

            String[] typeChunks = atts.getValue("type").split(":");
            currentElement.setType(typeChunks.length == 2 ? typeChunks[1] : typeChunks[0]);

            currentElement.setMinOccurs(atts.getValue("minOccurs"));
            currentElement.setMaxOccurs(atts.getValue("maxOccurs"));
            if (currentSequence != null) {
                currentSequence.add(currentElement);
            } else {
                rootElements.add(currentElement);
            }
        }
        if (qName.contains("attribute")) {
            currentComplexType.addAttribute(atts.getValue("name"), atts.getValue("type"));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.contains("simpleType")) {
            simpleTypes.put(currentSimpleTypeName, currentSimpleTypeBaseType);
            currentSimpleTypeName = null;
            currentSimpleTypeBaseType = null;
        }
        if (qName.contains("sequence")) {
            if (currentComplexType != null) {
                currentComplexType.setChildren(currentSequence);
            }
            currentSequence = null;
        }
        if (qName.contains("complexType")) {
            complexTypes.put(Objects.requireNonNull(currentComplexType).getName(), currentComplexType);
            currentComplexType = null;
        }

    }

    private void exportResultToFile(Path resultPath, List<String> lines) {
        String fileStr = lines.stream().collect(Collectors.joining(System.lineSeparator()));
        try {
            if (Files.exists(resultPath)) {
                Files.writeString(resultPath, fileStr, StandardOpenOption.APPEND);
            } else {
                Files.writeString(resultPath, fileStr);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        generatedCsvPath = resultPath;
    }

    private String getCsvHeader() {
        return "name,type,xPath,minOccurs,maxOccurs";
    }

    private String getCsvString(String newParentPath, SchemaElement element) {
        return element.getName() + "," +
        element.getType() + "," +
        newParentPath + "," +
        defaultOrCurrentOccurs(element.getMinOccurs()) + "," +
        defaultOrCurrentOccurs(element.getMaxOccurs());
    }

    private String defaultOrCurrentOccurs(String occurs) {
        return occurs == null ? "1" : occurs;
    }

    private void generateXPathCsv(SchemaElement element, String parentPath, boolean isCsvFile, boolean isCsvLogs) {
        String newParentPath = parentPath + "/" + element.getName();
        String csvLine = getCsvString(newParentPath, element);

        if (isCsvLogs) System.out.println(csvLine);
        if (isCsvFile) csvLines.add(csvLine);

        List<SchemaElement> children = element.getChildren();
        if (children != null) {
            for (SchemaElement child : children) {
                generateXPathCsv(child, newParentPath, isCsvFile, isCsvLogs);
            }
        }
    }


    private void printTree(SchemaElement element, String indent) {
        System.out.println(getTreeLine(element, indent));
        treeLines.add(getTreeLine(element, indent));

// TODO пока не нужны

//        Map<String, String> attributes = element.getAttributes();
//        if (attributes != null) {
//            for (Map.Entry<String, String> entry : attributes.entrySet()) {
//                System.out.println("    @" + entry.getKey() + " : " + simpleTypes.get(entry.getValue()));
//            }
//        }

        List<SchemaElement> children = element.getChildren();
        if (children != null) {
            for (SchemaElement child : children) {
                printTree(child, indent + "    ");
            }
        }
    }

    private static String getTreeLine(SchemaElement element, String indent) {
        return indent + element.getName() + " : " + element.getType();
    }

}

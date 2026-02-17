package ru.alfabank.epk.reactive.ui.xsd_comparator;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

import javax.swing.table.DefaultTableCellRenderer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public final class TreeTableGenerator {

    public static JXTreeTable generate(Path xsdCsvPath, Path compared, TreeType type) {

        List<String> comparedXsds;
        try {
            comparedXsds = Files.readAllLines(compared);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении файла " + compared);
        }

        Set<String> onlyXsdXPath = comparedXsds.stream()
                .map(str -> str.split(","))
                .filter(
                        split -> (type == TreeType.LEFT)
                                ? split[5].equals("+") && split[6].equals("-")
                                : split[5].equals("-") && split[6].equals("+")
                ).map(split -> split[2])
                .collect(Collectors.toSet());

        List<String> xsdCsv;
        try {
            xsdCsv = Files.readAllLines(xsdCsvPath);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении файла " + xsdCsvPath);
        }

        Map<String, List<ArrayNode>> parentXPathsAndArrayNodesList = xsdCsv.stream()
                .map(str -> (str + ", ").split(",")) // добавить 6ой элемент для заметок
                .map(ArrayNode::new)
                .collect(Collectors.groupingBy(ArrayNode::getParentXPath, toList()));

        // общий корень swing дерева
        ArrayNode treeRoot = new ArrayNode(new Object[]{"root", "", "", "", "", ""});

        // найти корни xsd
        List<ArrayNode> xsdRoots = parentXPathsAndArrayNodesList.values().stream()
                .flatMap(Collection::stream)
                .filter(ArrayNode::isRoot)
                .toList();

        // для каждого корня запустить рекурсивный метод генерации дерева
        for (ArrayNode root : xsdRoots) {
            treeRoot.add(root);
            addChild(root, parentXPathsAndArrayNodesList);
        }
        return generateCustomizedTreeTableFromTreeRoot(treeRoot, onlyXsdXPath, type);
    }

    private static void addChild(ArrayNode parent, Map<String, List<ArrayNode>> parentXPathsAndArrayNodesList) {
        if (parentXPathsAndArrayNodesList.containsKey(parent.getXPathString())) {
            List<ArrayNode> children = parentXPathsAndArrayNodesList.get(parent.getXPathString());
            for (ArrayNode child : children) {
                parent.add(child);
                addChild(child, parentXPathsAndArrayNodesList);
            }
        }
    }

    public static JXTreeTable initGenerate(String initName) {
        // Создаем модель данных для дерева
        ArrayNode root = new ArrayNode(new Object[]{"root", "", "", "", "", ""});

        ArrayNode arrayNode = new ArrayNode(new Object[]{initName, "", "", "", "", ""});
        root.add(arrayNode);

        return generateCustomizedTreeTableFromTreeRoot(root, Set.of(), TreeType.LEFT);
    }

    private static JXTreeTable generateCustomizedTreeTableFromTreeRoot(
            ArrayNode treeRoot, Set<String> onlyXsdXPath, TreeType treeType
    ) {
        JXTreeTable treeTable = new JXTreeTable(new DefaultTreeTableModel(
                treeRoot,
                new ArrayList<>(List.of("name", "type", "xPath", "minOccurs", "maxOccurs", "примеч.")))
        );

        treeTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer());
        treeTable.setTreeCellRenderer(new TreeTableCustomizer(onlyXsdXPath, treeType));
        treeTable.expandAll();
        return treeTable;
    }

    public enum TreeType {LEFT, RIGHT}
}

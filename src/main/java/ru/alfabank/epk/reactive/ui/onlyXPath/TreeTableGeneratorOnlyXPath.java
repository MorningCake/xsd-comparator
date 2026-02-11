package ru.alfabank.epk.reactive.ui.onlyXPath;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import ru.alfabank.epk.reactive.ui.ArrayNode;
import ru.alfabank.epk.reactive.ui.TreeTableCustomizer;

import javax.swing.table.DefaultTableCellRenderer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public final class TreeTableGeneratorOnlyXPath {

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
                                ? split[1].equals("+") && split[2].equals("-")
                                : split[1].equals("-") && split[2].equals("+")
                ).map(split -> split[0])
                .collect(Collectors.toSet());

        List<String> xsdCsv;
        try {
            xsdCsv = Files.readAllLines(xsdCsvPath);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении файла " + xsdCsvPath);
        }

        Map<String, List<ArrayNodeOnlyXPath>> parentXPathsAndArrayNodesList = xsdCsv.stream()
                .map(str -> (str + ", ").split(",")) // добавить 6ой элемент для заметок
                .map(ArrayNodeOnlyXPath::new)
                .collect(Collectors.groupingBy(ArrayNodeOnlyXPath::getParentXPath, toList()));

        // общий корень swing дерева
        ArrayNodeOnlyXPath treeRoot = new ArrayNodeOnlyXPath(new Object[]{"root", ""});

        // найти корни xsd
        List<ArrayNodeOnlyXPath> xsdRoots = parentXPathsAndArrayNodesList.values().stream()
                .flatMap(Collection::stream)
                .filter(ArrayNodeOnlyXPath::isRoot)
                .toList();

        // для каждого корня запустить рекурсивный метод генерации дерева
        for (ArrayNodeOnlyXPath root : xsdRoots) {
            treeRoot.add(root);
            addChild(root, parentXPathsAndArrayNodesList);
        }
        return generateCustomizedTreeTableFromTreeRoot(treeRoot, onlyXsdXPath, type);
    }

    private static void addChild(ArrayNodeOnlyXPath parent, Map<String, List<ArrayNodeOnlyXPath>> parentXPathsAndArrayNodesList) {
        if (parentXPathsAndArrayNodesList.containsKey(parent.getXPathString())) {
            List<ArrayNodeOnlyXPath> children = parentXPathsAndArrayNodesList.get(parent.getXPathString());
            for (ArrayNodeOnlyXPath child : children) {
                parent.add(child);
                addChild(child, parentXPathsAndArrayNodesList);
            }
        }
    }

    public static JXTreeTable initGenerate(String initName) {
        // Создаем модель данных для дерева
        ArrayNodeOnlyXPath root = new ArrayNodeOnlyXPath(new Object[]{"root", ""});

        ArrayNodeOnlyXPath arrayNode = new ArrayNodeOnlyXPath(new Object[]{initName, ""});
        root.add(arrayNode);

        return generateCustomizedTreeTableFromTreeRoot(root, Set.of(), TreeType.LEFT);
    }

    private static JXTreeTable generateCustomizedTreeTableFromTreeRoot(
            ArrayNodeOnlyXPath treeRoot, Set<String> onlyXsdXPath, TreeType treeType
    ) {
        JXTreeTable treeTable = new JXTreeTable(new DefaultTreeTableModel(
                treeRoot,
                new ArrayList<>(List.of("xPath", "примеч.")))
        );

        treeTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer());
        treeTable.setTreeCellRenderer(new TreeTableCustomizerOnlyXPath(onlyXsdXPath, treeType));
        treeTable.expandAll();
        return treeTable;
    }

    public enum TreeType {LEFT, RIGHT}
}

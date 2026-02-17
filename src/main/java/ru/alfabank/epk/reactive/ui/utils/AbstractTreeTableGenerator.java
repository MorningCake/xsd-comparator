package ru.alfabank.epk.reactive.ui.utils;

import lombok.RequiredArgsConstructor;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import ru.alfabank.epk.reactive.ui.utils.factory.ArrayNodeFactory;
import ru.alfabank.epk.reactive.ui.utils.factory.CustomizerFactory;

import javax.swing.table.DefaultTableCellRenderer;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public abstract class AbstractTreeTableGenerator<T extends AbstractArrayNode, C extends AbstractTreeTableCustomizer<?>> {

    private final ArrayNodeFactory<T> arrayNodeFactory;
    private final CustomizerFactory<C> customizerFactory;

    private final int leftFileLineColumn;
    private final int rightFileLineColumn;
    private final int xpathColumn;

    public JXTreeTable generate(Path xsdCsvPath, Path compared, TreeType type) {

        List<String> comparedXsds = UiUtils.readAllLines(compared);

        Set<String> onlyXsdXPath = comparedXsds.stream()
                .map(str -> str.split(","))
                .filter(
                        split -> (type == TreeType.LEFT)
                                ? split[leftFileLineColumn].equals("+") && split[rightFileLineColumn].equals("-")
                                : split[leftFileLineColumn].equals("-") && split[rightFileLineColumn].equals("+")
                ).map(split -> split[xpathColumn])
                .collect(Collectors.toSet());

        List<String> xsdCsv = UiUtils.readAllLines(xsdCsvPath);

        Map<String, List<T>> parentXPathsAndArrayNodesList = xsdCsv.stream()
                .map(str -> (str + ", ").split(",")) // добавить 6ой элемент для заметок
                .map(arrayNodeFactory::create)
                .collect(Collectors.groupingBy(T::getParentXPath, toList()));

        // общий корень swing дерева
        T treeRoot = arrayNodeFactory.createWithOnlyName("root");

        // найти корни xsd
        List<T> xsdRoots = parentXPathsAndArrayNodesList.values().stream()
                .flatMap(Collection::stream)
                .filter(T::isRoot)
                .toList();

        // для каждого корня запустить рекурсивный метод генерации дерева
        for (T root : xsdRoots) {
            treeRoot.add(root);
            addChild(root, parentXPathsAndArrayNodesList);
        }
        return generateCustomizedTreeTableFromTreeRoot(treeRoot, onlyXsdXPath, type);
    }

    private void addChild(T parent, Map<String, List<T>> parentXPathsAndArrayNodesList) {
        if (parentXPathsAndArrayNodesList.containsKey(parent.getXPathString())) {
            List<T> children = parentXPathsAndArrayNodesList.get(parent.getXPathString());
            for (T child : children) {
                parent.add(child);
                addChild(child, parentXPathsAndArrayNodesList);
            }
        }
    }

    public JXTreeTable initGenerate(String initName) {
        // Создаем модель данных для дерева
        T root = arrayNodeFactory.createWithOnlyName("root");

        T arrayNode = arrayNodeFactory.createWithOnlyName(initName);
        root.add(arrayNode);

        return generateCustomizedTreeTableFromTreeRoot(root, Set.of(), TreeType.LEFT);
    }

    private JXTreeTable generateCustomizedTreeTableFromTreeRoot(
            T treeRoot, Set<String> onlyXsdXPath, TreeType treeType
    ) {
        JXTreeTable treeTable = new JXTreeTable(new DefaultTreeTableModel(
                treeRoot,
                new ArrayList<>(arrayNodeFactory.getCsvHeader()))
        );

        treeTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer());
        treeTable.setTreeCellRenderer(customizerFactory.create(onlyXsdXPath, treeType));
        treeTable.expandAll();
        return treeTable;
    }

    public enum TreeType { LEFT, RIGHT }
}

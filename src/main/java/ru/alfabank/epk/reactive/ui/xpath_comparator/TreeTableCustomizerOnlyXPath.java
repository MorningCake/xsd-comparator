package ru.alfabank.epk.reactive.ui.xpath_comparator;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.util.Set;

@RequiredArgsConstructor
@AllArgsConstructor
public class TreeTableCustomizerOnlyXPath extends DefaultTreeCellRenderer {

    private final Set<String> onlyXsdXPath;

    @Setter
    private TreeTableGeneratorOnlyXPath.TreeType treeType = TreeTableGeneratorOnlyXPath.TreeType.LEFT;

    public Component getTreeCellRendererComponent(
            JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus
    ) {
        Component renderComponent = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

        ArrayNodeOnlyXPath node = ((ArrayNodeOnlyXPath) value);
        if (onlyXsdXPath.contains(node.getValueAt(0))) {
            renderComponent.setForeground(treeType == TreeTableGeneratorOnlyXPath.TreeType.LEFT ? Color.MAGENTA : Color.BLUE);
        }
        return renderComponent;
    }
}
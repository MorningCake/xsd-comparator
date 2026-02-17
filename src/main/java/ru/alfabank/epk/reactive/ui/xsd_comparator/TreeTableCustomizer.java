package ru.alfabank.epk.reactive.ui.xsd_comparator;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.util.Set;

@RequiredArgsConstructor
@AllArgsConstructor
public class TreeTableCustomizer extends DefaultTreeCellRenderer {

    private final Set<String> onlyXsdXPath;

    @Setter
    private TreeTableGenerator.TreeType treeType = TreeTableGenerator.TreeType.LEFT;

    public Component getTreeCellRendererComponent(
            JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus
    ) {
        Component renderComponent = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

        ArrayNode node = ((ArrayNode) value);
        if (onlyXsdXPath.contains(node.getValueAt(2))) {
            renderComponent.setForeground(treeType == TreeTableGenerator.TreeType.LEFT ? Color.GREEN : Color.BLUE);
        }
        return renderComponent;
    }
}
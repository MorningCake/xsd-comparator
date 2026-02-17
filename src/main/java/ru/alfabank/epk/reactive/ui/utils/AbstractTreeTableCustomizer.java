package ru.alfabank.epk.reactive.ui.utils;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.util.Set;

@RequiredArgsConstructor
@AllArgsConstructor
@SuppressWarnings("unchecked")
public abstract class AbstractTreeTableCustomizer<T extends AbstractArrayNode> extends DefaultTreeCellRenderer {

    private final Set<String> onlyXsdXPath;
    private final int xPathColumnIndex;

    @Setter
    private AbstractTreeTableGenerator.TreeType treeType = AbstractTreeTableGenerator.TreeType.LEFT;

    public Component getTreeCellRendererComponent(
            JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus
    ) {
        Component renderComponent = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

        T node = ((T) value);
        if (onlyXsdXPath.contains(node.getValueAt(xPathColumnIndex))) {
            renderComponent.setForeground(treeType == AbstractTreeTableGenerator.TreeType.LEFT ? Color.GREEN : Color.BLUE);
        }
        return renderComponent;
    }
}
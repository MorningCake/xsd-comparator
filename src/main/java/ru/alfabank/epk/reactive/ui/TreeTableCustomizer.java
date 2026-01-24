package ru.alfabank.epk.reactive.ui;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.util.Set;

@RequiredArgsConstructor
@AllArgsConstructor
class TreeTableCustomizer extends DefaultTreeCellRenderer {

    private final Set<String> onlyXsdXPath;

    @Setter
    private TreeTableGenerator.TreeType treeType = TreeTableGenerator.TreeType.LEFT;

    public Component getTreeCellRendererComponent(
            JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus
    ) {
//        for (int i = 0; i < tree.getRowCount(); i++) {
//            tree.expandRow(i);
//        }
        Component renderComponent = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

        ArrayNode node = ((ArrayNode) value);
        if (onlyXsdXPath.contains(node.getValueAt(2))) {
            renderComponent.setForeground(treeType == TreeTableGenerator.TreeType.LEFT ? Color.GREEN : Color.BLUE);
        }
        return renderComponent;
    }

//    private void expandAll(JTree tree) {
//        DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
//        Enumeration<TreePath> expandedPaths = tree.getExpandedDescendants(new TreePath(root));
//
//        Enumeration<?> e = root.preorderEnumeration();
//        while (e.hasMoreElements()) {
//            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
//            if (node.isLeaf()) continue;
//            tree.expandPath(new TreePath(node.getPath()));
//        }
//    }
}
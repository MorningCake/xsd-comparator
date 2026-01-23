package ru.alfabank.epk.reactive.ui;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableModel;

import java.util.List;

/**
 * A custom node implementation for the tree table.
 */
public class JXTreeTableNode extends AbstractMutableTreeTableNode {

    public JXTreeTableNode(Object[] data) {
        super(data);
    }

    @Override
    public Object getValueAt(int column) {
        Object[] data = (Object[]) getUserObject();
        if (column >= 0 && column < data.length) {
            return data[column];
        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int column) {
        Object[] data = (Object[]) getUserObject();
        if (column >= 0 && column < data.length) {
            data[column] = aValue;
        }
    }

    @Override
    public int getColumnCount() {
        return ((Object[]) getUserObject()).length;
    }

    @Override
    public boolean isEditable(int column) {
        return true;
    }

    public static JXTreeTable getJXTreeTable(JXTreeTableNode root) {
        // Create the model
        TreeTableModel treeTableModel = new DefaultTreeTableModel(root, List.of("1", "2"));

        // Create the JXTreeTable
        return new JXTreeTable(treeTableModel);
    }
}


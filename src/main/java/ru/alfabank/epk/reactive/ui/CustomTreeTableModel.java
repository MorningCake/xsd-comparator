package ru.alfabank.epk.reactive.ui;


import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import org.jdesktop.swingx.treetable.MutableTreeTableNode;

public class CustomTreeTableModel extends AbstractTreeTableModel {

    private final String[] columnNames = {"Имя узла", "Данные"};

    public CustomTreeTableModel(MutableTreeTableNode root) {
        super(root);
    }

    @Override
    public Object getValueAt(Object node, int column) {
        return ((MutableTreeTableNode) node).getValueAt(column);
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Имя узла";
            case 1:
                return "Данные";
            default:
                return "";
        }
    }

    @Override
    public Class<?> getColumnClass(int column) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(Object node, int column) {
        return true;
    }

    @Override
    public Object getChild(Object parent, int index) {
        return null;
    }

    @Override
    public int getChildCount(Object parent) {
        return 0;
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        return 0;
    }
}


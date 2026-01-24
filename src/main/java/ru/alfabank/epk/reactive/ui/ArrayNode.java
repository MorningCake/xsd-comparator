package ru.alfabank.epk.reactive.ui;

import org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode;

public class ArrayNode extends AbstractMutableTreeTableNode {

    public ArrayNode(Object[] data) {
        super(data);
    }

    @Override
    public Object getValueAt(int column) {
        return getUserObject()[column];
    }

    @Override
    public void setValueAt(Object aValue, int column) {
        getUserObject()[column] = aValue;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object[] getUserObject() {
        return (Object[]) super.getUserObject();
    }

    @Override
    public boolean isEditable(int column) {
        return false;
    }

    @Override
    public String toString() {
        return getValueAt(0).toString();
    }


}

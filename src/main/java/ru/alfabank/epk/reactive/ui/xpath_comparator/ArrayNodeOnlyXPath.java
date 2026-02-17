package ru.alfabank.epk.reactive.ui.xpath_comparator;

import lombok.Getter;
import org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode;

import java.util.Arrays;

public class ArrayNodeOnlyXPath extends AbstractMutableTreeTableNode {

    @Getter
    private String[] xPath;

    /**
     * Нода TreeTable. Столбцы
     * 0 - xPath
     * 1 - примеч.
     */
    public ArrayNodeOnlyXPath(Object[] data) {
        super(data);
        if (data.length != 2) throw new RuntimeException("Incorrect data: " + Arrays.toString(data));
        try {
            xPath = data[0].toString().substring(1).split("/");
        } catch (StringIndexOutOfBoundsException ex) {
            xPath = new String[] {"TreeRoot"};
            System.out.println("JTree root has not xPath");
        }
    }

    public boolean isRoot() {
        return xPath.length == 1;
    }

    public int xPathLength() {
        return xPath.length;
    }

    public String xPathElement(int index) {
        return xPath[index];
    }

    public String getXPathString() {
        return getValueAt(0).toString();
    }

    public String getParentXPath() {
        if (xPath.length < 2) return "";
        StringBuilder builder = new StringBuilder();
        for (int i=0; i < xPathLength()-1; i++) {
            builder.append("/").append(xPath[i]);
        }
        return builder.toString();
    }

    public boolean isParentFor(ArrayNodeOnlyXPath potentialChild) {
        boolean isChild = false;
        if (xPath.length + 1 == potentialChild.xPathLength()) {
            isChild = true;
            for (int i=0; i < xPath.length; i++) {
                if (!xPath[i].equals(potentialChild.xPathElement(i))) {
                    isChild = false;
                    break;
                }
            }
        }
        return isChild;
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
        return column == 1;
    }

    @Override
    public String toString() {
        return getValueAt(0).toString();
    }



}

package ru.alfabank.epk.reactive.ui.xsd_comparator;

import lombok.Getter;
import org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode;

import java.util.Arrays;

public class ArrayNode extends AbstractMutableTreeTableNode {

    @Getter
    private String[] xPath;

    /**
     * Нода TreeTable. Столбцы
     * 0 - name
     * 1 - type
     * 2 - xPath
     * 3 - minOccurs
     * 4 - maxOccurs
     * 5 - примеч.
     */
    public ArrayNode(Object[] data) {
        super(data);
        if (data.length != 6) throw new RuntimeException("Incorrect data: " + Arrays.toString(data));
        try {
            xPath = data[2].toString().substring(1).split("/");
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
        return getValueAt(2).toString();
    }

    public String getParentXPath() {
        if (xPath.length < 2) return "";
        StringBuilder builder = new StringBuilder();
        for (int i=0; i < xPathLength()-1; i++) {
            builder.append("/").append(xPath[i]);
        }
        return builder.toString();
    }

    public boolean isParentFor(ArrayNode potentialChild) {
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
        return 6;
    }

    @Override
    public Object[] getUserObject() {
        return (Object[]) super.getUserObject();
    }

    @Override
    public boolean isEditable(int column) {
        return column == 5;
    }

    @Override
    public String toString() {
        return getValueAt(0).toString();
    }



}

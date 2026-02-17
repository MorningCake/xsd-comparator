package ru.alfabank.epk.reactive.ui.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode;
import ru.alfabank.epk.reactive.ui.xsd_comparator.ArrayNode;

import java.util.Arrays;

public abstract class AbstractArrayNode extends AbstractMutableTreeTableNode {

    protected final int columnsQnt;
    protected final int xpathColumn;
    protected final int editableColumn;
    protected final int nodeNameColumn;

    @Getter
    private String[] xPath;

    /**
     * Нода TreeTable. Столбцы xPath и примеч. - обязательны
     */
    public AbstractArrayNode(int columnsQnt, int xpathColumn, int editableColumn, int nodeNameColumn, Object[] data) {
        super(data);
        this.columnsQnt = columnsQnt;
        this.xpathColumn = xpathColumn;
        this.editableColumn = editableColumn;
        this.nodeNameColumn = nodeNameColumn;

        if (data.length != columnsQnt) throw new RuntimeException("Incorrect data: " + Arrays.toString(data));
        try {
            xPath = data[xpathColumn].toString().substring(1).split("/");
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
        return getValueAt(xpathColumn).toString();
    }

    public String getParentXPath() {
        if (xPath.length < 2) return "";
        StringBuilder builder = new StringBuilder();
        for (int i=0; i < xPathLength()-1; i++) {
            builder.append("/").append(xPath[i]);
        }
        return builder.toString();
    }

    public boolean isParentFor(AbstractArrayNode potentialChild) {
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
        return columnsQnt;
    }

    @Override
    public Object[] getUserObject() {
        return (Object[]) super.getUserObject();
    }

    @Override
    public boolean isEditable(int column) {
        return column == editableColumn;
    }

    @Override
    public String toString() {
        return getValueAt(nodeNameColumn).toString();
    }

}

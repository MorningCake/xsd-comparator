package ru.alfabank.epk.reactive.ui;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

class TreeTableCustomizer extends DefaultTreeCellRenderer {

    private static final long serialVersionUID = 4842418316518803090L;

    public Component getTreeCellRendererComponent(
            JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus
    ) {
        Component renderComponent = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        ArrayNode node = ((ArrayNode) value);
        if (node.getValueAt(1).equals("2")) {
            renderComponent.setForeground( Color.GREEN );
            renderComponent.setBackground(new Color(200, 200, 200));
        }


//        rendererComponent.setBackground( Color.WHITE );
//        if (row== this.numeroRighe-1) {
//            rendererComponent.setForeground(Color.GRAY);
//            rendererComponent.setBackground( Color.RED );
//            rendererComponent.setFont(fontTotale);
//        } else if(row != this.numeroRighe/* && column !=3*/){
//            rendererComponent.setForeground( Color.GREEN );
//            rendererComponent.setBackground(new Color(200, 200, 200));
//        } else if(row != this.numeroRighe-1 /*&& column ==3*/){
//
//        }

        return renderComponent;
    }
}
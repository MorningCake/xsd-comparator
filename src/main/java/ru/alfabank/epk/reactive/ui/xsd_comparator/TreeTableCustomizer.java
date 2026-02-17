package ru.alfabank.epk.reactive.ui.xsd_comparator;

import ru.alfabank.epk.reactive.ui.utils.AbstractTreeTableCustomizer;
import ru.alfabank.epk.reactive.ui.utils.AbstractTreeTableGenerator;

import java.util.Set;


public class TreeTableCustomizer extends AbstractTreeTableCustomizer<ArrayNode> {

    public TreeTableCustomizer(Set<String> onlyXsdXPath, AbstractTreeTableGenerator.TreeType treeType) {
        super(onlyXsdXPath, 2, treeType);
    }
}
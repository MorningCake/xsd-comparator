package ru.alfabank.epk.reactive.ui.xpath_comparator;

import ru.alfabank.epk.reactive.ui.utils.AbstractTreeTableCustomizer;
import ru.alfabank.epk.reactive.ui.utils.AbstractTreeTableGenerator;

import java.util.Set;


public class TreeTableCustomizerOnlyXPath extends AbstractTreeTableCustomizer<ArrayNodeOnlyXPath> {

    public TreeTableCustomizerOnlyXPath(Set<String> onlyXsdXPath, AbstractTreeTableGenerator.TreeType treeType) {
        super(onlyXsdXPath, 0, treeType);
    }
}
package ru.alfabank.epk.reactive.ui.utils.factory;

import ru.alfabank.epk.reactive.ui.utils.AbstractTreeTableGenerator;
import ru.alfabank.epk.reactive.ui.xsd_comparator.TreeTableCustomizer;

import java.util.Set;

public class TreeTableCustomizerFactory implements CustomizerFactory<TreeTableCustomizer> {

    @Override
    public TreeTableCustomizer create(Set<String> onlyXsdXPath, AbstractTreeTableGenerator.TreeType treeType) {
        return new TreeTableCustomizer(onlyXsdXPath, treeType);
    }
}

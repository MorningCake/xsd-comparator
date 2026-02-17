package ru.alfabank.epk.reactive.ui.utils.factory;

import ru.alfabank.epk.reactive.ui.utils.AbstractTreeTableGenerator;
import ru.alfabank.epk.reactive.ui.xpath_comparator.TreeTableCustomizerOnlyXPath;

import java.util.Set;

public class TreeTableCustomizerOnlyXPathFactory implements CustomizerFactory<TreeTableCustomizerOnlyXPath> {

    @Override
    public TreeTableCustomizerOnlyXPath create(Set<String> onlyXsdXPath, AbstractTreeTableGenerator.TreeType treeType) {
        return new TreeTableCustomizerOnlyXPath(onlyXsdXPath, treeType);
    }
}

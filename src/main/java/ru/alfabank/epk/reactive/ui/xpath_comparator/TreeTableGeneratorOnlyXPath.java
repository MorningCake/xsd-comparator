package ru.alfabank.epk.reactive.ui.xpath_comparator;

import ru.alfabank.epk.reactive.ui.utils.AbstractTreeTableGenerator;
import ru.alfabank.epk.reactive.ui.utils.factory.ArrayNodeOnlyXPathFactory;
import ru.alfabank.epk.reactive.ui.utils.factory.TreeTableCustomizerOnlyXPathFactory;

public final class TreeTableGeneratorOnlyXPath
        extends AbstractTreeTableGenerator<ArrayNodeOnlyXPath, TreeTableCustomizerOnlyXPath> {

    public TreeTableGeneratorOnlyXPath() {
        super(new ArrayNodeOnlyXPathFactory(), new TreeTableCustomizerOnlyXPathFactory(),
                1, 2, 0);
    }
}

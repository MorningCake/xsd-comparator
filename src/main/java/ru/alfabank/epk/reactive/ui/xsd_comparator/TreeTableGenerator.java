package ru.alfabank.epk.reactive.ui.xsd_comparator;

import ru.alfabank.epk.reactive.ui.utils.AbstractTreeTableGenerator;
import ru.alfabank.epk.reactive.ui.utils.factory.ArrayNodeFactoryImpl;
import ru.alfabank.epk.reactive.ui.utils.factory.TreeTableCustomizerFactory;

public final class TreeTableGenerator extends AbstractTreeTableGenerator<ArrayNode, TreeTableCustomizer> {

    public TreeTableGenerator() {
        super(new ArrayNodeFactoryImpl(), new TreeTableCustomizerFactory(), 5, 6, 2);
    }
}

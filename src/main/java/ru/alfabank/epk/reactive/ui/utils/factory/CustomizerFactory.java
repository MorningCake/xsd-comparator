package ru.alfabank.epk.reactive.ui.utils.factory;

import ru.alfabank.epk.reactive.ui.utils.AbstractTreeTableCustomizer;
import ru.alfabank.epk.reactive.ui.utils.AbstractTreeTableGenerator;

import java.util.Set;

public interface CustomizerFactory<C extends AbstractTreeTableCustomizer<?>> {
    C create(Set<String> onlyXsdXPath, AbstractTreeTableGenerator.TreeType treeType);
}

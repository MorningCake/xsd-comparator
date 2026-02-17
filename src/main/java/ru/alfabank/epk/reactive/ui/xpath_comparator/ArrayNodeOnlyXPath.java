package ru.alfabank.epk.reactive.ui.xpath_comparator;

import ru.alfabank.epk.reactive.ui.utils.AbstractArrayNode;

/**
 * Нода TreeTable. Столбцы
 * 0 - xPath
 * 1 - примеч.
 */
public final class ArrayNodeOnlyXPath extends AbstractArrayNode {

    public ArrayNodeOnlyXPath(Object[] data) {
        super(2, 0, 1, 0, data);
    }
}

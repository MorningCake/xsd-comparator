package ru.alfabank.epk.reactive.ui.xsd_comparator;

import ru.alfabank.epk.reactive.ui.utils.AbstractArrayNode;

import java.nio.file.Path;

/**
 * Нода TreeTable. Столбцы
 * 0 - name
 * 1 - type
 * 2 - xPath
 * 3 - minOccurs
 * 4 - maxOccurs
 * 5 - примеч.
 */
public final class ArrayNode extends AbstractArrayNode {

    public ArrayNode(Object[] data) {
        super(6, 2, 5, 0, data);

    }
}

package ru.alfabank.epk.reactive.ui.utils.factory;

import ru.alfabank.epk.reactive.ui.xsd_comparator.ArrayNode;

import java.util.List;

public class ArrayNodeFactoryImpl implements ArrayNodeFactory<ArrayNode> {

    @Override
    public ArrayNode create(Object[] data) {
        return new ArrayNode(data);
    }

    @Override
    public ArrayNode createWithOnlyName(String name) {
        return new ArrayNode(new Object[]{name, "", "", "", "", ""});
    }

    @Override
    public List<String> getCsvHeader() {
        return List.of("name", "type", "xPath", "minOccurs", "maxOccurs", "примеч.");
    }
}

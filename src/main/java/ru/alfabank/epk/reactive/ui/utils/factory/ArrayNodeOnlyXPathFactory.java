package ru.alfabank.epk.reactive.ui.utils.factory;

import ru.alfabank.epk.reactive.ui.xpath_comparator.ArrayNodeOnlyXPath;

import java.util.List;

public class ArrayNodeOnlyXPathFactory implements ArrayNodeFactory<ArrayNodeOnlyXPath> {

    @Override
    public ArrayNodeOnlyXPath create(Object[] data) {
        return new ArrayNodeOnlyXPath(data);
    }

    @Override
    public ArrayNodeOnlyXPath createWithOnlyName(String name) {
        return new ArrayNodeOnlyXPath(new Object[]{name, ""});
    }

    @Override
    public List<String> getCsvHeader() {
        return List.of("xPath", "примеч.");
    }


}

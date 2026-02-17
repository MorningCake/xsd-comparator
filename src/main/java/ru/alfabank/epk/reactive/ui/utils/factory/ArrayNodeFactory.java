package ru.alfabank.epk.reactive.ui.utils.factory;

import ru.alfabank.epk.reactive.ui.utils.AbstractArrayNode;

import java.util.List;

public interface ArrayNodeFactory<T extends AbstractArrayNode> {
    T create(Object[] data);
    T createWithOnlyName(String name);
    List<String> getCsvHeader();
}

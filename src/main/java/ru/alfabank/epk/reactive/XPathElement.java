package ru.alfabank.epk.reactive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class XPathElement {
    @Builder.Default
    private List<XPathElement> children = new ArrayList<>();
    private String parentName;
    private String name;
    private String type;
    /** Относительный путь */
    private String relPath;
    /** Абсолютный путь */
    private String fullXpath;
    /** Если элемент - корневой */
    private boolean isAbsRoot;
    /** Если элемент - дочерний от :schema */
    private boolean isSchemaChild;
    /** Если элемент - крайний элемент в дереве (нет дочерних) */
    private boolean isLast;

    public void addChild(XPathElement child) {
        children.add(child);
    }

}

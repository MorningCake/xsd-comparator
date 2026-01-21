package ru.alfabank.epk.reactive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class XPathElement {
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
}

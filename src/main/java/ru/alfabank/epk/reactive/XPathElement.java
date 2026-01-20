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
    private String name;
    private String type;
    private String relPath;
    private String fullXpath;
    private boolean isAbsRoot;
    private boolean isLast;
}

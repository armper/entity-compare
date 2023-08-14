package org.vaadin.addons.mygroup;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Difference {
    private final String property;
    private final Object left;
    private final Object right;
    private final boolean isPrimitive;
    private final boolean hasChange;
    private final List<Difference> nestedDifferences;

}

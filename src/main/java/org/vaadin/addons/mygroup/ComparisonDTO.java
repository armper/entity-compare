package org.vaadin.addons.mygroup;

import java.util.List;

public class ComparisonDTO {
    private final List<Difference> differences;
    private final boolean hasChange;

    public ComparisonDTO(List<Difference> differences, boolean hasChange) {
        this.differences = differences;
        this.hasChange = hasChange;
    }

    public List<Difference> getDifferences() {
        return differences;
    }

    public boolean hasChange() {
        return hasChange;
    }
}

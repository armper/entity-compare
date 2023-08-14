package org.vaadin.addons.mygroup;

import java.util.LinkedList;
import java.util.List;

// This class represents a list of items that are prioritized.
// There is a primary, secondary, and the rest are unordered.
public class PrioritizedList<T> {
    private T primary;
    private T secondary;
    private List<T> unorderedItems;

    public PrioritizedList() {
        this.unorderedItems = new LinkedList<>();
    }

    public T getPrimary() {
        return primary;
    }

    public void setPrimary(T primary) {
        this.primary = primary;
    }

    public T getSecondary() {
        return secondary;
    }

    public void setSecondary(T secondary) {
        this.secondary = secondary;
    }

    public void addUnorderedItem(T item) {
        unorderedItems.add(item);
    }

    public boolean removeUnorderedItem(T item) {
        return unorderedItems.remove(item);
    }

    public List<T> getUnorderedItems() {
        return unorderedItems;
    }

    @Override
    public String toString() {
        return "PrioritizedList{" +
                "primary=" + primary +
                ", secondary=" + secondary +
                ", unorderedItems=" + unorderedItems +
                '}';
    }
}

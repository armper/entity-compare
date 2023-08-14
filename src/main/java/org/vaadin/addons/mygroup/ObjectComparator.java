package org.vaadin.addons.mygroup;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ObjectComparator {

    public ComparisonDTO compare(Object left, Object right) throws IllegalAccessException {
        List<Difference> differences = new ArrayList<>();
        boolean hasChange = compareObjects(left, right, differences, "");
        return new ComparisonDTO(differences, hasChange);
    }

    private boolean compareObjects(Object left, Object right, List<Difference> differences, String path)
            throws IllegalAccessException {
        if (left == null || right == null) {
            return handleNullValues(left, right, differences, path);
        }

        if (left instanceof List && right instanceof List) {
            return compareLists((List<?>) left, (List<?>) right, differences, path);
        }

        return compareFields(left, right, differences, path);
    }

    private boolean handleNullValues(Object left, Object right, List<Difference> differences, String path) {
        boolean hasChange = left != right; // If one is null and the other is not, it's a change
        differences.add(new Difference(path, left, right, true, hasChange, null));
        return hasChange;
    }

    private boolean compareFields(Object left, Object right, List<Difference> differences, String path)
            throws IllegalAccessException {
        boolean hasChange = false;
        Class<?> clazz = left.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isSynthetic() && !field.getName().equals("serialVersionUID")) {
                hasChange |= compareField(left, right, differences, path, field);
            }
        }
        return hasChange;
    }

    private boolean compareField(Object left, Object right, List<Difference> differences, String path, Field field)
            throws IllegalAccessException {
        field.setAccessible(true);
        Object leftValue = field.get(left);
        Object rightValue = field.get(right);
        String property = path + field.getName();
        boolean fieldHasChange = !objectsEqual(leftValue, rightValue);

        if (isPrimitive(field.getType())) {
            differences.add(new Difference(property, leftValue, rightValue, true, fieldHasChange, null));
        } else {
            List<Difference> nestedDifferences = new ArrayList<>();
            compareObjects(leftValue, rightValue, nestedDifferences, property + ".");
            differences.add(new Difference(property, leftValue, rightValue, false, fieldHasChange, nestedDifferences));
        }
        return fieldHasChange;
    }

    private boolean objectsEqual(Object left, Object right) {
        return (left != null && left.equals(right)) || (left == null && right == null);
    }

    private boolean isPrimitive(Class<?> clazz) {
        return clazz.isPrimitive() || String.class.isAssignableFrom(clazz) ||
                Number.class.isAssignableFrom(clazz) || Boolean.class.isAssignableFrom(clazz) ||
                Character.class.isAssignableFrom(clazz);
    }

    private boolean compareLists(List<?> leftList, List<?> rightList, List<Difference> differences, String path)
            throws IllegalAccessException {
        boolean hasChange = false;
        int minLength = Math.min(leftList.size(), rightList.size());

        // Compare the elements at the corresponding indices up to the size of the
        // smaller list
        for (int i = 0; i < minLength; i++) {
            Object left = leftList.get(i);
            Object right = rightList.get(i);
            String indexPath = path + "[" + i + "]";

            if (left != null && right != null && left.getClass().isAssignableFrom(right.getClass())) {
                List<Difference> nestedDifferences = new ArrayList<>();
                boolean fieldHasChange = compareObjects(left, right, nestedDifferences, indexPath + ".");
                hasChange = hasChange || fieldHasChange;
                differences.add(new Difference(indexPath, left, right, false, fieldHasChange, nestedDifferences));
            } else {
                boolean fieldHasChange = (left == null && right != null) || (left != null && right == null)
                        || !left.equals(right);
                hasChange = hasChange || fieldHasChange;
                differences.add(new Difference(indexPath, left, right, true, fieldHasChange, null));
            }
        }

        // Handle additional elements in the larger list, if any
        if (leftList.size() != rightList.size()) {
            hasChange = true;
            for (int i = minLength; i < Math.max(leftList.size(), rightList.size()); i++) {
                String indexPath = path + "[" + i + "]";
                Object left = i < leftList.size() ? leftList.get(i) : null;
                Object right = i < rightList.size() ? rightList.get(i) : null;
                differences.add(new Difference(indexPath, left, right, true, true, null));
            }
        }

        return hasChange;
    }

}

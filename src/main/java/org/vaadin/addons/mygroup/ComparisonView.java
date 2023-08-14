package org.vaadin.addons.mygroup;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

public class ComparisonView extends VerticalLayout {

    public ComparisonView(ComparisonDTO comparison) {
        renderDifferences(comparison.getDifferences(), this);
    }

    private void renderDifferences(List<Difference> differences, VerticalLayout container) {
        for (Difference difference : differences) {
            if (difference.isPrimitive()) {
                container.add(renderPrimitiveDifference(difference));
            } else {
                container.add(renderNestedDifference(difference));
            }
        }
    }

    private HorizontalLayout renderPrimitiveDifference(Difference difference) {
        String humanReadableProperty = makeHumanReadable(difference.getProperty());
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        Div leftDiv = createDivWithStyle(String.format("%s: %s", humanReadableProperty, difference.getLeft()),
                "justify-left");
        horizontalLayout.add(leftDiv, determineIcon(difference));

        Div rightDiv = createDivWithStyle(formatRightValue(difference.getRight()), "justify-right");
        horizontalLayout.add(rightDiv);

        return horizontalLayout;
    }

    private Div createDivWithStyle(String text, String style) {
        Div div = new Div();
        div.setText(text);
        div.addClassName(style); // Add custom CSS class name
        return div;
    }

    private Icon determineIcon(Difference difference) {
        return difference.isHasChange() ? new Icon(VaadinIcon.ARROW_RIGHT) : new Icon(VaadinIcon.MINUS);
    }

    private Details renderNestedDifference(Difference difference) {
        String humanReadableProperty = makeHumanReadable(difference.getProperty());
        Details details = new Details();
        details.setSummaryText(humanReadableProperty);
        VerticalLayout detailsContainer = new VerticalLayout();
        renderDifferences(difference.getNestedDifferences(), detailsContainer);
        details.addContent(detailsContainer);
        return details;
    }

    private String formatRightValue(Object rightValue) {
        return Optional.ofNullable(rightValue).map(Object::toString).orElse("null");
    }

    private String makeHumanReadable(String input) {
        StringJoiner joiner = new StringJoiner(" ");
        for (String word : input.split("(?=\\p{Upper})")) {
            word = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
            joiner.add(word);
        }
        return joiner.toString();
    }

}

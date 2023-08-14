package org.vaadin.addons.mygroup;

import java.util.Arrays;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route("")
public class AddonView extends Div {

    public AddonView() {

        // Create Orders
        Order order1 = new Order(100, "Laptop");
        Order order2 = new Order(200, "Monitor");
        Order order3 = new Order(50, "Mouse");
        Order order4 = new Order(30, "Keyboard");

        // Referred Customers
        Customer referredCustomer1 = new Customer("Samuel", "Jackson", Arrays.asList(order3, order4), null);
        Customer referredCustomer2 = new Customer("Emily", "Adams", Arrays.asList(order2), null);

        // Main Customers
        Customer customer1 = new Customer("Jane", "Smith", Arrays.asList(order1, order2), referredCustomer1);
        PrioritizedList<Email> emails = new PrioritizedList<>();
        Email primaryEmail = new Email("derp@derp.com");
        emails.addUnorderedItem(primaryEmail);
        emails.setPrimary(primaryEmail);
        customer1.setEmails(emails);
        Customer customer2 = new Customer("Jane", "Doe", Arrays.asList(order3, order4), referredCustomer2);

        // Comparing the two main customers
        ComparisonDTO comparison;
        try {
            comparison = new ObjectComparator().compare(customer1, customer2);
            ComparisonView view = new ComparisonView(comparison);

            Dialog dialog = new Dialog();
            dialog.add(view);
            dialog.setDraggable(true);
            dialog.setResizable(true);
            add(dialog);
            dialog.open();

        } catch (IllegalAccessException e) {
            e.printStackTrace();
            add(e.getMessage());
        }

    }
}

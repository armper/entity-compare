package org.vaadin.addons.mygroup;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Customer {
    private String firstName;
    private String lastName;
    private List<Order> orders;
    private Customer referralCustomer;
    private PrioritizedList<Email> emails;

    public Customer(String firstName, String lastName, List<Order> orders, Customer referralCustomer) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.orders = orders;
        this.referralCustomer = referralCustomer;
        this.emails = new PrioritizedList<>();
    }
}

package org.vaadin.addons.mygroup;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ObjectComparatorTest {

    private ObjectComparator comparator;

    @BeforeEach
    public void setUp() {
        comparator = new ObjectComparator();
    }

    @Test
    public void testCustomerComparison() throws IllegalAccessException {
        Order order1 = new Order(100, "Laptop");
        Order order2 = new Order(200, "Monitor");

        Customer referredCustomer1 = new Customer("Samuel", "Jackson", Collections.emptyList(), null);
        Customer customer1 = new Customer("John", "Smith", Arrays.asList(order1, order2), referredCustomer1);
        Customer customer2 = new Customer("John", "Smith", Arrays.asList(order1, order2), referredCustomer1);

        ComparisonDTO comparison = comparator.compare(customer1, customer2);
        assertFalse(comparison.hasChange());
        assertFalse(comparison.getDifferences().isEmpty());

        Customer customer3 = new Customer("Jane", "Doe", Arrays.asList(order1), referredCustomer1);
        comparison = comparator.compare(customer1, customer3);
        assertTrue(comparison.hasChange());
        assertFalse(comparison.getDifferences().isEmpty());
    }

    @Test
    public void testCustomerWithDifferentReferredCustomer() throws IllegalAccessException {
        Order order1 = new Order(100, "Laptop");
        Order order2 = new Order(200, "Monitor");

        Customer referredCustomer1 = new Customer("Samuel", "Jackson", Collections.emptyList(), null);
        Customer referredCustomer2 = new Customer("Emily", "Adams", Collections.emptyList(), null);
        Customer customer1 = new Customer("John", "Smith", Arrays.asList(order1, order2), referredCustomer1);
        Customer customer2 = new Customer("John", "Smith", Arrays.asList(order1, order2), referredCustomer2);

        ComparisonDTO comparison = comparator.compare(customer1, customer2);
        assertTrue(comparison.hasChange());
        assertFalse(comparison.getDifferences().isEmpty());
    }

    @Test
    public void testCustomerWithDifferentOrders() throws IllegalAccessException {
        Order order1 = new Order(100, "Laptop");
        Order order2 = new Order(200, "Monitor");

        Customer customer1 = new Customer("John", "Smith", Arrays.asList(order1), null);
        Customer customer2 = new Customer("John", "Smith", Arrays.asList(order1, order2), null);

        ComparisonDTO comparison = comparator.compare(customer1, customer2);
        assertTrue(comparison.hasChange());
        assertFalse(comparison.getDifferences().isEmpty());
    }

    @Test
    public void testCustomerWithNullReferredCustomer() throws IllegalAccessException {
        Order order1 = new Order(100, "Laptop");

        Customer customer1 = new Customer("John", "Smith", Arrays.asList(order1), null);
        Customer customer2 = new Customer("John", "Smith", null, null);

        ComparisonDTO comparison = comparator.compare(customer1, customer2);
        assertTrue(comparison.hasChange());
        assertFalse(comparison.getDifferences().isEmpty());
    }

    @Test
    public void testIncompatibleTypesComparison() {
        Order order1 = new Order(100, "Laptop");
        Customer customer1 = new Customer("John", "Smith", Arrays.asList(order1), null);

        // Negative Test: Comparing incompatible types
        assertThrows(IllegalArgumentException.class, () -> {
            comparator.compare(order1, customer1);
        });
    }

    @Test
    public void testComparingWithItself() throws IllegalAccessException {
        Order order1 = new Order(100, "Laptop");
        Customer customer1 = new Customer("John", "Smith", Arrays.asList(order1), null);

        ComparisonDTO comparison = comparator.compare(customer1, customer1);
        assertFalse(comparison.hasChange());
        assertFalse(comparison.getDifferences().isEmpty()); // Expecting differences to be populated even if no changes
    }

    @Test
    public void testEmptyCustomersComparison() throws IllegalAccessException {
        Customer customer1 = new Customer("", "", Collections.emptyList(), null);
        Customer customer2 = new Customer("", "", Collections.emptyList(), null);

        ComparisonDTO comparison = comparator.compare(customer1, customer2);
        assertFalse(comparison.hasChange());
        assertFalse(comparison.getDifferences().isEmpty());
    }

    @Test
    public void testOrderComparison() throws IllegalAccessException {
        Order order1 = new Order(100, "Laptop");
        Order order2 = new Order(100, "Laptop");
        Order order3 = new Order(200, "Monitor");

        ComparisonDTO comparison = comparator.compare(order1, order2);
        assertFalse(comparison.hasChange());
        assertFalse(comparison.getDifferences().isEmpty());

        comparison = comparator.compare(order1, order3);
        assertTrue(comparison.hasChange());
        assertFalse(comparison.getDifferences().isEmpty());
    }
}

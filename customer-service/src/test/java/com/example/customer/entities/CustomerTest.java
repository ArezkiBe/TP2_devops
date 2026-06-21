package com.example.customer.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void testCustomerCreation() {
        Customer customer = new Customer("Alice", "Dupont", "alice@example.com");
        assertEquals("Alice", customer.getFirstName());
        assertEquals("Dupont", customer.getLastName());
        assertEquals("alice@example.com", customer.getEmail());
        assertNull(customer.getId());
    }

    @Test
    void testDefaultConstructor() {
        Customer customer = new Customer();
        assertNull(customer.getFirstName());
        assertNull(customer.getLastName());
        assertNull(customer.getEmail());
        assertNull(customer.getId());
    }

    @Test
    void testSetters() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("Bob");
        customer.setLastName("Martin");
        customer.setEmail("bob@example.com");
        assertEquals(1L, customer.getId());
        assertEquals("Bob", customer.getFirstName());
        assertEquals("Martin", customer.getLastName());
        assertEquals("bob@example.com", customer.getEmail());
    }

    @Test
    void testToString() {
        Customer customer = new Customer("Alice", "Dupont", "alice@example.com");
        String str = customer.toString();
        assertTrue(str.contains("Alice"));
        assertTrue(str.contains("Dupont"));
        assertTrue(str.contains("alice@example.com"));
    }
}

package com.example.customer.services;

import com.example.customer.entities.Customer;
import com.example.customer.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer alice;
    private Customer bob;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        alice = new Customer("Alice", "Dupont", "alice@example.com");
        alice.setId(1L);
        bob = new Customer("Bob", "Martin", "bob@example.com");
        bob.setId(2L);
    }

    @Test
    void testGetAllCustomers() {
        when(customerRepository.findAll()).thenReturn(List.of(alice, bob));
        List<Customer> result = customerService.getAllCustomers();
        assertEquals(2, result.size());
        verify(customerRepository).findAll();
    }

    @Test
    void testGetCustomer_found() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(alice));
        Customer result = customerService.getCustomer(1L);
        assertNotNull(result);
        assertEquals("Alice", result.getFirstName());
    }

    @Test
    void testGetCustomer_notFound() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());
        assertNull(customerService.getCustomer(99L));
    }

    @Test
    void testAddCustomer() {
        when(customerRepository.save(any(Customer.class))).thenReturn(alice);
        Customer result = customerService.addCustomer(alice);
        assertNotNull(result);
        assertEquals("Alice", result.getFirstName());
        verify(customerRepository).save(alice);
    }

    @Test
    void testUpdateCustomer_found() {
        Customer update = new Customer("Alicia", "Dupont", "alicia@example.com");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(alice));
        when(customerRepository.save(any(Customer.class))).thenReturn(alice);
        assertTrue(customerService.updateCustomer(1L, update));
        assertEquals("Alicia", alice.getFirstName());
        assertEquals("alicia@example.com", alice.getEmail());
    }

    @Test
    void testUpdateCustomer_notFound() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());
        assertFalse(customerService.updateCustomer(99L, new Customer("X", "Y", "x@y.com")));
        verify(customerRepository, never()).save(any());
    }

    @Test
    void testDeleteCustomer_found() {
        when(customerRepository.existsById(1L)).thenReturn(true);
        assertTrue(customerService.deleteCustomer(1L));
        verify(customerRepository).deleteById(1L);
    }

    @Test
    void testDeleteCustomer_notFound() {
        when(customerRepository.existsById(99L)).thenReturn(false);
        assertFalse(customerService.deleteCustomer(99L));
        verify(customerRepository, never()).deleteById(any());
    }
}

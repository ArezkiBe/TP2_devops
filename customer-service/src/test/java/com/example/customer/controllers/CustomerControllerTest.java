package com.example.customer.controllers;

import com.example.customer.entities.Customer;
import com.example.customer.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class CustomerControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockitoBean
    private CustomerService customerService;

    private Customer alice;
    private Customer bob;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        alice = new Customer("Alice", "Dupont", "alice@example.com");
        alice.setId(1L);
        bob = new Customer("Bob", "Martin", "bob@example.com");
        bob.setId(2L);
    }

    @Test
    void testGetAllCustomers() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(List.of(alice, bob));
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetCustomer_found() throws Exception {
        when(customerService.getCustomer(1L)).thenReturn(alice);
        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    void testGetCustomer_notFound() throws Exception {
        when(customerService.getCustomer(99L)).thenReturn(null);
        mockMvc.perform(get("/api/customers/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddCustomer() throws Exception {
        Customer saved = new Customer("Charlie", "Brown", "charlie@example.com");
        saved.setId(3L);
        when(customerService.addCustomer(any(Customer.class))).thenReturn(saved);
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Charlie\",\"lastName\":\"Brown\",\"email\":\"charlie@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Charlie"));
        verify(customerService).addCustomer(any(Customer.class));
    }

    @Test
    void testUpdateCustomer_found() throws Exception {
        when(customerService.updateCustomer(eq(1L), any(Customer.class))).thenReturn(true);
        mockMvc.perform(put("/api/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Alicia\",\"lastName\":\"Dupont\",\"email\":\"alicia@example.com\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateCustomer_notFound() throws Exception {
        when(customerService.updateCustomer(eq(99L), any(Customer.class))).thenReturn(false);
        mockMvc.perform(put("/api/customers/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"X\",\"lastName\":\"Y\",\"email\":\"x@y.com\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteCustomer_found() throws Exception {
        when(customerService.deleteCustomer(1L)).thenReturn(true);
        mockMvc.perform(delete("/api/customers/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteCustomer_notFound() throws Exception {
        when(customerService.deleteCustomer(99L)).thenReturn(false);
        mockMvc.perform(delete("/api/customers/99"))
                .andExpect(status().isNotFound());
    }
}

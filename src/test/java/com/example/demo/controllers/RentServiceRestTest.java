package com.example.demo.controllers;

import com.example.demo.entities.Car;
import com.example.demo.services.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class RentServiceRestTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockitoBean
    private CarService carService;

    private List<Car> sampleCars;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        sampleCars = new ArrayList<>(List.of(
                new Car("ABC123", "Toyota", 50.0),
                new Car("XYZ789", "Honda", 45.0),
                new Car("DEF456", "BMW", 100.0)
        ));
    }

    @Test
    void testWelcome() throws Exception {
        mockMvc.perform(get("/api/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Welcome to Car Rental Service"));
    }

    @Test
    void testGetAllCars() throws Exception {
        when(carService.getCars()).thenReturn(sampleCars);
        mockMvc.perform(get("/api/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void testGetAvailableCars() throws Exception {
        when(carService.getAvailableCars()).thenReturn(List.of(sampleCars.get(1), sampleCars.get(2)));
        mockMvc.perform(get("/api/cars/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetCar_found() throws Exception {
        when(carService.getCar("ABC123")).thenReturn(sampleCars.get(0));
        mockMvc.perform(get("/api/cars/ABC123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value("Toyota"))
                .andExpect(jsonPath("$.plateNumber").value("ABC123"));
    }

    @Test
    void testGetCar_notFound() throws Exception {
        when(carService.getCar("NOTEXIST")).thenReturn(null);
        mockMvc.perform(get("/api/cars/NOTEXIST"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddCar() throws Exception {
        mockMvc.perform(post("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"plateNumber\":\"TEST999\",\"brand\":\"Tesla\",\"price\":120.0,\"rented\":false}"))
                .andExpect(status().isCreated());
        verify(carService).addCar(any(Car.class));
    }

    @Test
    void testUpdateCar_found() throws Exception {
        when(carService.updateCar(eq("ABC123"), any(Car.class))).thenReturn(true);
        mockMvc.perform(put("/api/cars/ABC123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"plateNumber\":\"ABC123\",\"brand\":\"Tesla\",\"price\":100.0,\"rented\":false}"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateCar_notFound() throws Exception {
        when(carService.updateCar(eq("NOTEXIST"), any(Car.class))).thenReturn(false);
        mockMvc.perform(put("/api/cars/NOTEXIST")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"plateNumber\":\"NOTEXIST\",\"brand\":\"Tesla\",\"price\":100.0,\"rented\":false}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteCar_found() throws Exception {
        when(carService.deleteCar("ABC123")).thenReturn(true);
        mockMvc.perform(delete("/api/cars/ABC123"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteCar_notFound() throws Exception {
        when(carService.deleteCar("NOTEXIST")).thenReturn(false);
        mockMvc.perform(delete("/api/cars/NOTEXIST"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testRentCar_success() throws Exception {
        when(carService.rentCar("ABC123")).thenReturn(true);
        mockMvc.perform(post("/api/cars/ABC123/rent"))
                .andExpect(status().isOk());
    }

    @Test
    void testRentCar_failure() throws Exception {
        when(carService.rentCar("NOTEXIST")).thenReturn(false);
        mockMvc.perform(post("/api/cars/NOTEXIST/rent"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testReturnCar_success() throws Exception {
        when(carService.returnCar("ABC123")).thenReturn(true);
        mockMvc.perform(post("/api/cars/ABC123/return"))
                .andExpect(status().isOk());
    }

    @Test
    void testReturnCar_failure() throws Exception {
        when(carService.returnCar("NOTEXIST")).thenReturn(false);
        mockMvc.perform(post("/api/cars/NOTEXIST/return"))
                .andExpect(status().isBadRequest());
    }
}

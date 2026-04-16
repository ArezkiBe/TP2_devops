package com.example.demo.controllers;

import com.example.demo.entities.Car;
import com.example.demo.services.CarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RentServiceRestTest {

    @Autowired
    private RentServiceRest controller;

    @Autowired
    private CarService carService;

    @Test
    void testControllerLoads() {
        assertNotNull(controller);
    }

    @Test
    void testGetAllCars() {
        assertNotNull(controller.getCars());
        assertTrue(controller.getCars().size() >= 3);
    }

    @Test
    void testGetAvailableCars() {
        assertNotNull(controller.getAvailableCars());
    }

    @Test
    void testGetCar() {
        var response = controller.getCar("ABC123");
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void testAddCar() {
        Car newCar = new Car("TEST999", "Tesla", 120.0);
        var response = controller.addCar(newCar);
        assertEquals(201, response.getStatusCode().value());
    }
}

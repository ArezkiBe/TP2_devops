package com.example.demo.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CarTest {

    @Test
    void testCarCreation() {
        Car car = new Car("ABC123", "Toyota", 50.0);
        
        assertEquals("ABC123", car.getPlateNumber());
        assertEquals("Toyota", car.getBrand());
        assertEquals(50.0, car.getPrice());
        assertFalse(car.isRented());
    }

    @Test
    void testRentedStatus() {
        Car car = new Car("ABC123", "Toyota", 50.0);
        
        car.setRented(true);
        assertTrue(car.isRented());
        
        car.setRented(false);
        assertFalse(car.isRented());
    }
}

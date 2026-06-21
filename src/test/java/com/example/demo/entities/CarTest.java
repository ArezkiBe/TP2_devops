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
    void testDefaultConstructor() {
        Car car = new Car();
        assertFalse(car.isRented());
        assertNull(car.getPlateNumber());
        assertNull(car.getBrand());
    }

    @Test
    void testSetters() {
        Car car = new Car();
        car.setPlateNumber("XYZ789");
        car.setBrand("Honda");
        car.setPrice(45.0);
        assertEquals("XYZ789", car.getPlateNumber());
        assertEquals("Honda", car.getBrand());
        assertEquals(45.0, car.getPrice());
    }

    @Test
    void testRentedStatus() {
        Car car = new Car("ABC123", "Toyota", 50.0);
        car.setRented(true);
        assertTrue(car.isRented());
        car.setRented(false);
        assertFalse(car.isRented());
    }

    @Test
    void testToString() {
        Car car = new Car("ABC123", "Toyota", 50.0);
        String str = car.toString();
        assertTrue(str.contains("ABC123"));
        assertTrue(str.contains("Toyota"));
        assertTrue(str.contains("50.0"));
    }
}

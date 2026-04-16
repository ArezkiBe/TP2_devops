package com.example.demo.services;

import com.example.demo.entities.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CarServiceTest {

    private CarService carService;

    @BeforeEach
    void setUp() {
        carService = new CarService();
        carService.init();
    }

    @Test
    void testInitialCars() {
        List<Car> cars = carService.getCars();
        assertEquals(3, cars.size());
    }

    @Test
    void testAddCar() {
        Car newCar = new Car("TEST123", "Tesla", 120.0);
        carService.addCar(newCar);
        
        assertEquals(4, carService.getCars().size());
        assertNotNull(carService.getCar("TEST123"));
    }

    @Test
    void testGetCar() {
        Car car = carService.getCar("ABC123");
        
        assertNotNull(car);
        assertEquals("Toyota", car.getBrand());
    }

    @Test
    void testRentCar() {
        assertTrue(carService.rentCar("ABC123"));
        assertTrue(carService.getCar("ABC123").isRented());
        
        // Cannot rent same car again
        assertFalse(carService.rentCar("ABC123"));
    }

    @Test
    void testReturnCar() {
        carService.rentCar("ABC123");
        assertTrue(carService.returnCar("ABC123"));
        assertFalse(carService.getCar("ABC123").isRented());
    }

    @Test
    void testGetAvailableCars() {
        carService.rentCar("ABC123");
        List<Car> available = carService.getAvailableCars();
        
        assertEquals(2, available.size());
    }

    @Test
    void testDeleteCar() {
        assertTrue(carService.deleteCar("ABC123"));
        assertEquals(2, carService.getCars().size());
        assertNull(carService.getCar("ABC123"));
    }
}

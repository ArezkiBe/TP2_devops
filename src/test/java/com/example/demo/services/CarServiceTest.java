package com.example.demo.services;

import com.example.demo.entities.Car;
import com.example.demo.repositories.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;

    private List<Car> sampleCars;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleCars = new ArrayList<>(List.of(
                new Car("ABC123", "Toyota", 50.0),
                new Car("XYZ789", "Honda", 45.0),
                new Car("DEF456", "BMW", 100.0)
        ));
    }

    @Test
    void testGetAllCars() {
        when(carRepository.findAll()).thenReturn(sampleCars);
        List<Car> cars = carService.getCars();
        assertEquals(3, cars.size());
        verify(carRepository).findAll();
    }

    @Test
    void testGetCar_found() {
        Car toyota = sampleCars.get(0);
        when(carRepository.findById("ABC123")).thenReturn(Optional.of(toyota));
        Car car = carService.getCar("ABC123");
        assertNotNull(car);
        assertEquals("Toyota", car.getBrand());
    }

    @Test
    void testGetCar_notFound() {
        when(carRepository.findById("NOTEXIST")).thenReturn(Optional.empty());
        assertNull(carService.getCar("NOTEXIST"));
    }

    @Test
    void testAddCar() {
        Car newCar = new Car("TEST123", "Tesla", 120.0);
        carService.addCar(newCar);
        verify(carRepository).save(newCar);
    }

    @Test
    void testGetAvailableCars() {
        List<Car> available = List.of(sampleCars.get(1), sampleCars.get(2));
        when(carRepository.findByRented(false)).thenReturn(available);
        List<Car> result = carService.getAvailableCars();
        assertEquals(2, result.size());
    }

    @Test
    void testRentCar_success() {
        Car toyota = sampleCars.get(0);
        when(carRepository.findById("ABC123")).thenReturn(Optional.of(toyota));
        when(carRepository.save(any(Car.class))).thenReturn(toyota);
        assertTrue(carService.rentCar("ABC123"));
        assertTrue(toyota.isRented());
        verify(carRepository).save(toyota);
    }

    @Test
    void testRentCar_alreadyRented() {
        Car rented = new Car("ABC123", "Toyota", 50.0);
        rented.setRented(true);
        when(carRepository.findById("ABC123")).thenReturn(Optional.of(rented));
        assertFalse(carService.rentCar("ABC123"));
        verify(carRepository, never()).save(any());
    }

    @Test
    void testRentCar_notFound() {
        when(carRepository.findById("NOTEXIST")).thenReturn(Optional.empty());
        assertFalse(carService.rentCar("NOTEXIST"));
    }

    @Test
    void testReturnCar_success() {
        Car rented = new Car("ABC123", "Toyota", 50.0);
        rented.setRented(true);
        when(carRepository.findById("ABC123")).thenReturn(Optional.of(rented));
        when(carRepository.save(any(Car.class))).thenReturn(rented);
        assertTrue(carService.returnCar("ABC123"));
        assertFalse(rented.isRented());
        verify(carRepository).save(rented);
    }

    @Test
    void testReturnCar_notRented() {
        Car car = new Car("ABC123", "Toyota", 50.0);
        when(carRepository.findById("ABC123")).thenReturn(Optional.of(car));
        assertFalse(carService.returnCar("ABC123"));
        verify(carRepository, never()).save(any());
    }

    @Test
    void testReturnCar_notFound() {
        when(carRepository.findById("NOTEXIST")).thenReturn(Optional.empty());
        assertFalse(carService.returnCar("NOTEXIST"));
    }

    @Test
    void testDeleteCar_success() {
        when(carRepository.existsById("ABC123")).thenReturn(true);
        assertTrue(carService.deleteCar("ABC123"));
        verify(carRepository).deleteById("ABC123");
    }

    @Test
    void testDeleteCar_notFound() {
        when(carRepository.existsById("NOTEXIST")).thenReturn(false);
        assertFalse(carService.deleteCar("NOTEXIST"));
        verify(carRepository, never()).deleteById(any());
    }

    @Test
    void testUpdateCar_success() {
        Car existing = sampleCars.get(0);
        Car update = new Car("ABC123", "Tesla", 100.0);
        when(carRepository.findById("ABC123")).thenReturn(Optional.of(existing));
        when(carRepository.save(any(Car.class))).thenReturn(existing);
        assertTrue(carService.updateCar("ABC123", update));
        assertEquals("Tesla", existing.getBrand());
        assertEquals(100.0, existing.getPrice());
    }

    @Test
    void testUpdateCar_notFound() {
        when(carRepository.findById("NOTEXIST")).thenReturn(Optional.empty());
        assertFalse(carService.updateCar("NOTEXIST", new Car("NOTEXIST", "Tesla", 100.0)));
        verify(carRepository, never()).save(any());
    }
}

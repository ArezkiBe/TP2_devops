package com.example.demo.services;

import com.example.demo.entities.Car;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class CarService {
    private List<Car> cars = new ArrayList<>();

    @PostConstruct
    public void init() {
        // Initialize with some sample cars
        cars.add(new Car("ABC123", "Toyota", 50.0));
        cars.add(new Car("XYZ789", "Honda", 45.0));
        cars.add(new Car("DEF456", "BMW", 100.0));
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public Car getCar(String plateNumber) {
        return cars.stream()
                .filter(car -> car.getPlateNumber().equals(plateNumber))
                .findFirst()
                .orElse(null);
    }

    public List<Car> getCars() {
        return new ArrayList<>(cars);
    }

    public List<Car> getAvailableCars() {
        return cars.stream()
                .filter(car -> !car.isRented())
                .toList();
    }

    public boolean rentCar(String plateNumber) {
        Car car = getCar(plateNumber);
        if (car != null && !car.isRented()) {
            car.setRented(true);
            return true;
        }
        return false;
    }

    public boolean returnCar(String plateNumber) {
        Car car = getCar(plateNumber);
        if (car != null && car.isRented()) {
            car.setRented(false);
            return true;
        }
        return false;
    }

    public boolean deleteCar(String plateNumber) {
        return cars.removeIf(car -> car.getPlateNumber().equals(plateNumber));
    }

    public boolean updateCar(String plateNumber, Car updatedCar) {
        Car car = getCar(plateNumber);
        if (car != null) {
            car.setBrand(updatedCar.getBrand());
            car.setPrice(updatedCar.getPrice());
            return true;
        }
        return false;
    }
}

package com.example.demo.services;

import com.example.demo.entities.Car;
import com.example.demo.repositories.CarRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CarService {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public void addCar(Car car) {
        carRepository.save(car);
    }

    public Car getCar(String plateNumber) {
        return carRepository.findById(plateNumber).orElse(null);
    }

    public List<Car> getCars() {
        return carRepository.findAll();
    }

    public List<Car> getAvailableCars() {
        return carRepository.findByRented(false);
    }

    public boolean rentCar(String plateNumber) {
        Car car = getCar(plateNumber);
        if (car != null && !car.isRented()) {
            car.setRented(true);
            carRepository.save(car);
            return true;
        }
        return false;
    }

    public boolean returnCar(String plateNumber) {
        Car car = getCar(plateNumber);
        if (car != null && car.isRented()) {
            car.setRented(false);
            carRepository.save(car);
            return true;
        }
        return false;
    }

    public boolean deleteCar(String plateNumber) {
        if (carRepository.existsById(plateNumber)) {
            carRepository.deleteById(plateNumber);
            return true;
        }
        return false;
    }

    public boolean updateCar(String plateNumber, Car updatedCar) {
        Car car = getCar(plateNumber);
        if (car != null) {
            car.setBrand(updatedCar.getBrand());
            car.setPrice(updatedCar.getPrice());
            carRepository.save(car);
            return true;
        }
        return false;
    }
}

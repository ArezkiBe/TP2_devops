package com.example.demo.controllers;

import com.example.demo.entities.Car;
import com.example.demo.services.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RentServiceRest {

    @Autowired
    private CarService carService;

    @GetMapping("/")
    public Map<String, String> welcome(){
        return Map.of(
            "message", "Welcome to Car Rental Service",
            "version", "1.0",
            "endpoints", "/api/cars, /api/cars/available, /api/cars/{plateNumber}"
        );
    }

    @PostMapping("/cars")
    public ResponseEntity<String> addCar(@RequestBody Car car){
        carService.addCar(car);
        return ResponseEntity.status(HttpStatus.CREATED).body("Car added successfully");
    }

    @GetMapping("/cars")
    public List<Car> getCars(){
        return carService.getCars();
    }

    @GetMapping("/cars/available")
    public List<Car> getAvailableCars(){
        return carService.getAvailableCars();
    }

    @GetMapping("/cars/{plateNumber}")
    public ResponseEntity<Car> getCar(@PathVariable String plateNumber){
        Car car = carService.getCar(plateNumber);
        if (car != null) {
            return ResponseEntity.ok(car);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/cars/{plateNumber}")
    public ResponseEntity<String> updateCar(@PathVariable String plateNumber, @RequestBody Car car){
        boolean updated = carService.updateCar(plateNumber, car);
        if (updated) {
            return ResponseEntity.ok("Car updated successfully");
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/cars/{plateNumber}")
    public ResponseEntity<String> deleteCar(@PathVariable String plateNumber){
        boolean deleted = carService.deleteCar(plateNumber);
        if (deleted) {
            return ResponseEntity.ok("Car deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/cars/{plateNumber}/rent")
    public ResponseEntity<String> rentCar(@PathVariable String plateNumber){
        boolean rented = carService.rentCar(plateNumber);
        if (rented) {
            return ResponseEntity.ok("Car rented successfully");
        }
        return ResponseEntity.badRequest().body("Car not available or doesn't exist");
    }

    @PostMapping("/cars/{plateNumber}/return")
    public ResponseEntity<String> returnCar(@PathVariable String plateNumber){
        boolean returned = carService.returnCar(plateNumber);
        if (returned) {
            return ResponseEntity.ok("Car returned successfully");
        }
        return ResponseEntity.badRequest().body("Car was not rented or doesn't exist");
    }

}

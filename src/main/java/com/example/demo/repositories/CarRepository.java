package com.example.demo.repositories;

import com.example.demo.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, String> {
    List<Car> findByRented(boolean rented);
}

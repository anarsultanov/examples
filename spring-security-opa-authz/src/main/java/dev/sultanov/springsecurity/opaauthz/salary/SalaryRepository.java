package dev.sultanov.springsecurity.opaauthz.salary;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface SalaryRepository extends CrudRepository<Salary, Long> {
    Optional<Salary> findByUsername(String username);
}

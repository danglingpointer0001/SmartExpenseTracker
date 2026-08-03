package com.exp.smartexpensetracker.repository;

import com.exp.smartexpensetracker.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

}

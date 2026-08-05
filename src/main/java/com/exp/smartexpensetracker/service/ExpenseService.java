package com.exp.smartexpensetracker.service;

import com.exp.smartexpensetracker.entity.Expense;
import com.exp.smartexpensetracker.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import com.exp.smartexpensetracker.entity.User;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public Expense saveExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }

    public Expense getExpenseById(Long id) {
        return expenseRepository.findById(id).orElse(null);
    }

    public Expense updateExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    public Double getTotalExpense() {
        return expenseRepository.getTotalExpense();
    }

    public Double getHighestExpense() {
        return expenseRepository.getHighestExpense();
    }

    public Double getLowestExpense() {
        return expenseRepository.getLowestExpense();
    }

    public long getTotalTransactions() {
        return expenseRepository.count();
    }
    public List<Expense> searchByTitle(String title) {
        return expenseRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Expense> filterByCategory(String category) {
        return expenseRepository.findByCategory(category);
    }

    public List<Expense> filterByDate(LocalDate date) {
        return expenseRepository.findByExpenseDate(date);
    }
    public List<Expense> getExpensesByUser(User user) {
        return expenseRepository.findByUser(user);
    }

    public List<Expense> searchByTitle(User user, String title) {
        return expenseRepository.findByUserAndTitleContainingIgnoreCase(user, title);
    }

    public List<Expense> filterByCategory(User user, String category) {
        return expenseRepository.findByUserAndCategory(user, category);
    }

    public List<Expense> filterByDate(User user, LocalDate date) {
        return expenseRepository.findByUserAndExpenseDate(user, date);
    }
}

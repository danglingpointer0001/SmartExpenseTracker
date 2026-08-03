package com.exp.smartexpensetracker.controller;

import com.exp.smartexpensetracker.entity.Expense;
import com.exp.smartexpensetracker.service.ExpenseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping("/expenses")
    public String expensePage(Model model) {

        model.addAttribute("expense", new Expense());
        model.addAttribute("expenses", expenseService.getAllExpenses());

        return "expenses";
    }

    @PostMapping("/expenses")
    public String saveExpense(@ModelAttribute Expense expense) {

        expenseService.saveExpense(expense);

        return "redirect:/expenses";
    }

    @GetMapping("/expenses/delete/{id}")
    public String deleteExpense(@PathVariable Long id) {

        expenseService.deleteExpense(id);

        return "redirect:/expenses";
    }

    @GetMapping("/expenses/edit/{id}")
    public String editExpense(@PathVariable Long id, Model model) {

        model.addAttribute("expense",
                expenseService.getExpenseById(id));

        model.addAttribute("expenses",
                expenseService.getAllExpenses());

        return "expenses";
    }
}

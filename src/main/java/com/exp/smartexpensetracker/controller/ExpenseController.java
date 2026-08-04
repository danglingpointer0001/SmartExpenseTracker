package com.exp.smartexpensetracker.controller;

import com.exp.smartexpensetracker.entity.Expense;
import com.exp.smartexpensetracker.service.ExpenseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@Controller
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping("/expenses")
    public String expensePage(

            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) LocalDate date,
            Model model) {

        model.addAttribute("expense", new Expense());

        List<Expense> expenses;

        if (keyword != null && !keyword.isBlank()) {

            expenses = expenseService.searchByTitle(keyword);

        } else if (category != null && !category.isBlank()) {

            expenses = expenseService.filterByCategory(category);

        } else if (date != null) {

            expenses = expenseService.filterByDate(date);

        } else {

            expenses = expenseService.getAllExpenses();

        }

        model.addAttribute("expenses", expenses);

        model.addAttribute("totalExpense", expenseService.getTotalExpense());
        model.addAttribute("highestExpense", expenseService.getHighestExpense());
        model.addAttribute("lowestExpense", expenseService.getLowestExpense());
        model.addAttribute("totalTransactions", expenseService.getTotalTransactions());

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

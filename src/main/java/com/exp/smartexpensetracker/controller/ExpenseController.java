package com.exp.smartexpensetracker.controller;

import com.exp.smartexpensetracker.entity.Expense;
import com.exp.smartexpensetracker.entity.User;
import com.exp.smartexpensetracker.service.ExpenseService;
import com.exp.smartexpensetracker.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.exp.smartexpensetracker.service.CsvExportService;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

@Controller
public class ExpenseController {

    private final ExpenseService expenseService;
    private final UserService userService;
    private final CsvExportService csvExportService;

    public ExpenseController(ExpenseService expenseService,
                             UserService userService,
                             CsvExportService csvExportService) {

        this.expenseService = expenseService;
        this.userService = userService;
        this.csvExportService = csvExportService;
    }

    @GetMapping("/expenses")
    public String expensePage(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) LocalDate date,
            Authentication authentication,
            Model model) {

        User user = userService.getUserByEmailOrThrow(authentication.getName());

        model.addAttribute("expense", new Expense());

        List<Expense> expenses;

        if (keyword != null && !keyword.isBlank()) {

            expenses = expenseService.searchByTitle(user, keyword);

        } else if (category != null && !category.isBlank()) {

            expenses = expenseService.filterByCategory(user, category);

        } else if (date != null) {

            expenses = expenseService.filterByDate(user, date);

        } else {

            expenses = expenseService.getExpensesByUser(user);

        }

        model.addAttribute("expenses", expenses);

        model.addAttribute("totalExpense", expenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum());

        model.addAttribute("totalTransactions", expenses.size());

        model.addAttribute("highestExpense",
                expenses.stream()
                        .mapToDouble(Expense::getAmount)
                        .max()
                        .orElse(0));

        model.addAttribute("lowestExpense",
                expenses.stream()
                        .mapToDouble(Expense::getAmount)
                        .min()
                        .orElse(0));

        return "expenses";
    }

    @PostMapping("/expenses")
    public String saveExpense(@Valid @ModelAttribute("expense") Expense expense,
                              BindingResult result,
                              Authentication authentication,
                              Model model) {
        if (result.hasErrors()) {

            User user = userService.getUserByEmailOrThrow(authentication.getName());

            model.addAttribute("expenses", expenseService.getExpensesByUser(user));
            model.addAttribute("totalExpense", expenseService.getExpensesByUser(user)
                    .stream()
                    .mapToDouble(Expense::getAmount)
                    .sum());
            model.addAttribute("totalTransactions",
                    expenseService.getExpensesByUser(user).size());

            model.addAttribute("highestExpense",
                    expenseService.getExpensesByUser(user)
                            .stream()
                            .mapToDouble(Expense::getAmount)
                            .max()
                            .orElse(0));

            model.addAttribute("lowestExpense",
                    expenseService.getExpensesByUser(user)
                            .stream()
                            .mapToDouble(Expense::getAmount)
                            .min()
                            .orElse(0));

            return "expenses";
        }

        User user = userService.getUserByEmailOrThrow(authentication.getName());

        expense.setUser(user);

        expenseService.saveExpense(expense);

        return "redirect:/expenses";
    }

    @GetMapping("/expenses/edit/{id}")
    public String editExpense(@PathVariable Long id,
                              Authentication authentication,
                              Model model) {

        User user = userService.getUserByEmailOrThrow(authentication.getName());

        model.addAttribute("expense",
                expenseService.getExpenseById(id));

        model.addAttribute("expenses",
                expenseService.getExpensesByUser(user));

        model.addAttribute("totalExpense",
                expenseService.getExpensesByUser(user)
                        .stream()
                        .mapToDouble(Expense::getAmount)
                        .sum());

        model.addAttribute("totalTransactions",
                expenseService.getExpensesByUser(user).size());

        model.addAttribute("highestExpense",
                expenseService.getExpensesByUser(user)
                        .stream()
                        .mapToDouble(Expense::getAmount)
                        .max()
                        .orElse(0));

        model.addAttribute("lowestExpense",
                expenseService.getExpensesByUser(user)
                        .stream()
                        .mapToDouble(Expense::getAmount)
                        .min()
                        .orElse(0));

        return "expenses";
    }

    @GetMapping("/expenses/delete/{id}")
    public String deleteExpense(@PathVariable Long id) {

        expenseService.deleteExpense(id);

        return "redirect:/expenses";
    }
    @GetMapping("/expenses/export/csv")
    public ResponseEntity<String> exportCsv(Authentication authentication) {

        User user = userService.getUserByEmailOrThrow(authentication.getName());

        String csv = csvExportService.exportExpenses(
                expenseService.getExpensesByUser(user)
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=expenses.csv")
                .contentType(MediaType.TEXT_PLAIN)
                .body(csv);
    }
}

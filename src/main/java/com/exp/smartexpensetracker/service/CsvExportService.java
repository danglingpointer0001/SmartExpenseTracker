package com.exp.smartexpensetracker.service;

import com.exp.smartexpensetracker.entity.Expense;
import com.opencsv.CSVWriter;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.List;

@Service
public class CsvExportService {

    public String exportExpenses(List<Expense> expenses) {

        StringWriter writer = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(writer);

        // Header
        csvWriter.writeNext(new String[]{
                "Title",
                "Category",
                "Amount",
                "Date"
        });

        // Data
        for (Expense expense : expenses) {

            csvWriter.writeNext(new String[]{
                    expense.getTitle(),
                    expense.getCategory(),
                    String.valueOf(expense.getAmount()),
                    String.valueOf(expense.getExpenseDate())
            });

        }

        try {
            csvWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return writer.toString();
    }
}

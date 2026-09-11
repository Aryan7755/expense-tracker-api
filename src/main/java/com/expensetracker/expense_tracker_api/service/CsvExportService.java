package com.expensetracker.expense_tracker_api.service;

import com.expensetracker.expense_tracker_api.dto.ExpenseResponseDTO;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.List;

@Service
public class CsvExportService {

    public ByteArrayInputStream exportExpensesToCsv(List<ExpenseResponseDTO> expenses) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);

        // CSV Header
        writer.println("ID,Title,Amount,Date,Category,Description");

        for (ExpenseResponseDTO expense : expenses) {
            writer.printf("%d,\"%s\",%.2f,%s,\"%s\",\"%s\"\n",
                    expense.getId(),
                    expense.getTitle(),
                    expense.getAmount(),
                    expense.getDate(),
                    expense.getCategoryName(),
                    expense.getDescription() != null ? expense.getDescription() : "");
        }

        writer.flush();
        return new ByteArrayInputStream(out.toByteArray());
    }
}
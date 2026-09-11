package com.expensetracker.expense_tracker_api.controller;

import com.expensetracker.expense_tracker_api.dto.ExpenseRequestDTO;
import com.expensetracker.expense_tracker_api.dto.ExpenseResponseDTO;
import com.expensetracker.expense_tracker_api.service.CsvExportService;
import com.expensetracker.expense_tracker_api.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;

@Tag(name = "Expenses", description = "Endpoints for creating, filtering, and exporting user expenses")
@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;
    private final CsvExportService csvExportService;

    @Operation(summary = "Create a new expense", description = "Creates an expense record and returns a budget warning if monthly limit is exceeded")
    @PostMapping
    public ResponseEntity<ExpenseResponseDTO> createExpense(@Valid @RequestBody ExpenseRequestDTO dto) {
        return new ResponseEntity<>(expenseService.createExpense(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Get filtered expenses", description = "Retrieves expenses filtered optionally by category ID and date range")
    @GetMapping
    public ResponseEntity<List<ExpenseResponseDTO>> getExpenses(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(expenseService.getFilteredExpenses(categoryId, startDate, endDate));
    }

    @Operation(summary = "Export expenses to CSV", description = "Generates and downloads a CSV file containing filtered user expenses")
    @GetMapping("/export")
    public ResponseEntity<Resource> exportCsv(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<ExpenseResponseDTO> expenses = expenseService.getFilteredExpenses(categoryId, startDate, endDate);
        ByteArrayInputStream stream = csvExportService.exportExpensesToCsv(expenses);
        InputStreamResource file = new InputStreamResource(stream);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=expenses.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(file);
    }
}
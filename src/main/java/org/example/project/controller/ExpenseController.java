package org.example.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.project.dto.ApiDTOs.*;
import org.example.project.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@Tag(name = "Expense Management", description = "APIs for managing expenses and categories")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;


    @Operation(summary = "Get all categories", description = "Retrieve a list of all expense categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved categories"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories() {
        try {
            List<String> categories = expenseService.getAllCategories();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("DATABASE_ERROR", e.getMessage()));
        }
    }

    @Operation(summary = "Add a new category", description = "Create a new expense category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/categories")
    public ResponseEntity<?> addCategory(@RequestBody CategoryRequest request) {
        try {
            CategoryResponse response = expenseService.addCategory(request.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("INVALID_INPUT", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("DATABASE_ERROR", e.getMessage()));
        }
    }

    @Operation(summary = "Delete a category", description = "Delete an expense category by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Cannot delete category with expenses"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/categories/{categoryName}")
    public ResponseEntity<?> deleteCategory(
            @Parameter(description = "Name of the category to delete") 
            @PathVariable String categoryName,
            @Parameter(description = "Whether to delete all expenses in this category") 
            @RequestParam(defaultValue = "false") boolean deleteExpenses) {
        try {
            CategoryResponse response = expenseService.deleteCategory(categoryName, deleteExpenses);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("CANNOT_DELETE", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("DATABASE_ERROR", e.getMessage()));
        }
    }


    @Operation(summary = "Get all expenses", description = "Retrieve a list of all expenses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved expenses"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<?> getAllExpenses() {
        try {
            List<String> expenses = expenseService.getAllExpenses();
            return ResponseEntity.ok(expenses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("DATABASE_ERROR", e.getMessage()));
        }
    }

    @Operation(summary = "Get expenses by category", description = "Retrieve all expenses for a specific category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved expenses"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/category/{categoryName}")
    public ResponseEntity<?> getExpensesByCategory(
            @Parameter(description = "Name of the category") 
            @PathVariable String categoryName) {
        try {
            List<String> expenses = expenseService.getExpensesByCategory(categoryName);
            return ResponseEntity.ok(expenses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("DATABASE_ERROR", e.getMessage()));
        }
    }

    @Operation(summary = "Add a new expense", description = "Create a new expense entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Expense created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<?> addExpense(@RequestBody ExpenseRequest request) {
        try {
            ExpenseResponse response = expenseService.addExpense(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("INVALID_INPUT", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("DATABASE_ERROR", e.getMessage()));
        }
    }

    @Operation(summary = "Delete an expense", description = "Delete an expense by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expense deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Expense not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{expenseId}")
    public ResponseEntity<?> deleteExpense(
            @Parameter(description = "ID of the expense to delete") 
            @PathVariable int expenseId) {
        try {
            ExpenseResponse response = expenseService.deleteExpense(expenseId);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("NOT_FOUND", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("DATABASE_ERROR", e.getMessage()));
        }
    }


    @Operation(summary = "Generate expense report", description = "Get a comprehensive report of all expenses with category breakdowns")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report generated successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/report")
    public ResponseEntity<?> generateReport() {
        try {
            ReportResponse report = expenseService.generateReport();
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("REPORT_ERROR", e.getMessage()));
        }
    }

    @Operation(summary = "Get total expenses", description = "Get the total amount of all expenses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Total calculated successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/total")
    public ResponseEntity<?> getTotalExpenses() {
        try {
            double total = expenseService.getTotalExpenses();
            return ResponseEntity.ok(new TotalResponse(total));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("DATABASE_ERROR", e.getMessage()));
        }
    }

    @Operation(summary = "Get category total", description = "Get the total expenses for a specific category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Total calculated successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/category/{categoryName}/total")
    public ResponseEntity<?> getCategoryTotal(
            @Parameter(description = "Name of the category") 
            @PathVariable String categoryName) {
        try {
            double total = expenseService.getTotalExpensesByCategory(categoryName);
            return ResponseEntity.ok(new TotalResponse(total, categoryName));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("DATABASE_ERROR", e.getMessage()));
        }
    }

    private static class TotalResponse {
        private double total;
        private String category;

        public TotalResponse(double total) {
            this.total = total;
        }

        public TotalResponse(double total, String category) {
            this.total = total;
            this.category = category;
        }

        public double getTotal() {
            return total;
        }

        public String getCategory() {
            return category;
        }
    }
}
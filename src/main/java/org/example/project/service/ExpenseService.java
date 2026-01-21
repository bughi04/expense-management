package org.example.project.service;

import org.example.project.DatabaseManager;
import org.example.project.dto.ApiDTOs.*;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    private final DatabaseManager databaseManager;

    public ExpenseService() {
        this.databaseManager = new DatabaseManager();
    }

    public ExpenseService(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public List<String> getAllCategories() throws SQLException {
        return databaseManager.getCategories();
    }

    public CategoryResponse addCategory(String categoryName) throws SQLException {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be empty");
        }

        databaseManager.addCategory(categoryName);
        return new CategoryResponse(categoryName, "Category added successfully");
    }

    public CategoryResponse deleteCategory(String categoryName, boolean deleteExpenses) throws SQLException {
        boolean success = databaseManager.deleteCategory(categoryName, deleteExpenses);
        
        if (success) {
            return new CategoryResponse(categoryName, "Category deleted successfully");
        } else {
            throw new IllegalStateException("Cannot delete category with existing expenses. Set deleteExpenses=true to delete all expenses.");
        }
    }

    public List<String> getAllExpenses() throws SQLException {
        return databaseManager.getAllExpenses();
    }

    public List<String> getExpensesByCategory(String categoryName) throws SQLException {
        return databaseManager.getExpensesByCategory(categoryName);
    }

    public ExpenseResponse addExpense(ExpenseRequest request) throws Exception {
        if (request.getCategoryName() == null || request.getCategoryName().trim().isEmpty()) {
            throw new IllegalArgumentException("Category name is required");
        }
        if (request.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (request.getCurrency() == null || request.getCurrency().trim().isEmpty()) {
            throw new IllegalArgumentException("Currency is required");
        }

        databaseManager.addExpense(
                request.getCategoryName(),
                request.getAmount(),
                request.getTransactionDate(),
                request.getExpenseDate(),
                request.getCurrency()
        );

        return new ExpenseResponse("Expense added successfully");
    }

    public ExpenseResponse deleteExpense(int expenseId) throws SQLException {
        boolean success = databaseManager.deleteExpense(expenseId);
        
        if (success) {
            return new ExpenseResponse("Expense deleted successfully");
        } else {
            throw new IllegalStateException("Expense not found or already deleted");
        }
    }

    public ReportResponse generateReport() throws SQLException {
        double totalExpenses = databaseManager.getTotalExpenses();
        List<String> categories = databaseManager.getCategories();

        List<ReportResponse.CategoryBreakdown> breakdowns = categories.stream()
                .map(category -> {
                    try {
                        double categoryTotal = databaseManager.getTotalExpensesByCategory(category);
                        double percentage = totalExpenses > 0 ? (categoryTotal / totalExpenses) * 100 : 0;
                        return new ReportResponse.CategoryBreakdown(category, categoryTotal, percentage);
                    } catch (SQLException e) {
                        throw new RuntimeException("Error calculating category breakdown", e);
                    }
                })
                .collect(Collectors.toList());

        return new ReportResponse(totalExpenses, breakdowns);
    }

    public double getTotalExpensesByCategory(String categoryName) throws SQLException {
        return databaseManager.getTotalExpensesByCategory(categoryName);
    }

    public double getTotalExpenses() throws SQLException {
        return databaseManager.getTotalExpenses();
    }
}
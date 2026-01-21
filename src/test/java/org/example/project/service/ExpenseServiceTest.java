package org.example.project.service;

import org.example.project.DatabaseManager;
import org.example.project.dto.ApiDTOs.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock
    private DatabaseManager databaseManager;

    private ExpenseService expenseService;

    @BeforeEach
    void setUp() {
        expenseService = new ExpenseService(databaseManager);
    }


    @Test
    void testGetAllCategories_Success() throws SQLException {
        List<String> expectedCategories = Arrays.asList("Food", "Transport", "Entertainment");
        when(databaseManager.getCategories()).thenReturn(expectedCategories);

        List<String> actualCategories = expenseService.getAllCategories();

        assertEquals(expectedCategories, actualCategories);
        verify(databaseManager, times(1)).getCategories();
    }

    @Test
    void testAddCategory_Success() throws SQLException {
        String categoryName = "Food";
        doNothing().when(databaseManager).addCategory(categoryName);

        CategoryResponse response = expenseService.addCategory(categoryName);

        assertNotNull(response);
        assertEquals(categoryName, response.getName());
        assertEquals("Category added successfully", response.getMessage());
        verify(databaseManager, times(1)).addCategory(categoryName);
    }

    @Test
    void testAddCategory_EmptyName_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            expenseService.addCategory("");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            expenseService.addCategory("   ");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            expenseService.addCategory(null);
        });

        verifyNoInteractions(databaseManager);
    }

    @Test
    void testDeleteCategory_Success() throws SQLException {
        String categoryName = "Food";
        when(databaseManager.deleteCategory(categoryName, false)).thenReturn(true);

        CategoryResponse response = expenseService.deleteCategory(categoryName, false);

        assertNotNull(response);
        assertEquals(categoryName, response.getName());
        assertEquals("Category deleted successfully", response.getMessage());
        verify(databaseManager, times(1)).deleteCategory(categoryName, false);
    }

    @Test
    void testDeleteCategory_WithExpenses_ThrowsException() throws SQLException {
        String categoryName = "Food";
        when(databaseManager.deleteCategory(categoryName, false)).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> {
            expenseService.deleteCategory(categoryName, false);
        });

        verify(databaseManager, times(1)).deleteCategory(categoryName, false);
    }


    @Test
    void testGetAllExpenses_Success() throws SQLException {
        List<String> expectedExpenses = Arrays.asList(
                "[ID 1] Category: Food, Amount: 50.00 USD",
                "[ID 2] Category: Transport, Amount: 30.00 USD"
        );
        when(databaseManager.getAllExpenses()).thenReturn(expectedExpenses);

        List<String> actualExpenses = expenseService.getAllExpenses();

        assertEquals(expectedExpenses, actualExpenses);
        verify(databaseManager, times(1)).getAllExpenses();
    }

    @Test
    void testGetExpensesByCategory_Success() throws SQLException {
        String categoryName = "Food";
        List<String> expectedExpenses = Arrays.asList(
                "[ID 1] Amount: 50.00 USD, Transaction Date: 2024-01-01"
        );
        when(databaseManager.getExpensesByCategory(categoryName)).thenReturn(expectedExpenses);

        List<String> actualExpenses = expenseService.getExpensesByCategory(categoryName);

        assertEquals(expectedExpenses, actualExpenses);
        verify(databaseManager, times(1)).getExpensesByCategory(categoryName);
    }

    @Test
    void testAddExpense_Success() throws Exception {
        ExpenseRequest request = new ExpenseRequest(
                "Food", 50.0, "2024-01-01", "2024-01-01", "USD"
        );
        doNothing().when(databaseManager).addExpense(
                request.getCategoryName(),
                request.getAmount(),
                request.getTransactionDate(),
                request.getExpenseDate(),
                request.getCurrency()
        );

        ExpenseResponse response = expenseService.addExpense(request);

        assertNotNull(response);
        assertEquals("Expense added successfully", response.getMessage());
        verify(databaseManager, times(1)).addExpense(
                request.getCategoryName(),
                request.getAmount(),
                request.getTransactionDate(),
                request.getExpenseDate(),
                request.getCurrency()
        );
    }

    @Test
    void testAddExpense_InvalidInput_ThrowsException() {
        ExpenseRequest request1 = new ExpenseRequest("", 50.0, "2024-01-01", "2024-01-01", "USD");
        assertThrows(IllegalArgumentException.class, () -> {
            expenseService.addExpense(request1);
        });

        ExpenseRequest request2 = new ExpenseRequest(null, 50.0, "2024-01-01", "2024-01-01", "USD");
        assertThrows(IllegalArgumentException.class, () -> {
            expenseService.addExpense(request2);
        });

        ExpenseRequest request3 = new ExpenseRequest("Food", -50.0, "2024-01-01", "2024-01-01", "USD");
        assertThrows(IllegalArgumentException.class, () -> {
            expenseService.addExpense(request3);
        });

        ExpenseRequest request4 = new ExpenseRequest("Food", 0.0, "2024-01-01", "2024-01-01", "USD");
        assertThrows(IllegalArgumentException.class, () -> {
            expenseService.addExpense(request4);
        });

        ExpenseRequest request5 = new ExpenseRequest("Food", 50.0, "2024-01-01", "2024-01-01", "");
        assertThrows(IllegalArgumentException.class, () -> {
            expenseService.addExpense(request5);
        });

        verifyNoInteractions(databaseManager);
    }

    @Test
    void testDeleteExpense_Success() throws SQLException {
        int expenseId = 1;
        when(databaseManager.deleteExpense(expenseId)).thenReturn(true);

        ExpenseResponse response = expenseService.deleteExpense(expenseId);

        assertNotNull(response);
        assertEquals("Expense deleted successfully", response.getMessage());
        verify(databaseManager, times(1)).deleteExpense(expenseId);
    }

    @Test
    void testDeleteExpense_NotFound_ThrowsException() throws SQLException {
        int expenseId = 999;
        when(databaseManager.deleteExpense(expenseId)).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> {
            expenseService.deleteExpense(expenseId);
        });

        verify(databaseManager, times(1)).deleteExpense(expenseId);
    }


    @Test
    void testGenerateReport_Success() throws SQLException {
        when(databaseManager.getTotalExpenses()).thenReturn(100.0);
        when(databaseManager.getCategories()).thenReturn(Arrays.asList("Food", "Transport"));
        when(databaseManager.getTotalExpensesByCategory("Food")).thenReturn(60.0);
        when(databaseManager.getTotalExpensesByCategory("Transport")).thenReturn(40.0);

        ReportResponse report = expenseService.generateReport();

        assertNotNull(report);
        assertEquals(100.0, report.getTotalExpenses());
        assertEquals(2, report.getCategoryBreakdowns().size());

        ReportResponse.CategoryBreakdown foodBreakdown = report.getCategoryBreakdowns().get(0);
        assertEquals("Food", foodBreakdown.getCategory());
        assertEquals(60.0, foodBreakdown.getTotal());
        assertEquals(60.0, foodBreakdown.getPercentage(), 0.01);

        ReportResponse.CategoryBreakdown transportBreakdown = report.getCategoryBreakdowns().get(1);
        assertEquals("Transport", transportBreakdown.getCategory());
        assertEquals(40.0, transportBreakdown.getTotal());
        assertEquals(40.0, transportBreakdown.getPercentage(), 0.01);

        verify(databaseManager, times(1)).getTotalExpenses();
        verify(databaseManager, times(1)).getCategories();
        verify(databaseManager, times(1)).getTotalExpensesByCategory("Food");
        verify(databaseManager, times(1)).getTotalExpensesByCategory("Transport");
    }

    @Test
    void testGenerateReport_NoExpenses() throws SQLException {
        when(databaseManager.getTotalExpenses()).thenReturn(0.0);
        when(databaseManager.getCategories()).thenReturn(Arrays.asList("Food"));
        when(databaseManager.getTotalExpensesByCategory("Food")).thenReturn(0.0);

        ReportResponse report = expenseService.generateReport();

        assertNotNull(report);
        assertEquals(0.0, report.getTotalExpenses());
        assertEquals(1, report.getCategoryBreakdowns().size());

        ReportResponse.CategoryBreakdown breakdown = report.getCategoryBreakdowns().get(0);
        assertEquals(0.0, breakdown.getPercentage());
    }

    @Test
    void testGetTotalExpenses_Success() throws SQLException {
        when(databaseManager.getTotalExpenses()).thenReturn(250.75);

        double total = expenseService.getTotalExpenses();

        assertEquals(250.75, total);
        verify(databaseManager, times(1)).getTotalExpenses();
    }

    @Test
    void testGetTotalExpensesByCategory_Success() throws SQLException {
        String categoryName = "Food";
        when(databaseManager.getTotalExpensesByCategory(categoryName)).thenReturn(125.50);

        double total = expenseService.getTotalExpensesByCategory(categoryName);

        assertEquals(125.50, total);
        verify(databaseManager, times(1)).getTotalExpensesByCategory(categoryName);
    }
}
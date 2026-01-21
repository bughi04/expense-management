package org.example.project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.concurrent.Task;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpenseManagerApp extends Application {

    private final DatabaseManager dbManager = new DatabaseManager();

    private Scene mainScene;

    private TextField categoryInput;
    private ListView<String> categoryListView;

    private ComboBox<String> categoryComboBox;
    private TextField amountInput;
    private DatePicker transactionDatePicker;
    private DatePicker expenseDatePicker;
    private ComboBox<String> currencyComboBox;
    private ListView<String> expenseListView;
    private Label statusMessage;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Expense Manager");

        createMainScene(primaryStage);

        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    private void createMainScene(Stage stage) {
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Expense Manager");
        titleLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");

        Button manageCategoriesButton = new Button("Manage Categories");
        manageCategoriesButton.setMaxWidth(Double.MAX_VALUE);
        manageCategoriesButton.setOnAction(e -> showCategoryManagementScene(stage));

        Button manageExpensesButton = new Button("Manage Expenses");
        manageExpensesButton.setMaxWidth(Double.MAX_VALUE);
        manageExpensesButton.setOnAction(e -> showExpenseManagementScene(stage));

        Button viewReportsButton = new Button("View Reports");
        viewReportsButton.setMaxWidth(Double.MAX_VALUE);
        viewReportsButton.setOnAction(e -> showReportsScene(stage));

        Button currencyPredictionsButton = new Button("Currency Predictions (AI)");
        currencyPredictionsButton.setMaxWidth(Double.MAX_VALUE);
        currencyPredictionsButton.setStyle("-fx-base: #a3d9ff;");
        currencyPredictionsButton.setOnAction(e -> showCurrencyPredictionScene(stage));

        mainLayout.getChildren().addAll(
                titleLabel,
                manageCategoriesButton,
                manageExpensesButton,
                viewReportsButton,
                currencyPredictionsButton
        );

        mainScene = new Scene(mainLayout, 400, 350);
    }

    private void showCategoryManagementScene(Stage stage) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Manage Categories");
        titleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        categoryInput = new TextField();
        categoryInput.setPromptText("Category Name");
        categoryInput.setMaxWidth(Double.MAX_VALUE);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button addButton = new Button("Add Category");
        addButton.setPrefWidth(150);
        addButton.setOnAction(e -> addCategory());

        Button deleteButton = new Button("Delete Selected");
        deleteButton.setPrefWidth(150);
        deleteButton.setOnAction(e -> deleteSelectedCategory());

        buttonBox.getChildren().addAll(addButton, deleteButton);

        categoryListView = new ListView<>();
        categoryListView.setPrefHeight(200);

        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(e -> deleteSelectedCategory());
        contextMenu.getItems().add(deleteMenuItem);

        categoryListView.setContextMenu(contextMenu);

        Button backButton = new Button("Back to Main Menu");
        backButton.setMaxWidth(Double.MAX_VALUE);
        backButton.setOnAction(e -> stage.setScene(mainScene));

        layout.getChildren().addAll(
                titleLabel,
                categoryInput,
                buttonBox,
                categoryListView,
                backButton
        );

        loadCategories();

        Scene scene = new Scene(layout, 400, 500);
        stage.setScene(scene);
    }

    private void showExpenseManagementScene(Stage stage) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Manage Expenses");
        titleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        categoryComboBox = new ComboBox<>();
        categoryComboBox.setPromptText("Select Category");
        categoryComboBox.setMaxWidth(Double.MAX_VALUE);

        amountInput = new TextField();
        amountInput.setPromptText("Amount");
        amountInput.setMaxWidth(Double.MAX_VALUE);

        transactionDatePicker = new DatePicker();
        transactionDatePicker.setPromptText("Transaction Date");
        transactionDatePicker.setMaxWidth(Double.MAX_VALUE);

        expenseDatePicker = new DatePicker();
        expenseDatePicker.setPromptText("Expense Date");
        expenseDatePicker.setMaxWidth(Double.MAX_VALUE);

        currencyComboBox = new ComboBox<>();
        currencyComboBox.setPromptText("Select Currency");
        currencyComboBox.getItems().addAll("USD", "EUR", "GBP", "JPY", "AUD", "RON");
        currencyComboBox.setMaxWidth(Double.MAX_VALUE);

        Label infoLabel = new Label("All expenses are converted to and stored in USD.");

        statusMessage = new Label();
        statusMessage.setMaxWidth(Double.MAX_VALUE);

        Button addButton = new Button("Add Expense");
        addButton.setMaxWidth(Double.MAX_VALUE);
        addButton.setOnAction(e -> addExpense());

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button viewAllButton = new Button("View All Expenses");
        viewAllButton.setPrefWidth(200);
        viewAllButton.setOnAction(e -> viewAllExpenses());

        Button deleteButton = new Button("Delete Selected");
        deleteButton.setPrefWidth(150);
        deleteButton.setOnAction(e -> deleteSelectedExpense());

        buttonBox.getChildren().addAll(viewAllButton, deleteButton);

        Button viewByCategoryButton = new Button("View Expenses by Selected Category");
        viewByCategoryButton.setMaxWidth(Double.MAX_VALUE);
        viewByCategoryButton.setOnAction(e -> viewExpensesByCategory());

        expenseListView = new ListView<>();
        expenseListView.setPrefHeight(200);

        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(e -> deleteSelectedExpense());
        contextMenu.getItems().add(deleteMenuItem);

        expenseListView.setContextMenu(contextMenu);

        Button backButton = new Button("Back to Main Menu");
        backButton.setMaxWidth(Double.MAX_VALUE);
        backButton.setOnAction(e -> stage.setScene(mainScene));

        layout.getChildren().addAll(
                titleLabel,
                categoryComboBox,
                amountInput,
                transactionDatePicker,
                expenseDatePicker,
                currencyComboBox,
                infoLabel,
                statusMessage,
                addButton,
                buttonBox,
                viewByCategoryButton,
                expenseListView,
                backButton
        );

        loadCategoriesIntoComboBox();

        Scene scene = new Scene(layout, 500, 700);
        stage.setScene(scene);
    }

    private void showReportsScene(Stage stage) {
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));

        Label titleLabel = new Label("Expense Reports");
        titleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        VBox topBox = new VBox(10);
        topBox.setAlignment(Pos.CENTER);
        topBox.getChildren().add(titleLabel);

        VBox reportsContent = new VBox(15);
        reportsContent.setPadding(new Insets(10));

        Label totalExpensesLabel = new Label("Loading total expenses...");
        totalExpensesLabel.setStyle("-fx-font-weight: bold;");

        Label categoryBreakdownLabel = new Label("Category Breakdown:");
        categoryBreakdownLabel.setStyle("-fx-font-weight: bold;");

        ListView<String> categoryBreakdownList = new ListView<>();
        categoryBreakdownList.setPrefHeight(200);

        try {
            double totalExpenses = dbManager.getTotalExpenses();
            totalExpensesLabel.setText(String.format("Total Expenses: $%.2f USD", totalExpenses));

            List<String> categories = dbManager.getCategories();
            ObservableList<String> breakdownItems = FXCollections.observableArrayList();

            for (String category : categories) {
                double categoryTotal = dbManager.getTotalExpensesByCategory(category);
                double percentage = totalExpenses > 0 ? (categoryTotal / totalExpenses) * 100 : 0;

                breakdownItems.add(String.format(
                        "%s: $%.2f (%.1f%%)",
                        category, categoryTotal, percentage
                ));
            }

            categoryBreakdownList.setItems(breakdownItems);

        } catch (SQLException e) {
            totalExpensesLabel.setText("Error loading expenses: " + e.getMessage());
        }

        reportsContent.getChildren().addAll(
                totalExpensesLabel,
                new Separator(),
                categoryBreakdownLabel,
                categoryBreakdownList
        );

        Button backButton = new Button("Back to Main Menu");
        backButton.setMaxWidth(Double.MAX_VALUE);
        backButton.setOnAction(e -> stage.setScene(mainScene));

        VBox bottomBox = new VBox(10);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.getChildren().add(backButton);

        layout.setTop(topBox);
        layout.setCenter(reportsContent);
        layout.setBottom(bottomBox);

        Scene scene = new Scene(layout, 500, 500);
        stage.setScene(scene);
    }

    private void showCurrencyPredictionScene(Stage stage) {
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));

        Label titleLabel = new Label("Currency Exchange Rate Predictions");
        titleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        VBox topBox = new VBox(10);
        topBox.setAlignment(Pos.CENTER);
        topBox.getChildren().add(titleLabel);

        VBox contentBox = new VBox(15);
        contentBox.setPadding(new Insets(10));

        Label explanationLabel = new Label(
                "This screen uses machine learning (linear regression) to predict currency " +
                        "exchange rate trends based on historical data from the past 30 days. " +
                        "The predictions show the expected exchange rates for the next 7 days."
        );
        explanationLabel.setWrapText(true);

        Label statusLabel = new Label("Loading predictions...");
        statusLabel.setStyle("-fx-font-weight: bold;");

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setPrefSize(30, 30);

        HBox statusBox = new HBox(10);
        statusBox.setAlignment(Pos.CENTER);
        statusBox.getChildren().addAll(progressIndicator, statusLabel);

        TableView<CurrencyPredictionData> tableView = new TableView<>();
        tableView.setPrefHeight(300);

        TableColumn<CurrencyPredictionData, String> currencyCol = new TableColumn<>("Currency");
        currencyCol.setCellValueFactory(new PropertyValueFactory<>("currency"));
        currencyCol.setPrefWidth(80);

        TableColumn<CurrencyPredictionData, String> currentRateCol = new TableColumn<>("Current Rate");
        currentRateCol.setCellValueFactory(new PropertyValueFactory<>("currentRate"));
        currentRateCol.setPrefWidth(110);

        TableColumn<CurrencyPredictionData, String> predictedRateCol = new TableColumn<>("Predicted in 7 Days");
        predictedRateCol.setCellValueFactory(new PropertyValueFactory<>("predictedRate"));
        predictedRateCol.setPrefWidth(130);

        TableColumn<CurrencyPredictionData, String> changeCol = new TableColumn<>("% Change");
        changeCol.setCellValueFactory(new PropertyValueFactory<>("changePercentage"));
        changeCol.setPrefWidth(100);

        TableColumn<CurrencyPredictionData, String> recommendationCol = new TableColumn<>("Recommendation");
        recommendationCol.setCellValueFactory(new PropertyValueFactory<>("recommendation"));
        recommendationCol.setPrefWidth(200);

        tableView.getColumns().addAll(currencyCol, currentRateCol, predictedRateCol, changeCol, recommendationCol);

        contentBox.getChildren().addAll(explanationLabel, statusBox, tableView);

        Task<ObservableList<CurrencyPredictionData>> loadPredictionsTask = new Task<>() {
            @Override
            protected ObservableList<CurrencyPredictionData> call() throws Exception {
                CurrencyPredictor predictor = new CurrencyPredictor();
                List<String> currencies = predictor.getSupportedCurrencies();

                ObservableList<CurrencyPredictionData> predictionData = FXCollections.observableArrayList();

                for (String currency : currencies) {
                    Map<LocalDate, Double> historicalRates = predictor.getHistoricalRates(currency);
                    Map<LocalDate, Double> predictions = predictor.predictFutureRates(currency);

                    double currentRate = historicalRates.values().stream()
                            .reduce((first, second) -> second).orElse(1.0);

                    double futureRate = predictions.values().stream()
                            .reduce((first, second) -> second).orElse(currentRate);

                    double changePercentage = ((futureRate - currentRate) / currentRate) * 100;

                    String recommendation;
                    if (Math.abs(changePercentage) < 0.5) {
                        recommendation = "Stable";
                    } else if (changePercentage > 0) {
                        recommendation = "USD likely to strengthen";
                    } else {
                        recommendation = "USD likely to weaken";
                    }

                    CurrencyPredictionData data = new CurrencyPredictionData(
                            currency,
                            String.format("1 USD = %.4f %s", currentRate, currency),
                            String.format("1 USD = %.4f %s", futureRate, currency),
                            String.format("%.2f%%", changePercentage),
                            recommendation
                    );

                    predictionData.add(data);
                }

                return predictionData;
            }
        };

        loadPredictionsTask.setOnSucceeded(e -> {
            ObservableList<CurrencyPredictionData> data = loadPredictionsTask.getValue();
            tableView.setItems(data);
            statusBox.getChildren().clear();
            statusLabel.setText("Predictions loaded successfully.");
            statusLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            statusBox.getChildren().add(statusLabel);
        });

        loadPredictionsTask.setOnFailed(e -> {
            Throwable exception = loadPredictionsTask.getException();
            statusBox.getChildren().clear();
            statusLabel.setText("Error loading predictions: " + exception.getMessage());
            statusLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            statusBox.getChildren().add(statusLabel);
        });

        Thread thread = new Thread(loadPredictionsTask);
        thread.setDaemon(true);
        thread.start();

        Button backButton = new Button("Back to Main Menu");
        backButton.setMaxWidth(Double.MAX_VALUE);
        backButton.setOnAction(e -> stage.setScene(mainScene));

        VBox bottomBox = new VBox(10);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.getChildren().add(backButton);

        layout.setTop(topBox);
        layout.setCenter(contentBox);
        layout.setBottom(bottomBox);

        Scene scene = new Scene(layout, 650, 500);
        stage.setScene(scene);
    }

    private void loadCategories() {
        try {
            List<String> categories = dbManager.getCategories();
            categoryListView.getItems().setAll(categories);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to load categories: " + e.getMessage());
        }
    }

    private void loadCategoriesIntoComboBox() {
        try {
            List<String> categories = dbManager.getCategories();
            categoryComboBox.getItems().setAll(categories);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to load categories: " + e.getMessage());
        }
    }

    private void addCategory() {
        String categoryName = categoryInput.getText().trim();
        if (categoryName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Category name cannot be empty.");
            return;
        }

        try {
            dbManager.addCategory(categoryName);
            loadCategories();
            categoryInput.clear();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Category added successfully.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to add category: " + e.getMessage());
        }
    }

    private void deleteSelectedCategory() {
        String selectedCategory = categoryListView.getSelectionModel().getSelectedItem();
        if (selectedCategory == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a category to delete.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Delete Category: " + selectedCategory);
        confirmAlert.setContentText("Do you want to delete all expenses in this category as well?");

        ButtonType buttonTypeYes = new ButtonType("Yes, delete all");
        ButtonType buttonTypeNo = new ButtonType("No, keep expenses");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        confirmAlert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo, buttonTypeCancel);

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isEmpty() || result.get() == buttonTypeCancel) {
            return;
        }

        boolean deleteExpenses = result.get() == buttonTypeYes;

        try {
            boolean success = dbManager.deleteCategory(selectedCategory, deleteExpenses);

            if (success) {
                loadCategories();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Category deleted successfully.");
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning",
                        "Unable to delete category. It contains expenses. Please delete expenses first or choose to delete all expenses.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error deleting category: " + e.getMessage());
        }
    }

    private void addExpense() {
        String categoryName = categoryComboBox.getValue();
        String currency = currencyComboBox.getValue();

        if (categoryName == null || amountInput.getText().isEmpty() ||
                transactionDatePicker.getValue() == null || expenseDatePicker.getValue() == null || currency == null) {
            setStatusMessage("All fields must be filled out.", "error");
            return;
        }

        try {
            double amount = Double.parseDouble(amountInput.getText());
            dbManager.addExpense(categoryName, amount, transactionDatePicker.getValue().toString(),
                    expenseDatePicker.getValue().toString(), currency);

            setStatusMessage("Expense added successfully in USD.", "success");

            amountInput.clear();
            transactionDatePicker.setValue(null);
            expenseDatePicker.setValue(null);
            currencyComboBox.setValue(null);

            if (categoryName.equals(categoryComboBox.getValue())) {
                viewExpensesByCategory();
            }

        } catch (NumberFormatException e) {
            setStatusMessage("Invalid amount. Please enter a numeric value.", "error");
        } catch (Exception e) {
            setStatusMessage("Error adding expense: " + e.getMessage(), "error");
        }
    }

    private void deleteSelectedExpense() {
        String selectedExpense = expenseListView.getSelectionModel().getSelectedItem();
        if (selectedExpense == null || selectedExpense.startsWith("No expenses found")) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a valid expense to delete.");
            return;
        }

        Pattern pattern = Pattern.compile("\\[ID (\\d+)\\]");
        Matcher matcher = pattern.matcher(selectedExpense);
        if (!matcher.find()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not identify expense ID.");
            return;
        }

        int expenseId = Integer.parseInt(matcher.group(1));

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Delete Expense");
        confirmAlert.setContentText("Are you sure you want to delete this expense?\n\n" + selectedExpense);

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        try {
            boolean success = dbManager.deleteExpense(expenseId);

            if (success) {
                if (categoryComboBox.getValue() != null) {
                    viewExpensesByCategory();
                } else {
                    viewAllExpenses();
                }

                showAlert(Alert.AlertType.INFORMATION, "Success", "Expense deleted successfully.");
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning", "Unable to delete expense. It may have been deleted already.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error deleting expense: " + e.getMessage());
        }
    }

    private void viewAllExpenses() {
        try {
            List<String> expenses = dbManager.getAllExpenses();
            if (expenses.isEmpty()) {
                expenseListView.getItems().setAll("No expenses found.");
            } else {
                expenseListView.getItems().setAll(expenses);
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to load expenses: " + e.getMessage());
        }
    }

    private void viewExpensesByCategory() {
        String categoryName = categoryComboBox.getValue();
        if (categoryName == null) {
            setStatusMessage("Please select a category.", "error");
            return;
        }

        try {
            List<String> expenses = dbManager.getExpensesByCategory(categoryName);
            if (expenses.isEmpty()) {
                expenseListView.getItems().setAll("No expenses found for category: " + categoryName);
            } else {
                expenseListView.getItems().setAll(expenses);
            }
        } catch (SQLException e) {
            setStatusMessage("Error loading expenses: " + e.getMessage(), "error");
        }
    }

    private void setStatusMessage(String message, String type) {
        Platform.runLater(() -> {
            statusMessage.setText(message);
            statusMessage.setStyle(type.equals("error") ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
        });
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
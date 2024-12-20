import java.sql.*;
import java.util.*;
import java.text.DecimalFormat;

public class Main {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/tourist_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "mysqlpasswordpaverion09";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\nCarbon Footprint Calculator");
                System.out.println("-------------------------------");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
                if (scanner.hasNextInt()) {
                    int choice = scanner.nextInt();
                    switch (choice) {
                        case 1:
                            register(scanner, connection);
                            break;
                        case 2:
                            login(scanner, connection);
                            break;
                        case 3:
                            System.out.println("Exiting...");
                            scanner.close();
                            return;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next();
                }
            }
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }

    private static void register(Scanner scanner, Connection connection) {
        try {
            System.out.print("Enter your username: ");
            String username = scanner.next();

            System.out.print("Enter your password: ");
            String password = scanner.next();

            String query = "INSERT INTO users (username, password) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.executeUpdate();
            }

            System.out.println("Registration successful!");
        } catch (SQLException e) {
            System.out.println("Error during registration: " + e.getMessage());
        }
    }

    private static void login(Scanner scanner, Connection connection) {
        try {
            System.out.print("Enter your username: ");
            String username = scanner.next();

            System.out.print("Enter your password: ");
            String password = scanner.next();

            String query = "SELECT id, username FROM users WHERE BINARY username = ? AND BINARY password = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int userId = rs.getInt("id");
                        System.out.println("Login successful! Welcome, " + username + "!");
                        manageCarbonFootprint(scanner, connection, userId);
                    } else {
                        System.out.println("Invalid username or password. Please try again.");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error during login: " + e.getMessage());
        }
    }

    private static void manageCarbonFootprint(Scanner scanner, Connection connection, int userId) {
        while (true) {
            System.out.println("\nManage Carbon Footprint");
            System.out.println("-------------------------------");
            System.out.println("1. Add Electricity Usage");
            System.out.println("2. Add Vehicle Travel");
            System.out.println("3. Add Air Travel");
            System.out.println("4. Add Waste");
            System.out.println("5. Display All Entries");
            System.out.println("6. Display Total Carbon Footprint");
            System.out.println("7. Logout");
            System.out.print("Enter your choice: ");
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        addElectricity(scanner, connection, userId);
                        break;
                    case 2:
                        addVehicle(scanner, connection, userId);
                        break;
                    case 3:
                        addAirTravel(scanner, connection, userId);
                        break;
                    case 4:
                        addWaste(scanner, connection, userId);
                        break;
                    case 5:
                        readFootprintEntries(connection, userId);
                        break;
                    case 6:
                        displayTotalCarbonFootprint(connection, userId);
                        break;
                    case 7:
                        System.out.println("Logging out...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        }
    }

    private static void addElectricity(Scanner scanner, Connection connection, int userId) {
        System.out.print("Enter electricity usage in kWh: ");
        if (scanner.hasNextDouble()) {
            double kWh = scanner.nextDouble();
            if (kWh >= 0) {
                saveCarbonFootprintEntry(connection, userId, "Electricity", kWh);
            } else {
                System.out.println("Electricity usage cannot be negative.");
            }
        } else {
            System.out.println("Invalid input. Please enter a numeric value.");
            scanner.next();
        }
    }

    private static void addVehicle(Scanner scanner, Connection connection, int userId) {
        System.out.print("Enter liters of fuel used: ");
        if (scanner.hasNextDouble()) {
            double liters = scanner.nextDouble();
            if (liters >= 0) {
                saveCarbonFootprintEntry(connection, userId, "Vehicle", liters);
            } else {
                System.out.println("Fuel usage cannot be negative.");
            }
        } else {
            System.out.println("Invalid input. Please enter a numeric value.");
            scanner.next();
        }
    }

    private static void addAirTravel(Scanner scanner, Connection connection, int userId) {
        System.out.print("Enter air travel distance in km: ");
        if (scanner.hasNextDouble()) {
            double distance = scanner.nextDouble();
            if (distance >= 0) {
                saveCarbonFootprintEntry(connection, userId, "Air Travel", distance);
            } else {
                System.out.println("Distance cannot be negative.");
            }
        } else {
            System.out.println("Invalid input. Please enter a numeric value.");
            scanner.next();
        }
    }

    private static void addWaste(Scanner scanner, Connection connection, int userId) {
        System.out.print("Enter waste in kg: ");
        if (scanner.hasNextDouble()) {
            double waste = scanner.nextDouble();
            if (waste >= 0) {
                saveCarbonFootprintEntry(connection, userId, "Waste", waste);
            } else {
                System.out.println("Waste cannot be negative.");
            }
        } else {
            System.out.println("Invalid input. Please enter a numeric value.");
            scanner.next();
        }
    }

    private static void readFootprintEntries(Connection connection, int userId) {
        String query = "SELECT type, amount, carbon_footprint FROM footprint_entries WHERE user_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                
                System.out.println("Footprint Entries for User ID " + userId + ":");
                System.out.println("--------------------------------------------------------------------");
                System.out.printf("%-20s %-10s %-20s%n", "Type", "Amount", "Carbon Footprint (kg CO2)");
                System.out.println("--------------------------------------------------------------------");
                
                while (rs.next()) {
                    String type = rs.getString("type");
                    double amount = rs.getDouble("amount");
                    double carbonFootprint = rs.getDouble("carbon_footprint");
                    System.out.printf("%-20s %-10.2f %-20.2f%n", type, amount, carbonFootprint);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving footprint entries: " + e.getMessage());
        }
    }

    private static void saveCarbonFootprintEntry(Connection connection, int userId, String type, double amount) {
        double carbonFootprint = 0.0;
        DecimalFormat df = new DecimalFormat("#.00"); 
        
        switch (type) {
            case "Electricity":
                carbonFootprint = amount * 0.233;
                break;
            case "Vehicle":
                carbonFootprint = amount * 2.31;
                break;
            case "Air Travel":
                carbonFootprint = amount * 0.09;
                break;
            case "Waste":
                carbonFootprint = amount * 1.9;
                break;
            default:
                System.out.println("Unknown type: " + type);
                return;
        }
    
        String query = "INSERT INTO footprint_entries (user_id, type, amount, carbon_footprint) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setString(2, type);
            stmt.setDouble(3, amount);
            stmt.setDouble(4, carbonFootprint);
            stmt.executeUpdate();
            System.out.println(type + " entry added successfully with a carbon footprint of " + df.format(carbonFootprint) + " kg CO2.");
        } catch (SQLException e) {
            System.out.println("Error saving entry to database: " + e.getMessage());
            return;
        }
        updateTotalCarbonFootprint(connection, userId);
    }

    private static void updateTotalCarbonFootprint(Connection connection, int userId) {
        String sumQuery = "SELECT SUM(carbon_footprint) AS total FROM footprint_entries WHERE user_id = ?";
        String updateQuery = "UPDATE carbon_footprint SET totalCarbonFootprint = ? WHERE user_id = ?";
        String insertQuery = "INSERT INTO carbon_footprint (user_id, totalCarbonFootprint) VALUES (?, ?)";
    
        try (PreparedStatement sumStmt = connection.prepareStatement(sumQuery)) {
            sumStmt.setInt(1, userId);
            try (ResultSet rs = sumStmt.executeQuery()) {
                if (rs.next()) {
                    double totalCarbonFootprint = rs.getDouble("total");
    
                    try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                        updateStmt.setDouble(1, totalCarbonFootprint);
                        updateStmt.setInt(2, userId);
                        int rowsAffected = updateStmt.executeUpdate();
    
                        if (rowsAffected == 0) {
                            try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                                insertStmt.setInt(1, userId);
                                insertStmt.setDouble(2, totalCarbonFootprint);
                                insertStmt.executeUpdate();
                            }
                        }
                    }
                    System.out.println(String.format("Total Carbon Footprint updated to: %.2f kg CO2.", totalCarbonFootprint));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error updating total carbon footprint: " + e.getMessage());
        }
    }

    private static void displayTotalCarbonFootprint(Connection connection, int userId) {
        DecimalFormat df = new DecimalFormat("#.00");
        try {
            String query = "SELECT SUM(carbon_footprint) AS total FROM footprint_entries WHERE user_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, userId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        double total = rs.getDouble("total");
                        System.out.println("Total Carbon Footprint: " + df.format(total) + " kg CO2.");
                    } else {
                        System.out.println("No entries found.");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving total carbon footprint: " + e.getMessage());
        }
    }
}
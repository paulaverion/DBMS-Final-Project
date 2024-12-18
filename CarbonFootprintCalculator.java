import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

public class CarbonFootprintCalculator implements CarbonFootprintEntry {
    private double totalCarbonFootprint;
    private StringBuilder entries;
    private DecimalFormat df;
    private Connection connection;

    public CarbonFootprintCalculator(Connection connection) {
        this.totalCarbonFootprint = 0.0;
        this.entries = new StringBuilder();
        this.df = new DecimalFormat("#.00");
        this.connection = connection;
    }

    @Override
    public void addEntry(double value) {
        totalCarbonFootprint += value;
    }

    public void addElectricityUsage(double kWh) {
        double footprint = kWh * 0.233;
        totalCarbonFootprint += footprint;
        entries.append("Electricity Usage: ").append(kWh).append(" kWh, Carbon Footprint: ")
               .append(df.format(footprint)).append(" kg CO2\n");
        saveEntryToDatabase("Electricity", footprint);
    }

    public void addVehicleTravel(double liters) {
        double footprint = liters * 2.31;
        totalCarbonFootprint += footprint;
        entries.append("Vehicle Travel: ").append(liters).append(" liters, Carbon Footprint: ")
               .append(df.format(footprint)).append(" kg CO2\n");
        saveEntryToDatabase("Vehicle", footprint);
    }

    public void addAirTravel(double distance) {
        double footprint = distance * 0.09;
        totalCarbonFootprint += footprint;
        entries.append("Air Travel: ").append(distance).append(" km, Carbon Footprint: ")
               .append(df.format(footprint)).append(" kg CO2\n");
        saveEntryToDatabase("Air", footprint);
    }

    public void addWaste(double waste) {
        double footprint = waste * 1.9;
        totalCarbonFootprint += footprint;
        entries.append("Waste: ").append(waste).append(" kg, Carbon Footprint: ")
               .append(df.format(footprint)).append(" kg CO2\n");
        saveEntryToDatabase("Waste", footprint);
    }

    private void saveEntryToDatabase(String type, double amount) {
        String query = "INSERT INTO footprintentries (user_id, type, amount) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, 1);
            stmt.setString(2, type);
            stmt.setDouble(3, amount);
            stmt.executeUpdate();
            System.out.println(type + " entry added successfully.");
        } catch (SQLException e) {
            System.out.println("Error saving entry to database: " + e.getMessage());
        }
    }

    public double getTotalCarbonFootprint() {
        return totalCarbonFootprint;
    }

    public String getEntries() {
        return entries.toString();
    }

    @Override
    public void displayEntryDetails() {
        System.out.println("All Carbon Footprint Entries:");
        System.out.println(entries.length() > 0 ? entries.toString() : "No entries available.");
    }

    public void loadEntriesFromDatabase(int userId) {
        String query = "SELECT type, amount FROM footprintentries WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String type = rs.getString("type");
                    double amount = rs.getDouble("amount");
                    entries.append(type).append(": ").append(amount).append(" kg CO2\n");
                    totalCarbonFootprint += amount;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error loading entries from database: " + e.getMessage());
        }
    }
}
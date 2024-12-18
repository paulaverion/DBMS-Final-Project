public class Tourist extends User {
    private double totalCarbonFootprint;

    public Tourist(int id, String username) {
        super(id, username);
        this.totalCarbonFootprint = 0.0;
    }

    public double getTotalCarbonFootprint() {
        return totalCarbonFootprint;
    }

    public void setTotalCarbonFootprint(double totalCarbonFootprint) {
        this.totalCarbonFootprint = totalCarbonFootprint;
    }

    @Override
    public void displayInfo() {
        System.out.println("Tourist Information:");
        System.out.println("ID: " + getId());
        System.out.println("Username: " + getUsername());
        System.out.println("Total Carbon Footprint: " + totalCarbonFootprint + " kg CO2");
    }
}
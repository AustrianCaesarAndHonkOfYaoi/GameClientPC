package Logic.Objects;

public class Buildings {
    private int energyProduction;
    private int mineralProduction;
    private int alloyProduction;
    private int yaoiProduction;
    private int foodProduction;
    private int fleetProduction;

    public Buildings(int energyProduction, int mineralProduction, int alloyProduction, int yaoiProduction, int foodProduction,int fleetProduction) {
        this.energyProduction = energyProduction;
        this.mineralProduction = mineralProduction;
        this.alloyProduction = alloyProduction;
        this.yaoiProduction = yaoiProduction;
        this.foodProduction = foodProduction;
        this.fleetProduction = fleetProduction;
    }

    public int getEnergyProduction() {return energyProduction;}

    public int getMineralProduction() {return mineralProduction;}

    public int getAlloyProduction() {return alloyProduction;}

    public int getYaoiProduction() {return yaoiProduction;}

    public int getFoodProduction() {return foodProduction;}

    public int getFleetProduction() {return fleetProduction;}
}

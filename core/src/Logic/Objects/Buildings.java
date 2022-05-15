package Logic.Objects;

public class Buildings {
    private int energyProduction;
    private int mineralProduction;
    private int alloyProduction;
    private int yaoiProduction;
    private int foodProduction;
    private int cost;
    private int constructionTime;
    private int fleetProduction;

    public Buildings(int energyProduction, int mineralProduction, int alloyProduction, int yaoiProduction, int foodProduction, int cost, int constructionTime,int fleetProduction) {
        this.energyProduction = energyProduction;
        this.mineralProduction = mineralProduction;
        this.alloyProduction = alloyProduction;
        this.yaoiProduction = yaoiProduction;
        this.foodProduction = foodProduction;
        this.cost = cost;
        this.constructionTime = constructionTime;
        this.fleetProduction = fleetProduction;
    }
}

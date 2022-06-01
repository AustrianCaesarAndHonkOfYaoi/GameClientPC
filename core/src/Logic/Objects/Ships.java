package Logic.Objects;



public class Ships {
    private int cost;
    private int firepower;
    private int fleetSpace;
    private int constructionTime;


    public Ships(int cost, int firepower , int fleetSpace,int constructionTime) {
        this.cost = cost;
        this.firepower = firepower;
        this.fleetSpace = fleetSpace;
        this.constructionTime = constructionTime;
    }
    public int getFleetSpace(){return fleetSpace;}
    public int getCost(){return cost;}
    public int getFirepower(){return firepower;}
}

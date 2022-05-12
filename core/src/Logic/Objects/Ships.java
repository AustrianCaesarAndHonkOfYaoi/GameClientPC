package Logic.Objects;



public class Ships {
    private int cost;
    private int firepower;
    private boolean abilityToJoinFleet;
    private int fleetSpace;
    private int constructionTime;


    public Ships(int cost, int firepower, boolean abilityToJoinFleet, int fleetSpace,int constructionTime) {
        this.cost = cost;
        this.firepower = firepower;
        this.abilityToJoinFleet = abilityToJoinFleet;
        this.fleetSpace = fleetSpace;
        this.constructionTime = constructionTime;
    }

    public boolean getAbilityToJoin() {
        return abilityToJoinFleet;
    }
    public int getFleetSpace(){return fleetSpace;}
}

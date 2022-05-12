package Logic.Objects;



import java.util.ArrayList;


public class Nation {
    private String Name;
    private String species;
    private int incomeEnergy=12;
    private int incomeFood=12;
    private int incomeMinerals=12;
    private int incomeAlloy=12;
    private int incomeYaoi=12;

    private int amountEnergy;
    private int amountFood;
    private int amountMinerals;
    private int amountAlloy;
    private int amountYaoi;
    private int fleetCapacity=30;

    private ArrayList<System> nationSystems = new ArrayList<>();
    private ArrayList<Planets> nationPlanets = new ArrayList<>();
    private ArrayList<Fleet> nationFleets = new ArrayList<>();
public int getUsedFleetSpace(){
    int usedSpace = 0;
    for (int i = 0; i < nationFleets.size(); i++) {
        for (int j = 0; j < nationFleets.get(i).getShipsInFleet().size(); j++) {
            usedSpace += nationFleets.get(i).getShipsInFleet().get(j).getFleetSpace();
        }
    }
    return usedSpace;
}
    public int getIncomeEnergy() {
        return incomeEnergy;
    }

    public void setIncomeEnergy(int incomeEnergy) {
        this.incomeEnergy += incomeEnergy;
    }

    public int getIncomeFood() {
        return incomeFood;
    }

    public void setIncomeFood(int incomeFood) {
        this.incomeFood += incomeFood;
    }

    public int getIncomeMinerals() {
        return incomeMinerals;
    }

    public void setIncomeMinerals(int incomeMinerals) {
        this.incomeMinerals += incomeMinerals;
    }

    public int getIncomeAlloy() {
        return incomeAlloy;
    }

    public void setIncomeAlloy(int incomeAlloy) {
        this.incomeAlloy += incomeAlloy;
    }

    public int getIncomeYaoi() {
        return incomeYaoi;
    }

    public void setIncomeYaoi(int incomeYaoi) {
        this.incomeYaoi += incomeYaoi;
    }

    public int getAmountEnergy() {
        return amountEnergy;
    }

    public void setAmountEnergy(int amountEnergy) {
        this.amountEnergy += amountEnergy;
    }

    public int getAmountFood() {
        return amountFood;
    }

    public void setAmountFood(int amountFood) {
        this.amountFood += amountFood;
    }

    public int getAmountMinerals() {
        return amountMinerals;
    }

    public void setAmountMinerals(int amountMinerals) {
        this.amountMinerals += amountMinerals;
    }

    public int getAmountAlloy() {
        return amountAlloy;
    }

    public void setAmountAlloy(int amountAlloy) {
        this.amountAlloy += amountAlloy;
    }

    public int getAmountYaoi() {
        return amountYaoi;
    }

    public void setAmountYaoi(int amountYaoi) {
        this.amountYaoi += amountYaoi;
    }

    public int getFleetCapacity() {
        return fleetCapacity;
    }

    public void setFleetCapacity(int fleetCapacity) {
        this.fleetCapacity += fleetCapacity;
    }
}

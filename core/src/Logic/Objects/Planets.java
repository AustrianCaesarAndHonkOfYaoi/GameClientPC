package Logic.Objects;

import Logic.buildingsdistricts.*;

import java.util.ArrayList;

public class Planets {
    private double maxDistricts;
    private int maxBuildings;
    private ArrayList<Buildings> buildings = new ArrayList<>();
    private ArrayList<Buildings> districts = new ArrayList<>();

    public Planets() {
        initMaxDistricts();
        addDistrictsAtStart();
    }

    private void initMaxDistricts() {
        maxDistricts = Math.random() * ((32 - 12) + 1) + 12;
        maxBuildings=6;
    }

    private void addDistrictsAtStart() {
        double districtSeperation = maxDistricts / 4;
        for (int i = 0; i < districtSeperation; i++) {
            districts.add(new AgrarDistrict());
            districts.add(new GeneratorDisctrict());
            districts.add(new IndustryDistrict());
            districts.add(new MiningDistrict());
        }
        for (int i = 0; i < 3; i++) {
            buildings.add(new Fortress());
            buildings.add(new PropagandaStation());
        }
    }

    public double getMaxDistricts() {
        return maxDistricts;
    }

    public Buildings getDistrict(int j) {
        return districts.get(j);
    }
    public Buildings getBuildings(int j) {
        return buildings.get(j);
    }
}

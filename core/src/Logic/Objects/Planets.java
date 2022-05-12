package Logic.Objects;

import java.util.ArrayList;

public class Planets {
    private double maxDistricts = Math.random() * ((32 - 12) + 1) + 12;
    private int actualDistrict;
    private double blocks = Math.random() * ((12 - 1) + 1) + 1;
    private int maxBuildings = 12;
    private int actualBuildings;
    private ArrayList <Buildings> buildings = new ArrayList<>();
    private ArrayList <Buildings> districts = new ArrayList<>();

}

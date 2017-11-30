package RSI.Structure;

import java.util.HashMap;

/**
 * Created by Pedro Matos
 *
 * This class contains the information for every CID.
 * Each CID is composed by a name and an ensamble of triplets.
 *
 */
public class CID{
    private HashMap<String, Triplet> Triplet = new HashMap<>();
    private String name;

    public HashMap<String, RSI.Structure.Triplet> getTriplet() {
        return Triplet;
    }

    public void setTriplet(HashMap<String, RSI.Structure.Triplet> triplet) {
        Triplet = triplet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CID{" +
                "Triplet=" + Triplet +
                ", name='" + name + '\'' +
                '}';
    }
}

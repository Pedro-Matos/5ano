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
    private HashMap<String, Triplet> triplet = new HashMap<>();
    private String name;

    public HashMap<String, Triplet> getTriplet() {
        return triplet;
    }

    public void setTriplet(HashMap<String, Triplet> triplet) {
        this.triplet = triplet;
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
                "triplet=" + triplet +
                ", name='" + name + '\'' +
                '}';
    }
}

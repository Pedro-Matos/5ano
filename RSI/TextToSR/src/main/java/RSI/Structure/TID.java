package RSI.Structure;

import java.util.HashMap;

/**
 * Created by Pedro Matos
 *
 * This class contains the information for every TID.
 * Each TID is composed by a name and an ensamble of triplets and childs.
 *
 */
public class TID {
    private HashMap<String, Triplet> triplet = new HashMap<>();
    private HashMap<String, Childs> childs = new HashMap<>();
    private String name;

    public HashMap<String, Triplet> getTriplet() {
        return triplet;
    }

    public void setTriplet(HashMap<String, Triplet> triplet) {
        this.triplet = triplet;
    }

    public HashMap<String, Childs> getChilds() {
        return childs;
    }

    public void setChilds(HashMap<String, Childs> childs) {
        this.childs = childs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TID{" +
                "triplet=" + triplet +
                ", childs=" + childs +
                ", name='" + name + '\'' +
                '}';
    }
}

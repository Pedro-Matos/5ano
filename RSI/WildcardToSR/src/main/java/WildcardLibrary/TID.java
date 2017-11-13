package WildcardLibrary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Pedro Matos
 *
 * This class contains the information for every TID.
 * Each TID is composed by a name and an ensamble of triplets and childs.
 *
 */
public class TID {
    private HashMap<String, Triplet> Triplet = new HashMap<>();
    private HashMap<String, Childs> Childs = new HashMap<>();
    private String name;

    public HashMap<String, WildcardLibrary.Triplet> getTriplet() {
        return Triplet;
    }

    public void setTriplet(HashMap<String, WildcardLibrary.Triplet> triplet) {
        Triplet = triplet;
    }

    public HashMap<String, WildcardLibrary.Childs> getChilds() {
        return Childs;
    }

    public void setChilds(HashMap<String, WildcardLibrary.Childs> childs) {
        Childs = childs;
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
                "Triplet=" + Triplet +
                ", Childs=" + Childs +
                ", name='" + name + '\'' +
                '}';
    }
}

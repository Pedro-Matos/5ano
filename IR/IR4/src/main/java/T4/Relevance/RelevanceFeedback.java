package T4.Relevance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RelevanceFeedback {

    private Map<Integer, ArrayList<Integer>> relevanceImplicit;
    private Map<Integer, HashMap<Integer,Integer>> relevanceExplicit;
    private int max_relevance = 5;

    public RelevanceFeedback(){
        this.relevanceExplicit = new HashMap<>();
        this.relevanceImplicit = new HashMap<>();
    }



}

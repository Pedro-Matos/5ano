package T3.utils;

/**
 * Created by pmatos9 on 13/11/17.
 */
public class ScoringOption {

    private int query_id;
    private int doc_id;
    private double final_weighting;

    public int getQuery_id() {
        return query_id;
    }

    public void setQuery_id(int query_id) {
        this.query_id = query_id;
    }

    public int getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(int doc_id) {
        this.doc_id = doc_id;
    }

    public double getFinal_weighting() {
        return final_weighting;
    }

    public void setFinal_weighting(double final_weighting) {
        this.final_weighting = final_weighting;
    }
}


package T2.utils;

/**
 * Created by pmatos9 on 21/10/17.
 */
public class ScoringOption {

    private int query_id;
    private int doc_id;
    private int doc_score;

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

    public int getDoc_score() {
        return doc_score;
    }

    public void setDoc_score(int doc_score) {
        this.doc_score = doc_score;
    }
}

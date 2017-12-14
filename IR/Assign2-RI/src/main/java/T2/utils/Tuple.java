package T2.utils;



public class Tuple implements Comparable<Tuple> {


    private Integer query;
    private Integer doc;

    public Tuple(Integer query, Integer doc) {
        this.query = query;
        this.doc = doc;
    }

    public Integer getQuery() {
        return query;
    }

    public Integer getDoc() {
        return doc;
    }

    @Override
    public int compareTo(Tuple o) {
        if(this.query > o.query)
            return 1;
        else if(this.query < o.query)
            return -1;
        return this.doc.compareTo(o.doc);
    }
}
package T1.utils;


/**
 * Created by pmatos9 on 30/09/17.
 */
public class Document {

    private final int id;

    private final String corpus;

    public Document(int id, String corpus){
        this.id = id;
        this.corpus = corpus;
    }

    public int getId() {
        return id;
    }

    public String getCorpus() {
        return corpus;
    }
}

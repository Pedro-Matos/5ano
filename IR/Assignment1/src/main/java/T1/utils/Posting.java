package T1.utils;

/**
 * Created by pmatos9 on 30/09/17.
 */
public class Posting {

    private final int docId;

    private final int frequency;

    public Posting(int docId, int frequency){
        this.docId = docId;
        this.frequency = frequency;
    }

    public int getDocId() {
        return docId;
    }

    public int getFrequency() {
        return frequency;
    }

    @Override
    public String toString() {
        return docId + ":" + frequency +
                ',';
    }
}

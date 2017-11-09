package T3.utils;

/**
 * Created by pmatos9 on 08/11/17.
 */
public class TfIdfWeighting {

    private int docId;

    private int term_frequency;

    private int document_frequency;

    private double inverse_doc_freq;

    private int tf_idf_weighting;

    private int N;

    private double final_weighting;

    public TfIdfWeighting(int docId, int term_frequency,int document_frequency, int N){
        this.docId = docId;
        this.term_frequency = term_frequency;
        this.document_frequency = document_frequency;
        this.N = N;
    }


    public void setInverseDocFreq(){
        this.inverse_doc_freq = Math.log10(N/this.document_frequency);
    }

    public void setFinalWeighting(){
        double first_term = 1 + Math.log(term_frequency);
        this.final_weighting = first_term*inverse_doc_freq;
    }


}

package T4.utils;

public class Posting2 {


    private final Integer docId;
    private final double termWeight;

    /**
     * Constructor para a identificação de um Posting que é caracterizado
     * por identificador do documento e frequência respetiva.
     *
     * @param docId
     * @param termWeight
     */
    public Posting2(Integer docId, double termWeight) {
        this.docId = docId;
        this.termWeight = termWeight;
    }

    /**
     * Método que retorna o identificador do documento.
     *
     * @return docId
     */
    public Integer getDocId() {
        return docId;
    }

    /**
     * Método que retorna a frequência de repetição da palavra nesse documento.
     *
     * @return frequency
     */
    public double getTermWeight() {
        return termWeight;
    }
}

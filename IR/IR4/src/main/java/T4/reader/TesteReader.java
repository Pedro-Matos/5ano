package T4.reader;

import T4.utils.Posting2;

import java.util.*;
import java.util.stream.Collectors;

public class TesteReader {

    private final TreeMap<String, LinkedList<Posting2>> tokenDocIdFreq = new TreeMap<>();
    private LinkedList<Posting2> postings;

    /**
     * Método que faz a contagem dos termos processados e invoca o método addToTokenDocIdFreq para adicionar os termos
     * de um documento à TreeMap.
     * @param processedTerms
     * @param docId
     */
    public void addToSetAndCount(List<String> processedTerms, Integer docId) {
        Set<String> unique = new HashSet<>(processedTerms);
        LinkedList<Posting2> tmpFT = new LinkedList<>();

        unique.stream().map((key) -> Collections.frequency(processedTerms, key)).map((freq) -> new Posting2(docId, freq)).forEach((tmpPosting) -> {
            tmpFT.push(tmpPosting);
        });

        // TODO tf + normalization
        LinkedList tmp = tmpFT.stream()
                .map(p-> new Posting2(p.getDocId(), 1 + Math.log10(p.getTermWeight())))
                .collect(Collectors.toCollection(LinkedList::new));

        int i=0;
        double norm = getNormalizationValueDoc(tmp);

        for(String key: unique){
            double tf = ((Posting2)tmp.get(i)).getTermWeight();
            addToTokenDocIdFreq(key, docId, normalizeTFDoc(tf, norm));
            i++;
        }
    }

    /**
     * Método que calcula o valor da normalização dos termos num dado documento.
     * @param tmp
     * @return
     */
    private double getNormalizationValueDoc(LinkedList<Posting2> tmp){
        double som=0;
        for(int i=0; i<tmp.size(); i++)
            som += Math.pow(((Posting2)tmp.get(i)).getTermWeight(), 2);
        return Math.sqrt(som);
    }

    /**
     * Método que retorna o valor normalizado de tf.
     * @param tf
     * @param som
     * @return
     */
    private double normalizeTFDoc(double tf, double som){
        return  tf/som;
    }


    /**
     * Método que procede à introdução dos termos na linkedList e posteriormente associa essa
     * linkedList à TreeMap.
     * @param token
     * @param docId
     * @param termWeight
     */
    public void addToTokenDocIdFreq(String token, int docId, double termWeight){
        Posting2 tmpPost = new Posting2(docId, termWeight);
        if(!tokenDocIdFreq.containsKey(token)){
            postings = new LinkedList<>();
            postings.add(tmpPost);
            tokenDocIdFreq.put(token, postings);
        }else{
            // caso key exista
            postings = tokenDocIdFreq.get(token);
            postings.add(tmpPost);
            tokenDocIdFreq.put(token, postings);
        }
    }

}

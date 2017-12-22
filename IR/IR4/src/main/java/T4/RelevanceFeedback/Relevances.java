package T4.RelevanceFeedback;

import T4.Relevance.RelevanceFeedback;
import T4.Word2Vec.Word2Vec;
import T4.tokenizer.StrongTokenizer;
import T4.utils.Posting2;
import T4.utils.TfIdfWeighting;
import org.apache.commons.io.FileUtils;

import javax.swing.text.StyledEditorKit;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Relevances {


    private double alpha = 1.0;
    private double betha = 0.0;
    private double miu = 0.0;
    private int corpus_size;
    private StrongTokenizer tokenizer;
    private TreeMap<Integer, TreeMap<Integer, Double>> queryIdDocIdRankTestImplicit;
    private TreeMap<Integer, TreeMap<Integer, Double>> queryIdDocIdRankTestExplicit;
    private TreeMap<String, LinkedList<Posting2>>dic_weight;
    private final TreeMap<Integer, TreeMap<Integer,Double>> queryIdDocIdRankWord2Vec;
    private final Word2Vec w2v = new Word2Vec();

    public Relevances(TreeMap<String, LinkedList<Posting2>>dic_weight, int corpus_size, String stop) {
        this.corpus_size = corpus_size;
        this.tokenizer = new StrongTokenizer(stop);
        this.queryIdDocIdRankTestExplicit = new TreeMap<>();
        this.queryIdDocIdRankTestImplicit = new TreeMap<>();
        this.dic_weight = dic_weight;
        this.queryIdDocIdRankWord2Vec = new TreeMap<>();
    }


    public TreeMap<Integer, TreeMap<Integer, Double>> getQueryIdDocIdRankWord2Vec() {
        return queryIdDocIdRankWord2Vec;
    }

    public TreeMap<Integer, TreeMap<Integer, Double>> getQueryIdDocIdRankTestImplicit() {
        return queryIdDocIdRankTestImplicit;
    }

    public TreeMap<Integer, TreeMap<Integer, Double>> getQueryIdDocIdRankTestExplicit() {
        return queryIdDocIdRankTestExplicit;
    }

    //10 melhores
    public void calculateImplicit(Map<Integer, ArrayList<Integer>> map_10_scores, int query_id, String query) {

        TreeMap<String, Double> results_implicit = new TreeMap<>();
        Map<String, Integer> term_count = new TreeMap<>();
        List<String> tokens = tokenizer.tokenize(query);

        ArrayList<Integer> relev_implicit = map_10_scores.get(query_id);

        Set<String> unique = new HashSet<>(tokens);
        unique.forEach((key) -> term_count.put(key, Collections.frequency(tokens, key)));

        double norm = getNormalizationValueQuery(term_count, corpus_size);

        for (String term : tokens) {

            if (dic_weight.containsKey(term)) {
                LinkedList<Posting2> posts = dic_weight.get(term);
                int termQueryFreq = Collections.frequency(tokens, term);

                for (Posting2 p : posts) {
                    if (relev_implicit.contains(p.getDocId())) {
                        double wtqOriginal = ((getTFQuery(termQueryFreq) * calculateIdfQuery(corpus_size, getNrDocsByTerm(term))) / norm) * alpha;
                        //double wtqPositive = tmpPostings.get(entryPosting.getDocId()).getTermWeight()*betha/(relevantDocsExplicit.size());
                        double wtdPositive = betha * p.getTermWeight() / (relev_implicit.size());
                        double finalScore = (wtqOriginal + wtdPositive); //* entryPosting.getTermWeight(); // * tf doc
                        //System.out.println("Querie: "+ query_id + "\t\t" + "wtqOriginal: " + wtqOriginal  + "wtdPositive: " + wtdPositive +"finalScore: " + finalScore);
                        if (!results_implicit.containsKey(term))
                            results_implicit.put(term, finalScore);
                        else
                            results_implicit.replace(term, results_implicit.get(term) + wtdPositive);

                    }
                }
            }

        }


        calcu_weights_implicit(results_implicit, query_id);


    }

    //precisas da relevância
    public void calculateExplicit(Map<Integer, ArrayList<Integer>> nonRelevantDocsHM, Map<Integer, Map<Integer, Integer>> relevante,
                                  int query_id, String query) {


        TreeMap<String, Double> results_explicit = new TreeMap<>();
        Map<String, Integer> term_count = new TreeMap<>();
        List<String> tokens = tokenizer.tokenize(query);

        ArrayList<Integer> non_rel = nonRelevantDocsHM.get(query_id);
        TreeMap<Integer, Integer> relev_explicit = (TreeMap<Integer, Integer>) relevante.get(query_id);

        Set<String> unique = new HashSet<>(tokens);
        unique.forEach((key) -> term_count.put(key, Collections.frequency(tokens, key)));

        double norm = getNormalizationValueQuery(term_count, corpus_size);


        for (String term : tokens) {

            if (dic_weight.containsKey(term)) {
                List<Posting2> posts = dic_weight.get(term);
                int termQueryFreq = Collections.frequency(tokens, term);

                for (Posting2 p : posts) {
                    if (relev_explicit.containsKey(p.getDocId())) {
                        int relevance = relev_explicit.get(p.getDocId());
                        double wtqOriginal = ((getTFQuery(termQueryFreq) * calculateIdfQuery(corpus_size, getNrDocsByTerm(term))) / norm) * alpha;
                        //double wtqPositive = tmpPostings.get(entryPosting.getDocId()).getTermWeight()*betha/(relevantDocsExplicit.size());
                        double wtdPositive = betha * relevance * p.getTermWeight() / getSumOfRelevant(relev_explicit);
                        double finalScore = (wtqOriginal + wtdPositive); //* entryPosting.getTermWeight(); // * tf doc
                        if (!results_explicit.containsKey(term))
                            results_explicit.put(term, finalScore);
                        else
                            results_explicit.replace(term, results_explicit.get(term) + wtdPositive);
                    } else {
                        double wtqOriginal = ((getTFQuery(termQueryFreq) * calculateIdfQuery(corpus_size, getNrDocsByTerm(term))) / norm) * alpha;
                        double wtdNegative = miu * p.getTermWeight() / (non_rel.size());
                        double finalScore = (wtqOriginal - wtdNegative); //* entryPosting.getTermWeight(); // * tf doc
                        if (!results_explicit.containsKey(term))
                            results_explicit.put(term, finalScore);
                        else
                            results_explicit.replace(term, results_explicit.get(term) - wtdNegative);
                    }
                }
            }
        }
        calcu_weights_explicit(results_explicit, query_id);

    }

    public double getSumOfRelevant(TreeMap<Integer, Integer> explicitRelevantDocs) {
        int sum = 0;
        for (Map.Entry<Integer, Integer> entry : explicitRelevantDocs.entrySet()) {
            sum += entry.getValue();
        }
        return sum / (explicitRelevantDocs.size());
    }

    public void calcu_weights_implicit(TreeMap<String, Double> tmpTM, int queryId) {

        for (Map.Entry<String, Double> entry : tmpTM.entrySet()) {
            if (dic_weight.containsKey(entry.getKey())) {
                List<Posting2> tmpPostings = dic_weight.get(entry.getKey());

                tmpPostings.forEach((entryPosting) -> {
                    // score = Wt,d * Wt,q = (tfdoc*1) * (tfquery*idfquery)
                    double wtd = entryPosting.getTermWeight();
                    double wtq = entry.getValue();
                    double score = wtd * wtq;
                    if (score != 0) {
                        addToQueryIdDocIdScoreTM(entryPosting.getDocId(), queryId, score, true);
                    }
                });
            }
        }
    }

    public void calcu_weights_explicit(TreeMap<String, Double> tmpTM, int queryId) {

        for (Map.Entry<String, Double> entry : tmpTM.entrySet()) {
            if (dic_weight.containsKey(entry.getKey())) {
                List<Posting2> tmpPostings = dic_weight.get(entry.getKey());

                tmpPostings.forEach((entryPosting) -> {
                    // score = Wt,d * Wt,q = (tfdoc*1) * (tfquery*idfquery)
                    double wtd = entryPosting.getTermWeight();
                    double wtq = entry.getValue();
                    double score = wtd * wtq;
                    //System.out.println("Query id: " + queryId + "\t\t\twtd: "+wtd+ "\t\t\twtq: "+wtq);
                    if (score != 0) {
                        //System.out.println(score);
                        addToQueryIdDocIdScoreTM(entryPosting.getDocId(), queryId, score, false);
                    }
                });
            }
        }
    }

    public void addToQueryIdDocIdScoreTM(int docId, int queryId, double score, boolean implicit) {

        if (implicit) {
            TreeMap<Integer, Double> docsScores;
            //DocWeight tmpScore = new DocWeight(docId, score);
            if (!queryIdDocIdRankTestImplicit.containsKey(queryId)) {
                docsScores = new TreeMap<>();
                docsScores.put(docId, score);

            } else {
                // caso key exista
                docsScores = queryIdDocIdRankTestImplicit.get(queryId);
                if (docsScores.containsKey(docId)) {
                    double res = docsScores.get(docId);
                    res += score;
                    docsScores.replace(docId, res);
                } else
                    docsScores.put(docId, score);
            }
            queryIdDocIdRankTestImplicit.put(queryId, docsScores);
        } else {
            TreeMap<Integer, Double> docsScores;
            //DocWeight tmpScore = new DocWeight(docId, score);
            if (!queryIdDocIdRankTestExplicit.containsKey(queryId)) {
                docsScores = new TreeMap<>();
                docsScores.put(docId, score);

            } else {
                // caso key exista
                docsScores = queryIdDocIdRankTestExplicit.get(queryId);
                if (docsScores.containsKey(docId)) {
                    double res = docsScores.get(docId);
                    res += score;
                    docsScores.replace(docId, res);
                } else
                    docsScores.put(docId, score);
            }
            queryIdDocIdRankTestExplicit.put(queryId, docsScores);

        }

    }

    /**
     * Método que retorna o valor da normalização dos termos da query.
     *
     * @param tmpMap
     * @param corpusSize
     * @return
     */
    private double getNormalizationValueQuery(Map<String, Integer> tmpMap, Integer corpusSize) {
        double norm = 0;
        norm = tmpMap.entrySet().stream()
                .map((entry) ->
                        getTFQuery(entry.getValue()) * calculateIdfQuery(corpusSize, getNrDocsByTerm(entry.getKey())))
                .map((tmp) ->
                        Math.pow(tmp, 2))
                .reduce(norm, (accumulator, _item) -> accumulator + _item); // tmp += tf * idf;
        return Math.sqrt(norm);
    }

    /**
     * Método que retorna o valor tf da query dada a frequência desse termo n query.
     *
     * @param freq
     * @return 1 + Math.log10(freq)
     */
    private double getTFQuery(int freq) {
        return 1 + Math.log10(freq);
    }

    /**
     * Método que retorna o valor do idf dado o tamanho do corpus e o número de documentos
     * onde existe uma dada query.
     *
     * @param corpusSize
     * @param nrDocsByTerm
     * @return
     */
    private double calculateIdfQuery(int corpusSize, int nrDocsByTerm) {
        if (nrDocsByTerm == 0)
            return 0;
        return Math.log10(corpusSize / nrDocsByTerm);
    }


    /**
     * Método que retorna o número de documentos onde o termo existe.
     *
     * @param term
     * @return nr of docs
     */
    private int getNrDocsByTerm(String term) {
        if (dic_weight.containsKey(term)) {
            return dic_weight.get(term).size();
        } else {
            return 0;
        }
    }

    /**
     * Método responsável por calcular o ranking para uma dada query aplicando a expansão da query
     *  (Query Expansion).
     * @param queryId
     * @param numWords
     */
    public void calculateWeightsWord2vec(String query, int queryId, int numWords) {

        // normalização: raiz quadrada do quadrado de todos os tfs da query
        Map<String, Integer> tmpMap = new TreeMap<>();
        List newProcessedList = new LinkedList<>();

        List<String> processedList = tokenizer.tokenize(query);

        newProcessedList.addAll(processedList);

        // Adicionar termos semelhantes a cada termo da query à lista de termos processada
        for(int i=0; i<processedList.size(); i++){
            String term = (String) processedList.get(i);
            Collection<String> newWords=null;
            newWords = w2v.generateNearestWords(term, numWords);

            if(newWords!=null){
                for(String newWord: newWords)
                    newProcessedList.add(newWord);
            }
        }

        Set<String> unique = new HashSet<>(newProcessedList);
        unique.forEach((key) -> {
            Integer freq = Collections.frequency(newProcessedList, key);
            tmpMap.put(key, freq);
        });

        double norm = getNormalizationValueQuery(tmpMap, corpus_size);

        //Score for a document-query pair: sum over terms t in both q and d:
        for(int query_term=0; query_term<newProcessedList.size(); query_term++){
            String key = (String) newProcessedList.get(query_term);

            if(dic_weight.containsKey(key)){
                LinkedList<Posting2> tmpPostings = dic_weight.get(key);
                // term: 2:0.06801255271278411,588:0.08628994543116196,1039:0.09235327689371566
                int termQueryFreq = Collections.frequency(newProcessedList, key);

                tmpPostings.forEach((entryPosting) -> {
                    // score = Wt,d * Wt,q = (tfdoc*1) * (tfquery*idfquery)
                    double wtd = entryPosting.getTermWeight();
                    double wtq = (getTFQuery(termQueryFreq) * calculateIdfQuery(corpus_size, getNrDocsByTerm(key)))/norm;
                    double score = wtd*wtq;
                    if (score != 0) {
                        addToQueryIdDocIdScoreTM_words2vec(entryPosting.getDocId(), queryId, score, queryIdDocIdRankWord2Vec);
                    }
                });
            }
        }
    }

    /**
     * Método que procede à introdução dos termos na linkedList e posteriormente associa essa
     * linkedList à TreeMap.
     * @param docId
     * @param queryId
     * @param score
     * @param tmap
     */
    public void addToQueryIdDocIdScoreTM_words2vec(int docId, int queryId, double score, TreeMap<Integer, TreeMap<Integer,Double>> tmap){

        TreeMap<Integer, Double> docsScores;
        //DocWeight tmpScore = new DocWeight(docId, score);
        if(!tmap.containsKey(queryId)){
            docsScores = new TreeMap<>();
            docsScores.put(docId,score);

        } else {
            // caso key exista
            docsScores = tmap.get(queryId);
            if(docsScores.containsKey(docId))
                docsScores.replace(docId,docsScores.get(docId)+ score);
            else
                docsScores.put(docId,score);
        }
        tmap.put(queryId, docsScores);
    }





    public void writeScores(File dir, boolean implicit) {
        dir.mkdir();

        try {

            if (implicit) {

                String blockFileName = "implict.txt";
                PrintWriter pwt = new PrintWriter(new File(dir, blockFileName));
                pwt.println("query_id" + "\t" + "doc_id" + "\t" + "doc_score");
                Iterator it = queryIdDocIdRankTestImplicit.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    //TreeMap<Integer, HashMap<Integer,Double>>

                    int query_id = (int) pair.getKey();

                    TreeMap<Integer, Double> map = (TreeMap<Integer, Double>) pair.getValue();
                    map.entrySet().stream()
                            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                            .forEach(integerDoubleEntry ->
                                    pwt.println(query_id + "\t\t\t" + integerDoubleEntry.getKey() + "\t\t\t" + integerDoubleEntry.getValue()));


                    /*
                    TreeMap<Integer,Double> map = (TreeMap<Integer, Double>) pair.getValue();

                    Iterator tmp_it = map.entrySet().iterator();
                    while (tmp_it.hasNext()){
                        Map.Entry p_tmp = (Map.Entry) tmp_it.next();

                        int doc_id = (int) p_tmp.getKey();
                        double score = (double) p_tmp.getValue();
                        pwt.println(query_id + "\t\t\t" + doc_id + "\t\t\t" + score);
                    }
                     */
                }
                pwt.close();
            } else {
                String blockFileName = "explicit.txt";
                PrintWriter pwt = new PrintWriter(new File(dir, blockFileName));
                pwt.println("query_id" + "\t" + "doc_id" + "\t" + "doc_score");
                Iterator it = queryIdDocIdRankTestExplicit.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    //TreeMap<Integer, HashMap<Integer,Double>>

                    int query_id = (int) pair.getKey();

                    TreeMap<Integer, Double> map = (TreeMap<Integer, Double>) pair.getValue();
                    map.entrySet().stream()
                            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                            .forEach(integerDoubleEntry ->
                                    pwt.println(query_id + "\t\t\t" + integerDoubleEntry.getKey() + "\t\t\t" + integerDoubleEntry.getValue()));


                    /*
                    TreeMap<Integer,Double> map = (TreeMap<Integer, Double>) pair.getValue();

                    Iterator tmp_it = map.entrySet().iterator();
                    while (tmp_it.hasNext()){
                        Map.Entry p_tmp = (Map.Entry) tmp_it.next();

                        int doc_id = (int) p_tmp.getKey();
                        double score = (double) p_tmp.getValue();
                        pwt.println(query_id + "\t\t\t" + doc_id + "\t\t\t" + score);
                    }
                     */
                }
                pwt.close();


            }

        } catch (IOException ex) {
            throw new RuntimeException("There was a problem writing the index to a file", ex);
        }
    }


    public void printWord2Vec(File dir) {
        dir.mkdir();

        try {

            String blockFileName = "word2vec.txt";
            PrintWriter pwt = new PrintWriter(new File(dir, blockFileName));
            pwt.println("query_id" + "\t" + "doc_id" + "\t" + "doc_score");
            Iterator it = queryIdDocIdRankWord2Vec.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                //TreeMap<Integer, HashMap<Integer,Double>>

                int query_id = (int) pair.getKey();

                TreeMap<Integer, Double> map = (TreeMap<Integer, Double>) pair.getValue();
                map.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .forEach(integerDoubleEntry ->
                                pwt.println(query_id + "\t\t\t" + integerDoubleEntry.getKey() + "\t\t\t" + integerDoubleEntry.getValue()));
            }
            pwt.close();
        } catch (IOException ex) {
            throw new RuntimeException("There was a problem writing the index to a file", ex);
        }
    }
}

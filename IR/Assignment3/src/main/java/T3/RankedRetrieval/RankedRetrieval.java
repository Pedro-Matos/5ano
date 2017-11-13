package T3.RankedRetrieval;

import T3.tokenizer.SimpleTokenizer;
import T3.utils.ScoringOption;
import T3.utils.TfIdfWeighting;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by pmatos9 on 13/11/17.
 */
public class RankedRetrieval {

    private SimpleTokenizer tokenizer = new SimpleTokenizer();
    private List<ScoringOption> scoring_opt = new LinkedList<>();

    public List<String> ParseQuerys(File f){
        List<String> terms = new LinkedList<String>();

        try {
            terms = FileUtils.readLines(f,"utf-8");
        } catch (IOException ex) {
            throw new RuntimeException("There was a problem reading the document "
                    + f.getName(), ex);
        }
        return terms;
    }

    public void ProcessTerms(List<String> query, Map<String, List<TfIdfWeighting>> dic, File dir){
        int query_id = 1;
        String query_term;

        for (String tQuery : query) {
            query_term = tQuery;
            List<String> tokens = tokenizer.tokenize(query_term);

            //tokens processed by the tokenizer...
            for(String abc : tokens){

                if(dic.containsKey(abc)){
                    //Get all the postings
                    List<TfIdfWeighting> list_weights = dic.get(abc);

                    //create Scoring for each posting
                    for(int i = 0; i < list_weights.size(); i++){
                        TfIdfWeighting weight = list_weights.get(i);

                        ScoringOption scoring = new ScoringOption();
                        scoring.setQuery_id(query_id);
                        scoring.setDoc_id(weight.getDocId());
                        scoring.setFinal_weighting(weight.getFinal_weighting());
                        scoring_opt.add(scoring);
                    }
                }
            }
            query_id++;
        }
        WriteScores(dir);
    }

    private void WriteScores(File dir){

        dir.mkdir();
        try {
            FileUtils.cleanDirectory(dir);
        } catch (IOException ex) {
            throw new RuntimeException("There was a problem cleaning the directory.", ex);
        }

        try {
            String blockFileName = "scores.txt";
            PrintWriter pwt = new PrintWriter(new File(dir, blockFileName));

            pwt.println("query_id" + "\t" + "doc_id" + "\t" + "doc_score");
            for(ScoringOption scor : scoring_opt){
                pwt.println(scor.getQuery_id() + "\t\t\t" + scor.getDoc_id() + "\t\t\t" + scor.getFinal_weighting());
            }

            pwt.close();
        } catch (IOException ex) {
            throw new RuntimeException("There was a problem writing the index to a file", ex);
        }
    }
}

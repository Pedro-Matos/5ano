package T2.BooleanRetrieval;

import T2.tokenizer.SimpleTokenizer;
import T2.utils.Posting;
import T2.utils.ScoringOption;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by pmatos9 on 21/10/17.
 */
public class BooleanRetrieval {

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



    public void ProcessTerms(List<String> query, Map<String, List<Posting>> dic, File dir){
        int query_id = 1;
        String query_term;

        for (String tQuery : query) {
            query_term = tQuery;
            List<String> tokens = tokenizer.tokenize(query_term);

            //tokens processed by the tokenizer...
            for(String abc : tokens){

                if(dic.containsKey(abc)){
                    //Get all the postings
                    List<Posting> postings = dic.get(abc);

                    //create Scoring for each posting
                    for(int i = 0; i < postings.size(); i++){
                        Posting post = postings.get(i);

                        ScoringOption scoring = new ScoringOption();
                        scoring.setQuery_id(query_id);
                        scoring.setDoc_id(post.getDocId());
                        scoring.setDoc_score(post.getFrequency());
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
                pwt.println(scor.getQuery_id() + "\t\t\t" + scor.getDoc_id() + "\t\t\t" + scor.getDoc_score());
            }

            pwt.close();
        } catch (IOException ex) {
            throw new RuntimeException("There was a problem writing the index to a file", ex);
        }

    }

}

package T2.BooleanRetrieval;

import T2.tokenizer.SimpleTokenizer;
import T2.utils.Posting;
import T2.utils.ScoringOption;
import T2.utils.Tuple;
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
    private Map<Tuple, Integer> map_scores = new TreeMap<>();
    private int query_id = 1;
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



    public void ProcessTerms(String query, Map<String, List<Posting>> dic){
            List<String> tokens = tokenizer.tokenize(query);

            //tokens processed by the tokenizer...
            for(String abc : tokens){

                if(dic.containsKey(abc)){
                    //Get all the postings
                    List<Posting> postings = dic.get(abc);

                    //create Scoring for each posting
                    for(int i = 0; i < postings.size(); i++){
                        Posting post = postings.get(i);

                        int tmp_doc_id = post.getDocId();
                        int tmp_post_frequency = post.getFrequency();

                        Tuple tmp_tuple = new Tuple(this.query_id, tmp_doc_id);
                        if(map_scores.containsKey(tmp_tuple)){
                            int score = map_scores.get(tmp_tuple) + tmp_post_frequency;
                            map_scores.put(tmp_tuple, score);
                        }
                        else{
                            map_scores.put(tmp_tuple, tmp_post_frequency);
                        }
                    }
                }
            }

        this.query_id++;
    }


    public void WriteScores(File dir){

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


            Iterator it = map_scores.entrySet().iterator();
            while (it.hasNext()){
                Map.Entry pair = (Map.Entry) it.next();

                Tuple tuple = (Tuple) pair.getKey();
                int score = (int) pair.getValue();
                pwt.println(tuple.getQuery() + "\t\t\t" + tuple.getDoc() + "\t\t\t" + score);
            }


            pwt.close();
        } catch (IOException ex) {
            throw new RuntimeException("There was a problem writing the index to a file", ex);
        }
    }
}

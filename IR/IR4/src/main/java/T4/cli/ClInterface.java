package T4.cli;

import T4.utils.DocumentScore;
import T4.utils.Posting;
import org.apache.commons.cli.*;

import java.io.*;
import java.util.*;

/**
 * Created by Pedro Matos & Tiago Bastos
 *
 * This is the main interface of our application. It works like a command line interface to the user.
 */
public class ClInterface {
    public static void main(String[] args) throws IOException {

        String file_scores = "scores_limited.txt";
        String file_relv = "cranfield.query.relevance.txt";
        String file_post = "index.txt";
        Map<Integer, Map<Double, Integer>> map_10_scores = readScores(file_scores);
        Map<Integer, Map<Integer, Integer>> map_relv = readRelevances(file_relv);
        Map<String, List<T4.utils.Posting>> dic_postings = readPostings(file_post);
    }

    private static Map<String, List<T4.utils.Posting>> dic = new TreeMap<String, List<Posting>>();

    public static Map<Integer, Map<Double, Integer>> readScores(String filename) throws IOException {

        FileReader freader = new FileReader(filename);
        BufferedReader br = new BufferedReader(freader);
        String s;
        Map<Integer, Map<Double, Integer>> map_of_scores = new HashMap<>();
        boolean header = true;

        while((s = br.readLine()) != null) {

            if(!header){

                String[] parsed = s.split("\t\t\t");
                int query_id = Integer.parseInt(parsed[0]);
                int doc_id = Integer.parseInt(parsed[1]);
                double doc_score = Double.parseDouble(parsed[2]);

                if(map_of_scores.containsKey(query_id)){
                    map_of_scores.get(query_id).put(doc_score,doc_id);
                }
                else{
                    Map<Double, Integer> tmp_mapv = new TreeMap(Collections.reverseOrder());
                    tmp_mapv.put(doc_score,doc_id);
                    map_of_scores.put(query_id,tmp_mapv);
                }
            }
            header = false;
        }
        freader.close();
        return map_of_scores;
    }

    public static Map<Integer, Map<Integer, Integer>> readRelevances(String filename) throws IOException {

        FileReader freader = new FileReader(filename);
        BufferedReader br = new BufferedReader(freader);
        String s;
        Map<Integer, Map<Integer, Integer>> map_of_relevances = new HashMap<>();

        while((s = br.readLine()) != null) {
            String[] parsed = s.split(" ");
            int query_id = Integer.parseInt(parsed[0]);
            int doc_id = Integer.parseInt(parsed[1]);
            int relv = Integer.parseInt(parsed[2]);

            if(map_of_relevances.containsKey(query_id)){
                map_of_relevances.get(query_id).put(doc_id,relv);
            }
            else{
                Map<Integer, Integer> tmp_mapv = new TreeMap();
                tmp_mapv.put(doc_id,relv);
                map_of_relevances.put(query_id,tmp_mapv);
            }

        }
        freader.close();
        return map_of_relevances;

    }

    public static Map<String, List<T4.utils.Posting>> readPostings(String filename) throws IOException {

        FileReader freader = new FileReader(filename);
        BufferedReader br = new BufferedReader(freader);
        String s;
        while((s = br.readLine()) != null) {
            parseContent(s);
        }
        freader.close();
        return dic;
    }

    private static void parseContent(String s){
        String[] tmp = s.split("\t");
        String term = tmp[0];   //retirar o termo
        String postings_raw = tmp[1].substring(1,tmp[1].length()-1);    //retirar o []
        LinkedList<String> postings_parsed = new LinkedList<String>();

        String[] array_to_list = postings_raw.split(",");
        postings_parsed.addAll(Arrays.asList(array_to_list));   //ter todos os postings separados

        Posting tmp_posting;

        for(int i = 0; i < postings_parsed.size(); i++){

            if(i == 0) {
                String[] doc_freq = postings_parsed.get(i).split(":");
                int docid = new Integer(doc_freq[0]);
                int freq = new Integer(doc_freq[1]);
                tmp_posting = new Posting(docid,freq);
            }
            else{
                String post_parsed_2 = postings_parsed.get(i).substring(1,postings_parsed.get(i).length());
                String[] doc_freq = post_parsed_2.split(":");
                int docid = new Integer(doc_freq[0]);
                int freq = new Integer(doc_freq[1]);
                tmp_posting = new Posting(docid,freq);
            }

            if(dic.containsKey(term)){
                dic.get(term).add(tmp_posting);
            }
            else{
                List<Posting> list = new ArrayList<Posting>();
                list.add(tmp_posting);
                dic.put(term,list);
            }
        }

    }
}

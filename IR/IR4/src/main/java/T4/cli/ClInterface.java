package T4.cli;

import T4.RankedRetrieval.RankedRetrieval;
import T4.index.InvertedIndex;
import T4.index.TfIdfIndexer;
import T4.tokenizer.StrongTokenizer;
import T4.tokenizer.Tokenizer;
import T4.utils.Document;
import T4.utils.DocumentScore;
import T4.utils.Posting;
import T4.reader.Reader;
import T4.utils.TfIdfWeighting;
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
        Map<Integer, Map<Integer, Integer>> map_relv = readRelevances(file_relv);
        //Map<String, List<T4.utils.Posting>> dic_postings = readPostings(file_post);



        String input_p = "cranfield/";
        String stopwords = "stopwords.txt";
        String out_dir = "output/";
        String queries = "cranfield.queries.txt";

        File folder = new File(input_p);
        File test = new File(input_p);
        int docs_number = 0;
        if (!test.isDirectory() || !test.canRead()) {
            System.out.println("The specified corpus directory is not a directory or is not readable.");
            System.exit(0);
        }
        else{
            docs_number = test.listFiles().length;
        }

        File output = new File(out_dir);
        File f = new File(queries);

        Tokenizer tokenizer = new StrongTokenizer(stopwords);
        Reader reader = new Reader();
        InvertedIndex invIndexer = new InvertedIndex(output);
        TfIdfIndexer tfidfIndexer = new TfIdfIndexer(output,docs_number);

        File[] listOfFiles = folder.listFiles();
        String[] tags = {"<TEXT>", "<AUTHOR>"};

        Map<String, List<Posting>> dic_postings;


        int id = 1;
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()){
                /*
                 * read the file and get the corpus and id
                 */
                Document corpus = reader.readFile(listOfFile.getPath(), tags,id);
                List<String> tokens = tokenizer.tokenize(corpus.getCorpus());
                /*
                 * Add tokens to inverted index
                 */
                invIndexer.addTokens(corpus.getId(), tokens);
                id++;
            }
        }
        dic_postings = invIndexer.getDic();
        Set<String> terms = invIndexer.getTerms();
        tfidfIndexer.setDic(dic_postings);
        tfidfIndexer.setTerms(terms);
        tfidfIndexer.addDocumentFrequency();

        Map<String, List<TfIdfWeighting>> dic_weight = tfidfIndexer.getDic_weight();
        RankedRetrieval ranked_ret = new RankedRetrieval(stopwords);
        List<String> querys = ranked_ret.ParseQuerys(f);
        ranked_ret.ProcessTerms(querys, dic_weight, output);
        Map<Integer, Map<Double, Integer>> map_10_scores = ranked_ret.getMap10();


        // guião 4 calculations
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

}

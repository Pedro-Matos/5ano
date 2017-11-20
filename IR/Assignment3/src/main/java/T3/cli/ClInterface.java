package T3.cli;

import T3.Evaluation.GoldEvaluator;
import T3.RankedRetrieval.RankedRetrieval;
import T3.index.InvertedIndex;
import T3.index.TfIdfIndexer;
import T3.tokenizer.SimpleTokenizer;
import T3.tokenizer.StrongTokenizer;
import T3.tokenizer.Tokenizer;
import T3.utils.Document;
import T3.utils.TfIdfWeighting;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Pedro Matos & Tiago Bastos
 *
 * This is the main interface of our application. It works like a command line interface to the user.
 */
public class ClInterface {
    public static void main(String[] args) throws IOException {
        int docs_number = 0;

        CommandLineParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("s", "stopwords-path", true, "File with stopwords.");
        options.addOption("o", "output-directory", true, "Output directory for the score information.");
        options.addOption("c", "cranfield-directory", true, "Directory with the documents.");
        options.addOption("q", "cranfield-queries", true, "File with the queries.");
        options.addOption("r", "cranfield-queries-relevance", true, "File with the queries relevance.");

        CommandLine commandLine;
        try {
            commandLine = parser.parse(options, args);
        } catch (ParseException ex) {
            System.out.println("There was a problem processing the input arguments.");
            return;
        }


        /*
         * if no arguments are given, we print the program help to the user.
         */
        if(commandLine.getOptions().length == 0){
            System.out.println("No arguments given.");
            System.exit(0);
        }

        /*
         * Cranfield
         */
        String input_p = "";
        if(commandLine.hasOption('c')){
            input_p = commandLine.getOptionValue('c');
            File test = new File(input_p);
            if (!test.isDirectory() || !test.canRead()) {
                System.out.println("The specified corpus directory is not a directory or is not readable.");
                System.exit(0);
            }
            else{
                docs_number = test.listFiles().length;
            }
        }


        /*
         * Stopwords location (file)
         */
        String stop = "";
        File stopwordsFile;
        if(commandLine.hasOption('s')){
            stop = commandLine.getOptionValue('s');
            stopwordsFile = new File(stop);
            if(!stopwordsFile.exists()) {
                System.out.println("The " + stop + " file does not exist");
                System.exit(0);
            }
        }

        /*
         * Output path of the index file (directory)
         */

        String out_p = "";
        File test_o;
        if(commandLine.hasOption('o')){
            out_p = commandLine.getOptionValue('o');
            test_o = new File(out_p);
            if (!test_o.isDirectory()) {
                if (!test_o.mkdir()) {
                    System.out.println("The specified output directory can't be created.");
                    System.exit(0);
                }
            }
        }


        /*
         * File with querys
         */

        String queries = "";
        if(commandLine.hasOption('q')) {
            queries = commandLine.getOptionValue('q');
        }


        /*
         * File with querys relevance
         */
        String querys_rel = "";
        if(commandLine.hasOption('r')) {
            querys_rel = commandLine.getOptionValue('r');
        }





        StrongTokenizer tokenizer = new StrongTokenizer(stop);

        /*
         * Creation of the reader and the inverted indexer
         */
        File output = new File(out_p);
        T3.reader.Reader reader = new T3.reader.Reader();
        //InvertedIndex invIndexer = new InvertedIndex(output);
        TfIdfIndexer tfidfIndexer = new TfIdfIndexer(output,docs_number);

        File folder = new File(input_p);
        File[] listOfFiles = folder.listFiles();
        /*
         * We use this tags to filter the information we want
         */
        String[] tags = {"<TEXT>", "<AUTHOR>"};



        int id = 1;
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()){
                /*
                 * read the file and get the corpus and id
                 */
                Document corpus = T3.reader.Reader.readFile(listOfFile.getPath(), tags,id);
                List<String> tokens = tokenizer.tokenize(corpus.getCorpus());
                /*
                 * Add tokens to inverted index
                 */
                tfidfIndexer.addTermFrequency(corpus.getId(), tokens);
                id++;
            }
        }
        tfidfIndexer.addDocumentFrequency();
        tfidfIndexer.writeFile();


        RankedRetrieval ranked_ret = new RankedRetrieval(stop);
        File f = new File(queries);
        List<String> terms = ranked_ret.ParseQuerys(f);
        Map<String, List<TfIdfWeighting>> dic_weight = tfidfIndexer.getDic_weight();
        long elapsedTime = ranked_ret.ProcessTerms(terms, dic_weight, output);
        double seconds = (double) elapsedTime/1000000000.0;
        double queryL = seconds/225;
        double queryT = (1.0/(queryL));



        GoldEvaluator ev = new GoldEvaluator();
        Map<Integer, Map<Double, Integer>> map_scores = ranked_ret.getMap_of_the_maps();
        ev.readQueryRelevance(querys_rel);
        ev.evaluateValues(map_scores,docs_number,10);
        ev.calculateAll(10);
        ev.writeScores(output,10, queryL, queryT);
    }

}

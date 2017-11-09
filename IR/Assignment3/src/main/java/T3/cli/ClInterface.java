package T3.cli;

import T3.index.InvertedIndex;
import T3.tokenizer.SimpleTokenizer;
import T3.tokenizer.StrongTokenizer;
import T3.tokenizer.Tokenizer;
import T3.utils.Document;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Pedro Matos & Tiago Bastos
 *
 * This is the main interface of our application. It works like a command line interface to the user.
 */
public class ClInterface {
    public static void main(String[] args) throws IOException {

        /*
         * Cranfield
         */
        String input_p = "cranfield/";
        File test = new File(input_p);
        if (!test.isDirectory() || !test.canRead()) {
            System.out.println("The specified corpus directory is not a directory or is not readable.");
            System.exit(0);
        }

        /*
         * Stopwords location (file)
         */
        String stop = "stopwords.txt";
        File stopwordsFile = new File(stop);
        if(!stopwordsFile.exists()) {
            System.out.println("The " + stop + " file does not exist");
            System.exit(0);
        }

        /*
         * Output path of the index file (directory)
         */

        String out_p = "output_files/";
        File test_o = new File("output_files/");
        if (!test_o.isDirectory()) {
            if (!test_o.mkdir()) {
                System.out.println("The specified output directory can't be created.");
                System.exit(0);
            }
        }


        StrongTokenizer tokenizer = new StrongTokenizer(stop);

        /*
         * Creation of the reader and the inverted indexer
         */
        File output = new File(out_p);
        T3.reader.Reader reader = new T3.reader.Reader();
        InvertedIndex invIndexer = new InvertedIndex(output);


        File folder = new File(input_p);
        File[] listOfFiles = folder.listFiles();
        /*
         * We use this tags to filter the information we want
         */
        String[] tags = {"<TEXT>", "<AUTHOR>"};

        //long start = System.nanoTime();

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
                invIndexer.addTokens(corpus.getId(), tokens);
                id++;
            }
        }
        invIndexer.close();

        //long elapsedTime = System.nanoTime() - start;
        //System.out.println("Elapsed Time: " + elapsedTime);

    }
}

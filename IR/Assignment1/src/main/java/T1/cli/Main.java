package T1.cli;

import T1.index.InvertedIndex;
import T1.stopwords.StopWordsRemover;
import T1.tokenizer.SimpleTokenizer;
import T1.tokenizer.StrongTokenizer;
import T1.tokenizer.Tokenizer;
import T1.utils.Document;
import T1.index.InvertedIndex.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static T1.printer.Printer.printNewCorpus;

/**
 * Created by pmatos9 on 30/09/17.
 */
public class Main {

    public static void main(String[] args) throws IOException {

        T1.reader.Reader reader = new T1.reader.Reader();
        File f = new File("token_out");
        InvertedIndex invIndexer = new InvertedIndex(f);

        /**
         * primeiro guia
         */

        File folder = new File("cranfield");
        File[] listOfFiles = folder.listFiles();
        String[] tags = {"<TEXT>", "<AUTHOR>"};
        Tokenizer tokenizer = new StrongTokenizer("stopwords.txt");

        int id = 1;
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()){
                Document corpus = T1.reader.Reader.readFile(listOfFile.getPath(), tags,id);
                //printNewCorpus(corpus.getCorpus(), listOfFile.getName());
                List<String> tokens = tokenizer.tokenize(corpus.getCorpus());
                // Add tokens to inverted index
                invIndexer.addTokens(corpus.getId(), tokens);
                id++;
            }
        }
        invIndexer.close();


    }


}

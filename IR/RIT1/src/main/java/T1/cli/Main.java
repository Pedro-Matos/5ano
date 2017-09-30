package T1.cli;

import T1.*;
import T1.utils.Document;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import static T1.printer.Printer.printNewCorpus;

/**
 * Created by pmatos9 on 30/09/17.
 */
public class Main {

    public static void main(String[] args) throws IOException {

        T1.reader.Reader reader = new T1.reader.Reader();

        /**
         * primeiro guia
         */

        File folder = new File("cranfield");
        File[] listOfFiles = folder.listFiles();
        String[] tags = {"<TEXT>", "<AUTHOR>"};

        int id = 1;
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()){
                Document corpus = T1.reader.Reader.readFile(listOfFile.getPath(), tags,id);
                printNewCorpus(corpus.getCorpus(), listOfFile.getName());
                id++;
            }

        }


    }


}

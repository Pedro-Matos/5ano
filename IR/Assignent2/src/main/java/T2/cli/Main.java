package T2.cli;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import T2.IndexReader.Reader;
import T2.BooleanRetrieval.BooleanRetrieval;
import T2.utils.Posting;

/**
 * Created by pmatos9 on 19/10/17.
 */
public class Main {
    public static void main(String[] args) throws IOException {

        String filename = "index0.txt" ;
        Reader indexreader = new Reader();
        indexreader.readFile(filename);
        indexreader.writeFile();

        BooleanRetrieval boolRetr = new BooleanRetrieval();
        File f = new File("cranfield.queries.txt");
        List<String> querys = boolRetr.ParseQuerys(f);

        Map<String, List<Posting>> dic = indexreader.getDic();

        boolRetr.ProcessTerms(querys,dic);




    }
}

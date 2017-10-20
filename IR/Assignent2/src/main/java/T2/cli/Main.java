package T2.cli;

import java.io.IOException;
import T2.IndexReader.Reader;

/**
 * Created by pmatos9 on 19/10/17.
 */
public class Main {
    public static void main(String[] args) throws IOException {

        String filename = "index0.txt" ;
        Reader indexreader = new Reader();
        indexreader.readFile(filename);
        indexreader.writeFile();




    }
}

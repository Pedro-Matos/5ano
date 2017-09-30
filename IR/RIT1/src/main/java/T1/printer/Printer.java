package T1.printer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by pmatos9 on 30/09/17.
 */
public class Printer {

    public static void printNewCorpus(String corpus, String cont) {
        BufferedWriter bw = null;
        String directoryName = "out_corpus";

        File directory = new File(directoryName);
        if (! directory.exists()){
            directory.mkdir();
        }

        try {

            File file = new File(directoryName + "/" + cont);

            FileWriter fw = new FileWriter(file);
            bw = new BufferedWriter(fw);


            bw.write(corpus);

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();
            } catch (Exception ex) {
                System.out.println("Error in closing the BufferedWriter" + ex);
            }
        }
    }


}

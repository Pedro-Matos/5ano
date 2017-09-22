/**
 * Created by pmatos9 on 22/09/17.
 */
import java.io.*;
import java.util.LinkedList;

public class main {
    public static void main(String[] args) throws IOException {
        LinkedList<String> corpus = new LinkedList<String>();

        File folder = new File("cranfield");
        File[] listOfFiles = folder.listFiles();
        String[] tags = {"<TEXT>", "<AUTHOR>"};
        int cont = 1;

        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()){
                corpus = readFile(listOfFile.getPath(),tags);
                printNewCorpus(corpus,cont);
                cont++;
            }

        }
    }

    private static LinkedList<String> readFile(String filename, String[] tags) throws IOException {
        LinkedList<String> corpus = new LinkedList<String>();
        boolean save = false;
        String[] tags_end = new String[tags.length];

        for(int i = 0; i < tags.length; i++){
            tags_end[i] = tags[i];
            tags_end[i] = tags_end[i].substring(0,1) + "/" +  tags_end[i].substring(1, tags_end[i].length());
        }

        FileReader freader = new FileReader(filename);
        BufferedReader br = new BufferedReader(freader);
        String s;
        while((s = br.readLine()) != null) {
            for(int i = 0; i < tags.length; i++){
                if(s.equals(tags[i])){
                    save = true;
                }
                if(s.equals(tags_end[i])){
                    save = false;
                }
            }

            if (save){
                if (s.charAt(0) != '<'){
                    s = s + "\n";
                    corpus.add(s);
                }

            }
        }
        freader.close();

        return corpus;
    }

    private static void printNewCorpus(LinkedList<String> corpus, int cont) {
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

            for (String corpu : corpus)
                bw.write(corpu);


            System.out.println("File written Successfully");

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
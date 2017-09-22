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

        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()){
                corpus = readFile(listOfFile.getPath());
                printNewCorpus(corpus);
            }

        }

        /*corpus = readFile("cranfield/cranfield0001",,"</TEXT>","<AUTHOR>","</AUTHOR>");
        printNewCorpus(corpus);*/
    }

    private static LinkedList<String> readFile(String filename) throws IOException {
        LinkedList<String> corpus = new LinkedList<String>();
        boolean save = false;
        FileReader freader = new FileReader(filename);
        BufferedReader br = new BufferedReader(freader);
        String s;
        while((s = br.readLine()) != null) {
            /*if(s.equals(first_tag)) save = true;
            if(s.equals(end)) save = false;
            if(s.equals(sc_tag)) save = true;
            if(s.equals(sc_end)) save = false;
            if (save){
                s = s.replaceAll("<[^>]+>", "");
                corpus.add(s);
            }*/
            s = s.replaceAll("<[^>]+>", "");
            corpus.add(s);
        }
        freader.close();

        return corpus;
    }

    private static void printNewCorpus(LinkedList<String> corpus){
        for(int i = 0; i < corpus.size(); i++){
            System.out.println(corpus.get(i));
        }

    }

}
package T1.reader;

import T1.utils.Document;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by pmatos9 on 30/09/17.
 */
public class Reader {

    public static Document readFile(String filename, String[] tags, int docID) throws IOException {
        String corpus  = "";
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
                    corpus = corpus.concat(s);
                }

            }
        }
        freader.close();

        return new Document(docID,corpus);
    }



}

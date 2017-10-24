package T2.IndexReader;

import java.io.*;
import java.util.*;

import T2.utils.Posting;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.apache.commons.io.FileUtils;
import sun.awt.image.ImageWatched;


/**
 * Created by pmatos9 on 19/10/17.
 */
public class Reader {

    private Map<String, List<T2.utils.Posting>> dic;

    public Reader() {
        this.dic = new TreeMap<String, List<Posting>>();
    }

    public void readFile(String filename) throws IOException {

        FileReader freader = new FileReader(filename);
        BufferedReader br = new BufferedReader(freader);
        String s;
        while((s = br.readLine()) != null) {
            parseContent(s);
        }
        freader.close();
    }

    private void parseContent(String s){
        String[] tmp = s.split("\t");
        String term = tmp[0];   //retirar o termo
        String postings_raw = tmp[1].substring(1,tmp[1].length()-1);    //retirar o []
        LinkedList<String> postings_parsed = new LinkedList<String>();

        String[] array_to_list = postings_raw.split(",");
        postings_parsed.addAll(Arrays.asList(array_to_list));   //ter todos os postings separados

        Posting tmp_posting;

        for(int i = 0; i < postings_parsed.size(); i++){

            if(i == 0) {
                String[] doc_freq = postings_parsed.get(i).split(":");
                int docid = new Integer(doc_freq[0]);
                int freq = new Integer(doc_freq[1]);
                tmp_posting = new Posting(docid,freq);
            }
            else{
                String post_parsed_2 = postings_parsed.get(i).substring(1,postings_parsed.get(i).length());
                String[] doc_freq = post_parsed_2.split(":");
                int docid = new Integer(doc_freq[0]);
                int freq = new Integer(doc_freq[1]);
                tmp_posting = new Posting(docid,freq);
            }

            if(dic.containsKey(term)){
                dic.get(term).add(tmp_posting);
            }
            else{
                List<Posting> list = new ArrayList<Posting>();
                list.add(tmp_posting);
                dic.put(term,list);
            }
        }

    }


    public Map<String, List<Posting>> getDic() {
        return dic;
    }

}

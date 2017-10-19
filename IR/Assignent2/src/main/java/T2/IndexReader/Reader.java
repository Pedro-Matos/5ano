package T2.IndexReader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import T2.utils.Posting;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
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

        System.out.println(term);
        for(int i = 0; i < postings_parsed.size(); i++){

            if(i == 0) {
                System.out.println(postings_parsed.get(i));
            }
            else{
                System.out.println(postings_parsed.get(i).substring(1,postings_parsed.get(i).length()));
            }

            
        }
        System.out.println("-------------------------");

    }

    /*
    public void addTokens(int docId, List<String> tokens){

    Multiset<String> multiset = HashMultiset.create(tokens);

    Posting tmp_posting;

        for(String token : multiset.elementSet()){
        tmp_posting = new Posting(docId,multiset.count(token));

        if(dic.containsKey(token)){
            dic.get(token).add(tmp_posting);
        }
        else{
            List<Posting> list = new ArrayList<Posting>();
            list.add(tmp_posting);
            dic.put(token,list);
        }

        terms.add(token);
    }


     */




}

package T3.index;

import T3.utils.Posting;
import T3.utils.TfIdfWeighting;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.util.*;

/**
 * Created by pmatos9 on 09/11/17.
 */
public class TfIdfIndexer {

    private static final String FILE_NAME = "tf_idf_indexer";
    private int cont;
    private Map<String, List<Posting>> dic;
    private Set<String> terms;
    private File outputDir;
    private int number_documents;

    public TfIdfIndexer(File outputDir, int number_documents){

        this.dic = new TreeMap();
        this.terms = new HashSet<String>();
        this.outputDir = outputDir;
        this.cont = 0;
        this.number_documents = number_documents;
        start_index();
    }

    /**
     * Initializes inverted index.
     */
    private void start_index(){
     /*
        Create output dir
      */
        outputDir.mkdir();
        try {
            FileUtils.cleanDirectory(outputDir);
        } catch (IOException ex) {
            throw new RuntimeException("There was a problem cleaning the directory.", ex);
        }
    }

    public void addTermFrequency(int docId, List<String> tokens){
        /*
            Count the tokens
         */
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
    }

    public void addDocumentFrequency(){
        /* o terms contêm todos os termos
           temos de percorrer todos os termos, e a cada termo ir ao dicionario ver a lista de postings
           na lista de postings temos de ver em quantos dicionarios aparece ()
         */


    }

    /**
     * Check the percentage of memory used
     * @return double with the percentage
     */
    private double getMemory(){
        double usage = 0;
        for (MemoryPoolMXBean mpBean: ManagementFactory.getMemoryPoolMXBeans()) {
            if ((mpBean.getType() == MemoryType.HEAP) && mpBean.getName().equalsIgnoreCase("PS Eden Space")) {
                usage = ((double) mpBean.getUsage().getUsed()/mpBean.getUsage().getMax()) * 100;
            }
        }
        return usage;
    }

}

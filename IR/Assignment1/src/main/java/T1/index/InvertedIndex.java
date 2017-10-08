package T1.index;

import T1.utils.Posting;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.apache.commons.io.FileUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Created by pmatos9 on 30/09/17.
 */
public class InvertedIndex {

    private static final String FILE_NAME = "index";
    private int cont;
    private Map<String, List<Posting>> dic;
    private Set<String> terms;
    /**
     * Output directory.
     */
    private File outputDir;

    public InvertedIndex(File outputDir){
        /*
            Garante que os elementos são organizados em ordem ascendente da chave.
         */
        this.dic = new TreeMap();
        this.terms = new HashSet<String>();
        this.outputDir = outputDir;
        this.cont = 0;
        start_index();
    }

    /**
     * Initializes inverted index.
     */
    private void start_index(){
     //Create output dir
        outputDir.mkdir();
        try {
            FileUtils.cleanDirectory(outputDir);
        } catch (IOException ex) {
            throw new RuntimeException("There was a problem cleaning the directory.", ex);
        }
    }

    public void addTokens(int docId, List<String> tokens){
        // Count tokens occurences
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

        /*verificar memória
         */
        if(getMemory()>80){
            writeFile();
            dic.clear();
            System.gc();
        }
    }

    public void writeFile(){
        File dir = new File(outputDir.getAbsolutePath());

        // Write block
        try {
            String blockFileName = FILE_NAME + cont++ + ".txt";
            PrintWriter pwt = new PrintWriter(new File(dir, blockFileName));

            for(Map.Entry<String, List<Posting>> entry : dic.entrySet()){
                pwt.println(entry.getKey() + "\t" + entry.getValue());
            }

            pwt.close();
        } catch (IOException ex) {
            throw new RuntimeException("There was a problem writing the index to a file", ex);
        }

    }
    
    private double getMemory(){
        double usage = 0;
        for (MemoryPoolMXBean mpBean: ManagementFactory.getMemoryPoolMXBeans()) {
            if ((mpBean.getType() == MemoryType.HEAP) && mpBean.getName().equalsIgnoreCase("PS Eden Space")) {
                //System.out.println(mpBean.getUsage().getUsed());
                //System.out.println(mpBean.getUsage().getMax());
                usage = ((double) mpBean.getUsage().getUsed()/mpBean.getUsage().getMax()) * 100;
            }
        }
        return usage;
    }


    public void close() {
        if (!dic.isEmpty()) {
            writeFile();
        }
        System.out.println("Done");
    }
}

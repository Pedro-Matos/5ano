package T3.Evaluation;

import T3.utils.DocumentScore;
import T3.utils.Eval;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by pmatos9 on 19/11/17.
 */
public class GoldEvaluator {

    private int max_relevante = 4;
    private Map<Integer, Map<Integer, Integer>> map_relevances = new HashMap<>();
    private Map<Integer, Eval> querys_evals = new HashMap<>();
    private double number_querys;


    public void readQueryRelevance(String filename) throws IOException {

        FileReader freader = new FileReader(filename);
        BufferedReader br = new BufferedReader(freader);
        String s;

        while((s = br.readLine()) != null) {
            String[] line_content = s.split(" ");
            int relevance = Integer.parseInt(line_content[2]);
            int doc_id = Integer.parseInt(line_content[1]);
            int query_id = Integer.parseInt(line_content[0]);

            if( relevance <= max_relevante){

                if(map_relevances.containsKey(query_id)){
                    map_relevances.get(query_id).put(doc_id,relevance);
                }
                else{
                    Map<Integer,Integer> tmp_map = new TreeMap();
                    tmp_map.put(doc_id,relevance);
                    map_relevances.put(query_id,tmp_map);
                }
            }
        }
        freader.close();
    }

    public void evaluateValues(Map<Integer, Map<Double, Integer>> map_of_maps, int n_corpus, int n_ranks){
        this.number_querys = map_of_maps.entrySet().size();

        Iterator it = map_of_maps.entrySet().iterator();
        while(it.hasNext()){
            Eval tmp_eval = new Eval();
            ArrayList<Integer> docs = new ArrayList<>();
            boolean first = false;

            Map.Entry pair = (Map.Entry) it.next();

            int query_id = (int) pair.getKey();

            //ir ao map_relevances e ir buscar os resultados da query em que estamos
            Map<Double, Integer> map_maps_values = (Map<Double, Integer>) pair.getValue();
            int countdocs = 0;
            Iterator tmp_it = map_maps_values.entrySet().iterator();
            while(tmp_it.hasNext()){
                countdocs++;
                Map.Entry tmp_pair = (Map.Entry) tmp_it.next();
                int doc_id = (int) tmp_pair.getValue();
                docs.add(doc_id);
                if(map_relevances.get(query_id).containsKey(doc_id)){
                    if (!first){
                        first = true;
                        tmp_eval.setMrr(1.0/countdocs);
                    }
                    double true_p = tmp_eval.getTrue_pos();
                    tmp_eval.setTrue_pos(true_p+1);
                    double map = tmp_eval.getMap();
                    //System.out.println("True_pos: " + tmp_eval.getTrue_pos());
                    //System.out.println("Countdocs: " + countdocs);
                    map += (tmp_eval.getTrue_pos()/countdocs * 1.0);
                    tmp_eval.setMap(map);
                    //System.out.println("Map: "+tmp_eval.getTrue_pos()/countdocs);
                }
                else{
                    double false_p = tmp_eval.getFalse_pos();
                    tmp_eval.setFalse_pos(false_p+1);
                }

                if(countdocs<= n_ranks){
                    tmp_eval.setTruePosRank(tmp_eval.getTrue_pos());
                    tmp_eval.setFalsePosRank(tmp_eval.getFalse_pos());
                }
            }

            Map<Integer, Integer> map_docs_rel = map_relevances.get(query_id);
            Iterator tmp_rel = map_docs_rel.entrySet().iterator();
            int cont = 0;
            while(tmp_rel.hasNext()){
                Map.Entry pair_rel = (Map.Entry) tmp_rel.next();

                int doc_id = (int) pair_rel.getKey();
                if(!docs.contains(doc_id)){
                    cont++;
                }
            }
            tmp_eval.setFalse_neg(cont);
            tmp_eval.setTrue_neg(n_corpus-tmp_eval.getTrue_pos()-tmp_eval.getFalse_pos() - tmp_eval.getFalse_neg());
            double tmp_map = tmp_eval.getMap();
            double true_ps = tmp_eval.getTrue_pos();
            tmp_map = true_ps != 0 ? tmp_map/true_ps : 0;
            tmp_eval.setMap(tmp_map);
            querys_evals.put(query_id,tmp_eval);
        }
    }

    public void calculateAll(int n_ranks){

        Iterator it = querys_evals.entrySet().iterator();
        while(it.hasNext()){
            //System.out.println("-----------------");

            Map.Entry pair = (Map.Entry) it.next();
            int query_id = (int) pair.getKey();
            Eval tmp_eval = (Eval) pair.getValue();
            //System.out.println("Map: " + tmp_eval.getMap());
            //set precion

            double true_pos_rank = tmp_eval.getTruePosRank();
            double false_pos_rank = tmp_eval.getFalsePosRank();
            tmp_eval.setMp10((true_pos_rank/(true_pos_rank+false_pos_rank))/n_ranks);



            double true_pos = tmp_eval.getTrue_pos();
            //System.out.println("True pos: " + true_pos);
            if(true_pos == 0){
                tmp_eval.setPrecision(0.0);
            }
            else{
                double false_pos = tmp_eval.getFalse_pos();
                //System.out.println("False pos:" + false_pos);
                tmp_eval.setPrecision(true_pos/(true_pos+false_pos));
                //System.out.println("Precision:" + tmp_eval.getPrecision());
            }

            //set recall
            double true_pos2 = tmp_eval.getTrue_pos();
            double false_neg = tmp_eval.getFalse_neg();
            if(true_pos2 == 0)
                tmp_eval.setRecal(0);
            else{
                tmp_eval.setRecal(true_pos2/(true_pos2+false_neg));
            }


            // set F-measure
            double recall = tmp_eval.getRecal();
            double precision = tmp_eval.getPrecision();

            if(recall == 0.0 && precision == 0.0)
                tmp_eval.setFmeasure(0.0);
            else
                tmp_eval.setFmeasure(2.0*recall*precision/(recall+precision));

            querys_evals.put(query_id,tmp_eval);

        }
    }

    public void writeScores(File dir, int rank_size){
        dir.mkdir();

        double f_precision = 0, f_recall = 0, f_meas = 0, f_map=0,
        f_mpr10 = 0, f_mrr = 0;

        try {
            String blockFileName = "eval.txt";
            PrintWriter pwt = new PrintWriter(new File(dir, blockFileName));

            Iterator it = querys_evals.entrySet().iterator();
            while (it.hasNext()){
                Map.Entry pair = (Map.Entry) it.next();

                int query_id = (int) pair.getKey();
                Eval tmp_eval = (Eval) pair.getValue();
                f_precision += tmp_eval.getPrecision();
                f_recall += tmp_eval.getRecal();
                f_meas += tmp_eval.getFmeasure();
                f_map += tmp_eval.getMap();
                f_mpr10 += tmp_eval.getMp10();
                f_mrr += tmp_eval.getMrr();
            }

            pwt.println("Mean Precision: " +f_precision/this.number_querys + "\n"
                    + "Mean Recall: " + f_recall/this.number_querys + "\n" +
                    "Mean F-measure: " + f_meas/this.number_querys + "\n" +
                    "Mean Average Precision: " + f_map/this.number_querys +  "\n" +
                    "Mean Precision at Rank"+rank_size +": " + f_mpr10/rank_size + "\n"
                    + "Mean Reciprocal Rank: " + f_mrr/this.number_querys + "\n" +
                    "Query throughput: " + "\n"
                    + "Median Query Latency"+ "\n");
            pwt.close();
        } catch (IOException ex) {
            throw new RuntimeException("There was a problem writing the index to a file", ex);
        }
    }






}

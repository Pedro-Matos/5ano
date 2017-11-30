package RSI.Reader;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by pmatos9 on 17/11/17.
 */

public class CsvReader {

    //patient csv
    private HashMap<String, LinkedList<String>> values = new HashMap<>();
    private LinkedList<String> lista = new LinkedList<>();

    //mapping csv
    private HashMap<String, String> mapping_values = new HashMap<>();
    private HashMap<String, String> description_values = new HashMap<>();
    private int csv_lines_n;



    /**
     * Name: csvReader
     *
     * This function is responsible for mapping the real values.
     * This means that it will fill an HashMap with one key a list of values,
     * each one for each line of the csv.
     */
    public HashMap<String, LinkedList<String>> read_patient(String name) throws IOException {

        try(BufferedReader br = new BufferedReader(new FileReader(name))) {
            String line = br.readLine();

            while (line != null) {
                lista.add(line);
                line = br.readLine();
            }
        }
        this.csv_lines_n = lista.size()-1;

        String [] header;
        header = lista.get(0).split(",");

        for(int n = 0; n < header.length; n++){
            LinkedList<String> l1 = new LinkedList<>();
            for(int i = 1; i < lista.size(); i++){
                String [] values = lista.get(i).split("/");
                l1.add(values[n]);
            }
            values.put(header[n], l1);
        }
        return values;
    }

    /**
     * Name: csvMappingReader
     *
     * This function is responsible for mapping the values from the auxiliary csv.
     * This csv is crucial to make the bridge between the names of the csv and the
     * TAG's names in the DICOM SR format.
     * It will fill a HashMap with one value per key.
     */
    public HashMap<String, String> read_mapping(String name)throws IOException{
        LinkedList<String> l = new LinkedList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(name))) {
            String line = br.readLine();

            while (line != null) {
                l.add(line);
                line = br.readLine();
            }
        }
        String [] keys;
        keys = l.get(0).split(",");
        String [] mapping;
        mapping = l.get(1).split(",");

        if(keys.length == mapping.length){
            for(int i = 0; i < keys.length; i++){
                mapping_values.put(mapping[i], keys[i]);
            }
        }
        else{
            System.exit(0);
        }

        return mapping_values;

    }

    public int getCsv_lines_n() {
        return csv_lines_n;
    }

}

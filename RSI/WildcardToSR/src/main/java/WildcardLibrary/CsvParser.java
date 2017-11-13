package WildcardLibrary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by Pedro Matos
 *
 * This class's objective is to obtain all the information from the csv in 2 HashMaps.
 *
 * The first HashMap 'mapping_values' is a map with all the keys from the principal csv and
 * the correspondence with the Tags in the DICOM SR format.
 *
 * The second HashMap 'real_values' is a map with the values of each key. For example, the 2º values
 * of the hash map, is the value from the 2º line of the csv.
 *
 */

public class CsvParser {
    private HashMap<String, String> mapping_values = new HashMap<>();
    private HashMap<String, LinkedList<String>> real_values = new HashMap<>();
    private LinkedList<String> lista = new LinkedList<>();
    private int csv_lines_n;

    /**
     * Name: csvReader
     *
     * This function is responsible for mapping the real values.
     * This means that it will fill an HashMap with one key a list of values,
     * each one for each line of the csv.
     */
    public void csvReader(String name) throws IOException {

        try(BufferedReader br = new BufferedReader(new FileReader(name))) {
            String line = br.readLine();

            while (line != null) {
                lista.add(line);
                line = br.readLine();
            }
        }
        csv_lines_n = lista.size();

        String [] keys;
        keys = lista.get(0).split(",");
        for(int n = 0; n < keys.length; n++){
            LinkedList<String> l1 = new LinkedList<>();
            for(int i = 1; i < lista.size(); i++){
                String [] values = lista.get(i).split("/");
                l1.add(values[n]);
            }
            real_values.put(keys[n], l1);
        }


        //Prints to check if the mapping is OK

        // get all the set of keys
        //Set<String> keys2 = real_values.keySet();
        // iterate through the key set and display key and values
        /*for (String key : keys2) {
            System.out.println("Key = " + key);
            System.out.println("Values = " + real_values.get(key));
        }*/

    }


    /**
     * Name: csvMappingReader
     *
     * This function is responsible for mapping the values from the auxiliary csv.
     * This csv is crucial to make the bridge between the names of the csv and the
     * TAG's names in the DICOM SR format.
     * It will fill a HashMap with one value per key.
     */
    public void csvMappingReader(String name)throws IOException{
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


        // get all the set of keys
        //Set<String> keys2 = mapping_values.keySet();
        // iterate through the key set and display key and values
        /*for (String key : keys2) {
            System.out.println("Key = " + key);
            System.out.println("Values = " + mapping_values.get(key));
        }*/

    }

    /**
     * Getters for the private attributes
     */

    public HashMap<String, String> getMapping_values() {
        return mapping_values;
    }

    public HashMap<String, LinkedList<String>> getReal_values() {
        return real_values;
    }

    public int getCsv_lines_n() {
        return csv_lines_n;
    }
}

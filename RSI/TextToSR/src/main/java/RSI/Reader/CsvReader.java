package RSI.Reader;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by pmatos9 on 17/11/17.
 */

public class CsvReader {

    private HashMap<String, LinkedList<String>> values = new HashMap<>();
    private LinkedList<String> lista = new LinkedList<>();


    /**
     * Name: csvReader
     *
     * This function is responsible for mapping the real values.
     * This means that it will fill an HashMap with one key a list of values,
     * each one for each line of the csv.
     */
    public HashMap<String, LinkedList<String>> read(String name) throws IOException {

        try(BufferedReader br = new BufferedReader(new FileReader(name))) {
            String line = br.readLine();

            while (line != null) {
                lista.add(line);
                line = br.readLine();
            }
        }
        int csv_lines_n = lista.size();

        String [] header;
        header = lista.get(0).split(",");

        for(int n = 0; n < header.length; n++){
            LinkedList<String> l1 = new LinkedList<>();
            for(int i = 1; i < lista.size(); i++){
                String [] values = lista.get(i).split(",");
                l1.add(values[n]);
            }
            values.put(header[n], l1);
        }
        return values;
    }

}

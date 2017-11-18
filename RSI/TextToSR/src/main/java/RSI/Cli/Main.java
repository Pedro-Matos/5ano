package RSI.Cli;

import RSI.Reader.CsvReader;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by pmatos9 on 17/11/17.
 */
public class Main {


    public static void main(String[] args) throws IOException {

        String file_csv = "patients.csv";
        CsvReader csvreader = new CsvReader();
        HashMap<String, LinkedList<String>> values;
        values = csvreader.read(file_csv);


    }


}

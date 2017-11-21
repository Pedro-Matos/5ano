package RSI.Cli;

import RSI.Reader.CsvReader;
import RSI.Reader.MappingTids;
import RSI.Structure.CID;
import RSI.Structure.TID;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by pmatos9 on 17/11/17.
 */
public class Main {


    public static void main(String[] args) throws IOException {


        /*
            Read csv information regarding the patient
         */
        String file_csv = "patients.csv";
        CsvReader csvreader = new CsvReader();
        HashMap<String, LinkedList<String>> values;
        values = csvreader.read(file_csv);


        /*
            Read the TID information
         */

        File mainFolder = new File("mamo_tids");
        File[] listOfFiles = mainFolder.listFiles();
        LinkedList<String > files_names = new LinkedList<>();
        for (File listOfFile : listOfFiles)
            files_names.add(listOfFile.getAbsolutePath());
        MappingTids m1 = new MappingTids(files_names, mainFolder.getPath());
        m1.StartMapping();
        HashMap<String, TID> tids = m1.getTIDs();
        System.out.println(tids.size());
        HashMap<String, CID> cidss = m1.getCIDs();
        System.out.println(cidss.size());
    }


}

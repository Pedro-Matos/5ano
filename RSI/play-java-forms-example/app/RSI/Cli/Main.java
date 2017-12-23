package RSI.Cli;

import RSI.Dicom.DicomCreator;
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
        String file_csv = "ideal.csv";
        CsvReader csvreader = new CsvReader();
        HashMap<String, LinkedList<String>> values;
        values = csvreader.read_patient(file_csv);
        String map_csv = "mapping.csv";
        HashMap<String, String> mapping_values;
        mapping_values = csvreader.read_mapping(map_csv);


        /*
            Read the TID information
         */
        File mainFolder = new File("mamo_tids");
        File[] listOfFiles = mainFolder.listFiles();
        LinkedList<String > files_names = new LinkedList<>();
        for (File listOfFile : listOfFiles) {
            files_names.add(listOfFile.getPath());
        }
        MappingTids m1 = new MappingTids(files_names);
        m1.StartMapping();
        HashMap<String, TID> tids = m1.getTIDs();
        HashMap<String, CID> cids = m1.getCIDs();


        /*Iterator it = tids.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry pair = (Map.Entry) it.next();

            TID tid = (TID) pair.getValue();
            System.out.println(tid.getTriplet());
        }*/

        String output_dir = "output";
        createDir(output_dir);
        DicomCreator d1 = new DicomCreator(tids,cids,mapping_values,values,output_dir);
        d1.CreateDicom(csvreader.getCsv_lines_n());

    }

    public static void createDir(String directory_name){
        File theDir = new File(directory_name);

        // if the directory does not exist, create it

        try{
            theDir.mkdir();
        }
        catch(SecurityException se){
            System.out.println(se);
        }

    }


}

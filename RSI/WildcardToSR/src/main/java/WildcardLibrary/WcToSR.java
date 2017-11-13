package WildcardLibrary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by Pedro Matos
 * The main class
 */
public class WcToSR {
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) throws IOException {
        LinkedList<String> files_names = new LinkedList<>();
        String ideal_csv, mapping_csv;
        String directory_name;

        /**
         * csv2dcm example1.csv —ties directoryTids —structure abc —output dicomFilesDirectory
         */

        // criar jar - check
        // ler com comand line - check
        // 1º argumento - csv principal
        // 2º argumento - csv mapping
        // 3º argumento - diretorio dos ficheiros tid
        // 4º argumento - directorio para guardar os dicom sr

        if(args.length == 0){
            System.out.println("Error. There are missing arguments.");
            System.exit(0);
        }

        ideal_csv = args[0].toString();
        mapping_csv = args[1].toString();
        directory_name = args[3].toString();
        System.out.println();
        File mainFolder = new File(args[2]);
        File files[];
        files = mainFolder.listFiles();


        for(int i = 0; i< files.length; i++){
            files_names.add(files[i].getName());
        }

        createDir(directory_name);
        MappingTids m1 = new MappingTids(files_names, mainFolder.getPath());
        m1.StartMapping();


        CsvParser c1 = new CsvParser();
        c1.csvReader(ideal_csv);
        c1.csvMappingReader(mapping_csv);

        DicomCreator d1 = new DicomCreator(m1.getTIDs(),m1.getCIDs(),c1.getMapping_values(),c1.getReal_values(), directory_name);
        d1.CreateDicom(c1.getCsv_lines_n());


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

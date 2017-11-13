package WildcardLibrary;

import org.dcm4che2.data.DicomObject;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by pedroferreiradematos on 16/09/16.
 *
 * This class is used when another user wants to use this library in another project in other to create
 * DICOM SR files.
 *
 */





public class Generator {
    private LinkedList<String> files_names = new LinkedList<>();
    private String ideal_csv, mapping_csv;
    private String template_dir, output_dir;
    private MappingTids m1;
    private CsvParser c1;



    /**
     * configureMapping(csv1, csv2, folder1)
     * is a method that allows the user to give as inputs the csv's containing the personal information necessary
     * to create the dicom sr.
     * The user also provides the folder where are stored all the jsons with the templat's format.
     *
     *Inputs: 2 csv files.
     *      1º: ideal.csv     - csv containing the personal information
     *      2º: mapping.csv   - csv indicating the mapping between the tags used in the 1º csv and the tags of the DICOM SR nomenclature.
     *      3º: folder_name   - name of the folder/dir
     *
     *Outputs: No output
     */

    public void configureMapping(String csv1, String csv2, String folder)throws IOException {
        ideal_csv = csv1;
        mapping_csv = csv2;
        template_dir = folder;

        File mainFolder = new File(template_dir);
        File files[];
        files = mainFolder.listFiles();

        for(int i = 0; i< files.length; i++){
            files_names.add(files[i].getName());
        }

        m1 = new MappingTids(files_names, mainFolder.getPath());
        m1.StartMapping();


        c1 = new CsvParser();
        c1.csvReader(ideal_csv);
        c1.csvMappingReader(mapping_csv);

    }


    /**
     * generateSR(String folder) is a method that creates the DICOM SR files in a directory specified by the user. That directory name
     * is the input parameter.
     */

    public void generateSR(String folder){
        output_dir = folder;
        createDir(output_dir);
        DicomCreator d1 = new DicomCreator(m1.getTIDs(),m1.getCIDs(),c1.getMapping_values(),c1.getReal_values(), output_dir);
        d1.CreateDicom(c1.getCsv_lines_n());
    }

    private static void createDir(String directory_name){
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

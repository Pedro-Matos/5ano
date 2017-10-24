package T2.cli;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import T2.IndexReader.Reader;
import T2.BooleanRetrieval.BooleanRetrieval;
import T2.utils.Posting;
import org.apache.commons.cli.*;

/**
 * Created by pmatos9 on 19/10/17.
 */
public class Main {
    public static void main(String[] args) throws IOException {

        /*
         * here we indicate all the options the user can use.
         **/

        CommandLineParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("i", "index-path", true, "File with index.");
        options.addOption("o", "output-directory", true, "Output directory for the score information.");
        options.addOption("c", "cranfield-directory", true, "Directory with the queries.");

        CommandLine commandLine;
        try {
            commandLine = parser.parse(options, args);
        } catch (ParseException ex) {
            printProgramHelp(options, "There was a problem processing the input arguments.");
            return;
        }

        /*
         * if no arguments are given, we print the program help to the user.
         */
        if(commandLine.getOptions().length == 0){
            printProgramHelp(options, "No arguments given.");
            System.exit(0);
        }

        /*
         * Input path of the index texts (directory)
         */
        String filename = null;
        if(commandLine.hasOption('i')){
            filename = commandLine.getOptionValue('i');

        }

        /*
         * Output path of score file (directory)
         */
        String out_p = null;
        if(commandLine.hasOption('o')){
            out_p = commandLine.getOptionValue('o');
            File test = new File(out_p);
            if (!test.isDirectory()) {
                if (!test.mkdir()) {
                    System.out.println("The specified output directory can't be created.");
                    System.exit(0);
                }
            }
        }

        /*
         * Input path of the cranfield queries (directory)
         */
        String queries = null;
        if(commandLine.hasOption('c')){
            queries = commandLine.getOptionValue('c');
        }

        Reader indexreader = new Reader();
        indexreader.readFile(filename);

        BooleanRetrieval boolRetr = new BooleanRetrieval();
        File f = new File(queries);
        List<String> querys = boolRetr.ParseQuerys(f);

        Map<String, List<Posting>> dic = indexreader.getDic();
        File dir = new File(out_p);
        boolRetr.ProcessTerms(querys,dic,dir);

    }
    /**
     * Print program help message.
     *
     * @param options Command line arguments
     * @param message Message to be shown
     */
    private static void printProgramHelp(final Options options, String message) {
        final String HEADER = "\nAssignment 1	\n";
        final String USAGE =  "-i <index> "
                + "-o <directory> ";

        if (message != null) {
            System.out.println(message);
        }
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(150, USAGE, HEADER, options, "");
    }
}

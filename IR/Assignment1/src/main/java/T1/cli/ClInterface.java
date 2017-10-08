package T1.cli;

import T1.index.InvertedIndex;
import T1.tokenizer.SimpleTokenizer;
import T1.tokenizer.StrongTokenizer;
import T1.tokenizer.Tokenizer;
import T1.utils.Document;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by pmatos9 on 05/10/17.
 */
public class ClInterface {
    public static void main(String[] args) throws IOException {

        CommandLineParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("i", "input-path", true, "Directory with corpus files.");
        options.addOption("o", "output-directory", true, "Output directory for the index information.");
        options.addOption("t", "tokenizer type", true, "Tokenizer type.");
        options.addOption("s", "stopwords-path", true, "Stopwords file path.");
        options.addOption("h", "help", false, "Help information.");

        CommandLine commandLine;
        try {
            commandLine = parser.parse(options, args);
        } catch (ParseException ex) {
            printProgramHelp(options, "There was a problem processing the input arguments.");
            return;
        }


        if(commandLine.getOptions().length == 0){
            printProgramHelp(options, "No arguments given.");
            System.exit(0);
        }

        // Input path
        String input_p = null;
        if(commandLine.hasOption('i')){
            input_p = commandLine.getOptionValue('i');
            File test = new File(input_p);
            if (!test.isDirectory() || !test.canRead()) {
                System.out.println("The specified corpus directory is not a directory or is not readable.");
                System.exit(0);
            }
        }

        // Output path
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

        // Stopwords location
        String stop = null;
        if(commandLine.hasOption('s')){
            stop = commandLine.getOptionValue('s');
            File stopwordsFile = new File(stop);
            if(!stopwordsFile.exists()) {
                System.out.println("The " + stop + " file does not exist");
                System.exit(0);
            }
        }

        Tokenizer tokenizer = null;
        // Type of tokenizer
        String str_tokenizer;
        if(commandLine.hasOption('t')){
            str_tokenizer = commandLine.getOptionValue('t');
            if(!str_tokenizer.equalsIgnoreCase("strong") && !str_tokenizer.equalsIgnoreCase("simple")) {
                System.out.println("Not a tokenizer type");
                System.exit(0);
            }
            if(str_tokenizer.equalsIgnoreCase("strong"))
                tokenizer = new StrongTokenizer(stop);
            else if(str_tokenizer.equalsIgnoreCase("simple"))
                tokenizer = new SimpleTokenizer();
        }

        // Help information
        if (commandLine.hasOption('h')) {
            printProgramHelp(options, "Help information");
            System.exit(0);
        }


        File output = new File(out_p);
        T1.reader.Reader reader = new T1.reader.Reader();
        InvertedIndex invIndexer = new InvertedIndex(output);


        File folder = new File(input_p);
        File[] listOfFiles = folder.listFiles();
        String[] tags = {"<TEXT>", "<AUTHOR>"};

        int id = 1;
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()){
                Document corpus = T1.reader.Reader.readFile(listOfFile.getPath(), tags,id);
                //printNewCorpus(corpus.getCorpus(), listOfFile.getName());
                List<String> tokens = tokenizer.tokenize(corpus.getCorpus());
                // Add tokens to inverted index
                invIndexer.addTokens(corpus.getId(), tokens);
                id++;
            }
        }
        invIndexer.close();

    }


    /**
     * Print program help message.
     *
     * @param options Command line arguments
     * @param message Message to be shown
     */
    private static void printProgramHelp(final Options options, String message) {
        final String HEADER = "\nAssignment 1	\n";
        final String USAGE =  "-i <directory> "
                + "-o <directory> "
                + "-t <tokenizer> simple/strong "
                + "-s <directory> "
                + "-h help ";

        if (message != null) {
            System.out.println(message);
        }
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(150, USAGE, HEADER, options, "");
    }
}

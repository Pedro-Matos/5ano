import org.tartarus.snowball.SnowballStemmer;

import java.io.*;
import java.util.HashSet;
import java.util.LinkedList;

public class Tokenizer {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException {

        String stopWordsFile = "stopwords.txt";
        HashSet<String> stopWords = loadStopWords(stopWordsFile);

        String outputDirectoryName = "out_tokenized";
        File directory = new File(outputDirectoryName);
        if (! directory.exists())
            directory.mkdir();

        File folder = new File("out_corpus");
        File[] listOfFiles = folder.listFiles();

        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()){
                LinkedList<String> file = removeStopWords(listOfFile, stopWords);
                tokenizeFile(file, listOfFile.getName());
            }

        }
    }

    private static void tokenizeFile(LinkedList<String> file, String cont) throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException {

        Class stemClass = Class.forName("org.tartarus.snowball.ext.englishStemmer");
        SnowballStemmer stemmer = (SnowballStemmer) stemClass.newInstance();

        int repeat = 1;

        Writer output = new OutputStreamWriter(new FileOutputStream("out_tokenized/" + cont));
        output = new BufferedWriter(output);

        StringBuffer input = new StringBuffer();

        for(String line: file){
            for(int l = 0; l<line.length(); l++){
                char ch = line.charAt(l);
                if(Character.isWhitespace(ch)){
                    if(input.length() > 0){
                        stemmer.setCurrent(input.toString());
                        for(int i = repeat; i != 0; i--)
                            stemmer.stem();
                        output.write(stemmer.getCurrent());
                        output.write('\n');
                        input.delete(0, input.length());
                    }
                } else {
                    input.append(Character.toLowerCase(ch));
                }
            }
        }
        output.flush();
        output.close();
    }

    private static HashSet<String> loadStopWords(String stopWordsFile) throws IOException {
        HashSet<String> words = new HashSet<String>();
        BufferedReader br = new BufferedReader(new FileReader(stopWordsFile));
        String line;
        while ((line = br.readLine()) != null)
            words.add(line);

        return words;
    }

    private static LinkedList<String> removeStopWords(File fileName, HashSet<String> stopWords) throws IOException {
        LinkedList<String> file = new LinkedList<String>();
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;

        while ((line = br.readLine()) != null) {
            StringBuilder stringBuilder = new StringBuilder();
            String[] splitStr = line.trim().split("\\s+");
            for(String str: splitStr){
                if(!stopWords.contains(str))
                    stringBuilder.append(str + " ");
            }

            file.add(stringBuilder.toString());
        }

        return file;
    }

}

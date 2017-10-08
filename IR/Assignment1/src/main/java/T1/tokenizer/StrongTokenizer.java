package T1.tokenizer;

import T1.stemmer.Stemmer;
import T1.stopwords.StopWordsRemover;
import com.google.common.base.Splitter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Create by Pedro Matos & Tiago Bastos
 */
public class StrongTokenizer implements Tokenizer {

    private StopWordsRemover stopWordsRemover;
    private Stemmer stemmer;

    public StrongTokenizer(String stopwords) {
        stopWordsRemover = new StopWordsRemover(stopwords);
        stemmer = new Stemmer();
    }

    public List<String> tokenize(String corpusText) {

        List<String> tokens = new ArrayList<String>();

        corpusText = corpusText.replaceAll("[,;.?=!()*\\/\\+\\-]", "");

        Splitter splitter = Splitter.onPattern("\\s+").omitEmptyStrings();
        tokens.addAll(splitter.splitToList(corpusText));

        tokens = stopWordsRemover.removeStopWords(tokens);
        tokens = stemmer.stem(tokens);

        return tokens;
    }

}

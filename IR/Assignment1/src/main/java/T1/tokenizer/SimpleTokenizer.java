package T1.tokenizer;

import T1.stemmer.Stemmer;
import T1.stopwords.StopWordsRemover;
import com.google.common.base.Splitter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SimpleTokenizer implements Tokenizer {

    private StopWordsRemover stopWordsRemover;
    private Stemmer stemmer;

    public SimpleTokenizer(String stopwords) {
        stopWordsRemover = new StopWordsRemover(stopwords);
        stemmer = new Stemmer();
    }

    public List<String> tokenize(String corpusText) {

        List<String> tokens = new ArrayList<String>();

        corpusText = corpusText.replaceAll("(\\d)\\.(\\d)", "$1\\$dot\\$$2");

        corpusText = corpusText.replaceAll("[,;.?!()*\\/\\+\\-]", "");

        corpusText = corpusText.replaceAll("\\'|\\.|(&[a-zA-Z\\d]+;)", " ");
        corpusText = corpusText.toLowerCase();

        Splitter splitter = Splitter.onPattern("[^a-zA-Z\\d\\.]+").omitEmptyStrings();
        tokens.addAll(splitter.splitToList(corpusText));

        tokens = tokens.stream().filter(x -> x.length() > 2).collect(Collectors.toList());

        tokens = stopWordsRemover.removeStopWords(tokens);
        tokens = stemmer.stem(tokens);

        return tokens;
    }


}

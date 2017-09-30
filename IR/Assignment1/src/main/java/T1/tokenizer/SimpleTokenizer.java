package T1.tokenizer;

import T1.stopwords.StopWordsRemover;
import com.google.common.base.Splitter;

import java.util.ArrayList;
import java.util.List;

public class SimpleTokenizer implements Tokenizer {

    private StopWordsRemover stopWordsRemover;

    public SimpleTokenizer() {
        stopWordsRemover = new StopWordsRemover("stopwords.txt");
    }

    public List<String> tokenize(String corpusText) {

        List<String> tokens = new ArrayList<String>();

        corpusText = corpusText.replaceAll("[,;.?!()]", "");

        corpusText = corpusText.toLowerCase();

        Splitter splitter = Splitter.onPattern("\\s+").omitEmptyStrings();
        tokens.addAll(splitter.splitToList(corpusText));

        tokens = stopWordsRemover.removeStopWords(tokens);

        return tokens;
    }


}

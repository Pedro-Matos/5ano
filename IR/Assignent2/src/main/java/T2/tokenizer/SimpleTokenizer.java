package T2.tokenizer;

import T2.stemmer.Stemmer;
import T2.stopwords.StopWordsRemover;
import com.google.common.base.Splitter;

import java.util.*;
import java.util.stream.Collectors;

public class SimpleTokenizer implements Tokenizer {


    public SimpleTokenizer() {
    }

    public List<String> tokenize(String corpusText) {

        List<String> tokens = new ArrayList<String>();



        corpusText = corpusText.replaceAll("[,;.?!=()*\\/\\+\\-]", "");

        corpusText = corpusText.replaceAll("\\'|(&[a-zA-Z]+;)", " ");
        corpusText = corpusText.replaceAll("\\d","");
        corpusText = corpusText.toLowerCase();

        Splitter splitter = Splitter.onPattern("\\s+").omitEmptyStrings();
        tokens.addAll(splitter.splitToList(corpusText));

        tokens = tokens.stream().filter(x -> x.length() > 2).collect(Collectors.toList());

        return tokens;
    }


}

package T1.tokenizer;

import com.google.common.base.Splitter;

import java.util.ArrayList;
import java.util.List;

public class SimpleTokenizer implements Tokenizer {
    public List<String> tokenize(String corpusText) {

        List<String> tokens = new ArrayList<String>();

        corpusText = corpusText.replaceAll("[,;.?!()]", "");

        corpusText = corpusText.toLowerCase();

        Splitter splitter = Splitter.onPattern("\\s+").omitEmptyStrings();
        tokens.addAll(splitter.splitToList(corpusText));

        return tokens;
    }


}

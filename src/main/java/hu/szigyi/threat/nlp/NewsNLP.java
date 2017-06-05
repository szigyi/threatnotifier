package hu.szigyi.threat.nlp;

import com.google.common.io.Files;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.time.TimeAnnotations;
import edu.stanford.nlp.time.TimeAnnotator;
import edu.stanford.nlp.time.TimeExpression;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import hu.szigyi.threat.model.PageModel;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Properties;

/**
 * Created by szabolcs on 04/06/2017.
 */
public class NewsNLP {

    private AnnotationPipeline pipeline;

    public NewsNLP() throws IOException, ClassNotFoundException {
        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
//        props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
//        props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse");

//        pipeline = new StanfordCoreNLP(props);
        pipeline = new AnnotationPipeline();
        pipeline.addAnnotator(new TokenizerAnnotator(true));
        pipeline.addAnnotator(new WordsToSentencesAnnotator(true));
        pipeline.addAnnotator(new POSTaggerAnnotator(true));
        pipeline.addAnnotator(new NERCombinerAnnotator(true));
//        pipeline.addAnnotator(new DependencyParseAnnotator());
//        pipeline.addAnnotator(new DeterministicCorefAnnotator(props));
        pipeline.addAnnotator(new TimeAnnotator("sutime", props));
        System.out.println("CoreNLP is ready");
    }

    public void process(PageModel model) {
        // create an empty Annotation just with the given text
        Annotation annotation = new Annotation(model.getContent());
        annotation.set(CoreAnnotations.DocDateAnnotation.class, "2013-07-14 15:38:29");

        // run all Annotators on this text
        pipeline.annotate(annotation);

        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);

        for(CoreMap sentence: sentences) {
            System.out.println(sentence.get(CoreAnnotations.TextAnnotation.class));
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                // this is the text of the token
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                // this is the POS tag of the token
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                // this is the NER label of the token
                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);

                System.out.println("word: " + word + " pos: " + pos + " ne:" + ne);
            }
            // this is the parse tree of the current sentence
            Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
            System.out.println("parse tree:\n" + tree);

            // this is the Stanford dependency graph of the current sentence
            SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
            System.out.println("dependency graph:\n" + dependencies);
        }

        List<CoreMap> timexAnnsAll = annotation.get(TimeAnnotations.TimexAnnotations.class);
        for (CoreMap cm : timexAnnsAll) {
            List<CoreLabel> tokens = cm.get(CoreAnnotations.TokensAnnotation.class);
            System.out.println(cm + " [from char offset " +
                    tokens.get(0).get(CoreAnnotations.CharacterOffsetBeginAnnotation.class) +
                    " to " + tokens.get(tokens.size() - 1).get(CoreAnnotations.CharacterOffsetEndAnnotation.class) + ']' +
                    " --> " + cm.get(TimeExpression.Annotation.class).getTemporal());
        }
    }
}

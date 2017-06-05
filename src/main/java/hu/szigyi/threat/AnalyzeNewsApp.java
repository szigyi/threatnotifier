package hu.szigyi.threat;

import hu.szigyi.threat.crawler.NewsCrawlController;
import hu.szigyi.threat.crawler.NewsCrawlerFactory;
import hu.szigyi.threat.model.PageModel;
import hu.szigyi.threat.nlp.NewsNLP;
import hu.szigyi.threat.validate.ModelValidator;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by szabolcs on 04/06/2017.
 */
public class AnalyzeNewsApp {

    public static void main(String[] args) throws Exception {
        String filePath = "/example_sentence.txt";

        URL resource = AnalyzeNewsApp.class.getResource(filePath);
        byte[] bytes = Files.readAllBytes(Paths.get(resource.toURI()));
        String document = new String(bytes);
        PageModel model = new PageModel();
        model.setContent(document);

        // get content and if contains terror | incident | attack
        // then retrieve the location, city, country
        // if city match then send alert with URL of the news which triggered the event!!!
        NewsNLP nlp = new NewsNLP();

        nlp.process(model);
    }
}

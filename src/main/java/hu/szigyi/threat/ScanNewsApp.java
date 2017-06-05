package hu.szigyi.threat;

import hu.szigyi.threat.crawler.NewsCrawlController;
import hu.szigyi.threat.crawler.NewsCrawlerFactory;
import hu.szigyi.threat.model.PageModel;
import hu.szigyi.threat.validate.ModelValidator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by szabolcs on 04/06/2017.
 */
public class ScanNewsApp {

    public static void main(String[] args) throws Exception {
        String rootUrl = "http://www.bbc.co.uk/news/uk-40146916";
        String newsRootUrl = "http://www.bbc.co.uk/news/uk-";
        String storageFolder = "data/crawl/root";
        int numberOfCrawlers = 7;
        int maxPagesToFetch = 100;
        List<String> suspiciousWords = Arrays.asList("terror", "incident", "attack", "terrorist");

        ModelValidator validator = new ModelValidator();
        validator.setSuspiciousWords(suspiciousWords);
        NewsCrawlController c = new NewsCrawlController(rootUrl, storageFolder, maxPagesToFetch);
        NewsCrawlerFactory factory = new NewsCrawlerFactory(newsRootUrl, validator);

        c.getController().start(factory, numberOfCrawlers);

        // get date - is it actual?
        List<Object> crawlersLocalData = c.getController().getCrawlersLocalData();
        List<PageModel> allModels = new ArrayList<>();
        System.out.println("Suspicious news");
        for (Object crawlersLocalD : crawlersLocalData) {
            List<PageModel> models = (List<PageModel>) crawlersLocalD;

            for (PageModel model : models) {
                System.out.println("");
                System.out.println("####################################");
                System.out.println(model.getUrl());
                if (model.getUrl().equals("http://www.bbc.co.uk/news/uk-40146916")) {
                    System.out.println(model.getContent());
                }
            }
            allModels.addAll(models);
        }

        // get content and if contains terror | incident | attack
        // then retrieve the location, city, country
        // if city match then send alert with URL of the news which triggered the event!!!
//        NewsNLP nlp = new NewsNLP();
//
//        for (PageModel model : allModels) {
//            nlp.process(model);
//        }

    }
}

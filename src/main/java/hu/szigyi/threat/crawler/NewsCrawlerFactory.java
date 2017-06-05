package hu.szigyi.threat.crawler;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import hu.szigyi.threat.validate.ModelValidator;

import java.util.Map;

/**
 * Created by szabolcs on 04/06/2017.
 */
public class NewsCrawlerFactory implements CrawlController.WebCrawlerFactory {

    private String newsRootUrl;

    private ModelValidator validator;

    public NewsCrawlerFactory(String newsRootUrl, ModelValidator validator) {
        this.newsRootUrl = newsRootUrl;
        this.validator = validator;
    }

    public WebCrawler newInstance() {
        NewsCrawler crawler = new NewsCrawler();
        crawler.setNewsRootUrl(newsRootUrl);
        crawler.setValidator(validator);
        return crawler;
    }
}

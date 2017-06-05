package hu.szigyi.threat.crawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

/**
 * Created by szabolcs on 04/06/2017.
 */
public class NewsCrawlController {

    private CrawlController controller;

    public NewsCrawlController(String rootUrl, String storageFolder, int maxPagesToFetch) throws Exception {
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(storageFolder);
        config.setPolitenessDelay(200);
        config.setMaxPagesToFetch(maxPagesToFetch);

        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        controller = new CrawlController(config, pageFetcher, robotstxtServer);

        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */
        controller.addSeed(rootUrl);
    }

    public CrawlController getController() {
        return controller;
    }
}

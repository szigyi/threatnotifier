package hu.szigyi.threat.crawler;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import hu.szigyi.threat.model.PageModel;
import hu.szigyi.threat.validate.ModelValidator;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by szabolcs on 04/06/2017.
 */
public class NewsCrawler extends WebCrawler {

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg|png|mp3|mp3|zip|gz))$");

    private DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ").withOffsetParsed();

    private String newsRootUrl;

    private ModelValidator validator;

    private List<PageModel> recentPages;

    public NewsCrawler() {
        recentPages = new ArrayList<>();
    }

    /**
     * This method receives two parameters. The first parameter is the page
     * in which we have discovered this new url and the second parameter is
     * the new url. You should implement this function to specify whether
     * the given url should be crawled or not (based on your crawling logic).
     * In this example, we are instructing the crawler to ignore urls that
     * have css, js, git, ... extensions and to only accept urls that start
     * with "http://www.ics.uci.edu/". In this case, we didn't need the
     * referringPage parameter to make the decision.
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        return !FILTERS.matcher(href).matches() && href.startsWith(newsRootUrl);
    }

    /**
     * This function is called when a page is fetched and ready
     * to be processed by your program.
     */
    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        System.out.println("URL: " + url);

        if (page.getParseData() instanceof HtmlParseData) {
            PageModel pageModel = new PageModel();
            pageModel.setUrl(url);
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            pageModel.setTitle(htmlParseData.getTitle());
            pageModel.setContent(htmlParseData.getText());
            pageModel.setHtmlContent(htmlParseData.getHtml());

            Map<String, String> metaTags = htmlParseData.getMetaTags();

//            for (Map.Entry<String, String> entry : metaTags.entrySet()) {
//                System.out.println(entry.getKey() + "--" + entry.getValue());
//            }
            String modifiedTime = metaTags.get("article:modified_time");
            if (null != modifiedTime) {
                DateTime dateTime = df.parseDateTime(modifiedTime);
                pageModel.setLastModified(dateTime);
            }
            String publishedTime = metaTags.get("article:published_time");
            if (null != publishedTime) {
                DateTime dateTime = df.parseDateTime(publishedTime);
                pageModel.setPublished(dateTime);
            }

            boolean valid = validator.validate(pageModel);

            if (valid) {
                recentPages.add(pageModel);
            }
        }
    }

    @Override
    public Object getMyLocalData() {
        return this.recentPages;
    }

    public void setNewsRootUrl(String newsRootUrl) {
        this.newsRootUrl = newsRootUrl;
    }

    public void setValidator(ModelValidator validator) {
        this.validator = validator;
    }
}

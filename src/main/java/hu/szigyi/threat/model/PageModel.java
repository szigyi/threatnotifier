package hu.szigyi.threat.model;

import edu.uci.ics.crawler4j.crawler.Page;
import org.joda.time.DateTime;

/**
 * Created by szabolcs on 04/06/2017.
 */
public class PageModel {

    private String url;

    private DateTime lastModified;

    private DateTime published;

    private String title;

    private String content;

    private String htmlContent;

    public DateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(DateTime lastModified) {
        this.lastModified = lastModified;
    }

    public DateTime getPublished() {
        return published;
    }

    public void setPublished(DateTime published) {
        this.published = published;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }
}

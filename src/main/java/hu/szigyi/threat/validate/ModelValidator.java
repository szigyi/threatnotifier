package hu.szigyi.threat.validate;

import hu.szigyi.threat.model.PageModel;

import java.util.List;

/**
 * Created by szabolcs on 04/06/2017.
 */
public class ModelValidator {

    private int recentNewsInHour = 5;

    private List<String> suspiciousWords;

    public boolean validate(PageModel model) {
        // Later version
//        DateTime now = DateTime.now();
//        if (null != model.getLastModified()) {
//            if (model.getLastModified().isAfter(now.minusHours(recentNewsInHour))) {
//                System.out.println("Recent news");
//            }
//        }
        String title = model.getTitle();
        String text = model.getContent();
        boolean titleContains = containsSuspiciousWord(title);
        boolean textContains = containsSuspiciousWord(text);

        return titleContains || textContains;
    }

    private boolean containsSuspiciousWord(String text) {
        for (String suspiciousWord : suspiciousWords) {
            boolean contains = text.toLowerCase().contains(suspiciousWord);
            if (contains) {
                return true;
            }
        }
        return false;
    }

    public void setSuspiciousWords(List<String> suspiciousWords) {
        this.suspiciousWords = suspiciousWords;
    }
}

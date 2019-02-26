package book_7days.wiki;

import java.util.HashMap;
/* ▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂ */

public class WikiFinder {

    /**
     *  (define counts-table '())
     *
     *  (define (countWord word)
     *      (merge word counts-table))
     *
     *  [main]
     *
     *
     *
     */
    private static final HashMap<String, Integer> counts = new HashMap<>();  // 唯一的全局变量

    static void countWord(String word) {
        Integer n = counts.get(word);
        if (n == null) {
            counts.put(word, 1);
        } else {
            counts.put(word, n + 1);
        }
    }
    //***************************************************************************************
    public static void work() {
        Iterable<Page> pages = new Pages(10000, "enwiki.xml");
        for (Page page : pages) {
            Iterable<String> words = new Words(page.getText());
            for (String word : words) {
                countWord(word);
            }
        }
    }
}


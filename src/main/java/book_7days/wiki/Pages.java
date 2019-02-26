package book_7days.wiki;

import java.util.Iterator;

/* ▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂ */

class Page {
    private boolean isEndPage;

    public Page(int idx) {
    }

    public Page(boolean flag) {
    }

    public boolean isPoisonPill(){
        return isEndPage;
    }

    String getText(){
        return "";
    }
}

public class Pages implements Iterable<Page> {

    public Pages(int cnt, String config) {
    }

    public Iterator<Page> iterator() {
        return null;
    }
}


class Words implements Iterable<String> {
    public Words(String text) {

    }

    public Iterator<String> iterator() {
        return null;
    }
}

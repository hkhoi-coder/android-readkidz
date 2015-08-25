package hkhoi.readkidz;


/**
 * @author hkhoi
 */
public class ContentReader {

    private Book curBook;
    private int curPage;

    /**
     * Set up book reader
     *
     * @param curBook
     */
    public ContentReader(Book curBook) {
        this.curBook = curBook;
        curPage = 1;
    }

    /**
     * Note: page is base 1 index
     *
     * @param page
     * @return Html String from a page
     */
    public String getHtmlFromPage(int page) {
        int totalPage = curBook.getPageList().size();
        if (page > totalPage) {
            curPage = totalPage;
        } else if (page < 1) {
            curPage = 1;
        } else {
            curPage = page;
        }
        return curBook.getPageList().get(curPage - 1);
    }

    /**
     * Note: page is base 1 index
     *
     * @return next page
     */
    public String nextPage() {
        ++curPage;
        if (curPage > curBook.getPageList().size()) {
            --curPage;
        }
        return curBook.getPageList().get(curPage - 1);
    }

    /**
     * Note: page is base 1 index
     *
     * @return previous page
     */
    public String prevPage() {
        --curPage;
        if (curPage < 1) {
            ++curPage;
        }
        return curBook.getPageList().get(curPage - 1);
    }

    /**
     * @return current page number
     */
    public int getCurPage() {
        return curPage;
    }

    /**
     * @return the first page
     */
    public String startReading() {
        return curBook.getPageList().get(curPage - 1);
    }

    public boolean isFirstPage() {
        return (curPage == 1);
    }

    public boolean isLastPage() {
        return (curPage == curBook.getPageList().size());
    }
}


package hkhoi.readkidz;

/**
 * @author hkhoi
 */
public class BookThumbnail {
    private String imageData;
    private int bookId;
    private String title;
    private String description;
    private String speech;

    /**
     * Set up a book thumbnail
     *
     * @param imageData
     * @param bookId
     * @param title
     */
    public BookThumbnail(String imageData, int bookId, String title, String description, String speech) {
        this.imageData = imageData;
        this.bookId = bookId;
        this.title = title;
        this.description = description;
        this.speech = speech;
    }

    /**
     * @return image id
     */
    public String getImageData() {
        return imageData;
    }

    /**
     * @return .hk book id
     */
    public int getBookId() {
        return bookId;
    }

    /**
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return Book description
     */
    public String getDescription() {
        return description;
    }

    public String getSpeech() {
        return speech;
    }

    /**
     * set bookId statically
     *
     * @param bookId
     */
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
}


package hkhoi.readkidz;

/**
 * Created by hkhoi on 6/29/15.
 */
import java.io.Serializable;

/**
 *
 * @author hkhoi
 */
public class Quiz implements Serializable{

    private String content;
    private String[] options;
    private int answer;

    /**
     * Set up a quiz
     * @param content
     * @param options
     * @param answer
     */
    public Quiz(String content, String[] options, int answer) {
        this.content = content;
        this.options = options;
        this.answer = answer;
    }
    /**
     *
     * @return Question
     */
    public String getContent() {
        return content;
    }

    /**
     *
     * @return an array of String of options
     */
    public String[] getOptions() {
        return options;
    }

    /**
     *
     * @return answer
     */
    public int getAnswer() {
        return answer;
    }
}

package hkhoi.readkidz;

import java.util.ArrayList;

/**
 * Created by hkhoi on 6/30/15.
 */
public class QuizHandler {
    private ArrayList<Quiz> quizSet;
    private int curPage;

    public QuizHandler(ArrayList<Quiz> quizSet) {
        this.quizSet = quizSet;
        curPage = 1;
    }

    public String getCurContent() {
        return quizSet.get(curPage - 1).getContent();
    }

    public String[] getCurOptions() {
        return quizSet.get(curPage - 1).getOptions();
    }

    public int getAnswer() {
        return quizSet.get(curPage - 1).getAnswer();
    }

    public int nextQuiz() {
        if (curPage < quizSet.size()) {
            return ++curPage;
        } else {
            return quizSet.size();
        }
    }

    public boolean isCorrected(int option) {
        return (option == getAnswer());
    }

    public boolean isLastQuiz() {
        return (curPage == quizSet.size());
    }

    public int numberOfQuiz() {
        return quizSet.size();
    }
}

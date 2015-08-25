package hkhoi.readkidz;

/**
 * Created by hkhoi on 6/29/15.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @author hkhoi
 */
public class Book {

    static private final int QUIZ_NUMBER = 4;
    static private final String SEPERATE = "**SEPERATE**";
    private ArrayList<String> pageList;
    private ArrayList<Quiz> quizList;

    /**
     * Create books from .hk file
     *
     * @param is
     */
    public Book(InputStream is) {
//        try {
            BufferedReader in
                    = new BufferedReader(new InputStreamReader(is));
            pageList = new ArrayList<>();
            quizList = new ArrayList<>();
            try {
                /*
				 * Import HTML contents of books
				 */
                int pageNum = Integer.parseInt(in.readLine());
                for (int i = 0; i < pageNum; ++i) {
                    String curLine = in.readLine();
                    StringBuilder sb = new StringBuilder();
                    while (!curLine.equals(SEPERATE)) {
                        sb.append(curLine);
                        curLine = in.readLine();
                    }
                    pageList.add(sb.toString());
                }
				/*
				 * Import Quiz
				 */
                int quizNum = Integer.parseInt(in.readLine());
                for (int i = 0; i < quizNum; ++i) {
                    StringBuilder sb = new StringBuilder();

                    String curLine = in.readLine();
                    while (!curLine.equals(SEPERATE)) {
                        sb.append(curLine);
                        curLine = in.readLine();
                    }

                    String[] options = new String[QUIZ_NUMBER];
                    for (int j = 0; j < QUIZ_NUMBER; ++j) {
                        options[j] = in.readLine();
                    }
                    int answer = Integer.parseInt(in.readLine());
                    quizList.add(new Quiz(sb.toString(), options, answer));
                }
            } catch (IOException ex) {
//                Log.e("error", ex.toString());
                System.exit(-1);
            }
            try {
                in.close();
            } catch (IOException ex) {
//                Log.e("error", ex.toString());
                System.exit(-1);
            }
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(Book.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    /**
     * @return an ArrayList of HTML pages
     */
    public ArrayList<String> getPageList() {
        return pageList;
    }

    /**
     * @return an ArrayList of quizzes
     */
    public ArrayList<Quiz> getQuizList() {
        return quizList;
    }
}


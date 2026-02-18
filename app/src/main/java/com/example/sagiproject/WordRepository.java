package com.example.sagiproject;

import java.util.ArrayList;
import java.util.List;

public class    WordRepository {

    public ArrayList<WordPair> easyWords = new ArrayList<>();
    public ArrayList<WordPair> hardWords = new ArrayList<>();


    public WordRepository() {
        loadEasyWords();
        loadHardWords();
    }
    private void loadEasyWords() {
        easyWords.add(new WordPair("Dog", "כלב"));
        easyWords.add(new WordPair("Cat", "חתול"));
        easyWords.add(new WordPair("Sun", "שמש"));
        easyWords.add(new WordPair("Apple", "תפוח"));

    }

    private void loadHardWords() {
        hardWords.add(new WordPair("Environment", "סביבה"));
        hardWords.add(new WordPair("Experience", "חוויה"));
        hardWords.add(new WordPair("Knowledge", "ידע"));
        hardWords.add(new WordPair("Responsibility", "אחריות"));
    }

    public static String easyWordListToString(ArrayList<WordPair> easyPair) {
        if (easyPair == null || easyPair.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < easyPair.size(); i++) {
            WordPair w = easyPair.get(i);

            sb.append(w.getEnglish())
                    .append(" - ")
                    .append(w.getHebrew());

            if (i < easyPair.size() - 1) {
                sb.append(" | "); // מפריד בין פריטים
            }
        }

        return sb.toString();
    }
    public static String hardWordListToString(ArrayList<WordPair> hardPair) {
        if (hardPair == null || hardPair.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < hardPair.size(); i++) {
            WordPair w = hardPair.get(i);

            sb.append(w.getEnglish())
                    .append(" - ")
                    .append(w.getHebrew());

            if (i < hardPair.size() - 1) {
                sb.append(" | "); // מפריד בין פריטים
            }
        }

        return sb.toString();
    }


    /**
     * הוספת 4 צמדי מילים לרמה קלה
     */

    public void addEasyWords(
            String e1, String h1,
            String e2, String h2,
            String e3, String h3,
            String e4, String h4) {

        easyWords.add(new WordPair(e1, h1));
        easyWords.add(new WordPair(e2, h2));
        easyWords.add(new WordPair(e3, h3));
        easyWords.add(new WordPair(e4, h4));
    }
    public void updateWords(ArrayList<WordPair> arrayList) {
        printHand(arrayList);

    }
    void printHand(ArrayList<WordPair> arrayList)
    {
        for(int i=0;i<arrayList.size();i++)
        {
            System.out.println(arrayList.get(i).getEnglish()+arrayList.get(i).getHebrew());
        }
    }


    /**
     * הוספת 4 צמדי מילים לרמה קשה
     */
    public void addHardWords(
            String e1, String h1,
            String e2, String h2,
            String e3, String h3,
            String e4, String h4) {

        hardWords.add(new WordPair(e1, h1));
        hardWords.add(new WordPair(e2, h2));
        hardWords.add(new WordPair(e3, h3));
        hardWords.add(new WordPair(e4, h4));
    }


    public List<WordPair> getWordsByLevel(int level) {


        // אם לא הצליח – נ fallback למילים המקומיות
        if (level == Level.EASY) {
            return easyWords;
        } else {
            return hardWords;
        }
    }


    public ArrayList<WordPair> getEasyWords(){
        return easyWords;
    }
    public ArrayList<WordPair> getHardWords(){
        return hardWords;
    }

}


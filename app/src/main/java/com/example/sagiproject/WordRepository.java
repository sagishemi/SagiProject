package com.example.sagiproject;

import java.util.ArrayList;
import java.util.List;

public class WordRepository {

    private List<WordPair> easyWords = new ArrayList<>();
    private List<WordPair> hardWords = new ArrayList<>();

    private GeminiWordsService geminiService = new GeminiWordsService();

    public WordRepository() {
        loadEasyWords();
        loadHardWords();
    }

    public List<WordPair> getWordsByLevel(int level) {
        // קודם ננסה להביא מגמיני
        List<WordPair> fromGemini = geminiService.getWordsFromGemini(level);
        if (fromGemini != null && !fromGemini.isEmpty()) {
            return fromGemini;
        }

        // אם לא הצליח – נ fallback למילים המקומיות
        if (level == Level.EASY) {
            return easyWords;
        } else {
            return hardWords;
        }
    }

    private void loadEasyWords() {
        easyWords.add(new WordPair("Dog", "כלב"));
        easyWords.add(new WordPair("Cat", "חתול"));
        easyWords.add(new WordPair("Sun", "שמש"));
        easyWords.add(new WordPair("Apple", "תפוח"));
        easyWords.add(new WordPair("Car", "מכונית"));
        easyWords.add(new WordPair("House", "בית"));
        easyWords.add(new WordPair("Book", "ספר"));
        easyWords.add(new WordPair("School", "בית ספר"));
        easyWords.add(new WordPair("Water", "מים"));
        easyWords.add(new WordPair("Food", "אוכל"));
    }

    private void loadHardWords() {
        hardWords.add(new WordPair("Environment", "סביבה"));
        hardWords.add(new WordPair("Experience", "חוויה"));
        hardWords.add(new WordPair("Knowledge", "ידע"));
        hardWords.add(new WordPair("Responsibility", "אחריות"));
        hardWords.add(new WordPair("Opportunity", "הזדמנות"));
        hardWords.add(new WordPair("Challenge", "אתגר"));
        hardWords.add(new WordPair("Communication", "תקשורת"));
        hardWords.add(new WordPair("Competition", "תחרות"));
        hardWords.add(new WordPair("Improvement", "שיפור"));
        hardWords.add(new WordPair("Independent", "עצמאי"));
    }
}


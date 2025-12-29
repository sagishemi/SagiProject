package com.example.sagiproject;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;

/**
 * שירות שמדבר עם Gemini ומחזיר רשימת WordPair לפי רמה.
 * שים לב: זה קוד דוגמה לפרויקט, לא קוד מאובטח לפרודקשן.
 */
public class GeminiWordsService {

    // TODO: להחליף למפתח האמיתי שלך מגוגל (Gemini / AI Studio)
    private static final String API_KEY = "AIzaSyD56D4i981i9zQtMSppjjrHhsCznb9PDBc";

    // TODO: לעדכן ל-URL העדכני של Gemini (תלוי במודל שתבחר)
    // למשל: "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent"
    private static final String GEMINI_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + API_KEY;

    public List<WordPair> getWordsFromGemini(int level) {
        List<WordPair> result = new ArrayList<>();

        try {
            String prompt = buildPrompt(level);
            String response = callGemini(prompt);

            if (response == null) {
                return result;
            }

            // כאן מניחים ש-Gemini מחזיר JSON בטקסט – נפרש אותו
            // אנחנו מבקשים ממנו להחזיר בפורמט:
            // [
            //   {"english":"Dog","hebrew":"כלב"},
            //   {"english":"Cat","hebrew":"חתול"},
            //   ...
            // ]
            JSONArray arr = new JSONArray(response);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                String english = obj.getString("english");
                String hebrew = obj.getString("hebrew");
                result.add(new WordPair(english, hebrew));
            }
        } catch (Exception e) {
            Log.e("GeminiWordsService", "Error getting words from Gemini", e);
        }

        return result;
    }

    private String buildPrompt(int level) {
        String levelText = (level == Level.EASY) ? "easy/basic" : "hard/advanced";

        return "Generate 10 English-Hebrew word pairs for an educational matching game. " +
                "The words should be in " + levelText + " level of difficulty. " +
                "Return ONLY a JSON array, with no explanation, in this format:\n" +
                "[\n" +
                "  {\"english\":\"Dog\", \"hebrew\":\"כלב\"},\n" +
                "  {\"english\":\"Cat\", \"hebrew\":\"חתול\"}\n" +
                "]";
    }

    private String callGemini(String prompt) {
        BufferedReader reader = null;
        try {
            URL url = new URL(GEMINI_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);

            // גוף הבקשה – לפי פורמט Gemini (פשוט יחסית):
            // {
            //   "contents": [
            //     {
            //       "parts": [{"text": "your prompt here"}]
            //     }
            //   ]
            // }
            JSONObject body = new JSONObject();
            JSONArray contents = new JSONArray();
            JSONObject content = new JSONObject();
            JSONArray parts = new JSONArray();
            JSONObject part = new JSONObject();

            part.put("text", prompt);
            parts.put(part);
            content.put("parts", parts);
            contents.put(content);
            body.put("contents", contents);

            String jsonBody = body.toString();

            OutputStream os = conn.getOutputStream();
            os.write(jsonBody.getBytes("UTF-8"));
            os.close();

            int code = conn.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) {
                Log.e("GeminiWordsService", "Bad response code: " + code);
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            String rawResponse = sb.toString();
            Log.d("GeminiWordsService", "Gemini raw response: " + rawResponse);

            // בפועל, Gemini מחזיר אובייקט מורכב. אנחנו מניחים שהטקסט נמצא ב:
            // candidates[0].content.parts[0].text
            JSONObject root = new JSONObject(rawResponse);
            JSONArray candidates = root.getJSONArray("candidates");
            if (candidates.length() == 0) return null;

            JSONObject firstCand = candidates.getJSONObject(0);
            JSONObject contentObj = firstCand.getJSONObject("content");
            JSONArray partsArr = contentObj.getJSONArray("parts");
            if (partsArr.length() == 0) return null;

            String text = partsArr.getJSONObject(0).getString("text");
            // text אמור להיות מחרוזת JSON כמו שתיארנו – נחזיר אותה למעלה לפונקציה השנייה
            return text;

        } catch (Exception e) {
            Log.e("GeminiWordsService", "Error calling Gemini", e);
            return null;
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (Exception ignore) {}
        }
    }
}

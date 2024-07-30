package com.hirapure.autotranslateviews;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatEditText;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.IOException;

public class TranslatableEditText extends AppCompatEditText {
    private OkHttpClient client;

    public TranslatableEditText(Context context) {
        super(context);
        init();
    }

    public TranslatableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TranslatableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        client = new OkHttpClient();
        LanguageManager.getInstance().registerEditText(this);
        updateHintTranslation();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        LanguageManager.getInstance().unregisterEditText(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        updateHintTranslation();
    }

    public void updateHintTranslation() {
        String hint = getHint().toString();
        if (hint != null && !hint.isEmpty()) {
            translateText(hint);
        }
    }

    private void translateText(String text) {
        new TranslateTask().execute(text);
    }

    private class TranslateTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String text = strings[0];
            String url = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=auto&tl=" + LanguageManager.getInstance().getTargetLanguage() + "&dt=t&q=" + text;

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JSONArray jsonArray = new JSONArray(responseBody);
                    return jsonArray.getJSONArray(0).getJSONArray(0).getString(0);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String translatedText) {
            if (translatedText != null) {
                setHint(translatedText);
            }
        }
    }
}

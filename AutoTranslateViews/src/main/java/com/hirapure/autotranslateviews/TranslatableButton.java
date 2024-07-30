package com.hirapure.autotranslateviews;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TranslatableButton extends AppCompatButton {
    private OkHttpClient client;

    public TranslatableButton(Context context) {
        super(context);
        init();
    }

    public TranslatableButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TranslatableButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        client = new OkHttpClient();
        LanguageManager.getInstance().registerButton(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        LanguageManager.getInstance().unregisterButton(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        updateTranslation();
    }

    public void updateTranslation() {
        String text = getText().toString();
        translateText(text);
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
                setText(translatedText);
            }
        }
    }
}

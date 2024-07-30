package com.hirapure.autotranslateviews;

import java.util.HashSet;
import java.util.Set;

public class LanguageManager {
    private static LanguageManager instance;
    private String targetLanguage;
    private Set<TranslatableTextView> registeredTextViews;
    private Set<TranslatableButton> registeredButtons;
    private Set<TranslatableEditText> registeredEditTexts;

    private LanguageManager() {
        targetLanguage = "hi"; // Default target language
        registeredTextViews = new HashSet<>();
        registeredButtons = new HashSet<>();
        registeredEditTexts = new HashSet<>();
    }

    public static synchronized LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager();
        }
        return instance;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }

    public void setTargetLanguage(String targetLanguage) {
        this.targetLanguage = targetLanguage;
        notifyAllViews();
    }

    public void registerTextView(TranslatableTextView textView) {
        registeredTextViews.add(textView);
    }

    public void unregisterTextView(TranslatableTextView textView) {
        registeredTextViews.remove(textView);
    }

    public void registerButton(TranslatableButton button) {
        registeredButtons.add(button);
    }

    public void unregisterButton(TranslatableButton button) {
        registeredButtons.remove(button);
    }

    public void registerEditText(TranslatableEditText editText) {
        registeredEditTexts.add(editText);
    }

    public void unregisterEditText(TranslatableEditText editText) {
        registeredEditTexts.remove(editText);
    }

    private void notifyAllViews() {
        for (TranslatableTextView textView : registeredTextViews) {
            textView.updateTranslation();
        }
        for (TranslatableButton button : registeredButtons) {
            button.updateTranslation();
        }
        for (TranslatableEditText editText : registeredEditTexts) {
            editText.updateHintTranslation();
        }
    }
}

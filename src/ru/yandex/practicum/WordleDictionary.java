package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.List;

public class WordleDictionary {

    private List<String> words;
    public static final int WORD_LENGTH = 5;

    public WordleDictionary(List<String> words) {
        this.words = words;
    }

    public void sortWordsList() {
        List<String> newWords = new ArrayList<>();
        for (String word : this.words) {
            if (word.length() == WORD_LENGTH) {
                newWords.add(word);
            }
        }
        this.words = newWords;
    }

    public void setToLowerCase() {
        for (int i = 0; i < this.words.size(); i++) {
            this.words.set(i, this.words.get(i).toLowerCase());
        }
    }

    public void letterFilter() {
        for (int i = 0; i < this.words.size(); i++) {
            String word = this.words.get(i);
            if (word.contains("ё")) {
                word = word.replace("ё", "е");
                this.words.set(i, word);
            }

        }
    }

    public List<String> getWords() {
        return words;
    }
}

package ru.yandex.practicum;

import exceptions.DictionaryIsEmptyException;
import exceptions.NoHintAvailableException;
import exceptions.WordNotFoundInDictionaryException;

import java.util.*;

public class WordleGame {

    private String answer;
    private int steps;
    private WordleDictionary dictionary;
    private List<String> attempts;
    private List<String> hintResults;
    private static final int MAX_STEPS = 6;
    private Logger logger;
    private boolean hintGiven = false;

    public WordleGame(WordleDictionary dictionary, Logger logger) {
        this.dictionary = dictionary;
        this.steps = 0;
        this.attempts = new ArrayList<>();
        this.hintResults = new ArrayList<>();
        this.logger = logger;

        try {
            this.answer = getRandomWord();
        } catch (DictionaryIsEmptyException e) {
            logger.log(e.getMessage());
        }
    }

    public String getRandomWord() {
        Random random;
        int index;
        try {
            if (dictionary.getWords().isEmpty()) {
                throw new DictionaryIsEmptyException("Словарь пуст");
            }
            random = new Random();
            index = random.nextInt(this.dictionary.getWords().size());

        } catch (RuntimeException e) {
            this.logger.log("Слово из списка не было выбрано: " + e.getMessage());
            return null;
        }
        return this.dictionary.getWords().get(index);
    }

    public String checkWord(String userInput) throws WordNotFoundInDictionaryException {
        if (!this.dictionary.getWords().contains(userInput)) {
            throw new WordNotFoundInDictionaryException(userInput);
        }

        this.attempts.add(userInput);
        this.steps++;

        StringBuilder result = new StringBuilder("-----");

        Map<Character, Integer> answerCharCount = new HashMap<>();
        for(char c : this.answer.toCharArray()) {
            answerCharCount.put(c, answerCharCount.getOrDefault(c, 0) + 1);
        }

        for (int i = 0; i < this.answer.length(); i++) {
            char secretChar = this.answer.charAt(i);
            char userChar = userInput.charAt(i);

            if (userChar == secretChar) {
                result.setCharAt(i, '+');
                answerCharCount.put(secretChar, answerCharCount.get(secretChar) - 1);
            }
        }

        for (int i = 0; i < this.answer.length(); i++) {
            char secretChar = this.answer.charAt(i);
            char userChar = userInput.charAt(i);

            if (userChar != secretChar && answerCharCount.getOrDefault(userChar, 0) > 0) {
                result.setCharAt(i, '^');
                answerCharCount.put(userChar, answerCharCount.get(userChar) - 1);
            }
        }

        this.hintResults.add(result.toString());

        return result.toString();
    }

    public String getHint() {
        System.out.println("hintGiven перед проверкой: " + hintGiven);
        if (hintGiven) {
            return "Подсказка уже выдавалась";
        }

        List<String> allWords = dictionary.getWords();
        Random random = new Random();
        List<String> possibleWords = new ArrayList<>();

        if (attempts.isEmpty()) {
            hintGiven = true;
            return allWords.get(random.nextInt(allWords.size()));
        }

        for (String word : allWords) {
            boolean fitsAllRules = true;
            for (int i = 0; i < this.attempts.size(); i++) {
                String attempt = this.attempts.get(i);
                String result = this.hintResults.get(i);

                if (!matchesAttempt(word, attempt, result)) {
                    fitsAllRules = false;
                    break;
                }
            }
            if (fitsAllRules) {
                possibleWords.add(word);
            }
        }

        if (possibleWords.isEmpty()) {
            throw new NoHintAvailableException("Не удалось подобрать подсказку — проверьте введённые слова.");
        }
        String hint = possibleWords.get(random.nextInt(possibleWords.size()));
        hintGiven = true;
        System.out.println("Установлен hintGiven = true, возвращаем: " + hint);
        return hint;
    }

    private boolean matchesAttempt(String word, String attempt, String result) {
        Map<Character, Integer> wordCharCount = new HashMap<>();
        for (char c : word.toCharArray()) {
            wordCharCount.put(c, wordCharCount.getOrDefault(c, 0) + 1);
        }

        for (int i = 0; i < attempt.length(); i++) {
            char c = attempt.charAt(i);
            if (result.charAt(i) == '+') {
                if (word.charAt(i) != c) {
                    return false;
                }
                wordCharCount.put(c, wordCharCount.get(c) - 1);
            }
        }

        for (int i = 0; i < attempt.length(); i++) {
            char c = attempt.charAt(i);
            if (result.charAt(i) == '^') {
                if(word.charAt(i) == c) {
                    return false;
                }
                if (wordCharCount.getOrDefault(c, 0) <= 0) {
                    return false;
                }
                wordCharCount.put(c, wordCharCount.get(c) - 1);
            } else if (result.charAt(i) == '-') {
                if (wordCharCount.containsKey(c)) {
                    wordCharCount.put(c, wordCharCount.get(c) - 1);
                    if (wordCharCount.get(c) < 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public String getAnswer() {
        return answer;
    }

    public int getStepsLeft() {
        return MAX_STEPS - steps;
    }

    public boolean isHintGiven() {
        return hintGiven;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}

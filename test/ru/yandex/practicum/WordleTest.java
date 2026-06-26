package ru.yandex.practicum;

import exceptions.WordNotFoundInDictionaryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {
    private static List<String> list;
    private static WordleDictionary dictionary;
    private WordleGame game;
    private Logger logger;

    @BeforeEach
    void setUpDictionary() {
        list = new ArrayList<>();
        list.add("трамвайщик");
        list.add("кошка");
        list.add("Абзац");
        list.add("нерпа");
        list.add("Трамп");
        list.add("ящерица");
        list.add("ёжик");
        dictionary = new WordleDictionary(list);
    }

    @BeforeEach
    void setUpGame() throws IOException {
        this.logger = new Logger("test-log.txt");
        this.game = new WordleGame(dictionary, logger);
    }

    @Test
    void testSortWordsList() {
        dictionary.sortWordsList();
        assertEquals(4, dictionary.getWords().size());
        assertTrue(dictionary.getWords().contains("кошка"));
        assertFalse(dictionary.getWords().contains("трамвайщик"));
    }

    @Test
    void testSetToLowerCase() {
        dictionary.setToLowerCase();
        assertTrue(dictionary.getWords().contains("абзац"));
        assertFalse(dictionary.getWords().contains("Трамп"));
    }

    @Test
    void testLetterFilter() {
        dictionary.letterFilter();
        assertFalse(dictionary.getWords().contains("ёжик"));
    }

    @Test
    void testCheckCorrectWord() {
        try {
            game.setAnswer("кошка");
            String result = game.checkWord("кошка");
            assertEquals("+++++", result);
        } catch (WordNotFoundInDictionaryException e) {
            fail("Не должно было возникнуть исключение: " + e.getMessage());
        }
    }

    @Test
    void testCheckIncorrectWord() {
        try {
            game.setAnswer("канал");
            String result = game.checkWord("кошка");
            assertEquals("+---^", result);

            result = game.checkWord("нерпа");
            assertEquals("^---^", result);
        } catch (WordNotFoundInDictionaryException e) {
            fail("Не должно было возникнуть исключение: " + e.getMessage());
        }
    }

    @Test
    void testGetFirstHint() {
        try {
            game.checkWord("кошка");
            String hint = game.getHint();
            assertNotNull(hint);
            assertEquals(5, hint.length());
        } catch (WordNotFoundInDictionaryException e) {
            fail("Не должно было возникнуть исключение: " + e.getMessage());
        }
    }

    @Test
    void testEmptyDictionary() {
        WordleDictionary emptyDictionary = new WordleDictionary(new ArrayList<>());
        WordleGame game = new WordleGame(emptyDictionary, logger);

        assertNull(game.getAnswer());
    }

    @Test
    void testDuplicateLetters1() throws Exception {
        dictionary.getWords().add("банан");
        dictionary.getWords().add("канал");

        game.setAnswer("банан");

        String result = game.checkWord("канал");

        assertEquals("-+++-", result);
    }

    @Test
    void testDuplicateLetters2() throws WordNotFoundInDictionaryException {
        dictionary.getWords().add("масса");
        dictionary.getWords().add("самса");

        game.setAnswer("масса");

        String result = game.checkWord("самса");

        assertEquals("^+^++", result);
    }

    @Test
    void testOnlyOneDuplicateLetterMarked() throws Exception {
        dictionary.getWords().add("лимон");
        dictionary.getWords().add("ооооо");

        game.setAnswer("лимон");

        String result = game.checkWord("ооооо");

        assertEquals("---+-", result);
    }

    @Test
    void testStepsDecrease() {
        try {
            game.setAnswer("кошка");
            game.checkWord("кошка");

            assertEquals(5, game.getStepsLeft());
        } catch (WordNotFoundInDictionaryException e) {
            fail("Ожидалось, что попыток останется 5");
        }
    }

    @Test
    void testHintWithoutAttempts() {
        String hint = game.getHint();

        assertNotNull(hint);
        assertTrue(dictionary.getWords().contains(hint));
    }

    @Test
    void testSecondHint() {
        String first = game.getHint();
        System.out.println("Первый вызов: " + first);
        String second = game.getHint();
        System.out.println("Второй вызов: " + second);
        assertEquals("Подсказка уже выдавалась", second);
    }

    @Test
    public void testHintGiven() {
        String firstHint = game.getHint();
        assertNotNull(firstHint);
        assertNotEquals("Подсказка уже выдавалась", firstHint);

        String secondHint = game.getHint();
        assertEquals("Подсказка уже выдавалась", secondHint);
    }

    @Test
    void testWordNotFoundException() {
        try {
            game.setAnswer("кошка");
            game.checkWord("домик");

            fail("Ожидалось исключение");
        } catch (WordNotFoundInDictionaryException e) {
            assertNotNull(e.getMessage());
        }
    }
}

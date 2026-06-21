package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeAll;
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

    @BeforeAll
    static void setUpDictionary() {
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
        game.setAnswer("кошка");
        String result = game.checkWord("кошка");
        assertEquals("+++++", result);
    }

    @Test
    void testCheckIncorrectWord() {
        game.setAnswer("канал");
        String result = game.checkWord("кошка");
        assertEquals("+---^", result);

        result = game.checkWord("нерпа");
        assertEquals("^---^", result);
    }

    @Test
    void testGetHint() {
        game.checkWord("кошка");
        String hint = game.getHint();
        assertNotNull(hint);
        assertEquals(5, hint.length());
    }
}

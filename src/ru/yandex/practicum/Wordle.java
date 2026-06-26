package ru.yandex.practicum;

import exceptions.NoHintAvailableException;
import exceptions.WordNotFoundInDictionaryException;

import java.util.List;
import java.util.Scanner;


public class Wordle {


    public static void main(String[] args) {
        Logger logger = null;

        try {
            logger = new Logger("log-file.txt");
            WordleDictionaryLoader loader = new WordleDictionaryLoader(logger);
            loader.loadDictionary();
            List<String> wordsList = loader.getList();
            WordleDictionary dictionary = new WordleDictionary(wordsList);

            dictionary.sortWordsList();
            dictionary.setToLowerCase();
            dictionary.letterFilter();

            logger.log("Словарь загружен");

            WordleGame wordleGame = new WordleGame(dictionary, logger);
            logger.log("Игра запущена");

            while(wordleGame.getStepsLeft() > 0) {
                printHint(wordleGame, logger);
            }

        } catch (Exception e) {
            if (logger != null) {
                logger.log("Ошибка: " + e.getMessage());
            } else {
                System.err.println("Не удалось создать логгер: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void printHint(WordleGame wordleGame, Logger logger) {
        Scanner scanner = new Scanner(System.in);
        String userInput;
        String computerInput;
        String hint;

        System.out.println("Введите слово:");
        userInput = scanner.nextLine();
        if (userInput.length() > WordleDictionary.WORD_LENGTH) {
            logger.log("Введено слово несоответствующее по размерам");
            System.out.println("Слово должно быть из пяти букв. Повторите ещё раз");
            return;
        }

        if (!userInput.isEmpty()) {
            try {
                if (userInput.equals(wordleGame.getAnswer())) {
                    System.out.println("Победа. Вы угадали слово: " + userInput);
                    scanner.close();
                    logger.log("Пользователь отгадал слово");
                    System.exit(0);
                }
                hint = wordleGame.checkWord(userInput);
                System.out.println(hint);
            } catch (WordNotFoundInDictionaryException e) {
                logger.log(e.getMessage());
                System.out.println("Пожалуйста, введите слово из словаря");
            }
        } else {
            try {
                computerInput = wordleGame.getHint();
                hint = wordleGame.checkWord(computerInput);
                if (computerInput.equals(wordleGame.getAnswer())) {
                    System.out.println("Победа. Вы угадали слово: " + computerInput);
                    scanner.close();
                    logger.log("Компьютер отгадал слово");
                    System.exit(0);
                }
                System.out.println(computerInput + "\n" + hint);
            } catch (NoHintAvailableException e) {
                logger.log("Подсказка недоступна " + e.getMessage());
            } catch (WordNotFoundInDictionaryException e) {
                logger.log(e.getMessage());
            }
        }


        System.out.println("Осталось попыток " + (wordleGame.getStepsLeft()));
        if (wordleGame.getStepsLeft() == 0) {
            System.out.println("Загаданное слово: " + wordleGame.getAnswer());
            logger.log("Слово не было отгадано");
        }
    }

}

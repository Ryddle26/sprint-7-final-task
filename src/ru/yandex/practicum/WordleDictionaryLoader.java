package ru.yandex.practicum;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

    public class WordleDictionaryLoader {

        List<String> list = new ArrayList<>();

        public void loadDictionary() {
            try (BufferedReader br = new BufferedReader(new FileReader("words_ru.txt", StandardCharsets.UTF_8))) {
                while (br.ready()) {
                    String line = br.readLine();
                    list.add(line);
                }
            } catch (IOException e) {
                System.err.println("Ошибка при загрузке словаря" + e.getMessage());
            }
        }

        public List<String> getList() {
            return list;
        }
    }

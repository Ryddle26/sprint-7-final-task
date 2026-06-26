package ru.yandex.practicum;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger implements AutoCloseable {
    private final PrintWriter writer;

    public Logger(String fileName) throws IOException {
        this.writer = new PrintWriter(new FileWriter(fileName, true));
    }

    public void log(String message) {
        writer.println(message);
    }

    @Override
    public void close() {
        writer.close();
    }
}

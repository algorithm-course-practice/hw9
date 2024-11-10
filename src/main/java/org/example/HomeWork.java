package org.example;


import lombok.SneakyThrows;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HomeWork {

    /**
     * <h1>Задание 1.</h1>
     * Решить задачу UPIT из файла contest7_tasks.pdf
     */
    @SneakyThrows
    public void processCommands(InputStream inputStream, OutputStream outputStream) {
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        PrintStream printStream = new PrintStream(outputStream);

        int sequenceLength = nextInt(dataInputStream);
        int queryCount = nextInt(dataInputStream);

        List<Long> sequence = new ArrayList<>();
        for (int i = 0; i < sequenceLength; i++) {
            sequence.add(nextLong(dataInputStream));
        }

        for (int i = 0; i < queryCount; i++) {
            int queryType = nextInt(dataInputStream);
            switch (queryType) {
                case 1 -> handleUpdateQuery(sequence, dataInputStream);
                case 2 -> handleAddQuery(sequence, dataInputStream);
                case 3 -> handleInsertQuery(sequence, dataInputStream);
                case 4 -> handleSumQuery(sequence, dataInputStream, printStream);
                default -> throw new IllegalArgumentException("Unknown query type: " + queryType);
            }
        }
    }

    private void handleUpdateQuery(List<Long> sequence, DataInputStream dataInputStream) throws IOException {
        int[] queryParams = readRangeAndValue(dataInputStream);
        int startIndex = queryParams[0];
        int endIndex = queryParams[1];
        long value = queryParams[2];
        for (int i = startIndex; i <= endIndex; i++) {
            sequence.set(i, value);
        }
    }

    private void handleAddQuery(List<Long> sequence, DataInputStream dataInputStream) throws IOException {
        int[] queryParams = readRangeAndValue(dataInputStream);
        int startIndex = queryParams[0];
        int endIndex = queryParams[1];
        long increment = queryParams[2];
        for (int i = startIndex; i <= endIndex; i++) {
            sequence.set(i, sequence.get(i) + (i - startIndex + 1) * increment);
        }
    }

    private void handleInsertQuery(List<Long> sequence, DataInputStream dataInputStream) throws IOException {
        int insertIndex = nextInt(dataInputStream) - 1;
        long value = nextLong(dataInputStream);
        sequence.add(insertIndex, value);
    }

    private void handleSumQuery(List<Long> sequence, DataInputStream dataInputStream, PrintStream printStream) throws IOException {
        int startIndex = nextInt(dataInputStream) - 1;
        int endIndex = nextInt(dataInputStream) - 1;
        long sum = 0;
        for (int i = startIndex; i <= endIndex; i++) {
            sum += sequence.get(i);
        }
        printStream.println(sum);
    }

    private int nextInt(DataInputStream dataInputStream) throws IOException {
        return Integer.parseInt(nextToken(dataInputStream));
    }

    private long nextLong(DataInputStream dataInputStream) throws IOException {
        return Long.parseLong(nextToken(dataInputStream));
    }

    private String nextToken(DataInputStream dataInputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int b;
        while ((b = dataInputStream.read()) != -1 && b != ' ' && b != '\n') {
            stringBuilder.append((char) b);
        }
        return stringBuilder.toString().trim();
    }

    private int[] readRangeAndValue(DataInputStream dataInputStream) throws IOException {
        int startIndex = nextInt(dataInputStream) - 1;
        int endIndex = nextInt(dataInputStream) - 1;
        long value = nextLong(dataInputStream);
        return new int[]{startIndex, endIndex, (int) value};
    }
}

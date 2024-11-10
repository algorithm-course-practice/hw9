package org.example;


import lombok.SneakyThrows;

import java.io.*;

public class HomeWork {

    /**
     * <h1>Задание 1.</h1>
     * Решить задачу UPIT из файла contest7_tasks.pdf
     */
    @SneakyThrows
    public void upit(InputStream in, OutputStream out) {
        DataInputStream dataInputStream = new DataInputStream(in);
        PrintStream printStream = new PrintStream(out);

        int N = readInt(dataInputStream);
        int Q = readInt(dataInputStream);

        long[] sequence = new long[N];
        for (int i = 0; i < N; i++) {
            sequence[i] = readLong(dataInputStream);
        }

        for (int i = 0; i < Q; i++) {
            int queryType = readInt(dataInputStream);
            if (queryType == 1) {
                updateSequence(sequence, dataInputStream);
            } else if (queryType == 2) {
                addToSequence(sequence, dataInputStream);
            } else if (queryType == 3) {
                sequence = insertElement(sequence, dataInputStream);
            } else if (queryType == 4) {
                long sum = calculateSum(sequence, dataInputStream);
                printStream.println(sum);
            }
        }

        printStream.close();
    }

    private void updateSequence(long[] sequence, DataInputStream in) throws IOException {
        int A = readInt(in) - 1;
        int B = readInt(in) - 1;
        long X = readLong(in);
        for (int j = A; j <= B; j++) {
            sequence[j] = X;
        }
    }

    private void addToSequence(long[] sequence, DataInputStream in) throws IOException {
        int A = readInt(in) - 1;
        int B = readInt(in) - 1;
        long X = readLong(in);
        for (int j = A; j <= B; j++) {
            sequence[j] += (j - A + 1) * X;
        }
    }

    private long[] insertElement(long[] sequence, DataInputStream in) throws IOException {
        int C = readInt(in) - 1;
        long X = readLong(in);
        long[] newSequence = new long[sequence.length + 1];
        if (C >= 0) System.arraycopy(sequence, 0, newSequence, 0, C);
        newSequence[C] = X;
        if (sequence.length - C >= 0) System.arraycopy(sequence, C, newSequence, C + 1, sequence.length - C);
        return newSequence;
    }

    private long calculateSum(long[] sequence, DataInputStream in) throws IOException {
        int A = readInt(in) - 1;
        int B = readInt(in) - 1;
        long sum = 0;
        for (int j = A; j <= B; j++) {
            sum += sequence[j];
        }
        return sum;
    }

    private int readInt(DataInputStream in) throws IOException {
        String input = readString(in).trim();
        if (input.isEmpty()) {
            throw new NumberFormatException("Input string is empty");
        }
        return Integer.parseInt(input);
    }

    private long readLong(DataInputStream in) throws IOException {
        String input = readString(in).trim();
        if (input.isEmpty()) {
            throw new NumberFormatException("Input string is empty");
        }
        return Long.parseLong(input);
    }

    private String readString(DataInputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        int b;
        while ((b = in.read()) != -1 && b != ' ' && b != '\n') {
            sb.append((char) b);
        }
        return sb.toString();
    }

}

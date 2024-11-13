package org.example;


import lombok.SneakyThrows;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HomeWork {

    /**
     * <h1>Задание 1.</h1>
     * Решить задачу UPIT из файла contest7_tasks.pdf
     */
    @SneakyThrows
    public void upit(InputStream in, OutputStream out) {
        String input = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        List<String> inList = Arrays.stream(input.split("\n"))
                .collect(Collectors.toList());
        String firstLine  = inList.get(0);
        List<Integer> firstLineListInt = Arrays.stream(firstLine.split(" "))
                .map(e -> Integer.parseInt(e))
                .collect(Collectors.toList());
        int n = firstLineListInt.get(0);
        if (n < 1 || n > 100000){
            new IllegalArgumentException("Не корректный N");
        }
        int q = firstLineListInt.get(1);
        if (q < 1 || q > 100000){
            new IllegalArgumentException("Не корректный Q");
        }
        List<Long> secondLine = Arrays.stream(inList.get(1).split(" "))
                .map(e -> Long.parseLong(e))
                .collect(Collectors.toList());
        Treap treap = new Treap();
        // строим дерамиду с неявным индексом
        for (Long number : secondLine){
            treap.add(number);
        }
        StringBuilder stringBuilder = new StringBuilder();
        PrintStream printStream = new PrintStream(out);
        // обрабатываем команды
        for (int i = 2; i < q + 2; i++) {
            List<Integer> comandLine = Arrays.stream(inList.get(i).split(" "))
                    .map(e -> Integer.parseInt(e))
                    .collect(Collectors.toList());
            switch (comandLine.get(0)) {
                case (1):
                    treap.fillValueCase1(comandLine.get(1), comandLine.get(2), comandLine.get(3));
                    break;
                case (2):
                    treap.fillValueCase2(comandLine.get(1), comandLine.get(2), comandLine.get(3));
                    break;
                case (3):
                    treap.add(comandLine.get(1), Long.valueOf(comandLine.get(2)));
                    break;
                case (4):
                    Treap.Statistic stats = treap.getStats(comandLine.get(1), comandLine.get(2));

                    if (stringBuilder.length() != 0) {
                        stringBuilder.append("\n" + stats.sumValue);
                    } else {
                        stringBuilder.append(stats.sumValue + "");
                    }
                    break;
                default:
                    new IllegalArgumentException("Необычная команда");
            }
        }
        byte[] bytes = stringBuilder.toString().getBytes(StandardCharsets.UTF_8);
        out.write(bytes);
    }
}

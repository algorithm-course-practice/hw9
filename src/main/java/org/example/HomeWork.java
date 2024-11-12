package org.example;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class HomeWork {

    /**
     * <h1>Задание 1.</h1>
     * Решить задачу UPIT из файла contest7_tasks.pdf
     */
    @SneakyThrows
    public void upit(InputStream in, OutputStream out) {
        Treap treap = new Treap();
        List<String> result = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        List<String> input = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
        for (String value : input.get(1).split(" ")) {
            treap.add(Integer.parseInt(value));
        }
//        System.out.println("Start treap: " + treap);
        input.stream().skip(2).forEach(str -> {
            calculate(str, treap, result);
//            System.out.println(treap.inorder());
        });

//        System.out.println("Result treap: " + treap.inorder());
        result.forEach(val -> stringBuilder.append(val).append(System.lineSeparator()));
        out.write(stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
    }

    private void calculate(String str, Treap treap, List<String> result) {
        String[] args = str.split(" ");
        switch (args[0]) {
            case "1":
                int val = Integer.parseInt(args[3]);
                for (int i = Integer.parseInt(args[1]) - 1; i < Integer.parseInt(args[2]); i++){
                    treap.updateKth(i, val);
                }
                break;
            case "2":
                int x = Integer.parseInt(args[3]);
                int multiplier = 1;
                for (int i = Integer.parseInt(args[1]) - 1; i < Integer.parseInt(args[2]); i++){
                    Integer kth = treap.findKth(i);
                    treap.updateKth(i, x * multiplier++ + kth);
                }
                break;
            case "3":
                treap.add(Integer.parseInt(args[1]) - 1, Integer.parseInt(args[2]));
                break;
            case "4":
                long res = 0;
                for (int i = Integer.parseInt(args[1]) - 1; i < Integer.parseInt(args[2]); i++){
                    res += treap.findKth(i);
                }
                result.add(String.valueOf(res));
        }
    }


}

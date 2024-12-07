package org.example;


import lombok.SneakyThrows;

import java.io.*;
import java.util.StringTokenizer;

public class HomeWork {

    /**
     * <h1>Задание 1.</h1>
     * Решить задачу UPIT из файла contest7_tasks.pdf
     */
    @SneakyThrows
    public void upit(InputStream in, OutputStream out) {

        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));

        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int q = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine());
        // строим дерамиду с неявным индексом
        Treap treap = new Treap();
        for (int i = 1; i <= n; i++) {
            Integer value = Integer.parseInt(st.nextToken());
            treap.add(value);
        }
        // обрабатываем команды
        while (q-- > 0) {

            st = new StringTokenizer(br.readLine());
            int type = Integer.parseInt(st.nextToken());

            if (type == 1) { // Установить все элементы от A до B в значение X
                int a = Integer.parseInt(st.nextToken()) - 1;
                int b = Integer.parseInt(st.nextToken());
                int x = Integer.parseInt(st.nextToken());
                treap.case1(a, b, x);
            } else if (type == 2) { // Добавить арифметическую прогрессию от A до B
                int a = Integer.parseInt(st.nextToken()) - 1;
                int b = Integer.parseInt(st.nextToken());
                int x = Integer.parseInt(st.nextToken());
                treap.case2(a, b, x);
            } else if (type == 3) { // Вставить новый элемент перед C
                int c = Integer.parseInt(st.nextToken()) - 1;
                int x = Integer.parseInt(st.nextToken());
                treap.add(c, x);
            } else if (type == 4) { // Вычислить сумму элементов от A до B
                int a = Integer.parseInt(st.nextToken()) - 1;
                int b = Integer.parseInt(st.nextToken());
                Treap.Node node = treap.getStats(a, b);
                long sum = node.statistic.sumValue;
                pw.println(sum);
            }
        }
        pw.flush();
    }


}

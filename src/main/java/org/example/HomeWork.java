package org.example;


import lombok.SneakyThrows;

import java.io.*;
import java.util.Arrays;

public class HomeWork {

    Treap treap = new Treap();
    //TreapSlow treap = new TreapSlow();

    boolean isDebug = false;

    /**
     * <h1>Задание 1.</h1>
     * Решить задачу UPIT из файла contest7_tasks.pdf
     */
    @SneakyThrows
    public void upit(InputStream in, OutputStream out) {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out))
        ) {
            String params = reader.readLine();
            Arrays.stream(reader.readLine().split(" ")).map(Integer::parseInt).forEach(treap::add);
            String line;
            if(isDebug &&false){
                treap.printer.printTree(treap.root);
            }
            int n = 0;
            while ((line = reader.readLine()) != null) {
                ++n;
                if (isDebug) {
                    System.out.println("----- " + (n) + " -----");
                    System.out.println("----- " + (line) + " -----");
                }
                apply(line, writer);
                if(isDebug && false) {
                    treap.printer.printTree(treap.root);
                }
            }
        }
    }

    @SneakyThrows
    private void apply(String line, BufferedWriter writer) {
        String[] params = line.split(" ");
        Integer p0 = Integer.parseInt(params[0]);
        Integer p1 = Integer.parseInt(params[1]);
        Integer p2 = Integer.parseInt(params[2]);
        Integer p3 = p0 < 3 ? Integer.parseInt(params[3]) : null;
        switch (p0) {
            case 1:
                setAll(p1-1, p2, p3);
                break;
            case 2:
                setProgression(p1-1, p2, p3);
                break;
            case 3:
                treap.add(p1-1, p2);
                break;
            case 4:
                var node = treap.getStats(p1 - 1, p2);
                var stats = node.statistic;
//                String expected = this.expected[expectedN++];
//                if(stats.sumValue != Long.parseLong(expected)){
//                    System.out.println("ERROR: "+stats.sumValue+" != "+expected);
//                }
//                if(stats.sumValue == 201740L || stats.sumValue == 201525L){
//                    System.out.println("");
//                }
                writer.write(String.format("%d\n", stats.sumValue));
                break;
        }
    }

    private void setProgression(Integer a, Integer b, Integer x) {
        treap.addProgression(a,b,x);
    }

    private void setAll(Integer a, Integer b, Integer x) {
        treap.setAll(a,b,x);
    }


}

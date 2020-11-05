package edu.neu.coe.info6205.union_find;

import edu.neu.coe.info6205.util.Benchmark_Timer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class UF_Client {

    public static int count(boolean isAlternate, int n, boolean pathCompression) {
        UF uf = null;
        if (!isAlternate) {
            uf = new UF_HWQUPC(n, pathCompression);
        } else {
            uf = new UF_Alternatives(n, pathCompression);
        }


        Random random = new Random();
        int connectNum = 0;
        boolean[] isConnected = new boolean[n];
        for (int i=0; i<n; ++i) {
            isConnected[i] = false;
        }
        List<Integer> roots = new ArrayList<>();
        System.out.println(uf.toString());

        while (connectNum != n) {
            int n1 = random.nextInt(n);
            int n2 = random.nextInt(n);
            if (n1 == n2) {
                continue;
            }
            if (!uf.isConnected(n1, n2)) {
                // if any of the two sites was connected to at least one site, skip
                if (isConnected[n1] && isConnected[n2]) {
                    continue;
                }
                // if any of the two sites was not connected
                else {
                    System.out.println("\nunion " + n1 + ", " + n2);
                    uf.union(n1, n2);

                    System.out.println(uf.toString());

                    if (isConnected[n1]) {
                        connectNum ++;
                        isConnected[n2] = true;
                    }else if (isConnected[n2]) {
                        connectNum ++;
                        isConnected[n1] = true;
                    }else {
                        isConnected[n1] = true;
                        isConnected[n2] = true;
                        connectNum += 2;
                    }
                    for (boolean b : isConnected) {
                        System.out.print(b + " ");
                    }
                    System.out.println("connectNum: " + connectNum + "\n");
                }
            }
        }

        for (int i=0; i<n; ++i) {
            int root = uf.find(i);
            if (!roots.contains(root)) {
                roots.add(root);
            }
        }

        return roots.size();
    }

    public static void main(String[] args) {
//        System.out.println(UF_Client.count(100));

        UF_Alternatives uf_al = new UF_Alternatives(10);
        System.out.println(uf_al.toString());
        uf_al.union(0, 4);
        System.out.println(uf_al.toString());
        uf_al.union(0, 8);
        System.out.println(uf_al.toString());
        uf_al.union(0, 9);
        System.out.println(uf_al.toString());
        uf_al.union(3, 8);
        System.out.println(uf_al.toString());
        uf_al.union(5, 6);
        System.out.println(uf_al.toString());
        uf_al.union(3, 6);
        System.out.println(uf_al.toString());
//        System.out.println(uf_al.getParent(6));


        //function to be benchmarked
        Consumer<Integer> func1 = inp -> count(false, 100, true);
        Consumer<Integer> func2 = inp -> count(true, 100, false);
        Consumer<Integer> func3 = inp -> count(true, 100, true);

        Benchmark_Timer<Integer> t1 = new Benchmark_Timer<>("UF_HWQUPC Benchmark", func1);
        Benchmark_Timer<Integer> t2 = new Benchmark_Timer<>("UF_Alternatives1, store depth rather than size, Benchmark", func2);
        Benchmark_Timer<Integer> t3 = new Benchmark_Timer<>("UF_Alternatives2, two loops, Benchmark", func3);

        System.out.println(t1.run(1, 10));
        System.out.println(t2.run(1, 10));
        System.out.println(t3.run(1, 10));
    }
}

package edu.neu.coe.info6205.union_find;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UF_Client {

    public static int count(int n) {
        UF_HWQUPC uf_hwqupc = new UF_HWQUPC(n);
        Random random = new Random();
        int connectNum = 0;
        boolean[] isConnected = new boolean[n];
        for (int i=0; i<n; ++i) {
            isConnected[i] = false;
        }
        List<Integer> roots = new ArrayList<>();
        System.out.println(uf_hwqupc.toString());

        while (connectNum != n) {
            int n1 = random.nextInt(n);
            int n2 = random.nextInt(n);
            if (n1 == n2) {
                continue;
            }
            if (!uf_hwqupc.connected(n1, n2)) {
                // if any of the two sites was connected to at least one site, skip
                if (isConnected[n1] && isConnected[n2]) {
                    continue;
                }
                // if any of the two sites was not connected
                else {
                    System.out.println("\nunion " + n1 + ", " + n2);
                    uf_hwqupc.union(n1, n2);

                    System.out.println(uf_hwqupc.toString());

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
            int root = uf_hwqupc.find(i);
            if (!roots.contains(root)) {
                roots.add(root);
            }
        }

        return roots.size();
    }

    public static void main(String[] args) {
        System.out.println(UF_Client.count(1000));
    }
}

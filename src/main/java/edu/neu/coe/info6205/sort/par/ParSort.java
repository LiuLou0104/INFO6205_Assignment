package edu.neu.coe.info6205.sort.par;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * This code has been fleshed out by Ziyao Qiao. Thanks very much.
 * TODO tidy it up a bit.
 */
class ParSort {

    public static int cutoff = 1000;

    // 'from' is the first index to sort, 'to' is the array length
    public static void sort(int[] array, int from, int to, Executor myPool) {
        ThreadPoolExecutor tpe = (ThreadPoolExecutor) myPool;
        System.out.println();

        System.out.println("Thread Pool Size："+ tpe.getCorePoolSize());

        int queueSize = tpe.getQueue().size();
        System.out.println("Line-up Threads Currently："+ queueSize);

        int activeCount = tpe.getActiveCount();
        System.out.println("Active Threads Currently："+ activeCount);

        long completedTaskCount = tpe.getCompletedTaskCount();
        System.out.println("Complete Threads Currently："+ completedTaskCount);

        long taskCount = tpe.getTaskCount();
        System.out.println("Overall Threads："+ taskCount);

        if (activeCount >= tpe.getCorePoolSize() || (to - from < cutoff && (to - from) > 1)) {
            System.out.println("Arrays.sort");
            Arrays.sort(array, from, to);
        }
        else {
            CompletableFuture<int[]> parsort1 = parsort(array, from, from + (to - from) / 2, myPool);// TO IMPLEMENT

            CompletableFuture<int[]> parsort2 = parsort(array, from + (to - from) / 2, to, myPool);// TO IMPLEMENT

            CompletableFuture<int[]> parsort = parsort1.thenCombine(parsort2, (xs1, xs2) -> {
                int[] result = new int[xs1.length + xs2.length];
                // TO IMPLEMENT
                int i = 0, j = 0;
                int k = 0; // inital index of merged subarray array
                while (i < xs1.length && j < xs2.length) {
                    if (xs1[i] <= xs2[j]) {
                        result[k] = xs1[i];
                        i ++;
                    }else {
                        result[k] = xs2[j];
                        j ++;
                    }
                    k ++;
                }
                // copy remaining elements of xs1 if any
                while (i < xs1.length) {
                    result[k] = xs1[i];
                    i ++;
                    k ++;
                }
                // copy remaining elements of xs2 if any
                while (j < xs2.length) {
                    result[k] = xs2[j];
                    j ++;
                    k ++;
                }
//                Arrays.stream(result).boxed().collect(Collectors.toList()).forEach(System.out::print);
                return result;
            });

            parsort.whenComplete((result, throwable) -> System.arraycopy(result, 0, array, from, result.length));
//            System.out.println("# threads: "+ ForkJoinPool.commonPool().getRunningThreadCount());
            parsort.join();
        }
    }

    private static CompletableFuture<int[]> parsort(int[] array, int from, int to, Executor myPool) {
        return CompletableFuture.supplyAsync(
                () -> {
                    int[] result = new int[to - from];
                    System.arraycopy(array, from, result, 0, result.length);
                    // TO IMPLEMENT
                    if (result.length > 1) {
                        sort(result, 0, to - from, myPool);
                    }
//                    Arrays.stream(result).boxed().collect(Collectors.toList()).forEach(System.out::print);
//                    System.out.println();
                    return result; // if result.length == 1, return the result directly
                }, myPool
        );
    }
}
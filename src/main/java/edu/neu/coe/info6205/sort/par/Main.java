package edu.neu.coe.info6205.sort.par;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;

/**
 * This code has been fleshed out by Ziyao Qiao. Thanks very much.
 * TODO tidy it up a bit.
 */
public class Main {

    /*
    * @Description: consistent thread pool size && array size; varying cutoff
    * @Param: []
    * @return: void
    **/
    public static void differentiateCutoff (int arraySizeStartFrom, int arrayElementRandomBound, int cutoffStartFrom, int threadPoolSizeExponentStartFrom, int experiments, int runTimePerExperiment, String outputFileName) {
        mainProcedure(true, arraySizeStartFrom, arrayElementRandomBound, false, cutoffStartFrom, true, threadPoolSizeExponentStartFrom, experiments, runTimePerExperiment, outputFileName);
    }

    /*
    * @Description: consistent cutoff && array size; varying thread pool size
    * @Param: []
    * @return: void
    **/
    public static void differentiateThreadPoolSize (int arraySizeStartFrom, int arrayElementRandomBound, int cutoffStartFrom, int threadPoolSizeExponentStartFrom, int experiments, int runTimePerExperiment, String outputFileName) {
        mainProcedure(true, arraySizeStartFrom, arrayElementRandomBound, true, cutoffStartFrom, false, threadPoolSizeExponentStartFrom, experiments, runTimePerExperiment, outputFileName);
    }

    /*
    * @Description: consistent thread pool size && cutoff; varying array size
    * @Param: []
    * @return: void
    **/
    public static void differentiateArraySize (int arraySizeStartFrom, int arrayElementRandomBound, int cutoffStartFrom, int threadPoolSizeExponentStartFrom, int experiments, int runTimePerExperiment, String outputFileName) {
        mainProcedure(false, arraySizeStartFrom, arrayElementRandomBound, true, cutoffStartFrom, true, threadPoolSizeExponentStartFrom, experiments, runTimePerExperiment, outputFileName);
    }

    private static void mainProcedure(boolean isFixedArraySize, int arraySizeStartFrom, int arrayElementRandomBound, boolean isFixedCutoff, int cutoffStartFrom, boolean isFixedThreadPoolSize, int threadPoolSizeExponentStartFrom, int experiments, int runTimePerExperiment, String outputFileName) {
        Random random = new Random();
        int initialArraySize = arraySizeStartFrom;
        int initialCutoff = cutoffStartFrom;

        int[] array = new int[arraySizeStartFrom];
        ParSort.cutoff = cutoffStartFrom;
        ExecutorService myPool = Executors.newFixedThreadPool((int) Math.pow(2, threadPoolSizeExponentStartFrom));

        ArrayList<Long> timeList = new ArrayList<>();
        int[] arraySizeArray = new int[experiments];
        int[] cutoffArray = new int[experiments];
        int[] threadPoolSizeArray = new int[experiments];

        for (int j = 0; j < experiments; j++) {

            threadPoolSizeArray[j] = (int) Math.pow(2, threadPoolSizeExponentStartFrom);

            if (!isFixedArraySize) {
                arraySizeStartFrom += initialArraySize / 10 * (j + 1);
                array = new int[arraySizeStartFrom];
            }
            arraySizeArray[j] = arraySizeStartFrom;

            if (!isFixedCutoff) {
                cutoffStartFrom = initialCutoff * (j + 1);
            }
            cutoffArray[j] = cutoffStartFrom;

            if (!isFixedThreadPoolSize) {
                myPool = Executors.newFixedThreadPool((int) Math.pow(2, threadPoolSizeExponentStartFrom ++));
            }

            long time;
            long startTime = System.currentTimeMillis();

            for (int t = 0; t < runTimePerExperiment; t++) {
                for (int i = 0; i < array.length; i++) {
                    array[i] = random.nextInt(arrayElementRandomBound);
                }

                ParSort.sort(array, 0, array.length, myPool);

            }

            long endTime = System.currentTimeMillis();
            time = (endTime - startTime);
            timeList.add(time);
            System.out.println("cutoff：" + (ParSort.cutoff) + "\t\tThread: " + ((int) Math.pow(2, threadPoolSizeExponentStartFrom - 1)) + "\t\t10times Time:" + time + "ms");
        }

        try {
            FileOutputStream fis = new FileOutputStream("./src/" + outputFileName + ".csv");
            OutputStreamWriter isr = new OutputStreamWriter(fis);
            BufferedWriter bw = new BufferedWriter(isr);
            int j = 0;
            for (long i : timeList) {
                String content = (double) cutoffArray[j] / arraySizeArray[j] + "," + threadPoolSizeArray[j] + "," + (double) i / runTimePerExperiment + "\n";
                j++;
                bw.write(content);
                bw.flush();
            }
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        shutdownExecutor(myPool);
    }

    private static void shutdownExecutor(ExecutorService executor) {
        try {
            System.out.println("attempt to shutdown executor");
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            System.err.println("tasks interrupted");
        }
        finally {
            if (!executor.isTerminated()) {
                System.err.println("cancel non-finished tasks");
            }
            executor.shutdownNow();
            System.out.println("shutdown finished");
        }
    }

    public static void main(String[] args) {
        Main.differentiateArraySize(1000000, 1000000, 10000, 5, 20, 10, "differentiateArraySize");
//        Main.differentiateCutoff(2000000, 1000000, 50000, 5, 50, 10, "differentiateCutoff");
//        Main.differentiateThreadPoolSize(2000000, 1000000, 50000, 0, 20, 10, "differentiateThreadPoolSize");



////        processArgs(args);
////        System.out.println("Degree of parallelism: " + ForkJoinPool.getCommonPoolParallelism());
//        Random random = new Random();
//        int[] array = new int[2000000];
//        ArrayList<Long> timeList = new ArrayList<>();
//        int threadPoolCapacityExponent = 0;
//        for (int j = 50; j < 100; j++) {
//            //ParSort.cutoff = 10000 * (j + 1); // consistent thread number, varying cutoff
//            ParSort.cutoff = 10000; // consistent cutoff, varying thread number
//
//            // for (int i = 0; i < array.length; i++) array[i] = random.nextInt(10000000);
//            long time;
//            long startTime = System.currentTimeMillis();
////            ForkJoinPool myPool = new ForkJoinPool((int) Math.pow(2, threadPoolCapacityExponent ++));
//            ExecutorService myPool = Executors.newFixedThreadPool((int) Math.pow(2, threadPoolCapacityExponent ++));
//            for (int t = 0; t < 10; t++) {
//                for (int i = 0; i < array.length; i++) {
//                    array[i] = random.nextInt(10000000);
////                    if (i < 100) {
////                        System.out.print(array[i] + " ");
////                    }
//                }
////                System.out.println();
//
//                ParSort.sort(array, 0, array.length, myPool);
//
////                for (int i = 0; i < 100; i++) {
////                    System.out.print(array[i] + " ");
////                }
//            }
//            long endTime = System.currentTimeMillis();
//            time = (endTime - startTime);
//            timeList.add(time);
//
//
//            System.out.println("cutoff：" + (ParSort.cutoff) + "\t\tThread: " + ((int) Math.pow(2, threadPoolCapacityExponent - 1)) + "\t\t10times Time:" + time + "ms");
//
//        }
//        try {
////            FileOutputStream fis = new FileOutputStream("./src/ParallelSort_result.csv");
//            FileOutputStream fis = new FileOutputStream("./src/ParallelSort_result2.csv");
//            OutputStreamWriter isr = new OutputStreamWriter(fis);
//            BufferedWriter bw = new BufferedWriter(isr);
//            int j = 0;
//            for (long i : timeList) {
////                String content = (double) 10000 * (j + 1) / 2000000 + "," + (double) i / 10 + "\n";
//                String content = (double) 10000 * (j + 1) / 2000000 + "," + ((int) Math.pow(2, j)) + "," + (double) i / 10 + "\n";
//                j++;
//                bw.write(content);
//                bw.flush();
//            }
//            bw.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private static void processArgs(String[] args) {
        String[] xs = args;
        while (xs.length > 0)
            if (xs[0].startsWith("-")) xs = processArg(xs);
    }

    private static String[] processArg(String[] xs) {
        String[] result = new String[0];
        System.arraycopy(xs, 2, result, 0, xs.length - 2);
        processCommand(xs[0], xs[1]);
        return result;
    }

    private static void processCommand(String x, String y) {
        if (x.equalsIgnoreCase("N")) setConfig(x, Integer.parseInt(y));
        else
            // TODO sort this out
            if (x.equalsIgnoreCase("P")) //noinspection ResultOfMethodCallIgnored
                ForkJoinPool.getCommonPoolParallelism();
    }

    private static void setConfig(String x, int i) {
        configuration.put(x, i);
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final Map<String, Integer> configuration = new HashMap<>();


}

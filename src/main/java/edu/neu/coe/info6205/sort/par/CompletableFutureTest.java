package edu.neu.coe.info6205.sort.par;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class CompletableFutureTest {
    private static Random rand = new Random();
    private static long t = System.currentTimeMillis();

    static int getMoreData() {
        System.out.println("begin to start compute");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("end to start compute. passed " + (System.currentTimeMillis() - t)/1000 + " seconds");
        return rand.nextInt(1000);
    }

    public static CompletableFuture<Integer> compute() {
        final CompletableFuture<Integer> future = new CompletableFuture<>();
        return future;
    }

    public static void main(String[] args) throws Exception {
//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(CompletableFutureTest::getMoreData);
//        Future<Integer> f = future.whenComplete((v, e) -> {
//            System.out.println(v);
//            System.out.println(e);
//        });
//        System.out.println(f.get());
//        System.in.read();


//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            return 100;
//        });
//        CompletableFuture<String> f =  future.thenApplyAsync(i -> i * 10).thenApply(i -> i.toString());
//        System.out.println(f.get());


//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            return 100;
//        });
//        CompletableFuture<Void> f =  future.thenAccept(System.out::println);
//        System.out.println(f.get());


//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            return 100;
//        });
//        CompletableFuture<Void> f =  future.thenAcceptBoth(CompletableFuture.completedFuture(10), (x, y) -> System.out.println(x - y));
//        System.out.println(f.get());


//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            return 100;
//        });
//        CompletableFuture<Void> f =  future.thenRun(() -> System.out.println("finished"));
//        System.out.println(f.get());


//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            return 100;
//        });
//        CompletableFuture<String> f =  future.thenCompose( i -> {
//            return CompletableFuture.supplyAsync(() -> {
//                return (i * 10) + "";
//            });
//        });
//        System.out.println(f.get());


//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            return 100;
//        });
//        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
//            return "abc";
//        });
//        CompletableFuture<String> f =  future.thenCombine(future2, (x,y) -> y + "-" + x);
//        System.out.println(f.get());


//        Random rand = new Random();
//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            try {
//                Thread.sleep(10000 + rand.nextInt(1000));
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            return 100;
//        });
//        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
//            try {
//                Thread.sleep(10000 + rand.nextInt(1000));
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            return 200;
//        });
//        CompletableFuture<String> f =  future.applyToEither(future2,i -> i.toString());
//        System.out.println(f.get());


//        Random rand = new Random();
//        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
//            try {
//                Thread.sleep(10000 + rand.nextInt(1000));
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            return 100;
//        });
//        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
//            try {
//                Thread.sleep(10000 + rand.nextInt(1000));
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            return "abc";
//        });
////        CompletableFuture<Void> f =  CompletableFuture.allOf(future1,future2);
//        CompletableFuture<Object> f =  CompletableFuture.anyOf(future1,future2);
//        System.out.println(f.get());


        final CompletableFuture<Integer> f = compute();
        class Client extends Thread {
            CompletableFuture<Integer> f;
            Client(String threadName, CompletableFuture<Integer> f) {
                super(threadName);
                this.f = f;
            }
            @Override
            public void run() {
                try {
                    System.out.println(this.getName() + ": " + f.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
        new Client("Client1", f).start();
        new Client("Client2", f).start();
        System.out.println("waiting");
        f.complete(100);
        //f.completeExceptionally(new Exception());
        //System.in.read();
    }
}

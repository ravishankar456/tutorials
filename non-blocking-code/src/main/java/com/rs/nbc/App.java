package com.rs.nbc;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Requirements to process order
 * 1. GET Order based on orderId
 * 2. Enrich Order with its name
 * 3. Pay for order
 * <p>
 * Each requires previous step to be completed
 */
public class App {

    private static final int RANGE = 10;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    public void run() {
//        fixedThreadPool();
//        asyncForkJoinPool();
//        nestedCompletables();
//        combineTwoFutures();
        combineMultipleFutures();
    }

    // This is still blocking as we run a get on step 1 before moving to step 2. So this works well for single Order but blocks for multiple orders.
    // Also main thread is blocked.
    public void fixedThreadPool() {
        LocalDateTime begin = LocalDateTime.now();
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        IntStream.rangeClosed(1, RANGE).boxed().map(orderId -> {
                    try {
                        Future<Optional<Order>> getOrder = executorService.submit(getOrderTask(orderId));
                        Optional<Order> order = getOrder.get();
                        Future<Optional<Order>> enrichOrder = executorService.submit(enrichOrderTask(order));
                        Optional<Order> enrichedOrder = enrichOrder.get();
                        Future<Optional<Order>> finalOrder = executorService.submit(payOrderTask(enrichedOrder));
                        return finalOrder.get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        return Optional.empty();
                    }
                }).collect(Collectors.toList())
                .forEach(System.out::println);
        LocalDateTime end = LocalDateTime.now();
        System.out.println("Total time taken is " + ChronoUnit.MILLIS.between(begin, end));
    }

    //Async with Chained methods ensures order within the flow but parallelism across the orders. Also the main Thread is not blocked.
    public void asyncForkJoinPool() {
        LocalDateTime begin = LocalDateTime.now();
        IntStream.rangeClosed(1, RANGE).boxed().map(orderId -> {
                    Future<Optional<Order>> future = CompletableFuture.supplyAsync(() -> getOrder(orderId))
                            .thenApply(order -> enrichOrder(order))
                            //Return Empty if any exception
                            .exceptionally(ex -> Optional.empty())
                            .thenApply(order -> payOrder(order))
                            .exceptionally(ex -> Optional.empty());
                    try {
                        return future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        return Optional.empty();
                    }
                }).collect(Collectors.toList())
                //Filter empty
                .stream().filter(order -> order.isPresent())
                .forEach(System.out::println);
        LocalDateTime end = LocalDateTime.now();
        System.out.println("Total time taken is " + ChronoUnit.MILLIS.between(begin, end));
    }

    //Use Compose if methods return CompletableFutures. This flattens the Futures , but still maintaining the chain.
    public void nestedCompletables() {
        LocalDateTime begin = LocalDateTime.now();
        IntStream.rangeClosed(1, RANGE).boxed().map(orderId -> {
                    Future<Optional<Order>> future = getOrderAsync(orderId)
                            .thenCompose(order -> enrichOrderAsync(order))
                            .thenCompose(order -> payOrderAsync(order));
                    try {
                        return future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        return Optional.empty();
                    }
                }).collect(Collectors.toList())
                //Filter empty
                .stream().filter(order -> order.isPresent())
                .forEach(System.out::println);
        LocalDateTime end = LocalDateTime.now();
        System.out.println("Total time taken is " + ChronoUnit.MILLIS.between(begin, end));
    }

    //Use Combine if methods return CompletableFutures but need not be dependent of each other. This flattens the Futures , and runs methods in parallel.
    public void combineTwoFutures() {
        LocalDateTime begin = LocalDateTime.now();
        IntStream.rangeClosed(1, RANGE).boxed().map(i -> {
                    CompletableFuture orderIdFuture = getOrderId();
                    CompletableFuture orderNameFuture = getOrderName();
                    Future<Optional<Order>> combinedFuture = orderIdFuture.thenCombine(orderNameFuture, (orderId, orderName) -> Optional.of(new Order(Integer.parseInt(orderId.toString()), orderName.toString())));
                    try {
                        return combinedFuture.get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        return Optional.empty();
                    }
                }).collect(Collectors.toList())
                //Filter empty
                .stream().filter(order -> order.isPresent())
                .forEach(System.out::println);
        LocalDateTime end = LocalDateTime.now();
        System.out.println("Total time taken is " + ChronoUnit.MILLIS.between(begin, end));
    }

    //Use Combine if methods return CompletableFutures but need not be dependent of each other. Can combine results from multiple futures.
    public void combineMultipleFutures() {
        LocalDateTime begin = LocalDateTime.now();
        List<CompletableFuture<Integer>> orderIdFutureList = IntStream.rangeClosed(1, RANGE).boxed().map(i -> getOrderId()).collect(Collectors.toList());
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(orderIdFutureList.toArray(new CompletableFuture[orderIdFutureList.size()]));
        CompletableFuture<List<Integer>> allPageContentsFuture = allFutures.thenApply(future -> {
            return orderIdFutureList.stream().map(orderIdFuture -> orderIdFuture.join()).collect(Collectors.toList());
        });
        try {
            allPageContentsFuture.get().forEach(System.out::println);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        LocalDateTime end = LocalDateTime.now();
        System.out.println("Total time taken is " + ChronoUnit.MILLIS.between(begin, end));
    }


    private Callable<Optional<Order>> getOrderTask(final int orderId) {
        return () -> getOrder(orderId);
    }

    private Optional<Order> getOrder(int orderId) {
        Optional<Order> order = Optional.empty();
        if (orderId != 0) order = Optional.of(new Order(orderId));
        return order;
    }

    private CompletableFuture<Optional<Order>> getOrderAsync(int orderId) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<Order> order = Optional.empty();
            if (orderId != 0) order = Optional.of(new Order(orderId));
            return order;
        });
    }

    private Callable<Optional<Order>> enrichOrderTask(final Optional<Order> order) {
        return () -> enrichOrder(order);
    }

    private Optional<Order> enrichOrder(final Optional<Order> order) {
        if (!order.isEmpty()) order.get().setOrderName("My Order");
        return order;
    }

    private CompletableFuture<Optional<Order>> enrichOrderAsync(final Optional<Order> order) {
        return CompletableFuture.supplyAsync(() -> {
            if (!order.isEmpty()) order.get().setOrderName("My Order");
            return order;
        });
    }

    private Callable<Optional<Order>> payOrderTask(final Optional<Order> order) {
        return () -> payOrder(order);
    }

    private Optional<Order> payOrder(final Optional<Order> order) {
        if (!order.isEmpty()) order.get().setPaid(true);
        return order;
    }

    private CompletableFuture<Optional<Order>> payOrderAsync(final Optional<Order> order) {
        return CompletableFuture.supplyAsync(() -> {
            if (!order.isEmpty()) order.get().setPaid(true);
            return order;
        });
    }

    private CompletableFuture<Integer> getOrderId() {
        return CompletableFuture.supplyAsync(() -> new Random().nextInt());
    }

    private CompletableFuture<String> getOrderName() {
        return CompletableFuture.supplyAsync(() -> "Order Name");
    }

    private CompletableFuture<Boolean> isOrderPaid() {
        return CompletableFuture.supplyAsync(() -> true);
    }

}

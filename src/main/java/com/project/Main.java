package com.project;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    public static void main(String[] args) {

        ConcurrentHashMap<String, Double> compte =
                new ConcurrentHashMap<>();

        ExecutorService executor =
                Executors.newFixedThreadPool(3);

        Runnable rebreOperacio = () -> {
            compte.put("saldo", 1000.0);

            System.out.println(
                    "Saldo inicial: " + compte.get("saldo"));
        };

        Runnable aplicarComissio = () -> {

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            Double saldo = compte.get("saldo");

            if (saldo != null) {

                saldo = saldo - 25.0;

                compte.put("saldo", saldo);

                System.out.println(
                        "Comissió aplicada: " + saldo);
            }
        };

        Callable<Double> saldoFinalTask = () -> {

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            return compte.get("saldo");
        };

        executor.submit(rebreOperacio);

        executor.submit(aplicarComissio);

        Future<Double> resultat =
                executor.submit(saldoFinalTask);

        try {

            Double saldoFinal = resultat.get();

            System.out.println(
                    "Saldo final del client: " + saldoFinal);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();

        } finally {
            executor.shutdown();
        }
    }
}
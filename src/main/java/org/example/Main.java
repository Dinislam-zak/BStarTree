package org.example;

import java.util.Arrays;

import static org.example.BStarTree.nullStats;

public class Main {
    public static void main(String[] args) {
        BStarTree tree = new BStarTree(3); // t=3, т.е. B*-дерево будет иметь минимальную степень 3
        int size = 10000;
        int [] array = TestArraysGenerator.testArrayGeneration(size);
        double [] insertTime = new double[size];
        double [] searchTime = new double[100];
        double [] deleteTime = new double[1000];
        int [] insertCount = new int[size];
        int [] searchCount = new int[100];
        int [] deleteCount = new int[1000];


        for (int i : array){
            double start = System.nanoTime();
            tree.insert(array[i]);
            double end = System.nanoTime();
            insertTime[i] = end - start;
            insertCount[i] = BStarTree.insertOpCount;
            nullStats();
        }
        int j = 1;
        while (j != 100){
            double start = System.nanoTime();
            tree.search((int)(Math.random()*size));
            double end = System.nanoTime();
            searchTime[j] = end - start;
            searchCount[j] = BStarTree.searchOpCount;
            nullStats();
            j++;

        }
        int k = 1;
        while (k != 1000){
            double start = System.nanoTime();
            tree.delete((int)(Math.random()*size));
            double end = System.nanoTime();
            deleteTime[k] = end - start;
            deleteCount[k] = BStarTree.deleteOpCount;
            nullStats();
            k++;
        }
        System.out.println("Вставка:");
        //System.out.println(Arrays.toString(insertTime));
        //System.out.println(Arrays.toString(insertCount));
        System.out.println("Среднее значение по итерациям: " + average(insertCount));
        System.out.println("Среднее значение по времени: " + average(insertTime));
        System.out.println("Поиск:");
        //System.out.println(Arrays.toString(searchTime));
        //System.out.println(Arrays.toString(searchCount));
        System.out.println("Среднее значение по итерациям: " + average(searchCount));
        System.out.println("Среднее значение по времени: " + average(searchTime));
        System.out.println("Удаление:");
        //System.out.println(Arrays.toString(deleteTime));
        //System.out.println(Arrays.toString(deleteCount));
        System.out.println("Среднее значение по итерациям: " + average(deleteCount));
        System.out.println("Среднее значение по времени: " + average(deleteTime));

    }
    public static double average(double[] array) {
        double sum = 0;
        for (double num : array) {
            sum += num;
        }
        return (sum / array.length) * 10e-9;
    }
    public static int average(int[] array) {
        int sum = 0;
        for (int num : array) {
            sum += num;
        }
        return sum / array.length;
    }
}
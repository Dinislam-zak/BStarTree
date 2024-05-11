package org.example;

public class TestArraysGenerator {
    public static int[] testArrayGeneration(int size){
        int [] result = new int[size];
        for (int i = 0; i < size; i++){
            result[i] = (int) (Math.random() * size);
        }
        return result;
    }
}

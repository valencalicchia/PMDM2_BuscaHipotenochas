package com.example.buscahipotenochas.helpers;

public class ArrayHelper {
    public int indexOf(String[] numbers, String target) {
        for (int index = 0; index < numbers.length; index++) {
            if (numbers[index] == target) {
                return index;
            }
        }
        return -1;
    }
}

package com.company;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Utility {
    public static boolean isNumeric(String str) {
        try {
            double y = Integer.parseInt(str);
        } catch (NumberFormatException c) {
            return false;
        }
        return true;
    }

    public static <T> void shuffleArray(T[] array) {
        Random rnd = ThreadLocalRandom.current();
        for (int i = array.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            T a = array[index];
            array[index] = array[i];
            array[i] = a;
        }
    }

    public static <T> void shuffleList(List<T> list) {
        Random rnd = ThreadLocalRandom.current();
        for (int i = list.size() - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            T a = list.get(index);
            list.set(index, list.get(i));
            list.set(i, a);
        }
    }

    public static <T> T[] concatinateArrays(T[] first, T[] second) {
        if (first == null) {
            return second;
        }
        if (second == null) {
            return first;
        }

        List<T> output = new ArrayList<>();
        for (int u = 0; u < first.length; u++)
        {
            output.add(first[u]);
        }
        for (int u = 0; u < first.length; u++)
        {
            output.add(second[u]);
        }

        return (T[])output.toArray();
    }
}

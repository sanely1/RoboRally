package model;

import java.util.List;
import java.util.Map;

/**
 * This class contains helper methods
 *
 * @author Johanna
 */

public class Tools {

    /**
     * @param min
     * @param max
     * @return a random number (including the given min and max)
     */
    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    /**
     * @param map
     * @param value
     * @param <K>   key
     * @param <V>   value
     * @return return the key of a value in the given hashmap
     */
    public static <K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * @param list
     * @param element
     * @return index of the element (starts from the end)
     */
    public static int findIndex(List<String> list, String element) {
        if (list == null) {
            return -1;
        }
        int i = list.size() - 1;

        while (i >= 0) {
            if (list.get(i).equals(element)) {
                return i;
            } else {
                i--;
            }
        }
        return -1;
    }
}

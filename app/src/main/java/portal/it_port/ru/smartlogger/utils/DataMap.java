package portal.it_port.ru.smartlogger.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper class with HashMap functions
 */
public class DataMap {

    /**
     * Constructs HashMap based on provided array of keys and values
     * @param items Keys and values to fill to Hashmap
     * @param <T> Type of hashmap keys
     * @param <U>
     * @return Constructed HashMap with data
     */
    public static <T,U> HashMap<T,U> create(Object... items) {
        HashMap<T,U> result = new HashMap<>();
        for (int i=0;i<items.length;i+=2) {
            if (i<items.length-1) {
                result.put((T)items[i],(U)items[i+1]);
            }
        }
        return result;
    }

    /**
     * Emulates "getOrDefault" method of Map class in modern Java. If specified key exists in
     * source map, then value of this key returned, otherwise default value returned
     * @param data Source map to work with
     * @param key Key to check and return
     * @param defaultValue Value returned if value of provided key not found
     * @return
     */
    public static Object getOrDefault(Map data,Object key, Object defaultValue) {
        if (data.get(key) != null) return data.get(key);
        return defaultValue;
    }
}

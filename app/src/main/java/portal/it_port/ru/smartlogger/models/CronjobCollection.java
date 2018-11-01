package portal.it_port.ru.smartlogger.models;

import android.database.Cursor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import portal.it_port.ru.smartlogger.utils.DataMap;

/**
 * Class used to manage list of Cronjob models.
 * Implemented as Singleton
 */
public final class CronjobCollection extends Collection {

    // Instance of single object of this type
    private static CronjobCollection instance = null;

    /**
     * Method returs single instance of this object
     * @return Singleton instance of this object
     */
    public static CronjobCollection getInstance() {
        if (instance == null) instance = new CronjobCollection();
        return instance;
    }

    /**
     * Private constructor
     */
    private CronjobCollection() {
        super();
    }

    // Data structure which holds list of Cronjobs, which this collection manages
    private ConcurrentHashMap<String,Cronjob> cronjobs = new ConcurrentHashMap<>();

    /**
     * Method used to add new or update existing cronjob in collection
     * @param cronjob Cronjob to put
     * @return True if operation finished successfully or false otherwise
     */
    public boolean putCronjob(Cronjob cronjob) {
        if (!isValidCronjob(cronjob)) return false;
        cronjobs.put(cronjob.getId(),cronjob);
        return true;
    }

    /**
     * Method used to add or update cronjob based on data from provided HashMap
     * @param data Source data. Must contain "id" field
     * @return Constructed cronjob object, which added to collection or null in case of failure
     */
    public Cronjob putCronjob(Map<Object,Object> data) {
        if (data == null || !data.containsKey("id")) return null;
        String id = data.get("id").toString();
        Cronjob cronjob = getCronjob(id);
        if (cronjob == null) cronjob = new Cronjob();
        cronjob.setId(id);
        cronjob.setName(DataMap.getOrDefault(data,"name","").toString());
        cronjob.setEnabled(Boolean.parseBoolean(DataMap.getOrDefault(data,"enabled",false).toString()));
        cronjob.setStatus(DataMap.getOrDefault(data,"status","IDLE").toString());
        cronjob.setType(data.get("type").toString());
        cronjob.setLastRunTimestamp(Long.parseLong(DataMap.getOrDefault(data,"lastRunTimestamp",0).toString()));
        putCronjob(cronjob);
        return cronjob;
    }

    /**
     * Method used to construct and put cronjob to colleciton based on data from Cursor of content provider
     * @param cursor Cursor, pointed to record, which need to transform to Cronjob object and add to collection
     * @return Constructed cronjob object, which added to collection or null in case of failure
     */
    public Cronjob putCronjob(Cursor cursor) {
        HashMap<Object,Object> data = new HashMap<>();
        data.put("id",cursor.getString(cursor.getColumnIndex("id")));
        data.put("name",cursor.getString(cursor.getColumnIndex("name")));
        data.put("type",cursor.getString(cursor.getColumnIndex("type")));
        data.put("enabled",cursor.getString(cursor.getColumnIndex("enabled")));
        data.put("lastRunTimestamp",cursor.getString(cursor.getColumnIndex("lastRunTimestamp")));
        return putCronjob(data);
    }

    /**
     * Method used to delete provided cronjob from collection
     * @param cronjob Cronjob to delete
     * @return True if removed successfully or false otherwise
     */
    public boolean removeCronjob(Cronjob cronjob) {
        if (!isValidCronjob(cronjob)) return false;
        if (!cronjobs.containsKey(cronjob.getId())) return false;
        this.removeCronjob(cronjob.getId());
        return true;
    }

    /**
     * Method used to delete provided cronjob from collection
     * @param id Id of Cronjob to delete
     * @return True if removed successfully or false otherwise
     */
    public boolean removeCronjob(String id) {
        if (!cronjobs.containsKey(id)) return false;
        cronjobs.remove(id);
        return true;
    }

    /**
     * Method used to return cronjob by ID
     * @param id Id of cronjob to find and return
     * @return Cronjob with specified id or null if not found
     */
    public Cronjob getCronjob(String id) {
        if (cronjobs == null || !cronjobs.containsKey(id)) return null;
        return cronjobs.get(id);
    }

    /**
     * Method returns whole cronjobs collection
     * @return Whole cronjobs collection
     */
    public HashMap<String,Cronjob> getCronjobs() {
        if (cronjobs == null) return null;
        return new HashMap<>(cronjobs);
    }

    /**
     * Method returns list of cronjobs of specified type as ArrayList
     * @param type Type of cronjob used as a filter. If null, returns all cronjobs
     * @return ArrayList with matched cronjobs
     */
    public List<Cronjob> getCronjobsList(String type) {
        List<Cronjob> result = new ArrayList<>();
        for (Cronjob it: cronjobs.values()) {
            if (type != null && !type.isEmpty() && !it.getType().equals(type)) continue;
            result.add(it);
        }
        Collections.sort(result);
        return result;
    }

    /**
     * Method returns set of types, which currently exists in the list
     * @return Set of string names of cronjob types
     */
    public Set<String> getCronjobTypes() {
        Set<String> result = new TreeSet<>();
        for (Cronjob it: cronjobs.values()) result.add(it.getType());
        return result;
    }

    /**
     * Method returns true if provided Cronjob is valid cronjob object
     * @param cronjob Cronjob to test
     * @return True if it's valid or false otherwise
     */
    private boolean isValidCronjob(Cronjob cronjob) {
        if (cronjob == null) return false;
        if (cronjob.getId() == null || cronjob.getId().isEmpty()) return false;
        return true;
    }
}
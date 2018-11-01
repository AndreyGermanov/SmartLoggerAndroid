package portal.it_port.ru.smartlogger.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import portal.it_port.ru.smartlogger.utils.DataMap;

/**
 * Content provider which manages list of Cronjobs. Used by activities to display
 * data of this list in UI
 */
public class CronjobsContentProvider extends ContentProvider {

    // URI which content resolvers uses to access this provider and run tasks related to
    // Cronjobs list
    public static final Uri CRONJOBS_LIST_URI = Uri.parse("content://.providers.CronjobsContentProvider/cronjobs");

    // Structure which contains current cronjobs list
    private ConcurrentHashMap<String,TreeMap<String,Object>> cronjobs = new ConcurrentHashMap<>();


    /**
     * Method automatically starts when this object instantiates first time
     */
    @Override
    public boolean onCreate() {
        return true;
    }

    /**
     * Method which responds to SELECT queries to this Content provider.
     * @param uri - Request URI
     * @param fields - List of fields to retrieve
     * @param condition -Select condition (everything after WHERE in SQL query)
     * @param selectArgs -Array of arguments, which will be passed to condition, if condition string
     *                   uses placeholders "?"
     * @param sortOrder -ORDER BY expression in SQL format, as "id ASC, name DESC"
     * @return Cursor with data
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] fields, @Nullable String condition, @Nullable String[] selectArgs, @Nullable String sortOrder) {
        MatrixCursor result = new MatrixCursor(new String[]{"enabled","id","lastRunTimestamp","name","status","type"});
        for (String key: cronjobs.keySet()) {
            TreeMap<String,Object> row = cronjobs.get(key);
            if (row == null) continue;
            result.addRow(getRow(row));
        }
        return result;
    }

    /**
     * Method used to construct single of query result
     * @param row Input row
     * @return Output array, in format that Cursor object accepts
     */
    private Object[] getRow(TreeMap<String,Object> row) {
        Object[] result = new Object[row.size()];
        int i = 0;
        for (String key: row.keySet()) {
            if (!key.equals("enabled"))
                result[i++] = DataMap.getOrDefault(row,key,"").toString();
            else
                result[i++] = Boolean.parseBoolean(DataMap.getOrDefault(row,key,false).toString());
        }
        return result;
    }

    /**
     * Method returns type of request to this Content Provider, based on provided request Uri
     * @param uri source URI
     * @return String representation of type of request
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    /**
     * Method used to implement INSERT operation to add rows to data collection
     * @param uri Request URI
     * @param contentValues : Map with data, where keys are field names and values are field values
     * @return URI of added item, which will be possible to use for future request to this row or
     * null if it not required
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        if (contentValues == null || !contentValues.containsKey("id")) return null;
        TreeMap<String,Object> row = new TreeMap<>();
        row.put("id",contentValues.getAsString("id"));
        row.put("name",contentValues.getAsString("name"));
        row.put("type",contentValues.getAsString("type"));
        row.put("status",contentValues.getAsString("status"));
        row.put("enabled",contentValues.getAsBoolean("enabled"));
        Long lastRunTimestamp = 0L;
        if (contentValues.getAsLong("lastRunTimestamp") != null)
            lastRunTimestamp = contentValues.getAsLong("lastRunTimestamp");
        row.put("lastRunTimestamp",lastRunTimestamp);
        cronjobs.put(contentValues.getAsString("id"),row);
        return null;
    }


    /**
     * Method used to implement DELETE operation to delete rows from data collection
     * @param uri Request Uri
     * @param condition - Condition in SQL format (everything after WHERE)
     * @param conditionArgs - List of condition arguments, if condition has "?" placeholders
     * @return number of affected rows
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String condition, @Nullable String[] conditionArgs) {
        return 0;
    }

    /**
     * Method used to implement UPDATE operation to udate rows in data collection
     * @param uri Request URI
     * @param contentValues : Map with data to set for matched records, where keys are field names and values are field values
     * @param condition - Condition in SQL format (everything after WHERE)
     * @param conditionArgs - List of condition arguments, if condition has "?" placeholders
     * @return number of affected rows
     */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String condition, @Nullable String[] conditionArgs) {
        return 0;
    }
}

package portal.it_port.ru.smartlogger.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import portal.it_port.ru.smartlogger.utils.DataMap;

/**
 * Created by Andrey Germanov on 10/28/18.
 */
public class CronjobsContentProvider extends ContentProvider {

    private final String TAG = this.getClass().getName();
    public static final String Uri = "content://.providers.CronjobsContentProvider/cronjobs";
    ConcurrentHashMap<String,TreeMap<String,Object>> cronjobs = new ConcurrentHashMap<>();

    @Override
    public boolean onCreate() {
        return true;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        MatrixCursor result = new MatrixCursor(new String[]{"enabled","id","name","status","type"});
        for (String key: cronjobs.keySet()) {
            TreeMap<String,Object> row = cronjobs.get(key);
            if (row == null) continue;
            result.addRow(getRow(row));
        }
        return result;
    }

    private Object[] getRow(TreeMap<String,Object> row) {
        Object[] result = new Object[row.size()];
        int i = 0;
        for (String key: row.keySet()) {
            if (key != "enabled")
                result[i++] = DataMap.getOrDefault(row,key,"").toString();
            else
                result[i++] = Boolean.parseBoolean(DataMap.getOrDefault(row,key,false).toString());
        }
        return result;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

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
        cronjobs.put(contentValues.getAsString("id"),row);
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}

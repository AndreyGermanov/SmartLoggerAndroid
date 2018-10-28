package portal.it_port.ru.smartlogger.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import portal.it_port.ru.smartlogger.utils.DataMap;

/**
 * Created by Andrey Germanov on 10/28/18.
 */
public final class CronjobCollection extends Collection {

    static CronjobCollection instance = null;

    public static CronjobCollection getInstance() {
        if (instance == null) instance = new CronjobCollection();
        return instance;
    }

    private CronjobCollection() {
        super();
    }

    ConcurrentHashMap<String,Cronjob> cronjobs = new ConcurrentHashMap<>();

    public boolean putCronjob(Cronjob cronjob) {
        if (!isValidCronjob(cronjob)) return false;
        cronjobs.put(cronjob.getId(),cronjob);
        return true;
    }

    public Cronjob putCronjob(Map<Object,Object> data) {
        if (data == null || !data.containsKey("id")) return null;
        String id = data.get("id").toString();
        Cronjob cronjob = getCronjob(id);
        if (cronjob == null) cronjob = new Cronjob();
        cronjob.setId(id);
        cronjob.setName(DataMap.getOrDefault(data,"name","").toString());
        cronjob.setEnabled(Boolean.parseBoolean(DataMap.getOrDefault(data,"enabled",false).toString()));
        cronjob.setStatus(DataMap.getOrDefault(data,"status","IDLE").toString());
        cronjobs.put(id,cronjob);
        return cronjob;
    }

    public boolean removeCronjob(Cronjob cronjob) {
        if (!isValidCronjob(cronjob)) return false;
        if (!cronjobs.containsKey(cronjob.getId())) return false;
        cronjobs.remove(cronjob.getId());
        return true;
    }

    public Cronjob getCronjob(String id) {
        if (cronjobs == null || !cronjobs.containsKey(id)) return null;
        return cronjobs.get(id);
    }

    public HashMap<String,Cronjob> getCronjobs() {
        if (cronjobs == null) return null;
        return new HashMap<>(cronjobs);
    }

    public Set<String> getCronjobsList() {
        if (cronjobs == null) return new HashSet<>();
        return cronjobs.keySet();
    }

    private boolean isValidCronjob(Cronjob cronjob) {
        if (cronjob == null) return false;
        if (cronjob.getId() == null || cronjob.getId().isEmpty()) return false;
        return true;
    }
}

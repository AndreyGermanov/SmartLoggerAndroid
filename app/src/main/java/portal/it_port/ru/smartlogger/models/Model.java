/**
 * Created by Andrey Germanov on 10/28/18.
 */

package portal.it_port.ru.smartlogger.models;

import android.support.annotation.NonNull;

/**
 * Base class for all data models
 */
public abstract class Model implements Comparable<Model> {
    // ID of model
    protected String id = "";

    /**
     * Standard method which used to compare models of this type
     * @param model Second model to compare current model with
     * @return 0 if models are equal, -1 if current model less than "model",
     * 1 if current model greater than "model"
     */
    @Override
    public int compareTo(@NonNull Model model) {
        return this.getId().compareTo(model.getId());
    }

    /**
     * Getters and setters for private fields
     */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

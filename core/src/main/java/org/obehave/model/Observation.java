package org.obehave.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.LocalDateTime;
import org.obehave.exceptions.Validate;
import org.obehave.model.coding.Coding;
import org.obehave.persistence.impl.ObservationDaoImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * During an observation, it's possible to code subjects and actions.
 */
@DatabaseTable(tableName = "Observation", daoClass = ObservationDaoImpl.class)
public class Observation extends BaseEntity implements Displayable {
    @DatabaseField(columnName = "name")
    private String name;

    @DatabaseField(columnName = "video")
    private File video;

    @DatabaseField(columnName = "date")
    private LocalDateTime dateTime;

    private List<Coding> codings = new ArrayList<>();

    public Observation() {

    }

    public Observation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name must not be null!");
        }
        this.name = name;
    }

    public File getVideo() {
        return video;
    }

    public void setVideo(File video) {
        this.video = video;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            throw new IllegalArgumentException("dateTime must not be null!");
        }

        this.dateTime = dateTime;
    }

    public void addCoding(Coding coding) {
        Validate.isNotNull(coding);

        codings.add(coding);
    }

    @Override
    public String getDisplayString() {
        return getName();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Observation rhs = (Observation) obj;

        return new EqualsBuilder().append(name, rhs.name).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).build();
    }
}

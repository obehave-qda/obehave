package org.obehave.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.obehave.exceptions.Validate;
import org.obehave.persistence.impl.ObservationDaoImpl;

import java.io.File;
import java.util.*;

/**
 * During an observation, it's possible to code subjects and actions.
 */
@DatabaseTable(tableName = "Observation", daoClass = ObservationDaoImpl.class)
public class Observation extends BaseEntity implements Displayable {
    public static final String COLUMN_NAME = "name";

    @DatabaseField(columnName = COLUMN_NAME)
    private String name;

    @DatabaseField(columnName = "video")
    private File video;

    @DatabaseField(columnName = "date")
    private DateTime dateTime;

    @ForeignCollectionField
    private Collection<Coding> codings = new ArrayList<>();

    @ForeignCollectionField(eager = false)
    private Collection<SubjectInObservation> participatingSubjects = new ArrayList<>();

    @DatabaseField(columnName = "focalSubject", foreign = true)
    private Subject focalSubject;

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

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        if (dateTime == null) {
            throw new IllegalArgumentException("dateTime must not be null!");
        }

        this.dateTime = dateTime;
    }

    public void addCoding(Coding coding) {
        Validate.isNotNull(coding, "Coding");

        codings.add(coding);
        coding.setObservation(this);
    }

    public List<Coding> getCodings() {
        return Collections.unmodifiableList(new ArrayList<>(codings));
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

    public void setParticipatingSubjects(List<Subject> subjects) {
        participatingSubjects.clear();

        for (Subject subject : subjects) {
            addParticipatingSubject(subject);
        }
    }

    private void addParticipatingSubject(Subject subject) {
        participatingSubjects.add(new SubjectInObservation(this, subject));
    }

    /**
     * Returns all subjects that are participating in this observation
     * @return a list of the participating subjects
     */
    public List<Subject> getParticipatingSubjects() {
        List<Subject> subjects = new ArrayList<>();
        for (SubjectInObservation participatingSubject : participatingSubjects) {
            subjects.add(participatingSubject.getSubject());
        }

        return Collections.unmodifiableList(subjects);
    }

    public Subject getFocalSubject() {
        return focalSubject;
    }

    public void setFocalSubject(Subject focalSubject) {
        this.focalSubject = focalSubject;
    }

    /**
     * Returns the end of the last coding
     * @return the end of the last coding
     */
    public long getEndOfLastCoding() {
        long max = 0;

        for (Coding coding : getCodings()) {
            if (coding.getEndMs() > max) {
                max = coding.getEndMs();
            }
        }

        return max;
    }

    /**
     * Returns all open codings
     * @return all open codings
     */
    public List<Coding> getOpenCodings() {
        List<Coding> openCodings = new ArrayList<>();

        for (Coding coding : getCodings()) {
            if (coding.isOpen()) {
                openCodings.add(coding);
            }
        }

        return openCodings;
    }

    /**
     * Searches for all subjects with open codings at {@code atMs}
     * @param atMs the time to look at
     * @return a list of subjects with open codings
     */
    public List<Subject> getSubjectsWithOpenCodings(long atMs) {
        Set<Subject> subjects = new HashSet<>();

        for (Coding coding : getOpenCodings()) {
            if (atMs >= coding.getStartMs()) {
                subjects.add(coding.getSubject());
            }
        }

        return new ArrayList<>(subjects);
    }

    /**
     * Searches for all coded actions, that a subject has at {@code atMs}
     * @param subject the subject to search actions for
     * @param atMs the time when the actions should be open
     * @return a list of actions a subject is doing at the given time
     */
    public List<Action> getActionsFromOpenCodingsOfSubject(Subject subject, long atMs) {
        Set<Action> actions = new HashSet<>();

        if (subject != null) {
            for (Coding coding : getOpenCodings()) {
                if (atMs >= coding.getStartMs() && coding.getSubject().equals(subject))
                    actions.add(coding.getAction());
            }
        }

        return new ArrayList<>(actions);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).build();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(super.toString()).append("name", name).append("dateTime", dateTime).append("video", video).toString();
    }
}

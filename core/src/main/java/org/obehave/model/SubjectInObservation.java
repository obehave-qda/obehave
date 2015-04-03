package org.obehave.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.obehave.persistence.impl.SubjectInObservationDaoImpl;

/**
 * Helper class to model m:n relationships with ORMLite between Subjects and Observations
 */
@DatabaseTable(tableName = "SubjectInObservation", daoClass = SubjectInObservationDaoImpl.class)
public class SubjectInObservation extends BaseEntity {
    @DatabaseField(columnName = "observation", foreign = true, foreignAutoRefresh = true)
    private Observation observation;

    @DatabaseField(columnName = "subject", foreign = true, foreignAutoRefresh = true)
    private Subject subject;

    private SubjectInObservation() {
        // for frameworks
    }

    public SubjectInObservation(Observation observation, Subject subject) {
        this.observation = observation;
        this.subject = subject;
    }

    public Observation getObservation() {
        return observation;
    }

    public Subject getSubject() {
        return subject;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString()).append("observation", observation.getDisplayString())
                .append("subject", subject).toString();
    }
}

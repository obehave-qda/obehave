package org.obehave.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.obehave.exceptions.FactoryException;
import org.obehave.model.modifier.Modifier;
import org.obehave.persistence.impl.CodingDaoImpl;

/**
 * @author Markus Möslinger
 */
@DatabaseTable(tableName = "Coding", daoClass = CodingDaoImpl.class)
public class Coding extends BaseEntity{
    @DatabaseField(columnName = "subject", foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Subject subject;
    @DatabaseField(columnName = "action", foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Action action;
    @DatabaseField(columnName = "modifier", foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Modifier modifier;
    @DatabaseField(columnName = "start")
    private long startMs;
    @DatabaseField(columnName = "end")
    private long endMs = -1;
    @DatabaseField(columnName = "observation", foreign = true, foreignAutoRefresh = true)
    private Observation observation;

    public Coding() {
        // framework
    }

    public Coding(Subject subject, Action action, long startMs) {
        this(subject, action, startMs, 0);
    }

    public Coding(Subject subject, Action action, long startMs, long endMs) {
        setSubject(subject);
        setAction(action);
        setStartMs(startMs);
        setEndMs(endMs);
    }

    public Coding(Subject subject, Action action, String modifierInput, long startMs) throws FactoryException {
        this(subject, action, modifierInput, startMs, 0);
    }

    public Coding(Subject subject, Action action, String modifierInput, long startMs, long endMs) throws FactoryException {
        this(subject, action, startMs, endMs);
        setModifier(modifierInput);
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        if (subject == null) {
            throw new IllegalArgumentException("Subject must not be null!");
        }

        this.subject = subject;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        if (action == null) {
            throw new IllegalArgumentException("Action must not be null!");
        }
        this.action = action;
    }

    public Modifier getModifier() {
        return modifier;
    }

    public void setModifier(String input) throws FactoryException {
        if (input != null && !input.isEmpty()) {
            if (action.getModifierFactory() == null) {
                throw new FactoryException("This action has no modifier factory!");
            }

            this.modifier = action.getModifierFactory().create(input);
        }
    }

    public long getStartMs() {
        return startMs;
    }

    public void setStartMs(long startMs) {
        if (startMs < 0) {
            throw new IllegalArgumentException("ms must not be lower than 0");
        }

        this.startMs = startMs;
    }

    public long getEndMs() {
        return endMs;
    }

    public void setEndMs(long endMs) {
        this.endMs = endMs;
    }

    public long getDuration() {
        validateStateCoding();

        return endMs - startMs;
    }

    public boolean isStateCoding() {
        return action.getType() == Action.Type.STATE;
    }

    public boolean isOpen() {
        return isStateCoding() && endMs < startMs;
    }

    private void validateStateCoding() {
        if (!isStateCoding()) {
            throw new IllegalStateException("Coding has to be a state coding!");
        }
    }

    public Observation getObservation() {
        return observation;
    }

    public void setObservation(Observation observation) {
        this.observation = observation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Coding coding = (Coding) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(startMs, coding.startMs)
                .append(subject, coding.subject)
                .append(action, coding.action)
                .append(modifier, coding.modifier)
                .append(observation, coding.observation)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(subject)
                .append(action)
                .append(modifier)
                .append(startMs)
                .append(observation)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("subject", subject)
                .append("action", action)
                .append("modifier", modifier)
                .append("startMs", startMs)
                .append("endMs", endMs)
                .toString();
    }
}

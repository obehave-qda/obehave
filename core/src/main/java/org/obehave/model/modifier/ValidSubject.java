package org.obehave.model.modifier;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.obehave.model.BaseEntity;
import org.obehave.model.Subject;
import org.obehave.persistence.impl.ValidSubjectDaoImpl;

/**
 * @author Markus Möslinger
 */
@DatabaseTable(tableName = "ValidSubject", daoClass = ValidSubjectDaoImpl.class)
public class ValidSubject extends BaseEntity {
    @DatabaseField(columnName = "subject", foreign = true, foreignAutoRefresh = true)
    private Subject subject;

    @DatabaseField(columnName = "modifierFactory", foreign = true, foreignAutoRefresh = true)
    private ModifierFactory modifierFactory;

    private ValidSubject() {
        // for frameworks
    }

    public ValidSubject(Subject subject, ModifierFactory modifierFactory) {
        this.subject = subject;
        this.modifierFactory = modifierFactory;
    }

    public Subject getSubject() {
        return subject;
    }

    public ModifierFactory getModifierFactory() {
        return modifierFactory;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString()).append("modifierFactory", modifierFactory.getDisplayString())
                .append("subject", subject).toString();
    }
}

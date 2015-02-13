package org.obehave.model.modifier;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.obehave.model.Subject;
import org.obehave.persistence.impl.ModifierDaoImpl;

/**
 * A {@code SubjectModifier} modifies a coded action with another {@link org.obehave.model.Subject}
 */
@DatabaseTable(tableName = Modifier.ORM_TABLE, daoClass = ModifierDaoImpl.class)
public class SubjectModifier extends Modifier<Subject> {
    @DatabaseField(columnName = "subject", foreign = true)
    private final Subject subject;

    SubjectModifier(Subject subject) {
        super(SubjectModifier.class);

        if (subject == null) {
            throw new IllegalArgumentException("subject must not be null!");
        }

        this.subject = subject;
    }

    public Subject get() {
        return subject;
    }
}

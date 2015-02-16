package org.obehave.model.modifier;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.obehave.model.Subject;

/**
 * @author Markus Möslinger
 */
@DatabaseTable(tableName = "validSubjects")
public class ValidSubject {
    @DatabaseField(columnName = "subject", foreign = true)
    private Subject subject;

    @DatabaseField(columnName = "modifierFactory", foreign = true)
    private ModifierFactory modifierFactory;

    public ValidSubject() {

    }

    public ValidSubject(Subject subject, ModifierFactory modifierFactory) {
        this.subject = subject;
        this.modifierFactory = modifierFactory;
    }
}

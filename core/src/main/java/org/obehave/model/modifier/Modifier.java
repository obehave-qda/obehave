package org.obehave.model.modifier;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.obehave.model.BaseEntity;
import org.obehave.model.Subject;
import org.obehave.persistence.impl.ModifierDaoImpl;
import org.obehave.persistence.impl.ModifierFactoryDaoImpl;
import sun.plugin.dom.exception.InvalidStateException;

import java.math.BigDecimal;

/**
 * A modifier can modify a coded action with other classes of type M
 */
@DatabaseTable(tableName = "Modifier", daoClass = ModifierDaoImpl.class)
public class Modifier extends BaseEntity {
    public static enum Type {
        // this sucks. Due to ORMLite's incapability of handling inheritance strategies, ie. one table per class hierarchy,
        // we are flattening the class hierarchy to only only class.
        DECIMAL_MODIFIER, ENUMERATION_MODIFIER, SUBJECT_MODIFIER
    }

    @DatabaseField(columnName = "type")
    private Type type;

    public Modifier(BigDecimal value) {
        type = Type.DECIMAL_MODIFIER;

        if (value == null) {
            throw new IllegalArgumentException("value must not be null!");
        }

        decimalValue = value;
    }

    public Modifier(String value) {
        type = Type.ENUMERATION_MODIFIER;

        if (value == null) {
            throw new IllegalArgumentException("value must not be null");
        }

        this.enumerationValue = value;
    }

    public Modifier(Subject subject) {
        type = Type.SUBJECT_MODIFIER;

        if (subject == null) {
            throw new IllegalArgumentException("subject must not be null!");
        }

        this.subject = subject;
    }


    /**
     * Returns the modifying class
     * @return the modifying class
     */
    public Object get() {
        switch (type) {
            case DECIMAL_MODIFIER:
                return decimalValue;
            case ENUMERATION_MODIFIER:
                return enumerationValue;
            case SUBJECT_MODIFIER:
                return subject;
            default:
                throw new IllegalStateException("Object is in an invalid state");
        }
    }

    // DECIMAL RANGE
    @DatabaseField(columnName = "number")
    private BigDecimal decimalValue;

    // ENUMERATION
    @DatabaseField(columnName = "enumerationValue")
    private String enumerationValue;

    // SUBJECT
    @DatabaseField(columnName = "subject", foreign = true)
    private Subject subject;
}

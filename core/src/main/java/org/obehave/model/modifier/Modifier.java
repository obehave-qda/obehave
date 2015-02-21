package org.obehave.model.modifier;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.obehave.exceptions.Validate;
import org.obehave.model.BaseEntity;
import org.obehave.model.Subject;
import org.obehave.persistence.impl.ModifierDaoImpl;

import java.math.BigDecimal;

/**
 * A modifier can modify a coded action with other classes of type M
 */
@DatabaseTable(tableName = "Modifier", daoClass = ModifierDaoImpl.class)
public class Modifier extends BaseEntity {
    public static final String COLUMN_TYPE = "type";

    @DatabaseField(columnName = COLUMN_TYPE)
    private Type type;
    @DatabaseField(columnName = "modifierFactory", foreign = true, foreignAutoRefresh = true)
    private ModifierFactory modifierFactory;
    // DECIMAL RANGE
    @DatabaseField(columnName = "number")
    private BigDecimal decimalValue;
    // ENUMERATION
    @DatabaseField(columnName = "enumerationValue")
    private String enumerationValue;
    // SUBJECT
    @DatabaseField(columnName = "subject", foreign = true, foreignAutoRefresh = true)
    private Subject subject;

    private Modifier() {
        // for frameworks
    }

    public Modifier(ModifierFactory modifierFactory, BigDecimal value) {
        type = Type.DECIMAL_MODIFIER;
        setModifierFactory(modifierFactory);

        Validate.isNotNull(value);
        decimalValue = value;
    }


    public Modifier(ModifierFactory modifierFactory, String value) {
        type = Type.ENUMERATION_MODIFIER;
        setModifierFactory(modifierFactory);

        Validate.isNotNull(value);
        this.enumerationValue = value;
    }

    public Modifier(ModifierFactory modifierFactory, Subject subject) {
        type = Type.SUBJECT_MODIFIER;
        setModifierFactory(modifierFactory);

        Validate.isNotNull(subject);
        this.subject = subject;
    }

    /**
     * Returns the modifying class
     *
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

    public ModifierFactory getModifierFactory() {
        return modifierFactory;
    }

    private void setModifierFactory(ModifierFactory modifierFactory) {
        Validate.isNotNull(modifierFactory);

        this.modifierFactory = modifierFactory;
    }

    public static enum Type {
        // this sucks. Due to ORMLite's incapability of handling inheritance strategies, ie. one table per class hierarchy,
        // we are flattening the class hierarchy to only only class.
        DECIMAL_MODIFIER, ENUMERATION_MODIFIER, SUBJECT_MODIFIER
    }
}

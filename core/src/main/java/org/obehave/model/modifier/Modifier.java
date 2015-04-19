package org.obehave.model.modifier;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.obehave.exceptions.Validate;
import org.obehave.model.BaseEntity;
import org.obehave.model.Subject;
import org.obehave.persistence.impl.ModifierDaoImpl;
import org.obehave.util.I18n;

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

    @DatabaseField(columnName = "buildString")
    private String buildString;

    private Modifier() {
        // for frameworks
    }

    public Modifier(ModifierFactory modifierFactory, BigDecimal value, String buildString) {
        type = Type.DECIMAL_MODIFIER;
        setModifierFactory(modifierFactory);

        Validate.isNotNull(value, I18n.get("validate.modifier.bigdecimal"));
        decimalValue = value;
        this.buildString = buildString;
    }


    public Modifier(ModifierFactory modifierFactory, String value) {
        type = Type.ENUMERATION_MODIFIER;
        setModifierFactory(modifierFactory);

        Validate.isNotNull(value, I18n.get("validate.modifier.string"));
        this.enumerationValue = value;
        buildString = value;
    }

    public Modifier(ModifierFactory modifierFactory, Subject subject, String buildString) {
        type = Type.SUBJECT_MODIFIER;
        setModifierFactory(modifierFactory);

        Validate.isNotNull(subject, I18n.get("validate.modifier.subject"));
        this.subject = subject;
        this.buildString = buildString;
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
                throw new IllegalStateException(I18n.get("exception.illegalstate", this));
        }
    }

    public Type getType() {
        return type;
    }

    public ModifierFactory getModifierFactory() {
        return modifierFactory;
    }

    private void setModifierFactory(ModifierFactory modifierFactory) {
        Validate.isNotNull(modifierFactory, I18n.get("validate.modifier.modifierfactory"));

        this.modifierFactory = modifierFactory;
    }

    public String getBuildString() {
        return buildString;
    }

    public enum Type {
        // this sucks. Due to ORMLite's incapability of handling inheritance strategies, ie. one table per class hierarchy,
        // we are flattening the class hierarchy to only only class.
        DECIMAL_MODIFIER, ENUMERATION_MODIFIER, SUBJECT_MODIFIER
    }
}

package org.obehave.model.modifier;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import org.obehave.exceptions.FactoryException;
import org.obehave.model.BaseEntity;
import org.obehave.model.Displayable;
import org.obehave.model.Subject;
import org.obehave.persistence.impl.ModifierFactoryDaoImpl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

/**
 * A class to create valid modifiers
 */
@DatabaseTable(tableName = "ModifierFactory", daoClass = ModifierFactoryDaoImpl.class)
public class ModifierFactory extends BaseEntity implements Displayable {
    public static enum Type {
        // this sucks. Due to ORMLite's incapability of handling inheritance strategies, ie. one table per class hierarchy,
        // we are flattening the class hierarchy to only only class.
        SUBJECT_MODIFIER_FACTORY, ENUMERATION_MODIFIER_FACTORY, DECIMAL_RANGE_MODIFIER_FACTORY
    }

    @DatabaseField(columnName = "type")
    private Type type;

    @DatabaseField(columnName = "name")
    private String name;

    @DatabaseField(columnName = "alias")
    private String alias;

    public ModifierFactory(int from, int to) {
        type = Type.DECIMAL_RANGE_MODIFIER_FACTORY;
        setRange(from, to);
    }

    public ModifierFactory(String... values) {
        type = Type.ENUMERATION_MODIFIER_FACTORY;
        addValidValues(values);
    }

    public ModifierFactory(Subject... subjects) {
        type = Type.SUBJECT_MODIFIER_FACTORY;
        addValidSubjects(subjects);
    }

    public Type getType() {
        return type;
    }

    public Modifier create(String input) throws FactoryException {
        switch(type) {
            case DECIMAL_RANGE_MODIFIER_FACTORY:
                return createDecimalRangeModifier(input);
            case ENUMERATION_MODIFIER_FACTORY:
                return createEnumerationModifier(input);
            case SUBJECT_MODIFIER_FACTORY:
                return createSubjectModifier(input);
            default:
                throw new FactoryException("Unknown factory type: " + type);
        }
    }

    /**
     * Gets the name of the modifier factory
     * @return a name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the modifier factory
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the alias for autocompletion
     * @return a alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Sets the alias for autocompletion
     * @param alias the alias to set
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String getDisplayString() {
        return getName();
    }

    private void validateType(Type type) {
        if (type != this.type) {
            throw new IllegalStateException("Factory is of type " + this.type + "!");
        }
    }

    // DECIMAL RANGE
    @DatabaseField(columnName = "rangeFrom")
    private int from;

    @DatabaseField(columnName = "rangeTo")
    private int to;

    public void setRange(int from, int to) {
        this.from = from;
        this.to = to;
    }

    public int getFrom() {
        validateType(Type.DECIMAL_RANGE_MODIFIER_FACTORY);

        return from;
    }

    public int getTo() {
        validateType(Type.DECIMAL_RANGE_MODIFIER_FACTORY);

        return to;
    }

    private Modifier createDecimalRangeModifier(String input) throws FactoryException {
        validateType(Type.DECIMAL_RANGE_MODIFIER_FACTORY);

        if (input == null || input.equals("")) {
            throw new FactoryException("input must be a non empty string");
        }

        BigDecimal value;
        try {
            value = stringToBigDecimal(input);
            if (value.compareTo(BigDecimal.valueOf(from)) >= 0 && value.compareTo(BigDecimal.valueOf(to)) <= 0) {
                return new Modifier(value);
            } else {
                throw new FactoryException("Value not in range");
            }
        } catch (ParseException e) {
            throw new FactoryException("Couldn't create instance with this input", e);
        }
    }

    private static BigDecimal stringToBigDecimal(String input) throws ParseException {
        return stringToBigDecimal(input, Locale.US);
    }

    private static BigDecimal stringToBigDecimal(String input, Locale locale) throws ParseException {
        DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance(locale);
        decimalFormat.setParseBigDecimal(true);

        return (BigDecimal) decimalFormat.parse(input);
    }

    // ENUMERATION
    private List<String> validValues = new ArrayList<>();

    public boolean addValidValues(String... values) {
        validateType(Type.ENUMERATION_MODIFIER_FACTORY);

        return validValues.addAll(Arrays.asList(values));
    }


    private Modifier createEnumerationModifier(String input) throws FactoryException {
        validateType(Type.ENUMERATION_MODIFIER_FACTORY);

        if (validValues.contains(input)) {
            return new Modifier(input);
        } else {
            throw new FactoryException("This isn't an allowed value");
        }
    }

    public List<String> getValidValues() {
        validateType(Type.ENUMERATION_MODIFIER_FACTORY);

        return Collections.unmodifiableList(validValues);
    }

    // SUBJECT
    @ForeignCollectionField(eager = false)
    private List<Subject> validSubjects = new ArrayList<>();

    /**
     * If {@code subjectName} is parsable to a valid {@code Subject} stored in this factory, return a new {@code SubjectModifier} containing the parsed {@code Subject}
     * @param subjectName a string matching either the name or the alias of a subject valid in this factory's context
     * @return a new {@code SubjectModifier} containing the matched subject
     */
    private Modifier createSubjectModifier(String subjectName) throws FactoryException {
        validateType(Type.SUBJECT_MODIFIER_FACTORY);

        for (Subject subject : validSubjects) {
            if (subjectName.equals(subject.getName()) || subjectName.equals(subject.getAlias())) {
                return new Modifier(subject);
            }
        }

        throw new FactoryException("This subject isn't allowed here!"); // more precise message
    }

    public boolean addValidSubjects(Subject... subjects) {
        validateType(Type.SUBJECT_MODIFIER_FACTORY);

        if (subjects == null) {
            throw new IllegalArgumentException("Subjects must not be null");
        }

        return validSubjects.addAll(Arrays.asList(subjects));
    }

    public List<Subject> getValidSubjects() {
        validateType(Type.SUBJECT_MODIFIER_FACTORY);

        return Collections.unmodifiableList(validSubjects);
    }
}

package org.obehave.service;

import org.obehave.model.Action;
import org.obehave.model.Observation;
import org.obehave.model.Subject;
import org.obehave.model.modifier.ModifierFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Markus Möslinger
 */
public class SuggestionService {
    public static class SuggestionServiceBuilder {
        private final Study study;
        protected SuggestionServiceBuilder(Study study) {
            this.study = study;
        }

        public SuggestionService build(Observation observation) {
            return new SuggestionService(study, observation);
        }
    }

    private final Study study;
    private final Observation observation;

    protected SuggestionService(Study study, Observation observation) {
        this.study = study;
        this.observation = observation;
    }

    public Collection<String> getSubjectSuggestions(String enteredText) {
        List<String> suggestedSubjects = new ArrayList<>();

        if (enteredText != null && !enteredText.isEmpty()) {
            for (Subject subject : observation.getParticipatingSubjects()) {
                final String enteredTextLower = enteredText.toLowerCase();

                if (subject.getAlias() != null && subject.getAlias().toLowerCase().contains(enteredTextLower)) {
                    // rank matching aliases before matching names
                    suggestedSubjects.add(0, subject.getDisplayString());
                } else if (subject.getName().toLowerCase().contains(enteredTextLower)) {
                    suggestedSubjects.add(subject.getDisplayString());
                }
            }
        }

        return suggestedSubjects;
    }

    public Collection<String> getActionSuggestions(String enteredText) {
        List<String> suggestedActions = new ArrayList<>();

        if (enteredText != null && !enteredText.isEmpty()) {
            for (Action a : study.getActionList()) {
                final String enteredTextLower = enteredText.toLowerCase();

                if (a.getAlias() != null && a.getAlias().toLowerCase().contains(enteredTextLower)) {
                    // rank matching aliases before matching names
                    suggestedActions.add(0, a.getDisplayString());
                } else if (a.getName().toLowerCase().contains(enteredTextLower)) {
                    suggestedActions.add(a.getDisplayString());
                }
            }
        }

        return suggestedActions;
    }

    public Collection<String> getModifierSuggestions(String enteredAction, String enteredText) {
        List<String> validValues = new ArrayList<>();
        Action action = study.getActionService().getForName(enteredAction);

        if (action != null && action.getModifierFactory() != null) {
            ModifierFactory mf = action.getModifierFactory();

            switch (mf.getType()) {
                case ENUMERATION_MODIFIER_FACTORY:
                    validValues.addAll(buildEnumerationSuggestions(mf));
                    break;
                case SUBJECT_MODIFIER_FACTORY:
                    validValues.addAll(buildSubjectListSuggestions(mf));
                    break;
                case DECIMAL_RANGE_MODIFIER_FACTORY:
                    validValues.addAll(buildDecimalRangeSuggestions(mf));
                    break;
            }
        }

        if (enteredText != null && !enteredText.isEmpty()) {
            List<String> filteredValues = new ArrayList<>();
            for (String value : validValues) {
                if (value.toLowerCase().contains(enteredText.toLowerCase())) {
                    filteredValues.add(value);
                }
            }

            validValues = filteredValues;
        }

        return validValues;
    }

    private Collection<String> buildEnumerationSuggestions(ModifierFactory mf) {
        return mf.getValidValues();
    }

    private Collection<String> buildDecimalRangeSuggestions(ModifierFactory mf) {
        List<String> suggestions = new ArrayList<>();

        for (int i = mf.getFrom(); i <= mf.getTo(); i++) {
            suggestions.add(String.valueOf(i));
        }

        return suggestions;
    }

    private Collection<String> buildSubjectListSuggestions(ModifierFactory mf) {
        List<String> suggestions = new ArrayList<>();
        List<Subject> participatingSubjects = observation.getParticipatingSubjects();

        for (Subject s : mf.getValidSubjects()) {
            if (participatingSubjects.contains(s)) {
                suggestions.add(s.getDisplayString());
            }
        }

        return suggestions;
    }
}

package org.obehave.service;

import org.obehave.model.Action;
import org.obehave.model.Observation;
import org.obehave.model.Subject;
import org.obehave.model.modifier.ModifierFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Service class to build a list of suggestions represanted as strings for different use cases.
 */
public class SuggestionService {
    /**
     * A builder to create an instance of {@link SuggestionService} with a {@link Study} and an {@link Observation}
     */
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

    private SuggestionService(Study study, Observation observation) {
        this.study = study;
        this.observation = observation;
    }

    /**
     * Get suggestions for possible subject values
     * @param filterText a text to filter subjects
     * @param endCoding if true, only subjects with open codings (at {@code atMs}) will be returned
     * @param atMs if {@code endCoding} is {@code true}, only codings that are open at the specific time will be suggested
     * @return a list of suggestions for a possible subject
     */
    public Collection<String> getSubjectSuggestions(String filterText, boolean endCoding, long atMs) {
        if (!endCoding) {
            return getAllSubjectSuggestions(filterText);
        } else {
            return getSubjectSuggestionsForOpenCodings(filterText, atMs);
        }
    }

    public Collection<String> getActionSuggestions(String enteredText, boolean endCoding, String enteredSubject, long atMs) {
        if (!endCoding) {
            return getAllActionSuggestions(enteredText);
        } else {
            return getActionSuggestionsForSubjectWithOpenCoding(enteredText, enteredSubject.substring(1), atMs);
        }
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

    private List<String> getAllActionSuggestions(String enteredText) {
        return getActionSuggestions(study.getActionList(), enteredText);
    }

    private List<String> getActionSuggestionsForSubjectWithOpenCoding(String enteredText, String enteredSubject, long atMs) {
        final Subject subject = study.getSubjectService().getForName(enteredSubject);

        return getActionSuggestions(observation.getActionsFromOpenCodingsOfSubject(subject, atMs), enteredText);
    }

    private List<String> getActionSuggestions(List<Action> actions, String nameFilter) {
        List<String> suggestedActions = new ArrayList<>();

        if (nameFilter != null) {
            for (Action a : actions) {
                final String enteredTextLower = nameFilter.toLowerCase();

                if (a.getAlias() != null && a.getAlias().toLowerCase().contains(enteredTextLower)) {
                    // rank matching aliases before matching names
                    suggestedActions.add(0, a.getDisplayString());
                } else if (a.getName().toLowerCase().contains(enteredTextLower)) {
                    suggestedActions.add(a.getDisplayString());
                }
            }
        }
        Collections.sort(suggestedActions);
        return suggestedActions;
    }

    private List<String> getAllSubjectSuggestions(String enteredText) {
        return getSubjectSuggestions(observation.getParticipatingSubjects(), enteredText, null);
    }

    private List<String> getSubjectSuggestionsForOpenCodings(String enteredText, long atMs) {
        return getSubjectSuggestions(observation.getSubjectsWithOpenCodings(atMs), enteredText.substring(1), "/");
    }

    private List<String> getSubjectSuggestions(List<Subject> subjects, String nameFilter, String prefix) {
        List<String> suggestedSubjects = new ArrayList<>();

        if (nameFilter != null) {
            for (Subject subject : subjects) {
                final String enteredTextLower = nameFilter.toLowerCase();

                if (subject.getAlias() != null && subject.getAlias().toLowerCase().contains(enteredTextLower)) {
                    // rank matching aliases before matching names
                    if (prefix == null) {
                        suggestedSubjects.add(0, subject.getDisplayString());
                    } else {
                        suggestedSubjects.add(0, prefix + subject.getDisplayString());
                    }
                } else if (subject.getName().toLowerCase().contains(enteredTextLower)) {
                    if (prefix == null) {
                        suggestedSubjects.add(subject.getDisplayString());
                    } else {
                        suggestedSubjects.add("/" + subject.getDisplayString());
                    }
                }
            }
        }

        Collections.sort(suggestedSubjects);
        return suggestedSubjects;
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

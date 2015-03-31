package org.obehave.service;

import org.obehave.model.Action;
import org.obehave.model.Displayable;
import org.obehave.model.Subject;
import org.obehave.model.modifier.ModifierFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Markus Möslinger
 */
public class SuggestionService {
    private final Study study;

    protected SuggestionService(Study study) {
        this.study = study;
    }

    public Collection<String> getSubjectSuggestions(String enteredText) {
        List<String> suggestedSubjects = new ArrayList<>();

        if (enteredText != null && !enteredText.isEmpty()) {
            List<Displayable> subjects = study.getSubjects().flatten();

            for (Displayable subject : subjects) {
                Subject s = (Subject) subject;

                if (s.getAlias().contains(enteredText)) {
                    // rank matching aliases before normal matches
                    suggestedSubjects.add(0, s.getDisplayString());
                } else if (s.getName().contains(enteredText)) {
                    suggestedSubjects.add(s.getDisplayString());
                }
            }
        }

        return suggestedSubjects;
    }

    public Collection<String> getActionSuggestions(String enteredText) {
        List<String> suggestedActions = new ArrayList<>();

        if (enteredText != null && !enteredText.isEmpty()) {
            List<Displayable> actions = study.getActions().flatten();

            for (Displayable action : actions) {
                Action a = (Action) action;

                if (a.getAlias().contains(enteredText)) {
                    // rank matching aliases before normal matches
                    suggestedActions.add(0, a.getDisplayString());
                } else if (a.getName().contains(enteredText)) {
                    suggestedActions.add(a.getDisplayString());
                }
            }
        }

        return suggestedActions;
    }

    public Collection<String> getModifierSuggestions(String enteredAction, String enteredText) {
        List<String> suggestedActions = new ArrayList<>();
        Action action = study.getActionService().getForName(enteredAction);

        if (action != null && action.getModifierFactory() != null) {
            ModifierFactory mf = action.getModifierFactory();

            switch (mf.getType()) {
                case ENUMERATION_MODIFIER_FACTORY:
                    suggestedActions.addAll(buildEnumerationSuggestions(mf));
                    break;
                case SUBJECT_MODIFIER_FACTORY:
                    suggestedActions.addAll(buildSubjectListSuggestions(mf));
                    break;
                case DECIMAL_RANGE_MODIFIER_FACTORY:
                    suggestedActions.addAll(buildDecimalRangeSuggestions(mf));
                    break;
            }
        }

        return suggestedActions;
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

        for (Subject s : mf.getValidSubjects()) {
            suggestions.add(s.getDisplayString());
        }

        return suggestions;
    }
}

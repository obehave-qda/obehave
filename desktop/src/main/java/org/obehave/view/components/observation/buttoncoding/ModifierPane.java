package org.obehave.view.components.observation.buttoncoding;

import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.obehave.events.EventBusHolder;
import org.obehave.model.modifier.ModifierFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Markus.Moeslinger on 18.06.2015.
 */
public class ModifierPane extends TilePane {
    public void forModifierFactory(ModifierFactory mf) {
        if (mf != null) {
            switch (mf.getType()) {
                case SUBJECT_MODIFIER_FACTORY:
                    showSubjectModifiers(mf);
                    break;
                case ENUMERATION_MODIFIER_FACTORY:
                    showEnumerationModifiers(mf);
                    break;
                case DECIMAL_RANGE_MODIFIER_FACTORY:
                    showNumberModifiers(mf);
                    break;
            }
        }
    }

    private void showSubjectModifiers(ModifierFactory mf) {
        final List<Button> buttons = mf.getValidSubjects().stream()
                .map(s -> new ModifierButton(s.getDisplayString())).collect(Collectors.toList());

        showButtons(buttons, 2);
    }

    private void showNumberModifiers(ModifierFactory mf) {
        final List<Button> buttons = new ArrayList<>();

        for (int i = mf.getFrom(); i <= mf.getTo(); i++) {
            buttons.add(new ModifierButton(String.valueOf(i)));
        }

        showButtons(buttons, 5);
    }

    private void showEnumerationModifiers(ModifierFactory mf) {
        final List<Button> buttons = mf.getValidValues().stream().map(ModifierButton::new).collect(Collectors.toList());

        showButtons(buttons, 2);
    }

    private void showButtons(List<Button> buttons, int columns) {
        getChildren().clear();
        setPrefColumns(columns);
        getChildren().addAll(buttons);
    }

    private static class ModifierButton extends Button {
        private final String modifierInput;

        public ModifierButton(String modifierInput) {
            super(modifierInput);

            this.modifierInput = modifierInput;

            setOnAction(event -> EventBusHolder.post(new eventsi.ModifierClicked(modifierInput)));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            ModifierButton that = (ModifierButton) o;

            return new EqualsBuilder()
                    .append(modifierInput, that.modifierInput)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(modifierInput)
                    .toHashCode();
        }
    }
}

package org.obehave.view.components.observation.buttoncoding;

import javafx.scene.layout.Pane;
import org.obehave.service.Study;

public class ButtonCodingPane extends Pane {
    private Study study;

    public void setStudy(Study study) {
        this.study = study;
    }
}

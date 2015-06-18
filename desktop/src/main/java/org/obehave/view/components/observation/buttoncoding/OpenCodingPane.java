package org.obehave.view.components.observation.buttoncoding;

import javafx.scene.layout.Pane;
import org.obehave.service.Study;

/**
 * Created by Markus.Moeslinger on 18.06.2015.
 */
public class OpenCodingPane extends Pane {
    private Study study;

    public void setStudy(Study study) {
        this.study = study;
    }
}

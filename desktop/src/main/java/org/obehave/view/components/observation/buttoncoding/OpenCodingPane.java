package org.obehave.view.components.observation.buttoncoding;

import javafx.scene.layout.TilePane;
import org.obehave.service.Study;

/**
 * Created by Markus.Moeslinger on 18.06.2015.
 */
public class OpenCodingPane extends TilePane {
    private Study study;

    public OpenCodingPane() {
        setPrefColumns(1);
    }

    public void setStudy(Study study) {
        this.study = study;
    }
}

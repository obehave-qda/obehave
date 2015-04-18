package org.obehave.view.util;

import org.junit.Before;

public class StopWatchTest {
    private StopWatch stopWatch;

    @Before
    public void prepare() {
        stopWatch = new StopWatch(new ControllableTimeProvider());
    }


}

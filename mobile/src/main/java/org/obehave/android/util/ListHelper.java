package org.obehave.android.util;


import org.obehave.util.DisplayWrapper;

import java.util.ArrayList;
import java.util.List;

public class ListHelper {

    public static <T> List<DisplayWrapper> convertToDisplayWrapperList(List<T> objects){
        List<DisplayWrapper> wrappedList = new ArrayList<DisplayWrapper>();
        for(Object obj: objects){
            wrappedList.add(DisplayWrapper.of(obj));
        }

        return wrappedList;
    }
}

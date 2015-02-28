package org.obehave.android.ui.adapters;

import org.obehave.model.Displayable;

import java.util.Comparator;

public class NameAscendingComparator implements Comparator {
    @Override
    public int compare(Object lhs, Object rhs) {

        if(lhs instanceof Displayable && rhs instanceof Displayable){
            return ((Displayable) lhs).getDisplayString().compareToIgnoreCase(((Displayable) rhs).getDisplayString());
        }
        else {
            throw new IllegalArgumentException("Given  class has to implement Displayable Interface");
        }

    }

    @Override
    public boolean equals(Object obj) {
            return true;
    }
}

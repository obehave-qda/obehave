package org.obehave.android.application;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.Stack;

public class BackStackHistory{
    private Stack<Fragment> history = new Stack<Fragment>();

    public void push(FragmentManager fm, Fragment fragment){
        history.push(fragment);
        fm.beginTransaction().addToBackStack(null).commit();
    }

    public void pop(FragmentManager fm){
        if(history.size() > 0){
            history.pop();
            fm.popBackStack();
        }
    }

    public void clear(FragmentManager fm){
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }

        history.clear();
    }

    public int size(){
        return history.size();
    }

    public Fragment peek(){
        return history.peek();
    }
}

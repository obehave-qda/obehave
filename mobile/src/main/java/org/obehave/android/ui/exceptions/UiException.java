package org.obehave.android.ui.exceptions;

/**
 * Created by patrick on 03.02.2015.
 */
public class UiException extends Exception{

    private Exception innerException;

    public UiException(String message, Exception ex){
        super(message);
        this.innerException = ex;
    }

    public UiException(String message){
        super(message);
    }

    public Exception getInnerException(){
        return innerException;
    }
}

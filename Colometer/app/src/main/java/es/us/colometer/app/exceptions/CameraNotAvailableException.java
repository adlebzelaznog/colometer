package es.us.colometer.app.exceptions;

/**
 * Created by David on 30/04/14.
 */
public class CameraNotAvailableException extends RuntimeException {

    public CameraNotAvailableException(){
        super();
    }

    public CameraNotAvailableException(String message){
        super(message);
    }
}

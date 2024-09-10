package model.exception;

public class BackupException extends Exception  {
    public BackupException (String msg, Throwable cause){
        super(msg, cause);
    }
}

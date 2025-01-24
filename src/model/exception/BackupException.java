package model.exception;

public class BackupException extends Exception  {
        private static final long serialVersionUID = 1L;
    
        public BackupException (String msg, Throwable cause){
        super(msg, cause);
    }
}

package exception;

public class MoneyTransferException extends Exception{
    public MoneyTransferException(String message, Throwable cause) {
        super(message, cause);
    }
}

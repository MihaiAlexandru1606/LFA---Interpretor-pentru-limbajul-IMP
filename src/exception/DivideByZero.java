package exception;

public class DivideByZero extends Exception {
    private static final long serialVersionUID = -2340892536297827474L;

    public DivideByZero() {
    }

    public DivideByZero(String message) {
        super(message);
    }
}

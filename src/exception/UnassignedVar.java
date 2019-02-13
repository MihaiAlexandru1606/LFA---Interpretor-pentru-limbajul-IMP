package exception;

public class UnassignedVar extends Exception {
    private static final long serialVersionUID = -2340892536297827473L;

    public UnassignedVar() {
    }

    public UnassignedVar(String message) {
        super(message);
    }
}

package visitor;

import exception.DivideByZero;
import exception.UnassignedVar;

public interface Visitable {

    public abstract String accept(Vistor vistor) throws DivideByZero, UnassignedVar;
}

package ast.node;

import exception.DivideByZero;
import exception.UnassignedVar;
import visitor.Vistor;

public class GreaterNode implements ASTNode {

    private ASTNode lhs;
    private ASTNode rhs;

    public GreaterNode() {
        this.lhs = null;
        this.rhs = null;
    }

    public GreaterNode(ASTNode lhs, ASTNode rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public ASTNode getLhs() {
        return lhs;
    }

    public void setLhs(ASTNode lhs) {
        this.lhs = lhs;
    }

    public ASTNode getRhs() {
        return rhs;
    }

    public void setRhs(ASTNode rhs) {
        this.rhs = rhs;
    }

    @Override
    public String accept(Vistor vistor) throws DivideByZero, UnassignedVar {
        return vistor.visit(this);
    }
}

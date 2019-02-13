package ast.node;

import exception.DivideByZero;
import exception.UnassignedVar;
import visitor.Vistor;

public class NotNode implements ASTNode {
    private ASTNode child;

    public NotNode() {
        this.child = null;
    }

    public NotNode(ASTNode child) {
        this.child = child;
    }

    public ASTNode getChild() {
        return child;
    }

    public void setChild(ASTNode child) {
        this.child = child;
    }


    @Override
    public String accept(Vistor vistor) throws DivideByZero, UnassignedVar {
        return vistor.visit(this);
    }
}

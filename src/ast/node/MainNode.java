package ast.node;

import exception.DivideByZero;
import exception.UnassignedVar;
import visitor.Visitable;
import visitor.Vistor;

public class MainNode implements Visitable {
    private ASTNode child;

    public MainNode() {
        this.child = null;
    }

    public MainNode(ASTNode child) {
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

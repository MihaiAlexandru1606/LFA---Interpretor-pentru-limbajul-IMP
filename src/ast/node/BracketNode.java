package ast.node;

import exception.DivideByZero;
import exception.UnassignedVar;
import visitor.Vistor;

public class BracketNode implements ASTNode {
    private ASTNode child;
    private int numberLine;

    public BracketNode() {
        this.child = null;
    }

    public BracketNode(ASTNode child) {
        this.child = child;
    }

    public ASTNode getChild() {
        return child;
    }

    public void setChild(ASTNode child) {
        this.child = child;
    }

    @Override
    public int getNumberLine() {
        return child.getNumberLine();
    }

    public void setNumberLine(int numberLine) {
        this.numberLine = numberLine;
    }

    @Override
    public String accept(Vistor vistor) throws DivideByZero, UnassignedVar {
        return vistor.visit(this);
    }
}

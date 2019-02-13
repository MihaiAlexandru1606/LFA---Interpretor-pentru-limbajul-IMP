package ast.node;

import exception.DivideByZero;
import exception.UnassignedVar;
import visitor.Vistor;

public class SequenceNode implements ASTNode {
    private ASTNode first;
    private ASTNode second;

    public SequenceNode() {
        this.first = null;
        this.second = null;
    }

    public SequenceNode(ASTNode first, ASTNode second) {
        this.first = first;
        this.second = second;
    }

    public ASTNode getFirst() {
        return first;
    }

    public void setFirst(ASTNode first) {
        this.first = first;
    }

    public ASTNode getSecond() {
        return second;
    }

    public void setSecond(ASTNode second) {
        this.second = second;
    }

    @Override
    public String accept(Vistor vistor) throws DivideByZero, UnassignedVar {
        return vistor.visit(this);
    }
}

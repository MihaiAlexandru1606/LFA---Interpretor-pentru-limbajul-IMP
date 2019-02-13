package ast.node;

import exception.DivideByZero;
import exception.UnassignedVar;
import visitor.Vistor;

public class DivNode implements ASTNode {
    private ASTNode left;
    private ASTNode right;
    private int numberLine;

    public DivNode() {
        this.left = null;
        this.right = null;
    }

    public DivNode(ASTNode left, ASTNode right) {
        this.left = left;
        this.right = right;
    }

    public ASTNode getLeft() {
        return left;
    }

    public void setLeft(ASTNode left) {
        this.left = left;
    }

    public ASTNode getRight() {
        return right;
    }

    public void setRight(ASTNode right) {
        this.right = right;
    }

    @Override
    public int getNumberLine() {
        return numberLine;
    }

    public DivNode setNumberLine(int numberLine) {
        this.numberLine = numberLine;
        return this;
    }

    @Override
    public String accept(Vistor vistor) throws DivideByZero, UnassignedVar {
        return vistor.visit(this);
    }
}

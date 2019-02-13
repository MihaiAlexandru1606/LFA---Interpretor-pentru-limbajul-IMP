package ast.node;

import visitor.Vistor;

public class IntNode implements ASTNode {
    private String value;
    private int numberLine;

    public IntNode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int getNumberLine() {
        return numberLine;
    }

    public IntNode setNumberLine(int numberLine) {
        this.numberLine = numberLine;
        return this;
    }

    @Override
    public String accept(Vistor vistor) {
        return vistor.visit(this);
    }

}

package ast.node;

import visitor.Vistor;

public class BoolNode implements ASTNode {

    private String value;

    public BoolNode(String value) {
        this.value = value;
    }

    public String isValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String accept(Vistor vistor) {
        return vistor.visit(this);
    }
}

package ast.node;

import exception.UnassignedVar;
import visitor.Vistor;

public class VarNode implements ASTNode {

    private String name;
    private int numberLine;

    public VarNode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getNumberLine() {
        return numberLine;
    }

    public VarNode setNumberLine(int numberLine) {
        this.numberLine = numberLine;
        return this;
    }

    @Override
    public String accept(Vistor vistor) throws UnassignedVar {
        return vistor.visit(this);
    }
}

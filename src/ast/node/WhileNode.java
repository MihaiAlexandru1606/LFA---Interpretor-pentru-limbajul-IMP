package ast.node;

import exception.DivideByZero;
import exception.UnassignedVar;
import visitor.Vistor;

public class WhileNode implements ASTNode {
    private BracketNode condition;
    private BlockNode body;

    public WhileNode() {
        this.condition = null;
        this.body = null;
    }

    public WhileNode(BracketNode condition, BlockNode body) {
        this.condition = condition;
        this.body = body;
    }

    public BracketNode getCondition() {
        return condition;
    }

    public void setCondition(BracketNode condition) {
        this.condition = condition;
    }

    public BlockNode getBody() {
        return body;
    }

    public void setBody(BlockNode body) {
        this.body = body;
    }


    @Override
    public String accept(Vistor vistor) throws DivideByZero, UnassignedVar {
        return vistor.visit(this);
    }
}

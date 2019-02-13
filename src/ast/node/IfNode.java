package ast.node;

import exception.DivideByZero;
import exception.UnassignedVar;
import visitor.Vistor;

public class IfNode implements ASTNode {
    BracketNode condition;
    BlockNode thenBlock;
    BlockNode elseBlock;

    public IfNode() {
        this.condition = null;
        this.elseBlock = null;
        this.thenBlock = null;
    }

    public IfNode(BracketNode condition, BlockNode thenBlock, BlockNode elseBlock) {
        this.condition = condition;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;
    }

    public BracketNode getCondition() {
        return condition;
    }

    public void setCondition(BracketNode condition) {
        this.condition = condition;
    }

    public BlockNode getThenBlock() {
        return thenBlock;
    }

    public void setThenBlock(BlockNode thenBlock) {
        this.thenBlock = thenBlock;
    }

    public BlockNode getElseBlock() {
        return elseBlock;
    }

    public void setElseBlock(BlockNode elseBlock) {
        this.elseBlock = elseBlock;
    }

    @Override
    public String accept(Vistor vistor) throws DivideByZero, UnassignedVar {
        return vistor.visit(this);
    }
}

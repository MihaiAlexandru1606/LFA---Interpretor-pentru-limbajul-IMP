package visitor;

import ast.node.*;
import exception.DivideByZero;
import exception.UnassignedVar;

public interface Vistor {

    public abstract String visit(MainNode mainNode) throws DivideByZero, UnassignedVar;

    public abstract String visit(IntNode intNode);

    public abstract String visit(BoolNode boolNode);

    public abstract String visit(VarNode varNode) throws UnassignedVar;

    public abstract String visit(PlusNode plusNode) throws UnassignedVar, DivideByZero;

    public abstract String visit(DivNode divNode) throws UnassignedVar, DivideByZero;

    public abstract String visit(BracketNode bracketNode) throws DivideByZero, UnassignedVar;

    public abstract String visit(AndNode andNode) throws DivideByZero, UnassignedVar;

    public abstract String visit(GreaterNode greaterNode) throws UnassignedVar, DivideByZero;

    public abstract String visit(NotNode notNode) throws DivideByZero, UnassignedVar;

    public abstract String visit(AssignmentNode assignmentNode) throws UnassignedVar, DivideByZero;

    public abstract String visit(BlockNode blockNode) throws DivideByZero, UnassignedVar;

    public abstract String visit(IfNode ifNode) throws DivideByZero, UnassignedVar;

    public abstract String visit(WhileNode whileNode) throws DivideByZero, UnassignedVar;

    public abstract String visit(SequenceNode sequenceNode) throws DivideByZero, UnassignedVar;
}

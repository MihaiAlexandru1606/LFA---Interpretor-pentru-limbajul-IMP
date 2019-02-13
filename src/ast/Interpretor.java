package ast;

import ast.node.*;
import exception.DivideByZero;
import exception.UnassignedVar;
import visitor.Vistor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Interpretor implements Vistor {
    private HashMap<String, String> variable;
    private BufferedWriter bufferedWriter;

    public Interpretor(HashMap<String, String> variable, BufferedWriter bufferedWriter) {
        this.variable = variable;
        this.bufferedWriter = bufferedWriter;
    }

    public BufferedWriter getBufferedWriter() {
        return bufferedWriter;
    }

    @Override
    public String visit(MainNode mainNode) throws DivideByZero, UnassignedVar {

        mainNode.getChild().accept(this);

        for (Map.Entry<String, String> entry : variable.entrySet()) {
            try {
                bufferedWriter.write(entry.getKey() + "=" + entry.getValue() + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public String visit(IntNode intNode) {
        return intNode.getValue();
    }

    @Override
    public String visit(BoolNode boolNode) {
        return boolNode.isValue();
    }

    @Override
    public String visit(VarNode varNode) throws UnassignedVar {

        if (!variable.containsKey(varNode.getName())) {
            throw new UnassignedVar("UnassignedVar " + varNode.getNumberLine());
        }

        return variable.get(varNode.getName());
    }

    @Override
    public String visit(PlusNode plusNode) throws UnassignedVar, DivideByZero {
        String left = plusNode.getLeft().accept(this);
        String right = plusNode.getRight().accept(this);

        if (left == null) {
            throw new UnassignedVar("UnassignedVar " + plusNode.getLeft().getNumberLine());
        }

        if (right == null) {
            throw new UnassignedVar("UnassignedVar " + plusNode.getRight().getNumberLine());
        }

        return (Integer.parseInt(left) + Integer.parseInt(right)) + "";
    }

    @Override
    public String visit(DivNode divNode) throws UnassignedVar, DivideByZero {
        String left = divNode.getLeft().accept(this);
        String right = divNode.getRight().accept(this);

        if (right == null) {
            throw new UnassignedVar("UnassignedVar " + divNode.getRight().getNumberLine());
        }

        if (left == null) {
            throw new UnassignedVar("UnassignedVar " + divNode.getLeft().getNumberLine());
        }

        if (Integer.parseInt(right) == 0) {
            throw new DivideByZero("DivideByZero " + divNode.getRight().getNumberLine());
        }

        return (Integer.parseInt(left) / Integer.parseInt(right)) + "";
    }

    @Override
    public String visit(BracketNode bracketNode) throws DivideByZero, UnassignedVar {
        return bracketNode.getChild().accept(this);
    }

    @Override
    public String visit(AndNode andNode) throws DivideByZero, UnassignedVar {
        return (Boolean.parseBoolean(andNode.getLeft().accept(this)) &&
                Boolean.parseBoolean(andNode.getRight().accept(this))) + "";
    }

    @Override
    public String visit(GreaterNode greaterNode) throws UnassignedVar, DivideByZero {
        String left = greaterNode.getLhs().accept(this);
        String right = greaterNode.getRhs().accept(this);

        if (right == null) {
            throw new UnassignedVar("UnassignedVar " + greaterNode.getRhs().getNumberLine());
        }

        if (left == null) {
            throw new UnassignedVar("UnassignedVar " + greaterNode.getLhs().getNumberLine());
        }

        return (Integer.parseInt(left) > Integer.parseInt(right)) + "";
    }

    @Override
    public String visit(NotNode notNode) throws DivideByZero, UnassignedVar {
        return (!Boolean.parseBoolean(notNode.getChild().accept(this))) + "";
    }

    @Override
    public String visit(AssignmentNode assignmentNode) throws UnassignedVar, DivideByZero {
        String varName = ((VarNode) assignmentNode.getLhs()).getName();
        String value = assignmentNode.getRhs().accept(this);

        if (value == null) {
            throw new UnassignedVar("UnassignedVar " + assignmentNode.getRhs().getNumberLine());
        }

        if (!variable.containsKey(varName)) {
            throw new UnassignedVar("UnassignedVar " +  assignmentNode.getLhs().getNumberLine());
        }

        variable.put(varName, value);

        return null;

    }

    @Override
    public String visit(BlockNode blockNode) throws DivideByZero, UnassignedVar {
        if (blockNode.getChild() != null) {
            blockNode.getChild().accept(this);
        }

        return null;
    }

    @Override
    public String visit(IfNode ifNode) throws DivideByZero, UnassignedVar {

        if (Boolean.parseBoolean(ifNode.getCondition().accept(this))) {
            ifNode.getThenBlock().accept(this);
        } else {
            ifNode.getElseBlock().accept(this);
        }

        return null;
    }

    @Override
    public String visit(WhileNode whileNode) throws DivideByZero, UnassignedVar {

        while (Boolean.parseBoolean(whileNode.getCondition().accept(this))) {
            whileNode.getBody().accept(this);
        }

        return null;
    }

    @Override
    public String visit(SequenceNode sequenceNode) throws DivideByZero, UnassignedVar {
        sequenceNode.getFirst().accept(this);
        sequenceNode.getSecond().accept(this);

        return null;
    }

    public void close() {
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

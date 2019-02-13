package ast;

import ast.node.*;
import exception.DivideByZero;
import exception.UnassignedVar;
import visitor.Vistor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ASTPrinter implements Vistor {

    private BufferedWriter bufferedWriter;
    private StringBuilder indent;

    public ASTPrinter(String fileName) {

        try {
            bufferedWriter = new BufferedWriter(new FileWriter(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        indent = new StringBuilder("");
    }

    @Override
    public String visit(MainNode mainNode) throws DivideByZero, UnassignedVar {

        try {
            bufferedWriter.write(indent.toString() + "<MainNode>" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(mainNode.getChild() != null){
            indent.append("\t");
            mainNode.getChild().accept(this);
            indent.deleteCharAt(0);
        }

        return "";
    }

    @Override
    public String visit(IntNode intNode) {
        try {
            bufferedWriter.write(indent.toString() + "<IntNode> " + intNode.getValue() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    public String visit(BoolNode boolNode) {
        try {
            bufferedWriter.write(indent.toString() + "<BoolNode> " + boolNode.isValue() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    public String visit(VarNode varNode) {
        try {
            bufferedWriter.write(indent.toString() + "<VariableNode> " + varNode.getName() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    public String visit(PlusNode plusNode) throws DivideByZero, UnassignedVar {
        try {
            bufferedWriter.write(indent.toString() + "<PlusNode> +" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        indent.append('\t');
        // stanga
        plusNode.getLeft().accept(this);
        // dreapta
        plusNode.getRight().accept(this);
        indent.deleteCharAt(0);

        return "";
    }

    @Override
    public String visit(DivNode divNode) throws DivideByZero, UnassignedVar {
        try {
            bufferedWriter.write(indent.toString() + "<DivNode> /" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        indent.append('\t');
        // stanga
        divNode.getLeft().accept(this);
        // dreapta
        divNode.getRight().accept(this);
        indent.deleteCharAt(0);

        return null;
    }

    @Override
    public String visit(BracketNode bracketNode) throws DivideByZero, UnassignedVar {
        try {
            bufferedWriter.write(indent.toString() + "<BracketNode> ()" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        indent.append("\t");
        bracketNode.getChild().accept(this);
        indent.deleteCharAt(0);

        return "";
    }

    @Override
    public String visit(AndNode andNode) throws DivideByZero, UnassignedVar {
        try {
            bufferedWriter.write(indent.toString() + "<AndNode> &&" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        indent.append('\t');
        // stanga
        andNode.getLeft().accept(this);
        // dreapta
        andNode.getRight().accept(this);
        indent.deleteCharAt(0);

        return "\n";
    }

    @Override
    public String visit(GreaterNode greaterNode) throws DivideByZero, UnassignedVar {
        try {
            bufferedWriter.write(indent.toString() + "<GreaterNode> >" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        indent.append('\t');
        // stanga
        greaterNode.getLhs().accept(this);
        // dreapta
        greaterNode.getRhs().accept(this);
        indent.deleteCharAt(0);

        return "";
    }

    @Override
    public String visit(NotNode notNode) throws DivideByZero, UnassignedVar {
        try {
            bufferedWriter.write(indent.toString() + "<NotNode> !" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        indent.append("\t");
        notNode.getChild().accept(this);
        indent.deleteCharAt(0);

        return "";
    }

    @Override
    public String visit(AssignmentNode assignmentNode) throws DivideByZero, UnassignedVar {
        try {
            bufferedWriter.write(indent.toString() + "<AssignmentNode> =" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        indent.append('\t');
        // stanga
        assignmentNode.getLhs().accept(this);
        // dreapta
        assignmentNode.getRhs().accept(this);
        indent.deleteCharAt(0);

        return null;
    }

    @Override
    public String visit(BlockNode blockNode) throws DivideByZero, UnassignedVar {

        try {
            bufferedWriter.write(indent.toString() + "<BlockNode> {}" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (blockNode.getChild() != null) {
            indent.append("\t");
            blockNode.getChild().accept(this);
            indent.deleteCharAt(0);
        }

        return null;
    }

    @Override
    public String visit(IfNode ifNode) throws DivideByZero, UnassignedVar {
        try {
            bufferedWriter.write(indent.toString() + "<IfNode> if" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        indent.append("\t");
        ifNode.getCondition().accept(this);
        ifNode.getThenBlock().accept(this);
        ifNode.getElseBlock().accept(this);
        indent.deleteCharAt(0);

        return null;
    }

    @Override
    public String visit(WhileNode whileNode) throws DivideByZero, UnassignedVar {
        try {
            bufferedWriter.write(indent.toString() + "<WhileNode> while" + "\n" );
        } catch (IOException e) {
            e.printStackTrace();
        }

        indent.append("\t");
        whileNode.getCondition().accept(this);
        whileNode.getBody().accept(this);
        indent.deleteCharAt(0);

        return null;
    }

    @Override
    public String visit(SequenceNode sequenceNode) throws DivideByZero, UnassignedVar {
        try {
            bufferedWriter.write(indent.toString() + "<SequenceNode>" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        indent.append('\t');
        sequenceNode.getFirst().accept(this);
        sequenceNode.getSecond().accept(this);
        indent.deleteCharAt(0);

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
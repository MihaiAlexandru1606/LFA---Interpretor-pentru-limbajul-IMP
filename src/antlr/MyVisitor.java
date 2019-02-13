package antlr;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MyVisitor extends IMPGrammarBaseVisitor<Integer> {
    private BufferedWriter bufferedWriter;
    private StringBuilder indent;

    public MyVisitor(String fileName) {
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        indent = new StringBuilder("");
    }

    private void visitVar(String nameVar) {
        try {
            bufferedWriter.write(indent.toString() + "<VariableNode> " + nameVar + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Integer visitProgram(IMPGrammarParser.ProgramContext ctx) {
        ctx.main().accept(this);
        return 0;
    }

    @Override
    public Integer visitMain(IMPGrammarParser.MainContext ctx) {
        try {
            bufferedWriter.write(indent.toString() + "<MainNode>" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        indent.append("\t");

        if (ctx.sequence() != null) {
            ctx.sequence().accept(this);
            return 0;
        }

        if (ctx.stmt() != null) {
            ctx.stmt().accept(this);
            return 0;
        }


        return 0;
    }

    @Override
    public Integer visitSequence(IMPGrammarParser.SequenceContext ctx) {
        try {
            bufferedWriter.write(indent.toString() + "<SequenceNode>" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        indent.append("\t");

        if (ctx.leftStmt != null && ctx.rightStmt != null) {
            ctx.leftStmt.accept(this);
            ctx.rightStmt.accept(this);
            indent.deleteCharAt(0);

            return 0;
        }

        if (ctx.nextStmt != null && ctx.sequence() != null) {
            ctx.nextStmt.accept(this);
            ctx.sequence().accept(this);
            indent.deleteCharAt(0);

            return 0;
        }

        return 0;
    }

    @Override
    public Integer visitStmt(IMPGrammarParser.StmtContext ctx) {

        if (ctx.assignment() != null) {
            ctx.assignment().accept(this);

            return 0;
        }

        if (ctx.block() != null) {
            ctx.block().accept(this);

            return 0;
        }

        if (ctx.ifStmt() != null) {
            ctx.ifStmt().accept(this);

            return 0;
        }

        if (ctx.whileStmt() != null) {
            ctx.whileStmt().accept(this);

            return 0;
        }

        return 0;
    }

    @Override
    public Integer visitBlock(IMPGrammarParser.BlockContext ctx) {
        try {
            bufferedWriter.write(indent.toString() + "<BlockNode> {}" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (ctx.stmt() != null) {
            indent.append('\t');
            ctx.stmt().accept(this);
            indent.deleteCharAt(0);

            return 0;
        }

        if (ctx.sequence() != null) {
            indent.append("\t");
            ctx.sequence().accept(this);
            indent.deleteCharAt(0);

            return 0;
        }

        return 0;
    }

    @Override
    public Integer visitAssignment(IMPGrammarParser.AssignmentContext ctx) {
        try {
            bufferedWriter.write(indent.toString() + "<AssignmentNode> =" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        indent.append("\t");
        visitVar(ctx.VAR().getText());
        ctx.arithmeticExpr().accept(this);
        indent.deleteCharAt(0);

        return 0;
    }

    @Override
    public Integer visitArithmeticExpr(IMPGrammarParser.ArithmeticExprContext ctx) {
        ctx.sumExpr().accept(this);

        return 0;
    }

    @Override
    public Integer visitSumExpr(IMPGrammarParser.SumExprContext ctx) {
        if (ctx.next != null) {
            ctx.next.accept(this);

            return 0;
        }

        try {
            bufferedWriter.write(indent.toString() + "<PlusNode> +" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        indent.append('\t');
        ctx.left.accept(this);
        ctx.right.accept(this);
        indent.deleteCharAt(0);

        return 0;
    }

    @Override
    public Integer visitDivExpr(IMPGrammarParser.DivExprContext ctx) {
        if (ctx.next != null) {
            ctx.next.accept(this);

            return 0;
        }

        try {
            bufferedWriter.write(indent.toString() + "<DivNode> /" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        indent.append("\t");
        ctx.left.accept(this);
        ctx.right.accept(this);
        indent.deleteCharAt(0);

        return 0;
    }

    @Override
    public Integer visitAtomNum(IMPGrammarParser.AtomNumContext ctx) {

        if (ctx.integer != null) {
            try {
                bufferedWriter.write(indent.toString() + "<IntNode> " + ctx.integer.getText() + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }

            return 0;
        }

        if (ctx.variable != null) {
            visitVar(ctx.variable.getText());

            return 0;
        }

        if (ctx.arithmeticExpr() != null) {
            try {
                bufferedWriter.write(indent.toString() + "<BracketNode> ()" + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }

            indent.append("\t");
            ctx.arithmeticExpr().accept(this);
            indent.deleteCharAt(0);
        }

        return 0;
    }

    @Override
    public Integer visitBoolExpr(IMPGrammarParser.BoolExprContext ctx) {
        ctx.andExpr().accept(this);

        return 0;
    }

    @Override
    public Integer visitAndExpr(IMPGrammarParser.AndExprContext ctx) {
        if (ctx.next != null) {
            ctx.next.accept(this);

            return 0;
        }

        try {
            bufferedWriter.write(indent.toString() + "<AndNode> &&" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        indent.append('\t');
        ctx.left.accept(this);
        ctx.right.accept(this);
        indent.deleteCharAt(0);

        return 0;
    }

    @Override
    public Integer visitNotExpr(IMPGrammarParser.NotExprContext ctx) {
        if (ctx.next != null) {
            ctx.next.accept(this);

            return 0;
        }

        try {
            bufferedWriter.write(indent.toString() + "<NotNode> !" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        indent.append("\t");
        ctx.current.accept(this);
        indent.deleteCharAt(0);

        return 0;
    }

    @Override
    public Integer visitAtomBool(IMPGrammarParser.AtomBoolContext ctx) {
        if (ctx.boolVal != null) {
            try {
                bufferedWriter.write(indent.toString() + "<BoolNode> " + ctx.boolVal.getText() + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }

            return 0;
        }

        if (ctx.left != null && ctx.right != null) {
            try {
                bufferedWriter.write(indent.toString() + "<GreaterNode> >" + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }

            indent.append("\t");
            ctx.left.accept(this);
            ctx.right.accept(this);
            indent.deleteCharAt(0);

            return 0;
        }

        if (ctx.boolExpr() != null) {
            try {
                bufferedWriter.write(indent.toString() + "<BracketNode> ()" + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }

            indent.append("\t");
            ctx.boolExpr().accept(this);
            indent.deleteCharAt(0);

            return 0;
        }

        return 0;
    }

    @Override
    public Integer visitIfStmt(IMPGrammarParser.IfStmtContext ctx) {
        try {
            bufferedWriter.write(indent.toString() + "<IfNode> if" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        indent.append("\t");
        ctx.boolExpr().accept(this);
        ctx.thenBlock.accept(this);
        ctx.elseBlock.accept(this);
        indent.deleteCharAt(0);

        return 0;
    }

    @Override
    public Integer visitWhileStmt(IMPGrammarParser.WhileStmtContext ctx) {
        try {
            bufferedWriter.write(indent.toString() + "<WhileNode> while" + "\n" );
        } catch (IOException e) {
            e.printStackTrace();
        }

        indent.append("\t");
        ctx.boolExpr().accept(this);
        ctx.block().accept(this);
        indent.deleteCharAt(0);

        return 0;
    }

    public void close() {
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

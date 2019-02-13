package ast.node;

public class OperatorNodeFactory {
    private static OperatorNodeFactory ourInstance = new OperatorNodeFactory();

    public static OperatorNodeFactory getInstance() {
        return ourInstance;
    }

    private OperatorNodeFactory() {
    }

    public ASTNode getOperator(String op, ASTNode right, ASTNode left, int numberLine) {
        if (op.equals("/")) {

            return new DivNode(left, right).setNumberLine(numberLine);
        }

        if (op.equals("+")) {
            return new PlusNode(left, right).setNumberLine(numberLine);
        }

        if (op.equals("!")) {
            return new NotNode(right);
        }

        if (op.equals(">")) {
            return new GreaterNode(left, right);
        }

        if (op.equals("&&")) {
            return new AndNode(left, right);
        }

        return null;
    }
}

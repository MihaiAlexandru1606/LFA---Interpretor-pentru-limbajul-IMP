package ast.node;

import visitor.Visitable;

public interface ASTNode extends Visitable {
    public default int getNumberLine(){
        return 0;
    }

}

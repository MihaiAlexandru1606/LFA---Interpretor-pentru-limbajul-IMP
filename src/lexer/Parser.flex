package lexer;

import ast.node.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;
import ast.node.helper.OpenBlock;
import exception.*;
%%

%public
%class Parser

%line
%int

/** constructorul */
%init{
    this.yybegin(YYINITIAL);

    this.priority = new HashMap<>();
    this.priority.put("/", 0);
    this.priority.put("+", 1);
    this.priority.put(">", 2);
    this.priority.put("!", 3);
    this.priority.put("&&", 4);
    this.priority.put("(", 5);
    this.priority.put(")", 6);

    this.variable = new HashMap<>();

    this.exceptions = new LinkedList<>();
    this.expression = new Stack<>();
    this.operators = new Stack<>();
    this.statement = new Stack<>();
%init}

/** declararea de metode si atribute */
%{
    /** atributele utilizate */
    private HashMap<String, Integer> priority; /**  */
    private HashMap<String, String> variable; /** numele variabilelor declarate la inceputul programului  */
    /** folosit pentru a a retine exceptiilele care apar in mementul compilari */
    private LinkedList<Exception> exceptions;

    private Stack<ASTNode> statement; /** stiva cu instructiuni */
    private Stack<ASTNode> expression; /** stiva in care se retin sub-expresi cand se constrieste AST pt o expresie */
    private Stack<String> operators; /** stiva cu opratori, plus paranteza deschisa */

    private MainNode program;

        /** metodele */
    public HashMap<String, String> getVariable(){
        return variable;
    }


    public LinkedList<Exception> getExceptions(){
        return exceptions;
    }

    public MainNode getProgram(){
        return program;
    }

    /**
     * functia este utilizata in cazul in care expresia este forma a + a + a
     * adica cand terminam de scant o expresie si stiva cu operatori nu este goala
     * este algoritmul aplicat in cazul in care se gaseste o paranteza inchisa
     * daca expresia este corect parantezata in acest caz nu se va afla nici o paranteza deschisa in operators
     */
    private void reduceExpr() {
        while (!operators.isEmpty()) {
            String op = operators.pop();
            ASTNode right = expression.pop();
            ASTNode left = null;

            if (!op.equals("!")) {
                left = expression.pop();
            }

            expression.push(OperatorNodeFactory.getInstance().getOperator(op, right, left, (yyline + 1)));
        }
    }

    /**
     * metoda care transforma 2 sau mai multe instructiuni (am consiterat constructia if, while si block tot ca o
     *  instructiune) creand acel arbore de secvente descris in enunt
     * @param stmnt stiva cu instrcutiuni
     * @return radacina arborelui creat
     */
    private ASTNode reduceStmt(Stack<ASTNode> stmnt){
        while (stmnt.size() > 1) {
            ASTNode second = stmnt.pop();
            ASTNode first = stmnt.pop();

            SequenceNode sequenceNode = new SequenceNode(first, second);
            stmnt.push(sequenceNode);
        }

        if (stmnt.isEmpty()) {
            return null;
        } else {
            return stmnt.pop();
        }
    }
%}

/**
 * starile in care se poate afla DFA
   YYINITIAL -> starea implicita, este starea initial
   VARIABLE -> starea cand sunt citite varibile declarate la inceputul programului
   ASSIGMENT -> cand i se atribui unei varibile o expresie aritmetica (doar face tranzitia catre EXPRESSION)
   EXPRESSION -> starea cand este construit AST pentru o expresie (de orice tip)
 */

%state VARIABLE ASSIGMENT EXPRESSION

/**expresile regulate */
newLine = \n|\r
whiteSpace = \t|" "
number = [1-9][0-9]* | 0
string = [a-z]+
var = {string}
bool = true|false

/** keyword -urile limbajului */
int = int
if = "if"
else = "else"
while = while

/** parantezele */
open_par = "("
close_par = ")"
open_block = "{"
close_block = "}"

/** operatori */
div = "/"
plus = "+"
gt = ">"
neg = "!"
and = "&&"
op = {div}|{plus}|{gt}|{neg}|{and}

semicolon = ;
comma = ,
assigment = =

%%

{newLine} { }

{whiteSpace} {}

<YYINITIAL> {
    {int} {
            /** se incepe citirea. retinirea variabilelor */
            yybegin(VARIABLE);
          }

   {if} {
          /** cand am gasit un if pune un IfNode si trecem la realizarea AST pentru conditie */
          this.statement.push(new IfNode());
          this.yybegin(EXPRESSION);
      }

   {else} {
          if (!(this.statement.peek() instanceof IfNode)) {
            System.out.println("EROOOOOOR!!!");
          }
      }

   {while} {
          this.statement.push( new WhileNode());
          this.yybegin(EXPRESSION);
      }


    {var} {
        /**  se verifica daca exista*/
        if (!this.variable.containsKey(yytext())) {
            this.exceptions.add(new UnassignedVar("UnassignedVar " + (yyline + 1)));
        }

        this.statement.push(new VarNode(yytext()).setNumberLine(yyline + 1));
        this.yybegin(ASSIGMENT);
      }

   {open_block} {
          /** "inceputul unui block" */
          this.statement.push(new OpenBlock());
      }

   {close_block} {
          /** mai intai extragem toate intructiunile pana la prima acolada deschisa, pentru a pastra ordinea din
          statement am folosit doua stive */

          Stack<ASTNode> aux = new Stack<>();
          Stack<ASTNode> blockStmnt = new Stack<>();

          while ( !(this.statement.peek() instanceof OpenBlock) ) {
                aux.push(statement.pop());
          }

          this.statement.pop(); // { extragem OpenBlock

          while (!aux.isEmpty()) {
                blockStmnt.push(aux.pop());
          }

          BlockNode blockNode = new BlockNode(reduceStmt(blockStmnt));

          if (this.statement.isEmpty()) {
              this.statement.push(blockNode);
          }else {
              ASTNode astNode = this.statement.pop();

              /** poate sa fie block-ul care apartine unui if, while */
              if (astNode instanceof IfNode) {

                  if ( ((IfNode) astNode).getThenBlock() == null) {
                    ((IfNode) astNode).setThenBlock(blockNode);
                  } else {
                    ((IfNode) astNode).setElseBlock(blockNode);
                  }
                    this.statement.push(astNode);

                } else if (astNode instanceof WhileNode) {
                    ((WhileNode)astNode).setBody(blockNode);
                    this.statement.push(astNode);

                } else {
                    this.statement.push(astNode);
                    this.statement.push(blockNode);
                }
          }


      }

}

/** cand citim variblile */
<VARIABLE> {
    {var} { this.variable.put(yytext(), null); }

    {comma} { }

    /** am terminat de citit varibilele  */
    {semicolon} { this.yybegin(YYINITIAL); }
}

<ASSIGMENT> {
    {assigment} {
          this.yybegin(EXPRESSION);
      }
}

<EXPRESSION> {
   {bool} {
          this.expression.push(new BoolNode(yytext()));
      }


    {var} {
          /** verificare daca exista */
          if (!this.variable.containsKey(yytext())) {
            this.exceptions.add(new UnassignedVar("UnassignedVar " + (yyline + 1)));
          }

          this.expression.push(new VarNode(yytext()).setNumberLine(yyline + 1));
      }

    {number} {
          this.expression.push(new IntNode(yytext()).setNumberLine(yyline + 1));
      }

    {open_par} {
          this.operators.push(yytext());
      }

    {op} {
          if (this.operators.isEmpty()) {
            this.operators.push(yytext());
          }else {
            
            while (!operators.isEmpty() && priority.get(operators.peek()) <= priority.get(yytext())) {
                String op = operators.pop();
                ASTNode right = expression.pop();
                ASTNode left = null;

                if (!op.equals("!")) {
                    left = expression.pop();
                }

                expression.push(OperatorNodeFactory.getInstance().getOperator(op, right, left, (yyline + 1)));
            }

            operators.push(yytext());
          }
      }

    {close_par} {
            /** contruirea subexpresiei din () */
            while (!operators.peek().equals("(")) {
                String op = operators.pop();
                ASTNode right = expression.pop();
                ASTNode left = null;

                if (!op.equals("!")) {
                    left = expression.pop();
                }

                expression.push(OperatorNodeFactory.getInstance().getOperator(op, right, left, (yyline + 1)));
            }

            operators.pop(); /** paranteza : ( */
            ASTNode exp = expression.pop();
            expression.push(new BracketNode(exp));
      }

    {semicolon} {
            reduceExpr();

            ASTNode var = statement.pop();
            ASTNode exp = expression.pop();
            AssignmentNode assignmentNode = new AssignmentNode(var, exp);
            statement.push(assignmentNode);

            yybegin(YYINITIAL);
        }

    {open_block} {
              /** inseamna ca expresia este conditia unui if sau while */
              reduceExpr();

              ASTNode node = this.statement.pop();
              BracketNode condition = new BracketNode( ((BracketNode)this.expression.pop()).getChild() );

              if (node instanceof  IfNode) {
                ((IfNode)node).setCondition(condition);
              } else if (node instanceof WhileNode) {
                ((WhileNode)node).setCondition(condition);
              } else {
                System.exit(-1);
              }
              this.statement.push(node);

              this.statement.push(new OpenBlock());
              yybegin(YYINITIAL);
          }
}
<<EOF>> {
          program = new MainNode(reduceStmt(this.statement));

          return 0;
      }

. { }
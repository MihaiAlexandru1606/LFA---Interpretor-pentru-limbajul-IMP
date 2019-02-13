grammar IMPGrammar;

/** grammar */

program : varDeclaration main;

main : stmt
     | sequence
     ;


sequence : leftStmt=stmt rightStmt=stmt
         | nextStmt=stmt sequence
         ;

stmt : block
     | assignment
     | ifStmt
     | whileStmt
     ;

block : OPEN_BLOCK stmt CLOSE_BLOCK
      | OPEN_BLOCK sequence CLOSE_BLOCK
      | OPEN_BLOCK CLOSE_BLOCK
      ;

assignment : VAR ASSIGNMENT arithmeticExpr SEMICOLON ;

arithmeticExpr : sumExpr;

sumExpr : left=sumExpr PLUS right=divExpr
        | next=divExpr
        ;

divExpr : left=divExpr DIV right=atomNum
        | next=atomNum
        ;

atomNum : integer=NUMBER
        | variable=VAR
        | OPEN_PAR arithmeticExpr CLOSE_PAR
        ;

boolExpr : andExpr;

andExpr : left=andExpr AND right=notExpr
        | next=notExpr
        ;

notExpr : NOT current=atomBool
        | next=atomBool
        ;

atomBool : boolVal=BoolConstant
         | left=arithmeticExpr GT right=arithmeticExpr
         | OPEN_PAR boolExpr CLOSE_PAR
         ;

varDeclaration : INT VAR (',' VAR)* SEMICOLON ;

ifStmt : IF boolExpr thenBlock=block ELSE elseBlock=block;

whileStmt : WHILE boolExpr block;

/** lexer */
/** keyword */
IF           : 'if';
ELSE         : 'else';
WHILE        : 'while';

INT          : 'int';

/**expresile regulate */
BoolConstant : 'true' | 'false';
NUMBER       : [0-9]+ ;
PLUS         : '+';
DIV          : '/';
VAR          : [a-z]+;


GT           : '>';
AND          : '&&';
NOT          : '!';

/** parantezele */
OPEN_PAR     : '(';
CLOSE_PAR    : ')';
OPEN_BLOCK   : '{';
CLOSE_BLOCK  : '}';

ASSIGNMENT    : '=';
SEMICOLON    : ';';


WHITESPACE   : [ \n\t] -> skip;
OTHER        : . -> skip;
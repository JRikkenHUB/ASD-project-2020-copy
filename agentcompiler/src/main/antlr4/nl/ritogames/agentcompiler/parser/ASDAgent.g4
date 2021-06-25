grammar ASDAgent;

//--- LEXER: ---
//All whitespace is skipped
WS: [ \t\r\n]+ -> skip;
// IF support:
IF: 'if';
ELSE: 'else';

OPEN_BRACE: '{';
CLOSING_BRACE: '}';

// Keywords:
SET: 'set';
BEHAVIOR: 'behavior';
DEFAULT: 'default';
ARTIFACT: 'artifact' | 'weapon' | 'flag' | 'armor';
UNIT: 'monster' | 'enemy' | 'teammate' | 'my';
ATTRIBUTE: 'strength' | 'health' | 'defense';
STATE: 'near' | 'attacking';
VARIABLE: 'gamemode' | 'playercount';

//Literals
PERCENTAGE: [0-9]+ '%';
SCALAR: [0-9]+;

// Operators
GT: '>'; // is higher than
LT: '<'; // is lower than
AND: '&'; //and
OR: '|'; //or
EQUALS: '='; //is
HAS: 'has';

IDENTIFIER: [a-z]+; // Everything will be converted to lowercase during pre-processing

//--- PARSER: ---
agentDefinition: defaultBehavior behavior+ EOF;

defaultBehavior: DEFAULT OPEN_BRACE body CLOSING_BRACE;
behavior: BEHAVIOR IDENTIFIER OPEN_BRACE body CLOSING_BRACE;
behaviorCall: SET IDENTIFIER | SET DEFAULT;

body: (ifStatement | behaviorCall | instruction)+;

ifStatement: IF expression OPEN_BRACE body CLOSING_BRACE elseClause?;
elseClause: ELSE OPEN_BRACE body CLOSING_BRACE;

unit: UNIT;
attribute: ATTRIBUTE;
unitAttribute: unit attribute;
state: STATE;
artifact: ARTIFACT;

variableReference: VARIABLE;

instruction: IDENTIFIER+ (unit | artifact)
            | IDENTIFIER IDENTIFIER;

literal: SCALAR #scalarLiteral
        | PERCENTAGE #percentageLiteral
        | IDENTIFIER #stringLiteral;

expression: expression AND expression #andExpression
            | expression OR expression #orExpression
            | (unitAttribute | variableReference) GT (unitAttribute | literal) #greaterThanExpression
            | (unitAttribute | variableReference) LT (unitAttribute | literal) #lowerThanExpression
            | (unitAttribute | variableReference) EQUALS (unitAttribute | literal) #equalsExpression
            | (unit | artifact) EQUALS state #stateExpression
            | unit HAS artifact #hasExpression;
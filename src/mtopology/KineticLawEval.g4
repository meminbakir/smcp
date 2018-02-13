//This class used for evaluation string expression and getting their result
//Specifically it is used for calculation weight of graphs which are string expression represented in MathML, and we use this class to get
//double value
grammar KineticLawEval;

eval
:
	expression
;
/* Addition and subtraction have the lowest precedence. */
expression
:
	'(' expression ')' # Parenthesis
	| expression operator =
	(
		 '*'
		| '/'
		|'%'
	) expression # MultiplyDivide
	| expression operator =
	(
		'+'
		| '-'
	) expression # AddSubtract
	| numeric # NoOperator
;

numeric
:
	IDENTIFIER
	| NUMBER
;

IDENTIFIER
:
	[a-zA-Z_] [a-zA-Z_0-9]*
;

NUMBER
:
	(
		'-'
	)?
	(
		'0' .. '9'
	)+
	(
		'.'
		(
			'0' .. '9'
		)+
	)?
;

MUL
:
	'*'
;

DIV
:
	'/'
;

MOD
:
	'%'
;

ADD
:
	'+'
;

SUB
:
	'-'
;

MULTILINECOMMENT
:
	'/*' .*? '*/' -> skip
;

LINECOMMENT
:
	'//' .+?
	(
		'\n'
		| EOF
	) -> skip
;

WS
:
	[ \r\t\u000C\n]+ -> skip
;
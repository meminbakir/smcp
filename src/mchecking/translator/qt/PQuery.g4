grammar PQuery;

//TODO: nested and bounded queries can be added

pQuery
:
	(
		inequalityQuery
		| questionMark
	)*
;

inequalityQuery
:
	(
		WITH_PROBABILITY probability
	)? query
;

WITH_PROBABILITY
:
	'with probability'
	| 'WITH PROBABILITY'
	//| 'PR'
	//| 'Pr'
//	| 'pr'
;

questionMark
:
	WHAT_PROBABILITY query
;

WHAT_PROBABILITY
:
	'what is the probability of'
	| 'WHAT IS THE PROBABILITY OF'
	| '=?'
;

query
:
	unaryPattern
	| binaryPattern
;

unaryPattern
:
	notPattern
	| eventually
	| always
	| next
	| never
	| infinitelyOften
	| steadyState
;

binaryPattern
:
	until
	| weakUntil
	| release
	| follows
	| precedes
;

//!(query)
notPattern
:
	NOT query
	| NOT '(' query ')'
;

// F(a), equivalent to P_{<>p} [true U a]

eventually
:
	EVENTUALLY expr
;

EVENTUALLY
:
	'eventually'
	| 'EVENTUALLY'
//	| 'F'
;

// G(a) equivalent to !F(!a) also equivalent to P_{!<>(1-p)} [F !(a)], It also equivalent to [false R a]

always
:
	ALWAYS expr
;

ALWAYS
:
	'always'
	| 'ALWAYS'
//	| 'G'
;

next
:
	NEXT expr
;

//X(a)
NEXT
:
	'next'
	| 'NEXT'
//	| 'X'
;

// !(F (a)) equivalent to P_{!<>(1-p)} [F (a)]

never
:
	NEVER expr
;

NEVER
:
	'never'
	| 'NEVER'
;
//GF(a)

infinitelyOften
:
	INFINITELY_OFTEN expr
;

INFINITELY_OFTEN
:
	'infinitely-often'
	| 'INFINITELY-OFTEN'
;

//FG(a)

steadyState
:
	STEADY_STATE expr
;

STEADY_STATE
:
	'steady-state'
	| 'STEADY-STATE'
//	| 'S'
;

until
:
	expr UNTIL expr
;
// [a U b]

UNTIL
:
	'until'
	| 'UNTIL'
//	| 'U'
;
//(a W b) equivalent to !(!b U !(a | b)) equivalent to (b R (a | b))

weakUntil
:
	expr WEAK_UNTIL expr
;

WEAK_UNTIL
:
	'weak-until'
	| 'WEAK-UNTIL'
//	| 'W'
;

//(a R b), is equivalent to !(!a U !b) also equivalent to P_{!<>(1-p)}[!a U !b]

release
:
	expr RELEASE expr
;

RELEASE
:
	'release'
	| 'RELEASE'
//	| 'R'
;

//second(b) FOLLOWS first(a) is translated to G(first -> F second)

follows
:
	expr FOLLOWS expr
;

FOLLOWS
:
	'follows'
	| 'FOLLOWS'
;

//e.g., A PRECEDES B in the alphabet, namely 1st precedes 2nd which is equivalent to (!b W a) which is also equivalent to  P_{!<>(1-p)}[b U (!a & b)]

precedes
:
	expr PRECEDES expr
;

PRECEDES
:
	'precedes'
	| 'PRECEDES'
;

expr
:
	NOT expr # notExpr
	| expr operator =
	(
		AND
		| OR
		| IMPLIES
	) expr # binaryLogicalExpr
	| boolExpr # noParanBoolExpr
	| '(' expr ')' # paranthesesExpr
;

boolExpr
:
	logicalEntity
	| numericExp RELATION numericExp
	| '(' boolExpr ')'
;

logicalConstant
:
	TRUE
	| FALSE
;

FALSE
:
	'false'
	| 'FALSE'
;

TRUE
:
	'true'
	| 'TRUE'
;

logicalEntity
:
	logicalConstant
	| IDENTIFIER //boolean variable

;

NOT
:
	'NOT'
	| 'not'
	| '!'
;

IMPLIES
:
	'implies'
	| 'IMPLIES'
;

OR
:
	'or'
	| 'OR'
;

AND
:
	'and'
	| 'AND'
;

probability
:
	RELATION numericExp
;

RELATION
:
	'>='
	| '<='
	| '>'
	| '<'
	| '='
	| '!='
;

numericExp
:
	numericExp op =
	(
		'*'
		| '/'
		| '%'
	) numericExp # multNumericExpr
	| numericExp op =
	(
		'+'
		| '-'
	) numericExp # addNumericExpr
	| numeric # noOpNumericExpr
	| '(' numericExp ')' # parensNumericExpr
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
// This is lexer for interpreter
//

lexer grammar CommandTreeLexer;

options {

   language = Java;
   superClass = AbstractTLexer;
}

@header {
    package edu.agh.commandgrammar;
    import edu.agh.commandgrammar.AbstractTLexer;
}

T_BETWEENESS    :   'betweeness' ;
T_CLOSENESS    :   'closeness' ;
T_PAGERANK     :   'page rank' ;
T_EIGENVECTOR  :   'eigenvector' ;

T_DENSITY : 'density';
T_COHESION : 'cohesion';

T_FORALL : 'for all';
T_LIST : 'list';
T_FRIENDS : 'friends';
T_MEMBERS : 'members';
T_USERS : 'users';
T_GROUPS : 'groups';
T_DRAW_GRAPH : 'draw graph';

T_HELP : 'help';
T_EXIT : 'quit' | 'exit' | 'close';

T_BYID  : 'by id' | 'of id';

T_BYGROUP  : 'by group' | 'of group';

NICK  :	('a'..'z'|'A'..'Z'|'_'|'-') ('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'-')*
	|   '*' ;

INT :	'0'..'9'+ ;

WS : WHITESPACE {};

fragment
WHITESPACE  :   ( 
	' ' | '\t'
        )
    ;
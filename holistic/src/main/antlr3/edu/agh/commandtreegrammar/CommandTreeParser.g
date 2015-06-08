// This is parser for J48 grammar
//

parser grammar CommandTreeParser;

options {

    language  = Java;
    output    = AST;
    superClass = AbstractTParser;
    tokenVocab = CommandTreeLexer;
	rewrite = true;

	//In case of trouble -shut off:
	ASTLabelType = CommonTree;
}

// Some tokens for tree rewrites
//
tokens {
    BETWEENESS;
  	CLOSENESS;
  	PAGERANK;
  	EIGENVECTOR;
    METRIC_BYID;
	DRAW_GRAPH;
    HELP;
    EXIT;
    LIST_USERS;
    LIST_FRIENDS_BYID;
    LIST_GROUPS_BYID;
    LIST_GROUPS;
    DRAW_GRAPH_GROUP;
    METRIC_BYGROUP;
    LIST_MEMBERS;
    GROUP_METRIC;
    DENSITY;
    COHESION;
    METRIC_FORALL;
}

@header {
    package edu.agh.commandgrammar;
    import edu.agh.commandgrammar.AbstractTLexer;
    import edu.agh.commandgrammar.AbstractTParser;
}

output : 
metric WS T_BYID WS name=INT
-> ^(METRIC_BYID metric $name)
 | T_DRAW_GRAPH WS (T_USERS | T_FORALL)
-> ^(DRAW_GRAPH)
 | T_DRAW_GRAPH WS T_BYGROUP WS name=NICK
-> ^(DRAW_GRAPH_GROUP $name)
 | T_LIST WS T_USERS
-> ^(LIST_USERS)
 | T_LIST WS T_GROUPS
-> ^(LIST_GROUPS)
 | T_LIST WS T_MEMBERS WS T_BYGROUP WS name=NICK
-> ^(LIST_MEMBERS $name)
 | metric WS T_BYGROUP WS name=NICK
-> ^(METRIC_BYGROUP metric $name)
 | group_metric WS T_FORALL
-> ^(GROUP_METRIC group_metric)
 | metric WS T_FORALL
-> ^(METRIC_FORALL metric)
 | T_LIST WS T_FRIENDS WS T_BYID WS name=INT
-> ^(LIST_FRIENDS_BYID $name)
 | T_LIST WS T_GROUPS WS T_BYID WS name=INT
-> ^(LIST_GROUPS_BYID $name)
 | T_EXIT
-> ^(EXIT)
 | T_HELP
-> ^(HELP)
 ;

metric : 
  T_BETWEENESS
 -> BETWEENESS
 | T_CLOSENESS
 -> CLOSENESS
 | T_PAGERANK
 -> PAGERANK
 | T_EIGENVECTOR
 -> EIGENVECTOR
 ;

group_metric :
 T_DENSITY
 -> DENSITY
 | T_COHESION
 -> COHESION
;

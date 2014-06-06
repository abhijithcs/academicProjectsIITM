%{
#include "p1.tab.h"
#include <stdio.h>
#include <stdlib.h>
%}

%%

"class"			{ yylval.set = strdup(yytext); return CLA; }
"public"		{ yylval.set = strdup(yytext); return PUB; }
"static"		{ yylval.set = strdup(yytext); return STA; }
"void"			{ yylval.set = strdup(yytext); return VOI; }
"main"			{ yylval.set = strdup(yytext); return MAI; }
"String"		{ yylval.set = strdup(yytext); return STR; }
"System.out.println"	{ yylval.set = strdup(yytext); return SOP; }
"extends"		{ yylval.set = strdup(yytext); return EXT; }
"int"			{ yylval.set = strdup(yytext); return INT; }
"boolean"		{ yylval.set = strdup(yytext); return BOO; }
"if"			{ yylval.set = strdup(yytext); return IF;  }
"else"			{ yylval.set = strdup(yytext); return ELS; }
"while"			{ yylval.set = strdup(yytext); return WHI; }
"length"		{ yylval.set = strdup(yytext); return LEN; }
"true"			{ yylval.set = strdup(yytext); return TRU; }
"false"			{ yylval.set = strdup(yytext); return FAL; }
"this"			{ yylval.set = strdup(yytext); return THI; }
"new"			{ yylval.set = strdup(yytext); return NEW; }
"#define"		{ yylval.set = strdup(yytext); return DEF; }
"return"		{ yylval.set = strdup(yytext); return RET; }
")"			{ yylval.set = strdup(yytext); return RRB; }
"("			{ yylval.set = strdup(yytext); return RLB; }
"}"			{ yylval.set = strdup(yytext); return CRB; }
"{"			{ yylval.set = strdup(yytext); return CLB; }
","			{ yylval.set = strdup(yytext); return COM; }
"]"			{ yylval.set = strdup(yytext); return SRB; }
"["			{ yylval.set = strdup(yytext); return SLB; }
";"			{ yylval.set = strdup(yytext); return SEM; }
"!"			{ yylval.set = strdup(yytext); return EXC; }
"="			{ yylval.set = strdup(yytext); return EQU; }
"&"			{ yylval.set = strdup(yytext); return AND; }
"<"			{ yylval.set = strdup(yytext); return LES; }
"+"			{ yylval.set = strdup(yytext); return ADD; }
"-"			{ yylval.set = strdup(yytext); return SUB; }
"*"			{ yylval.set = strdup(yytext); return MUL; }
"/"			{ yylval.set = strdup(yytext); return DIV; }
"."			{ yylval.set = strdup(yytext); return DOT; }

[a-zA-Z][a-zA-Z|'_']*[0-9]*	{ yylval.set = strdup(yytext); return IDE; }
[0-9]+ 				{ yylval.set = strdup(yytext); return INTS; }

"//".*				{}
.      				{}
%%

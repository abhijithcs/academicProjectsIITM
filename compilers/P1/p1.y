%{
#include <stdio.h>
#include <string.h>
int yydebug;
char* content;
%}

%union
{
char* set;
}

%token<set> CLA PUB STA VOI MAI STR SOP EXT INT BOO IF ELS WHI LEN INTS TRU FAL THI NEW DEF IDE RET RRB RLB CRB CLB COM SRB SLB SEM EXC EQU AND LES ADD SUB MUL DIV DOT
%type<set> Goal MainClass TypeDeclaration MethodDeclaration Type Statement Expression PrimaryExpression MacroDefinition MacroDefExpression Identifier MacroDefinitionStar TypeDeclarationStar TypeIdentifierSemiStar TICTIStarQuestion CommaTypeIdentifierStar StatementStar MethodDeclarationStar CommaIdentifierStar MacroDefStatement ICIStarQuestion CommaExpressionStar ECEStarQuestion
%nonassoc assign
%left AND LES ADD SUB MUL DIV DOT SLB 

%%

// Goal			::= (MacroDefinition)* MainClass ( TypeDeclaration )* <EOF>

Goal			: MacroDefinitionStar MainClass TypeDeclarationStar {asprintf(&$$,"%s %s %s",$1,$2,$3); printf("\nHERE!!\n\n%s",$$);printf("\n****************************\nSUCCESS!!!!!\n*****************************\n");content=$$;}
			;

MacroDefinitionStar	: MacroDefinitionStar MacroDefinition {asprintf(&$$,"%s %s",$1,$2);}
			| {$$="";}
			;

TypeDeclarationStar	: TypeDeclarationStar TypeDeclaration {asprintf(&$$,"%s %s",$1,$2);}
			| {$$="";}
			;

//MainClass		::= class Identifier { public static void main ( String [] Identifier ) { System.out.println ( Expression ); } }

MainClass		: CLA Identifier CLB PUB STA VOI MAI RLB STR SLB SRB Identifier RRB CLB SOP RLB Expression RRB SEM CRB CRB {asprintf(&$$,"%s %s\n%s\n %s %s %s %s%s%s%s%s %s%s\n%s\n%s%s%s%s%s\n%s\n%s\n",$1,$2,$3,$4,$5,$6,$7,$8,$9,$10,$11,$12,$13,$14,$15,$16,$17,$18,$19,$20,$21);}
			;

//TypeDeclaration	::= class Identifier { ( Type Identifier ;)* ( MethodDeclaration )* }
//			|   class Identifier extends Identifier { ( Type Identifier;)* ( MethodDeclaration )* }

TypeDeclaration		: CLA Identifier CLB TypeIdentifierSemiStar MethodDeclarationStar CRB {asprintf(&$$,"%s %s \n%s\n%s %s\n%s\n",$1,$2,$3,$4,$5,$6);}
			| CLA Identifier EXT Identifier CLB TypeIdentifierSemiStar MethodDeclarationStar CRB {asprintf(&$$,"%s %s %s %s\n%s\n %s %s\n%s\n",$1,$2,$3,$4,$5,$6,$7,$8);}
			;

TypeIdentifierSemiStar	: TypeIdentifierSemiStar Type Identifier SEM {asprintf(&$$,"%s %s %s%s\n",$1,$2,$3,$4);}
			| {$$="";}
			;

MethodDeclarationStar	: MethodDeclarationStar MethodDeclaration {asprintf(&$$,"%s %s",$1,$2);}
			| {$$="";}
			;
			
//MethodDeclaration	::= public Type Identifier ( ( Type Identifier (, Type Identifier)*)? ) { ( Type Identifier ;)* ( Statement )* return Expression ; }

MethodDeclaration	: PUB Type Identifier RLB TICTIStarQuestion RRB CLB TypeIdentifierSemiStar StatementStar RET Expression SEM CRB {asprintf(&$$,"%s %s %s%s%s%s \n%s\n %s %s %s %s%s\n%s\n",$1,$2,$3,$4,$5,$6,$7,$8,$9,$10,$11,$12,$13);}
			;

TICTIStarQuestion	: Type Identifier CommaTypeIdentifierStar {asprintf(&$$,"%s %s %s",$1,$2,$3);}
			| {$$="";}
			;

CommaTypeIdentifierStar	: CommaTypeIdentifierStar COM Type Identifier {asprintf(&$$,"%s %s %s %s",$1,$2,$3,$4);}
			| {$$="";}
			;

StatementStar		: Statement StatementStar {asprintf(&$$,"%s %s",$1,$2);}
			| {$$="";}
			;

/*Type			::= int [ ]
			| boolean
			| int
			| Identifier 		*/

Type			: INT SLB SRB {asprintf(&$$,"%s%s%s",$1,$2,$3);}
			| BOO {asprintf(&$$,"%s",$1);} 
			| INT {asprintf(&$$,"%s",$1);} 
			| Identifier {asprintf(&$$,"%s",$1);} 
			;

/*Statement		::= { ( Statement )* }
			| System.out.println ( Expression );
			| Identifier = Expression ;
			| Identifier [ Expression ] = Expression ;
			| if ( Expression ) Statement
			| if ( Expression ) Statement else Statement
			| while ( Expression ) Statement
			| Identifier ( (Expression (, Expression )*)?); /* Macro stmt call */		

Statement		: CLB StatementStar CRB {asprintf(&$$,"\n%s\n%s\n%s",$1,$2,$3);}
			| SOP RLB Expression RRB SEM {asprintf(&$$,"%s%s%s%s%s\n",$1,$2,$3,$4,$5);}
			| Identifier EQU Expression SEM {asprintf(&$$,"%s %s %s%s\n",$1,$2,$3,$4);}
			| Identifier SLB Expression SRB EQU Expression SEM {asprintf(&$$,"%s %s%s%s %s %s%s\n",$1,$2,$3,$4,$5,$6,$7);}
			| IF RLB Expression RRB Statement {asprintf(&$$,"%s %s%s%s %s",$1,$2,$3,$4,$5);}
			| IF RLB Expression RRB Statement ELS Statement {asprintf(&$$,"%s %s%s%s %s %s %s",$1,$2,$3,$4,$5,$6,$7);}
			| WHI RLB Expression RRB Statement {asprintf(&$$,"%s %s%s%s %s",$1,$2,$3,$4,$5);}
			| Identifier RLB ECEStarQuestion RRB SEM {asprintf(&$$,"%s %s%s%s%s\n",$1,$2,$3,$4,$5);}
			;

ECEStarQuestion		: Expression CommaExpressionStar {asprintf(&$$,"%s %s",$1,$2);}
			| {$$="";}
			;

CommaExpressionStar	: CommaExpressionStar COM Expression {asprintf(&$$,"%s %s %s",$1,$2,$3);}
			| {$$="";}
			;

/*Expression		::= PrimaryExpression & PrimaryExpression
			| PrimaryExpression < PrimaryExpression
			| PrimaryExpression + PrimaryExpression
			| PrimaryExpression - PrimaryExpression
			| PrimaryExpression * PrimaryExpression
			| PrimaryExpression / PrimaryExpression
			| PrimaryExpression [ PrimaryExpression ]
			| PrimaryExpression . length
			| PrimaryExpression
			| PrimaryExpression . Identifier ( (Expression (, Expression )*)? )
			| Identifier ( (Expression (, Expression )*)? )/* Macro expr call */

Expression		: PrimaryExpression AND PrimaryExpression {asprintf(&$$,"%s%s%s",$1,$2,$3);}
			| PrimaryExpression LES PrimaryExpression {asprintf(&$$,"%s%s%s",$1,$2,$3);}
			| PrimaryExpression ADD PrimaryExpression {asprintf(&$$,"%s%s%s",$1,$2,$3);}
			| PrimaryExpression SUB PrimaryExpression {asprintf(&$$,"%s%s%s",$1,$2,$3);}
			| PrimaryExpression MUL PrimaryExpression {asprintf(&$$,"%s%s%s",$1,$2,$3);}
			| PrimaryExpression DIV PrimaryExpression {asprintf(&$$,"%s%s%s",$1,$2,$3);}
			| PrimaryExpression SLB PrimaryExpression SRB {asprintf(&$$,"%s %s%s%s",$1,$2,$3,$4);}
			| PrimaryExpression DOT LEN {asprintf(&$$,"%s%s%s",$1,$2,$3);}
			| PrimaryExpression %prec assign {asprintf(&$$,"%s",$1);}
			| PrimaryExpression DOT Identifier RLB ECEStarQuestion RRB {asprintf(&$$,"%s%s%s%s%s%s",$1,$2,$3,$4,$5,$6);}
			| Identifier RLB ECEStarQuestion RRB {asprintf(&$$,"%s%s%s%s",$1,$2,$3,$4);}
			;

/*PrimaryExpression	::= <INTEGER_LITERAL>
			| true
			| false
			| Identifier
			| this
			| new int [ Expression ]
			| new Identifier ( )
			| ! Expression
			| ( Expression )			*/

PrimaryExpression	: INTS {asprintf(&$$,"%s",$1);}
			| TRU {asprintf(&$$,"%s",$1);}
			| FAL {asprintf(&$$,"%s",$1);}
			| Identifier {asprintf(&$$,"%s",$1);}
			| THI {asprintf(&$$,"%s",$1);}
			| NEW INT SLB Expression SRB {asprintf(&$$,"%s %s %s%s%s",$1,$2,$3,$4,$5);}
			| NEW Identifier RLB RRB {asprintf(&$$,"%s %s%s%s",$1,$2,$3,$4);}
			| EXC Expression {asprintf(&$$,"%s %s",$1,$2);}
			| RLB Expression RRB {asprintf(&$$,"%s%s%s",$1,$2,$3);}
			;

/*MacroDefinition	::= MacroDefExpression
			| MacroDefStatement			*/

MacroDefinition		: MacroDefExpression {asprintf(&$$,"%s",$1);}
			| MacroDefStatement {asprintf(&$$,"%s",$1);}
			;

//MacroDefStatement	::= #define Identifier ( (Identifier (, Identifier )*)? ) { ( Statement )* }

MacroDefStatement	: DEF Identifier RLB ICIStarQuestion RRB CLB StatementStar CRB {asprintf(&$$,"%s %s%s%s%s\n%s\n%s\n%s\n",$1,$2,$3,$4,$5,$6,$7,$8);}
			;
ICIStarQuestion		: Identifier CommaIdentifierStar {asprintf(&$$,"%s %s",$1,$2);}
			| {$$="";}
			;

CommaIdentifierStar	: CommaIdentifierStar COM Identifier {asprintf(&$$,"%s %s %s",$1,$2,$3);}
			| {$$="";}
			;

//MacroDefExpression	::= #define Identifier ( (Identifier (, Identifier )*)? ) ( Expression )

MacroDefExpression	: DEF Identifier RLB ICIStarQuestion RRB RLB Expression RRB {asprintf(&$$,"%s %s%s%s%s %s%s%s\n",$1,$2,$3,$4,$5,$6,$7,$8);}
			;

//Identifier		::= <IDENTIFIER>

Identifier		: IDE {asprintf(&$$,"%s",$1);}  
			;

%%
main (int argc, char **argv)
{
	yydebug=1;
	yyparse();
/*
	int size,i,j=0,k=0;
	int nA,nB,m=0,n=0;
	int count=0,replace=0,sub;
	char A[20]="";
	char B[20]="";
	
	size=strlen(content);
	char array[size];


	
	for(i=0;i<size;i++)
	{
		if(content[i]=='#')
			count++;			
	}

	printf("*****************************************%d",count);

	for(i=0;i<size;i++)
	{
		if(content[i]=='#')
		{
			while(content[i+8+j]!=')')		
			{
				A[j]=content[i+8+j];
				j++;
			}
			A[j]=content[i+8+j];
			printf("\nWORD:%s",A);
			nA=strlen(A);

			while(content[i+10+j+k]!=')')
			{
				B[k]=content[i+10+j+k];
				k++;
			}		
			B[k]=content[i+10+j+k];
			printf("\nWORD:%s",B);
			nB=strlen(B);		
			break;
		} 

	}
	i=0;

	while (i<size)//for(i=0;i<size;i++)
	{
		if(content[i]!=A[0])
		{
			array[i]=content[i];				
			i++;
		}
		else if(content[i]==A[0])
		{
			for(j=0;j<nA;j++)
			{
				if(content[i+j]==A[j])
					replace=1;
				else
					replace=0;
			}
			if(replace==1)
			{
				for(sub=i;sub<i+nB;sub++)				
				{
					array[sub]=B[n];
					n++;
				}
				n=0;
				i=i+nB;
			}
			else
			{
				array[i]=content[i];
				i++;
			}
		}
	}

printf("\n*****************************************************\n%s\n**************************************\n",array);*/
}

yyerror (char *s)
{
	fprintf(stdout,"%s","Failed to Parse Code.");
}

/**
 * JFlex specification for lexical analysis of a simple demo language.
 * Change this into the scanner for your implementation of MiniJava.
 *
 * CSE 401/M501/P501 18sp
 */


package Scanner;

import java_cup.runtime.Symbol;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;
import java_cup.runtime.ComplexSymbolFactory.Location;
import Parser.sym;

%%
%public
%final
%class scanner
%unicode
%cup
%line
%column

/* The following code block is copied literally into the generated scanner
 * class. You can use this to define methods and/or declare fields of the
 * scanner class, which the lexical actions may also reference. Most likely,
 * you will only need to tweak what's already provided below.
 *
 * We use CUP's ComplexSymbolFactory and its associated ComplexSymbol class
 * that tracks the source location of each scanned symbol.
 */
%{
  /** The CUP symbol factory, typically shared with parser. */
  private ComplexSymbolFactory symbolFactory = new ComplexSymbolFactory();

  /** Initialize scanner with input stream and a shared symbol factory. */
  public scanner(java.io.Reader in, ComplexSymbolFactory sf) {
    this(in);
    this.symbolFactory = sf;
  }

  /**
   * Construct a symbol with a given lexical token, a given
   * user-controlled datum, and the matched source location.
   *
   * @param code     identifier of the lexical token (i.e., sym.<TOKEN>)
   * @param value    user-controlled datum to associate with this symbol
   * @effects        constructs new ComplexSymbol via this.symbolFactory
   * @return         a fresh symbol storing the above-desribed information
   */
  private Symbol symbol(int code, Object value) {
    // Calculate symbol location
    int yylen = yylength();
    Location left = new Location(yyline + 1, yycolumn + 1, yychar);
    Location right = new Location(yyline + 1, yycolumn + yylen, yychar + yylen);
    // Calculate symbol name
    int max_code = sym.terminalNames.length;
    String name = code < max_code ? sym.terminalNames[code] : "<UNKNOWN(" + yytext() + ")>";
    return this.symbolFactory.newSymbol(name, code, left, right, value);
  }

  /**
   * Construct a symbol with a given lexical token and matched source
   * location, leaving the user-controlled value field uninitialized.
   *
   * @param code     identifier of the lexical token (i.e., sym.<TOKEN>)
   * @effects        constructs new ComplexSymbol via this.symbolFactory
   * @return         a fresh symbol storing the above-desribed information
   */
  private Symbol symbol(int code) {
    // Calculate symbol location
    int yylen = yylength();
    Location left = new Location(yyline + 1, yycolumn + 1, yychar);
    Location right = new Location(yyline + 1, yycolumn + yylen, yychar + yylen);
    // Calculate symbol name
    int max_code = sym.terminalNames.length;
    String name = code < max_code ? sym.terminalNames[code] : "<UNKNOWN(" + yytext() + ")>";
    return this.symbolFactory.newSymbol(name, code, left, right);
  }

  /**
   * Convert the symbol generated by this scanner into a string.
   *
   * This method is useful to include information in the string representation
   * in addition to the plain CUP name for a lexical token.
   *
   * @param symbol   symbol instance generated by this scanner
   * @return         string representation of the symbol
   */
   public String symbolToString(Symbol s) {
     // All symbols generated by this class are ComplexSymbol instances
     ComplexSymbol cs = (ComplexSymbol)s; 
     switch(cs.sym){
     	case sym.IDENTIFIER:
     		return "ID(" + (String)cs.value + ")"; 
     	case sym.STRINGLIT:
     		return "STRING(" + (String)cs.value + ")"; 
     	case sym.INTEGERLIT:
     		return "integer(" + (String)cs.value + ")"; 
     	case sym.FLOATLIT:
     		return "float(" + (String)cs.value + ")"; 
     	case sym.error:
     		return "<UNEXPECTED(" + (String)cs.value + ")>"; 
     	default:
     		return cs.getName();
     }
   }
%}

/* Helper definitions */
letter = [a-zA-Z]
digit = [0-9]
eol = [\r\n]
white = {eol}|[ \t]
identifier = {letter} ({letter}|{digit}|_)*
Zero = 0
DecInt = [1-9]{digit}*
OctalInt = 0[0-7]+
HexInt = 0[xX][0-9a-fA-F]+
Integer = ( {Zero} | {DecInt} | {OctalInt} | {HexInt} )[lL]?
Exponent = [eE] [\+\-]? [0-9]+
Float1 = [0-9]+ \. [0-9]+ {Exponent}?
Float2 = \. [0-9]+ {Exponent}?
Float3 = [0-9]+ \. {Exponent}?
Float4 = [0-9]+ {Exponent}
Float = ( {Float1} | {Float2} | {Float3} | {Float4} ) [fFdD]? | {digit} + [fFDd]
input_character = [^\r\n]
string = \" .*\"
%%

/* Token definitions */
/* reserved words (first so that they take precedence over identifiers) */

"class"	{ return symbol(sym.CLASS); }
"main"	{return symbol(sym.MAIN);}
"public"	{ return symbol(sym.PUBLIC); }
"static"	{ return symbol(sym.STATIC); }
"void"	{ return symbol(sym.VOID); }
"extends" { return symbol(sym.EXTENDS); }
"return"	{ return symbol(sym.RETURN); }
"new"	{ return symbol(sym.NEW); }
"private" { return symbol(sym.PRIVATE); } 
"super" { return symbol(sym.SUPER); }
"void" { return symbol(sym.VOID); }

"display" { return symbol(sym.DISPLAY); }
"if" { return symbol( sym.IF ); }
"then" { return symbol( sym.THEN ); }
"else" { return symbol( sym.ELSE ); }
"while" { return symbol( sym.WHILE ); }
"do" { return symbol( sym.DO ); }
"true" { return symbol( sym.TRUE ); }
"false" { return symbol( sym.FALSE ); }
"null" { return symbol( sym.NULLLIT ); }

"String"	{ return symbol(sym.STRING); }
"int"	{ return symbol(sym.INTEGER); }
"float"		{ return symbol(sym.FLOAT);}
"boolean"	{ return symbol(sym.BOOLEAN); }
"System.out.println"	{ return symbol(sym.PRINT); }
"length"	{ return symbol(sym.LENGTH); }
"this"	{ return symbol(sym.THIS); }
"instanceof" { return symbol(sym.INS); }

/* operators */


"=" { return symbol( sym.ASSIGN ); }
"+" { return symbol( sym.PLUS ); }
"-" { return symbol( sym.MINUS ); }
"++" { return symbol( sym.INCRE ); }
"--" { return symbol( sym.DECRE ); }
"*" { return symbol( sym.TIMES ); }
"/" { return symbol( sym.DIVIDE ); }
"%" { return symbol( sym.MOD ); }
"<" { return symbol( sym.LT ); }
"<=" { return symbol( sym.LE ); }
">" { return symbol( sym.GT ); }
">=" { return symbol( sym.GE ); }
"==" { return symbol( sym.EQ ); }
"!=" { return symbol( sym.NE ); }
"&" { return symbol( sym.BITAND ); }
"|" { return symbol( sym.BITOR ); }
"^" { return symbol( sym.BITXOR ); }
"&&" { return symbol( sym.AMPERSAND ); }
"||" { return symbol( sym.AMPERSOR ); }
"."	{ return symbol(sym.DOT); }
"!" { return symbol( sym.EXCLAIM ); }
"~" { return symbol( sym.BITNOT ); }
"~" { return symbol( sym.TILDE ); }


/* delimiters */

"(" { return symbol( sym.LPAREN ); }
")" { return symbol( sym.RPAREN ); }
"{" { return symbol( sym.LEFTCURLY ); }
"}" { return symbol( sym.RIGHTCURLY ); }
"[" { return symbol( sym.LEFTSQ ); }
"]" { return symbol( sym.RIGHTSQ ); }

";"	{ return symbol(sym.SEMICOLON); }
","	{ return symbol(sym.COMMA); }


/* comment */
"/*"~"*/" { }
"//"{input_character}*{eol}? { }

/* identifiers */
{identifier} {
  return symbol(sym.IDENTIFIER, yytext());
}

{string} {return symbol(sym.STRINGLIT, yytext());}

/* whitespace */
{white}+ { /* ignore whitespace */ }

/* integer */
{Integer}  { return symbol(sym.INTEGERLIT, yytext()); }

/* float */
{Float}  { return symbol(sym.FLOATLIT, yytext()); }

/* lexical errors (last so other matches take precedence) */
. {
    System.err.printf(
      "%nUnexpected character '%s' on line %d at column %d of input.%n",
      yytext(), yyline + 1, yycolumn + 1
    );
    return symbol(sym.error, yytext());
  }

<<EOF>> { return symbol(sym.EOF); }

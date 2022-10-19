package LexicalSystem;

// 存放关键字以及其对应的数值
public class Token {
    public final static int IDENFR = 1; // Ident
    public final static int INTCON = 2; // IntConst
    public final static int STRCON = 3; // FormatString
    public final static int MAINTK = 4; // main
    public final static int CONSTTK = 5; // const
    public final static int INTTK = 6; // int
    public final static int BREAKTK = 7; // break
    public final static int CONTINUETK = 8; // continue
    public final static int IFTK = 9; // if
    public final static int ELSETK = 10; // else
    public final static int NOT = 11; // !
    public final static int AND = 12; // &&
    public final static int OR = 13; // ||
    public final static int WHILETK = 14; // while
    public final static int GETINTTK = 15; // getint
    public final static int PRINTFTK = 16; // printf
    public final static int RETURNTK = 17; // return
    public final static int PLUS = 18; // +
    public final static int MINU = 19; // -
    public final static int VOIDTK = 20; // void
    public final static int MULT = 21; // *
    public final static int DIV = 22; // /
    public final static int MOD = 23; // %
    public final static int LSS = 24; // <
    public final static int LEQ = 25; // <=
    public final static int GRE = 26; // >
    public final static int GEQ = 27; // >=
    public final static int EQL = 28; // ==
    public final static int NEQ = 29; // !=
    public final static int ASSIGN = 30; // =
    public final static int SEMICN = 31; // ;
    public final static int COMMA = 32; // ,
    public final static int LPARENT = 33; // (
    public final static int RPARENT = 34; // )
    public final static int LBRACK = 35; // [
    public final static int RBRACK = 36; // ]
    public final static int LBRACE = 37; // {
    public final static int RBRACE = 38; // }
}

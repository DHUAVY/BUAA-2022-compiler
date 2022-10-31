package LexicalSystem;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import java.io.*;
import FileController.FileControl;


public class LexicalAnalysis {

    public static String fileName = FileControl.LexicalSystemFileName;

    static int notesMode = 0; // 是否处于注释状态下。
    static int poi = 0; // 当前读取的字符位置。
    static int len = 0; // 当前需要处理的句子的长度。
    public static int pop = 0; // 当前wordList中记载的单词数量。
    public static Lexical[] wordList = new Lexical[500000]; // 词法分析最后总结出的所有单词。

    public static int judgeResearve( String str ){
        //在关键词表中查找str
        switch(str){
            case "main":
                return Token.MAINTK;
            case "const":
                return Token.CONSTTK;
            case "int":
                return Token.INTTK;
            case "break":
                return Token.BREAKTK;
            case "continue":
                return Token.CONTINUETK;
            case "if":
                return Token.IFTK;
            case "else":
                return Token.ELSETK;
            case "while":
                return Token.WHILETK;
            case "getint":
                return Token.GETINTTK;
            case "printf":
                return Token.PRINTFTK;
            case "return":
                return Token.RETURNTK;
            case "void":
                return Token.VOIDTK;
            default:
                return -1;
        }
    }

    public static String getToken( int token ){ // 根据保留字的值返回对应的字符串。
        switch(token){
            case Token.IDENFR:
                return "IDENFR";
            case Token.INTCON:
                return "INTCON";
            case Token.STRCON:
                return "STRCON";
            case Token.MAINTK:
                return "MAINTK";
            case Token.CONSTTK:
                return "CONSTTK";
            case Token.INTTK:
                return "INTTK";
            case Token.BREAKTK:
                return "BREAKTK";
            case Token.CONTINUETK:
                return "CONTINUETK";
            case Token.IFTK:
                return "IFTK";
            case Token.ELSETK:
                return "ELSETK";
            case Token.NOT:
                return "NOT";
            case Token.AND:
                return "AND";
            case Token.OR:
                return "OR";
            case Token.WHILETK:
                return "WHILETK";
            case Token.GETINTTK:
                return "GETINTTK";
            case Token.PRINTFTK:
                return "PRINTFTK";
            case Token.RETURNTK:
                return "RETURNTK";
            case Token.PLUS:
                return "PLUS";
            case Token.MINU:
                return "MINU";
            case Token.VOIDTK:
                return "VOIDTK";
            case Token.MULT:
                return "MULT";
            case Token.DIV:
                return "DIV";
            case Token.MOD:
                return "MOD";
            case Token.LSS:
                return "LSS";
            case Token.LEQ:
                return "LEQ";
            case Token.GRE:
                return "GRE";
            case Token.GEQ:
                return "GEQ";
            case Token.EQL:
                return "EQL";
            case Token.NEQ:
                return "NEQ";
            case Token.ASSIGN:
                return "ASSIGN";
            case Token.SEMICN:
                return "SEMICN";
            case Token.COMMA:
                return "COMMA";
            case Token.LPARENT:
                return "LPARENT";
            case Token.RPARENT:
                return "RPARENT";
            case Token.LBRACK:
                return "LBRACK";
            case Token.RBRACK:
                return "RBRACK";
            case Token.LBRACE:
                return "LBRACE";
            case Token.RBRACE:
                return "RBRACE";
            default:
                return "";
        }
    }

    public static void writeInFile( String token, int identifyCode, int lineNumber) throws IOException {

//        String identify = "";
        if( notesMode == 1 || token.equals("") || identifyCode == 0){
            return;
        }
        wordList[pop] = new Lexical( token, identifyCode, lineNumber, pop);
        pop++;
//        identify = getToken( identifyCode );
//        if( identify.equals("") ){
//            return;
//        }
//        identify += ' ' + token + ' ' + lineNumber + '\n';
//        if( FileControl.LexicalSystemPrint ){
//            Files.write(Paths.get(fileName), identify.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
//        }
    }

    public static void wrong(){

    }

    public static void lexicalAnalysis(String sentence, int lineNumber) throws IOException {

        poi = 0;
        len = sentence.length();

        while( poi < len ){

            int status = 0;
            char c = '\0';
            StringBuilder token = new StringBuilder();

            do{
                if( poi >= len ){
                    return;
                }
                c = sentence.charAt(poi++);
            }while( Character.isWhitespace(c) );

            if( Character.isLetter(c) || c =='_'){
                if( notesMode == 1 ){
                    continue;
                } // 对于当前字符为字母的情况，如果现在处于注释状态下，可以直接跳过。
                while( Character.isLetter(c) || Character.isDigit(c) || c =='_'){
                    token.append(c);
                    if( poi >= len ){
                        status = judgeResearve(token.toString());
                        if(  status != -1 ){
                            writeInFile(token.toString(), status, lineNumber);
                        }
                        else{
                            writeInFile(token.toString(), Token.IDENFR, lineNumber);
                        }
                        return;
                    }
                    c = sentence.charAt(poi++);
                }
                poi--;
                status = judgeResearve(token.toString());
                if(  status != -1 ){
                    writeInFile(token.toString(), status, lineNumber);
                }
                else{
                    writeInFile(token.toString(), Token.IDENFR, lineNumber);
                }
            }
            else if( Character.isDigit(c) ){
                if( notesMode == 1 ){
                    continue;
                } // 对于当前字符为数字的情况，如果现在处于注释状态下，可以直接跳过。
                while( Character.isDigit(c) ){
                    token.append(c);
                    if( poi >= len ){
                        writeInFile(token.toString(), Token.INTCON, lineNumber);
                        return;
                    }
                    c = sentence.charAt(poi++);
                }
                poi--;
                writeInFile(token.toString(), Token.INTCON, lineNumber);
            }
            else{
                if( notesMode == 1 && c != '*'){
                    continue;
                } // 对于当前字符为符号的情况，如果不是*，直接跳过。
                token.append(c);
                switch(c){
                    case '+':
                        status = Token.PLUS;
                        break;
                    case '-':
                        status = Token.MINU;
                        break;
                    case '*':
                        if( poi >= len ){
                            status = Token.MULT;
                            break;
                        }
                        c = sentence.charAt(poi++);
                        if( c == '/' && notesMode == 1 ){
                            notesMode = 0;
                        }else {
                            poi--;
                            status = Token.MULT;
                        }
                        break;
                    case '/':
                        if( poi >= len ){
                            status = Token.DIV;
                            break;
                        }
                        c = sentence.charAt(poi++);
                        if( c == '/'){
                            return;
                        }
                        else if( c == '*' ){
                            notesMode = 1;
                        }
                        else{
                            poi--;
                            status = Token.DIV;
                        }
                        break;
                    case '%':
                        status = Token.MOD;
                        break;
                    case ';':
                        status = Token.SEMICN;
                        break;
                    case ',':
                        status = Token.COMMA;
                        break;
                    case '(':
                        status = Token.LPARENT;
                        break;
                    case ')':
                        status = Token.RPARENT;
                        break;
                    case '[':
                        status = Token.LBRACK;
                        break;
                    case ']':
                        status = Token.RBRACK;
                        break;
                    case '{':
                        status = Token.LBRACE;
                        break;
                    case '}':
                        status = Token.RBRACE;
                        break;
                    case '=':
                        if( poi >= len ){
                            status = Token.ASSIGN;
                            break;
                        }
                        c = sentence.charAt(poi++);
                        if( c == '=' ){
                            token.append(c);
                            status = Token.EQL;
                        }
                        else{
                            poi--;
                            status = Token.ASSIGN;
                        }
                        break;
                    case '!':
                        if( poi >= len ){
                            status = Token.NOT;
                            break;
                        }
                        c = sentence.charAt(poi++);
                        if( c == '=' ){
                            token.append(c);
                            status = Token.NEQ;
                        }
                        else{
                            poi--;
                            status = Token.NOT;
                        }
                        break;
                    case '>':
                        if( poi >= len ){
                            status = Token.GRE;
                            break;
                        }
                        c = sentence.charAt(poi++);
                        if( c == '=' ){
                            token.append(c);
                            status = Token.GEQ;
                        }
                        else{
                            poi--;
                            status = Token.GRE;
                        }
                        break;
                    case '<':
                        if( poi >= len ){
                            status = Token.LSS;
                            break;
                        }
                        c = sentence.charAt(poi++);
                        if( c == '=' ){
                            token.append(c);
                            status = Token.LEQ;
                        }
                        else{
                            poi--;
                            status = Token.LSS;
                        }
                        break;
                    case '|':
                        if( poi >= len ){
                            return;
                        }
                        c = sentence.charAt(poi++);
                        if( c == '|' ){
                            token.append(c);
                            status = Token.OR;
                        }
                        else{
                            poi--;
                            wrong();
                        }
                        break;
                    case '&':
                        if( poi >= len ){
                            return;
                        }
                        c = sentence.charAt(poi++);
                        if( c == '&' ){
                            token.append(c);
                            status = Token.AND;
                        }
                        else{
                            poi--;
                            wrong();
                        }
                        break;
                    case '\"':
                        if( poi >= len ){
                            return;
                        }
                        c = sentence.charAt(poi++);
                        while( poi < len && c != '\"'){
                            token.append(c);
                            c = sentence.charAt(poi++);
                        }
                        if( c == '\"' ){
                            token.append(c);
                            status = Token.STRCON;
                        }else{
                            wrong();
                        }
                        break;
                    default:
                        wrong();
                }
                writeInFile(token.toString(), status, lineNumber);
            }
        }
    }
}

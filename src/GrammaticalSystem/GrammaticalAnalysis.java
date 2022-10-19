package GrammaticalSystem;

import ErrorHandlingSystem.NameRedefinitionError;
import ErrorHandlingSystem.NameUndefinitionError;
import FileController.FileControl;
import IntermediateCodeSystem.ExpAnalyse;
import LexicalSystem.Lexical;
import LexicalSystem.LexicalAnalysis;
import LexicalSystem.Token;
import SymbolTableSystem.FunctionTable;
import SymbolTableSystem.SymbolTable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

// <BlockItem>, <Decl>, <BType> 不需要添加在文件中。
public class GrammaticalAnalysis {

    // 在每次进行判断的时候，当前poi对应的都是最新的单词位置。

    public static int poi = 0; // 当前单词的读取位置。

    public static String fileName = FileControl.GrammaticalSystemFileName;

    public static int dimensionNum = 0; // 维度的总计。
    public static int nowDimension = 0; // 当前正在处理的维度。
    public static boolean notWrite = false; // 是否向文件中写入对应信息。

    public static SymbolTable[] symbolTableList = new SymbolTable[50000];


    static void wrong() throws IOException { // 错误预处理。
        String str = "There is a wrong! poi.type = " + getWord(poi).type + " poi.token = " + getWord(poi).token + "\n";

        poi ++;
        if( FileControl.GrammaticalSystemPrint ){
            Files.write(Paths.get(fileName), str.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
        }
    }

    public static Lexical getWord( int i ){ // 返回当前读到的单词。
        if( i >= LexicalAnalysis.pop ){
            return new Lexical("", 0, 0, -1);
        }
        return LexicalAnalysis.wordList[i];
    }

    public static void writeGrammer( String str ) throws IOException { // 向文件中进行写入。

        if( notWrite )
            return;

        str = "<" + str + ">\n";
        if( FileControl.GrammaticalSystemPrint ){
            Files.write(Paths.get(fileName), str.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
        }

    }

    public static void writeWord( Lexical lex ) throws IOException { // 向文件中进行写入。

        if( notWrite )
            return;

        String str = LexicalAnalysis.getToken( lex.type ) + " " + lex.token + "\n";
        if( FileControl.GrammaticalSystemPrint ){
            Files.write(Paths.get(fileName), str.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
        }

    }

    public static void BType() throws IOException {
        if( getWord(poi).type == Token.INTTK ){
            writeWord( getWord(poi) );
            poi++;
        }else{
            wrong();
        }
    }


    public static int FuncType() throws IOException {
        if( getWord(poi).type == Token.INTTK || getWord(poi).type == Token.VOIDTK ){
            int ret = 0;
            if( getWord(poi).type == Token.INTTK ){
                ret = 1;
            }
            writeWord( getWord(poi) );
            poi++;
            writeGrammer("FuncType");
            return ret ; // 0 -> void, 1 -> int
        }else{
            wrong();
            return -1;
        }
    }

    public static void Number( ExpAnalyse e ) throws IOException{

        e.addSymbol(getWord(poi).token, 1);

        writeWord( getWord(poi) );
        poi++;
        writeGrammer("Number");
    }

    public static void UnaryOp() throws IOException{
        if( getWord(poi).type == Token.PLUS || getWord(poi).type == Token.MINU || getWord(poi).type == Token.NOT){
            writeWord( getWord(poi) );
            poi++;
            writeGrammer("UnaryOp");
        }
        else{
            wrong();
        }
    }

}

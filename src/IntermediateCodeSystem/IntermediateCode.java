package IntermediateCodeSystem;

import FileController.FileControl;
import LexicalSystem.Lexical;
import LexicalSystem.LexicalAnalysis;
import LexicalSystem.Token;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class IntermediateCode {

    public static int poiMed = 0; // 当前单词的读取位置。
    public static String fileName = FileControl.IntermediateCodeSystemFileName;

    public static int dimensionMediateNum = 0;
    public static int nowMediateDimension = 0;
    public static SymbolTableMediate[] symbolTableMediateList = new SymbolTableMediate[100000];

    public static void writeIntermediateCode( String str ) throws IOException { // 向文件中进行写入。

        str = str + "\n";

        if( FileControl.IntermediateCodeSystemPrint ){
            Files.write(Paths.get(fileName), str.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
        }

    }
    public static void BTypeMed() throws IOException {
        if( getWordMed(poiMed).type == Token.INTTK ){
            poiMed++;
        }
    }

    public static int FuncTypeMed() throws IOException {
        if( getWordMed(poiMed).type == Token.INTTK || getWordMed(poiMed).type == Token.VOIDTK ){
            int ret = 0;
            if( getWordMed(poiMed).type == Token.INTTK ){
                ret = 1;
            }
            poiMed++;
            return ret ; // 0 -> void, 1 -> int
        }else{
            return -1;
        }
    }

    public static void NumberMed( ExpAnalyse e ) throws IOException{
        e.addExpSymbol(getWordMed(poiMed).token, 1, true);
        poiMed++;
    }

    public static void UnaryOpMed() throws IOException{
        if( getWordMed(poiMed).type == Token.PLUS || getWordMed(poiMed).type == Token.MINU || getWordMed(poiMed).type == Token.NOT){
            poiMed++;
        }
    }

    public static Lexical getWordMed(int i ){ // 返回当前读到的单词。
        if( i >= LexicalAnalysis.pop ){
            return new Lexical("", 0, 0, -1);
        }
        return LexicalAnalysis.wordList[i];
    }
}

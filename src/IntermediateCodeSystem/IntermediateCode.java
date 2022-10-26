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
    public static String fileName = FileControl.LlvmIrFileName;

    public static int dimensionMediateNum = 0;
    public static int nowMediateDimension = 0;
    public static SymbolTableMediate[] symbolTableMediateList = new SymbolTableMediate[100000];

    public static boolean writeInFile = true;

    public static void writeIntermediateCode( String str ) throws IOException { // 向文件中进行写入。
        str = str + "\n";

        if( FileControl.IntermediateCodeSystemPrint ){
            Files.write(Paths.get(fileName), str.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
        }
    }

    public static boolean isNumeric(String str){
        for (int i = str.length(); --i >= 0;){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

    public static void writeGlobalVarDef( String str ) throws IOException {
        str = str + "\n";
        if( FileControl.LlvmIrFilePrint ){
            Files.write(Paths.get(fileName), str.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
        }
    }

    public static void writeLlvmIr( String str, boolean block ) throws IOException { // block对于当前语句是否需要空格进行判断。
        if( !writeInFile )
            return;
        str = str + "\n";
        if( block )
            str = "\t" + str;
        if( FileControl.LlvmIrFilePrint ){
            Files.write(Paths.get(fileName), str.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
        }
    }

    public static void writeLlvmIrWord( String str, boolean block ) throws IOException { // block对于当前语句是否需要空格进行判断。
        if( !writeInFile )
            return;
        if( block )
            str = "\t" + str;
        if( FileControl.LlvmIrFilePrint ){
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

    public static int UnaryOpMed() throws IOException{
        if( getWordMed(poiMed).type == Token.PLUS || getWordMed(poiMed).type == Token.MINU || getWordMed(poiMed).type == Token.NOT){
            poiMed++;
        }
        return getWordMed(poiMed).type;
    }

    public static Lexical getWordMed(int i ){ // 返回当前读到的单词。
        if( i >= LexicalAnalysis.pop ){
            return new Lexical("", 0, 0, -1);
        }
        return LexicalAnalysis.wordList[i];
    }

    public static String getPoiOneDim( String reg, String poi ){ // 获取一维数组的poi个元素。
        return " = getelementptr " + "i32, i32* " + reg + ", i32 " + poi;
    }

    public static String getArrOneDim( String reg, String dim2, String poi ){
        return " = getelementptr [" + dim2 + " x i32], [" + dim2 + " x i32]* " +
                reg + ", i32 " + poi + ", i32 0";
    }

    public static String getPoiTwoDim( String reg, String dim2, String poi1, String poi2 ){
        return " = getelementptr [" + dim2 + " x i32], [" + dim2 + " x i32]* " +
                reg + ", i32 " + poi1 + ", i32 " + poi2;
    }

    public static void changeOneDimensionPatten( String newreg, String reg, String dim2 ) throws IOException {
        String dim = "[" + dim2 + " x i32]";
        String str = newreg + " = getelementptr " + dim + ", " + dim + "* " + reg + ", i32 0, i32 0";
        writeLlvmIr( str, true );
    }

    public static void changeTwoDimensionPatten( String newreg, String reg, String dim1, String dim2 ) throws IOException {
        String dim = "[" + dim1 + " x [" + dim2 + " x i32]]";
        String str = newreg + " = getelementptr " + dim + ", " + dim + "* " + reg + ", i32 0, i32 0";
        writeLlvmIr( str, true );
    }
}

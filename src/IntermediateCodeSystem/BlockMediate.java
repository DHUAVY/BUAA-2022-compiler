package IntermediateCodeSystem;
import LexicalSystem.Token;
import SymbolTableSystem.SymbolTable;

import java.io.IOException;

import static IntermediateCodeSystem.IntermediateCode.getWordMed;
import static IntermediateCodeSystem.IntermediateCode.poiMed;
import static IntermediateCodeSystem.SymbolTableMediate.*;


public class BlockMediate {

    public static void analysis( boolean dimension ) throws IOException {
        // Block → '{' { BlockItem } '}'
        if( dimension ){
            // 添加新的dimension。
            IntermediateCode.symbolTableMediateList[IntermediateCode.dimensionMediateNum] =
                    new SymbolTableMediate( IntermediateCode.dimensionMediateNum, IntermediateCode.nowMediateDimension );
            IntermediateCode.nowMediateDimension = IntermediateCode.dimensionMediateNum;
            IntermediateCode.dimensionMediateNum++;
        }

        if( getWordMed(poiMed).type == Token.LBRACE ){ // {
            poiMed++;
            while( getWordMed(poiMed).type != Token.RBRACE ){ // 当下一个字符不为'}'时，进入BlockItem
                BlockItemMediate.analysis();
            }
            poiMed++;
        }

        //TODO 在每一个Block结束之后都将全局变量重置。
        SymbolTableMediate.globalVarInit();

        IntermediateCode.nowMediateDimension = IntermediateCode.symbolTableMediateList[IntermediateCode.nowMediateDimension].fatherId; // 回退当前维度至其本身的父维度。
    }

    public static void analysis( boolean dimension, FunctionMediate func ) throws IOException {
        // Block → '{' { BlockItem } '}'

        func.changeParam();

        if( dimension ){
            // 添加新的dimension。
            IntermediateCode.symbolTableMediateList[IntermediateCode.dimensionMediateNum] =
                    new SymbolTableMediate( IntermediateCode.dimensionMediateNum, IntermediateCode.nowMediateDimension );
            IntermediateCode.nowMediateDimension = IntermediateCode.dimensionMediateNum;
            IntermediateCode.dimensionMediateNum++;
        }

        if( getWordMed(poiMed).type == Token.LBRACE ){ // {
            poiMed++;
            while( getWordMed(poiMed).type != Token.RBRACE ){ // 当下一个字符不为'}'时，进入BlockItem
                BlockItemMediate.analysis();
            }
            poiMed++;
        }

        IntermediateCode.nowMediateDimension = IntermediateCode.symbolTableMediateList[IntermediateCode.nowMediateDimension].fatherId; // 回退当前维度至其本身的父维度。
    }

}

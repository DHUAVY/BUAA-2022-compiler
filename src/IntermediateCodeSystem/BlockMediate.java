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
            symbolTableMediateList[dimensionMediateNum] = new SymbolTableMediate( dimensionMediateNum, nowMediateDimension );
            nowMediateDimension = dimensionMediateNum;
            dimensionMediateNum++;
        }

        if( getWordMed(poiMed).type == Token.LBRACE ){ // {
            poiMed++;
            while( getWordMed(poiMed).type != Token.RBRACE ){ // 当下一个字符不为'}'时，进入BlockItem
                BlockItemMediate.analysis();
            }
            poiMed++;
        }

        nowMediateDimension = symbolTableMediateList[nowMediateDimension].fatherId; // 回退当前维度至其本身的父维度。
    }
}

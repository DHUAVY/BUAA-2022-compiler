package GrammaticalSystem;
import ErrorHandlingSystem.WithOutReturnError;
import LexicalSystem.Token;
import SymbolTableSystem.SymbolTable;

import java.io.IOException;
import static GrammaticalSystem.GrammaticalAnalysis.*;

public class Block {

    public static int analysis( boolean dimen ) throws IOException {
    // Block → '{' { BlockItem } '}'
        int ret = 0;
        if( dimen ){
            // 添加新的dimension。
            symbolTableList[dimensionNum] = new SymbolTable( dimensionNum, nowDimension );
            nowDimension = dimensionNum;
            dimensionNum++;
        }

        if( getWord(poi).type == Token.LBRACE ){ // {
            //writeWord( getWord(poi) );
            poi++;
            while( getWord(poi).type != Token.RBRACE ){ // 当下一个字符不为'}'时，进入BlockItem
                ret = BlockItem.analysis();
            }
            WithOutReturnError.lineNumber = getWord(poi).lineNumber;
            //writeWord( getWord(poi) ); // }
            poi++;
        }else{
            wrong();
        }

        nowDimension = symbolTableList[nowDimension].fatherId;

        //writeGrammer("Block");
        return ret;
    }
}

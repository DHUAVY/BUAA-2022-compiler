package GrammaticalSystem;

import LexicalSystem.Lexical;
import LexicalSystem.Token;
import SymbolTableSystem.FunctionTable;

import java.io.IOException;

import static GrammaticalSystem.GrammaticalAnalysis.*;

public class Ident {

    public static void ident() throws IOException {
        if( getWord(poi).type == Token.IDENFR ){
//            writeWord( getWord(poi) );
            poi++;
        }else{
            wrong();
        }
    }

    public static void identAnalyse( Lexical lex, int type, int detail ) throws IOException {
        if( lex.type == Token.IDENFR ){
            if( type == -1 ){
                // 此时添加到函数表之中。
                FunctionTable.addFunction( lex, detail );
                detail = 0;
            }
            symbolTableList[nowDimension].addSymbol( lex, type, detail);
            // 对于变量而言，这里的type象征数组的维度，detail象征是否为常量。
        }else{
            wrong();
        }
    }

}

package GrammaticalSystem;

import ErrorHandlingSystem.NameRedefinitionError;
import ErrorHandlingSystem.WithOutBracket;
import LexicalSystem.Lexical;
import LexicalSystem.Token;

import java.io.IOException;

import static GrammaticalSystem.GrammaticalAnalysis.*;

public class VarDef {
    public static void VarDefAnalysis() throws IOException {
        // VarDef → Ident { '[' ConstExp ']' } | Ident { '[' ConstExp ']' } '=' InitVal

        int type = 0;
        Lexical lex;

        lex = getWord(poi);
        Ident.ident();

        while( getWord(poi).type == Token.LBRACK ){
            type += 1;
            writeWord( getWord(poi) );
            poi++;
            Expression.ConstExp();
            if( getWord(poi).type == Token.RBRACK ){
                writeWord( getWord(poi) );
                poi++;
            }else{
                WithOutBracket.analyse(poi-1);
            }
        }

        if( !NameRedefinitionError.analyse( lex ) ){
            // 如果当前的变量还未被声明，则将其添加至符号表中。
            Ident.identAnalyse( lex, type, 0 );
        }

        if( getWord(poi).type == Token.ASSIGN ){
            writeWord( getWord(poi) );
            poi++;
            InitVal.InitValAnalysis();
        }

        writeGrammer("VarDef");
    }
}

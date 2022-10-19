package GrammaticalSystem;

import ErrorHandlingSystem.NameRedefinitionError;
import ErrorHandlingSystem.WithOutBracket;
import IntermediateCodeSystem.ConstAnalyse;
import LexicalSystem.Lexical;
import LexicalSystem.Token;

import java.io.IOException;

import static GrammaticalSystem.GrammaticalAnalysis.*;

public class ConstDef {

    public static void ConstDefAnalysis() throws IOException {
        // ConstDef → Ident { '[' ConstExp ']' } '=' ConstInitVal
        int type = 0;
        Lexical lex;

        // a[ dim_1 ][ dim_2 ]
        String dim = "";
        String dim_1 = ""; //
        String dim_2 = ""; //
        String value = "";

        lex = getWord(poi);
        Ident.ident();

        while( getWord(poi).type == Token.LBRACK ){ // {
            type += 1;
            writeWord( getWord(poi) );
            poi++;

//            int init_poi = poi;
            Expression.ConstExp();
//            for( int i = init_poi; i < poi; i++ ) { // '{'的下一位，到'}'的上一位。
//                dim += getWord(i).token;
//            }

//            if( type == 1 ){
//                dim_2 = dim;
//            }else if( type == 2 ){
//                dim_1 = dim;
//            }

            if( getWord(poi).type == Token.RBRACK ){ // }
                writeWord( getWord(poi) );
                poi++;
            }else{
                WithOutBracket.analyse(poi-1);
            }
        }

        if( !NameRedefinitionError.analyse( lex ) ){
            // 如果当前的变量还未被声明，则将其添加至符号表中。
            Ident.identAnalyse( lex, type, 1 );
        }



        if( getWord(poi).type == Token.ASSIGN ){
            writeWord( getWord(poi) );
            poi++;
        }else{
            wrong();
        }

//        int init_poi = poi;

        ConstInitVal.ConstInitValAnalysis();

//        if( type == 0 ){
//            for( int i = init_poi; i < poi; i++ ){
//                value += getWord(i).token;
//            }
//        }
//        ConstAnalyse.analyse(lex.token, type, dim_1, dim_2, value);

        writeGrammer("ConstDef");
    }

}

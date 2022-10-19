package GrammaticalSystem;

import ErrorHandlingSystem.WithOutSemiconError;
import LexicalSystem.Token;

import java.io.IOException;

import static GrammaticalSystem.GrammaticalAnalysis.*;
public class ConstDecl {

    public static void analysis() throws IOException {
        // ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';'

        if( getWord(poi).type == Token.CONSTTK ){
            writeWord( getWord(poi) );
            poi++;
        }else{
            wrong();
        }

        BType();

        ConstDef.ConstDefAnalysis();

        while( getWord(poi).type == Token.COMMA){
            writeWord( getWord(poi) );
            poi++;
            ConstDef.ConstDefAnalysis();
        }

        if( getWord(poi).type == Token.SEMICN ){
            writeWord( getWord(poi) );
            poi++;
        }else{
            WithOutSemiconError.analyse( poi - 1 );
        }

        writeGrammer("ConstDecl");
    }

}

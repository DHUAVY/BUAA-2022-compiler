package GrammaticalSystem;

import ErrorHandlingSystem.WithOutSemiconError;
import LexicalSystem.Token;

import java.io.IOException;

import static GrammaticalSystem.GrammaticalAnalysis.*;
public class VarDecl {

    public static void analysis() throws IOException {
        // VarDecl → BType VarDef { ',' VarDef } ';'
        BType();

        VarDef.VarDefAnalysis();

        while( getWord(poi).type == Token.COMMA){
//            writeWord( getWord(poi) );
            poi++;
            VarDef.VarDefAnalysis();
        }

        if( getWord(poi).type == Token.SEMICN ){
//            writeWord( getWord(poi) );
            poi++;
        }else{
            WithOutSemiconError.analyse( poi - 1 );
        }

//        writeGrammer("VarDecl");

    }
}

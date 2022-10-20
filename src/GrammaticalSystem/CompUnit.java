package GrammaticalSystem;

import LexicalSystem.Token;

import java.io.IOException;

import static GrammaticalSystem.GrammaticalAnalysis.*;

public class CompUnit {

    public static void analysis() throws IOException {

        while( (getWord(poi).type == Token.CONSTTK ||
                getWord(poi + 2 ).type != Token.LPARENT) &&
                (getWord(poi + 2 ).type != 0 ) ){
            // 0 -> const, 2 !-> (
            Decl.analysis();
        }

        while( getWord( poi + 1).type != Token.MAINTK && getWord( poi + 1).type != 0 ){
            FuncDef.FuncDefAnalysis();
        }

        MainFuncDef.Analysis();

        writeGrammer("CompUnit");
    }

}

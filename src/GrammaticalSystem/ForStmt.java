package GrammaticalSystem;

import ErrorHandlingSystem.NotInLoopError;
import ErrorHandlingSystem.WithOutSemiconError;
import ErrorHandlingSystem.WithReturnError;
import LexicalSystem.Token;

import java.io.IOException;

import static GrammaticalSystem.GrammaticalAnalysis.getWord;
import static GrammaticalSystem.GrammaticalAnalysis.poi;

public class ForStmt {
    // forStmt → LVal '=' Exp
    public static int analysis() throws IOException {
        LVal.Analysis(1);

        if(getWord(poi).type == Token.ASSIGN)
            poi ++;

        int ret = Expression.Exp();

        return ret;

    }

}

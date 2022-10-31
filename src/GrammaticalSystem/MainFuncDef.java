package GrammaticalSystem;
import ErrorHandlingSystem.WithOutParenError;
import ErrorHandlingSystem.WithOutReturnError;
import LexicalSystem.Token;
import java.io.IOException;
import static GrammaticalSystem.GrammaticalAnalysis.*;

public class MainFuncDef {

    public static void Analysis() throws IOException {

        // MainFuncDef → 'int' 'main' '(' ')' Block
        int returnValue = 0;
        if( getWord(poi).type == Token.INTTK ){
            //writeWord( getWord(poi) );
            poi++;
            if( getWord(poi).type == Token.MAINTK ){
                //writeWord( getWord(poi) );
                poi++;
                if( getWord(poi).type == Token.LPARENT ){
                    //writeWord( getWord(poi) );
                    poi++;

                    if( getWord(poi).type == Token.RPARENT ){
                        //writeWord( getWord(poi) );
                        poi++;
                    }else{
                        WithOutParenError.analyse(poi-1);
                    }
                    returnValue = Block.analysis(true);

                }else{
                    wrong();
                }
            }else{
                wrong();
            }
        }else{
            wrong();
        }

        if( returnValue == 0 ){
            WithOutReturnError.analyse();
        }
//        writeGrammer("MainFuncDef");
    }
}

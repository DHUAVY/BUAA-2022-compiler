package GrammaticalSystem;
import LexicalSystem.Token;
import java.io.IOException;
import static GrammaticalSystem.GrammaticalAnalysis.*;

public class BlockItem {

    public static int analysis() throws IOException {
        // BlockItem → Decl | Stmt
        int ret;
        if(getWord(poi).type == Token.CONSTTK || getWord(poi).type == Token.INTTK){
            Decl.analysis();
            ret = 0;
        }else{
            ret = Stmt.analysis();
        }
        return ret;
        // 不需要输出。
    }

}

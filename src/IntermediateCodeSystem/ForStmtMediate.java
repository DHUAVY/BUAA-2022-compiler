package IntermediateCodeSystem;

import java.io.IOException;

import static IntermediateCodeSystem.IntermediateCode.poiMed;

public class ForStmtMediate {
    public static void analysis() throws IOException {
        // ForStmt → LVal '=' Exp
        lvalSym lvsym = LValMediate.analysis();
        poiMed++;
        StmtMediate.lvalEqualExp( lvsym );
    }
}

package IntermediateCodeSystem;
import java.util.HashMap;

public class SymbolTableMediate {

    public int id; // 当前符号表的id。
    public int fatherId; // 当前符号表的父id。
    public HashMap<String, SymbolMediate> directory = new HashMap<>();

    public static void init(){
        IntermediateCode.symbolTableMediateList[0] = new SymbolTableMediate( 0, -1 ); // 建立全局变量的符号表。
        IntermediateCode.dimensionMediateNum = 1;
        IntermediateCode.nowMediateDimension = 0;
    }

    public SymbolTableMediate( int id, int fatherId ){
        this.id = id;
        this.fatherId = fatherId;
    }

    public SymbolMediate addSymbol( String token ){

        SymbolMediate newSymbol = new SymbolMediate();
        this.directory.put( token, newSymbol );
        return newSymbol;

    }

    public static SymbolMediate findSymbol( String token ){

        SymbolMediate sm = new SymbolMediate();
        int dimension = IntermediateCode.nowMediateDimension;
        while( dimension != -1 ){

            sm = IntermediateCode.symbolTableMediateList[dimension].directory.get( token );
            if( sm == null ){
                // 如果在当前维度的单词表中未得到对应的声明，则寻找上一维度。
                dimension = IntermediateCode.symbolTableMediateList[dimension].fatherId;
            }else{
                return sm;
            }
        }
        return sm;
    }
}
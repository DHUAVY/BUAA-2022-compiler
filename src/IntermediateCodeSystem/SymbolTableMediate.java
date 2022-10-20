package IntermediateCodeSystem;
import java.util.HashMap;

public class SymbolTableMediate {

    public int id; // 当前符号表的id。
    public int fatherId; // 当前符号表的父id。
    public HashMap<String, SymbolMediate> directory = new HashMap<>();

    public static SymbolTableMediate[] symbolTableMediateList = new SymbolTableMediate[100000];
    public static int dimensionMediateNum = 0;
    public static int nowMediateDimension = 0;

    public static void init(){
        symbolTableMediateList[0] = new SymbolTableMediate( 0, -1 ); // 建立全局变量的符号表。
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
}
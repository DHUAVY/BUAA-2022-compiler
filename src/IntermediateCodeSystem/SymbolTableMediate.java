package IntermediateCodeSystem;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static IntermediateCodeSystem.IntermediateCode.*;

public class SymbolTableMediate {

    public int id; // 当前符号表的id。
    public int fatherId; // 当前符号表的父id。
    public HashMap<String, SymbolMediate> directory = new HashMap<>();

    public static void init(){
        symbolTableMediateList[0] = new SymbolTableMediate( 0, -1 ); // 建立全局变量的符号表。
        dimensionMediateNum = 1;
        nowMediateDimension = 0;
    }

    public static void addDimension(){
        symbolTableMediateList[dimensionMediateNum] = new SymbolTableMediate( dimensionMediateNum, nowMediateDimension );
        nowMediateDimension = dimensionMediateNum;
        dimensionMediateNum ++;
    }


    //TODO 对于全局变量在每个函数中进行初始化。
    public static void globalVarInit(){
        Iterator< Map.Entry<String, SymbolMediate>> iterator = symbolTableMediateList[0].directory.entrySet().iterator();
        while( iterator.hasNext() ){
            Map.Entry<String, SymbolMediate> entry = iterator.next();
            SymbolMediate symmed = entry.getValue();
            symmed.initial = false;
            symmed.reg = "@" + symmed.token;
        }
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
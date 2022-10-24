package IntermediateCodeSystem;

import java.util.HashMap;

public class FunctionMediateTable {

    public static HashMap<String, FunctionMediate> directory = new HashMap<>();

    public static FunctionMediate addSymbol( String token ){
        FunctionMediate func = new FunctionMediate();
        func.token = token;
        directory.put( token, func );
        return func;
    }

    public static FunctionMediate findSymbol( String token ){
        return directory.get( token );
    }
}

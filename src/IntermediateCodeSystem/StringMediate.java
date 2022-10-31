package IntermediateCodeSystem;
import LexicalSystem.LexicalAnalysis;

import java.io.IOException;
import java.util.HashMap;

import static LexicalSystem.LexicalAnalysis.*;
import static LexicalSystem.Token.*;

public class StringMediate {

    public static HashMap<Integer, StringHandle> stringLibrary = new HashMap<>();

    public static void initIOString() throws IOException {
        for( int i = 0; i < pop; i++ ){
            if( wordList[i].type == STRCON ){
                handleWithStrCon( wordList[i].token, i );
            }
        }
    }

    public static void handleWithStrCon( String str, int poi ) throws IOException {
        StringBuilder strRet = new StringBuilder();
        int len = str.length();
        int specialCheck = 0;
        StringHandle strHandle = new StringHandle();
        for( int i = 0; i < len; i++ ){
            if( str.charAt(i) == '\"'){
                continue;
            }
            if( str.charAt(i) == '%' && i + 1 < str.length() && str.charAt(i+1) == 'd'){
                strRet.insert( 0, "\""); // \"(1->0), \\00(3->1)
                strRet.append("\\00\"");
                specialCheck += 4;

                int strlen = strRet.length()-specialCheck;
                strHandle.lenthList[strHandle.num] = strlen;
                strHandle.contentList[strHandle.num++] = StrReg.getFreeStringReg( strlen, strRet.toString() );

                specialCheck = 0;
                //TODO 相当于是一个标识位。

                strHandle.lenthList[strHandle.num] = -1;
                strHandle.contentList[strHandle.num++] = "0";
                strRet = new StringBuilder();
                i++;
            }
            else if( str.charAt(i) == '\\' && i + 1 < str.length() && str.charAt(i+1) == 'n'){
                strRet.append("\\0a"); // \\0a(3->1)
                specialCheck += 2;
                i++;
            }
            else{
                strRet.append(str.charAt(i));
            }
        }
        strRet.insert( 0, "\""); //  \"(1->0), \\00(3->1)
        strRet.append("\\00\"");
        specialCheck += 4;

        int strlen = strRet.length()-specialCheck;
        strHandle.lenthList[strHandle.num] = strlen;
        strHandle.contentList[strHandle.num++] = StrReg.getFreeStringReg( strlen, strRet.toString() );

        StringMediate.stringLibrary.put( poi, strHandle );
    }

}

class StringHandle{
    public int num = 0;
    public String[] contentList = new String[2000];
    public int[] lenthList = new int[2000];
}

class StrReg{
    public static int num = 0;

    public static String getFreeStringReg( int length, String content ) throws IOException {
        String retStr = "@_str_" + num;
        num ++;
        String ioStr = retStr + " = constant [" + length + " x i8] c" + content;
        IntermediateCode.writeGlobalVarDef(ioStr);
        return retStr;
    }

}

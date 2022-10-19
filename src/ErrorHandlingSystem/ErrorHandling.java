package ErrorHandlingSystem;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import FileController.FileControl;

public class ErrorHandling {

    public static final int a = 1, b = 2, c = 3, d = 4, e = 5, f = 6,
        g = 7, h = 8, i = 9, j = 10, k = 11, l = 12, m = 13;

    public static String errorFileName = FileControl.ErrorHandlingSystemFileName;

    public static boolean wrong = false; // 判断当前文件是否有错。
    public static boolean[] errorList = new boolean[500000]; // 默认值为false，当对应行进行报错后改为true，以此减少重复报错。

    public static String getType( int typeNumber ){

        switch(typeNumber){
            case a:
                return "a";
            case b:
                return "b";
            case c:
                return "c";
            case d:
                return "d";
            case e:
                return "e";
            case f:
                return "f";
            case g:
                return "g";
            case h:
                return "h";
            case i:
                return "i";
            case j:
                return "j";
            case k:
                return "k";
            case l:
                return "l";
            case m:
                return "m";
            default:
                return "other error";
        }
    }

    public static void writeError( int lineNumber, int typeNumber ) throws IOException { // 向文件中进行写入。
        wrong = true;
        if( !errorList[lineNumber] ){
            errorList[lineNumber] = true;
            String str = lineNumber + " " + getType(typeNumber) + "\n";
            if( FileControl.ErrorHandlingSystemPrint ){
                Files.write(Paths.get(errorFileName), str.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            }
        }
    }


}

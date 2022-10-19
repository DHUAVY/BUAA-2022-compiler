package IntermediateCodeSystem;

import FileController.FileControl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class IntermediateCode {

    public static String fileName = FileControl.IntermediateCodeSystemFileName;

    public static void writeIntermediateCode( String str ) throws IOException { // 向文件中进行写入。
        if( FileControl.IntermediateCodeSystemPrint ){
            Files.write(Paths.get(fileName), str.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
        }
    }
}

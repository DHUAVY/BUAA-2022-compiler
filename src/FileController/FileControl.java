package FileController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileControl {

    public static String inPutFileName = "testfile.txt";

    public static String LexicalSystemFileName = "LexicalSystem.txt";
    public static String GrammaticalSystemFileName = "output.txt";
    public static String ErrorHandlingSystemFileName = "error.txt";
    public static String IntermediateCodeSystemFileName = "middle.txt";

    public static boolean LexicalSystemPrint = false;
    public static boolean GrammaticalSystemPrint = false;
    public static boolean ErrorHandlingSystemPrint = false;
    public static boolean IntermediateCodeSystemPrint = true;

    public static void fileMake() throws IOException {
        if( LexicalSystemPrint ){
            Files.write(Paths.get(LexicalSystemFileName), "".getBytes(StandardCharsets.UTF_8));
        }
        if( GrammaticalSystemPrint ){
            Files.write(Paths.get(GrammaticalSystemFileName), "".getBytes(StandardCharsets.UTF_8));
        }
        if( ErrorHandlingSystemPrint ){
            Files.write(Paths.get(ErrorHandlingSystemFileName), "".getBytes(StandardCharsets.UTF_8));
        }
        if( IntermediateCodeSystemPrint ){
            Files.write(Paths.get(IntermediateCodeSystemFileName), "".getBytes(StandardCharsets.UTF_8));
        }

    }
}

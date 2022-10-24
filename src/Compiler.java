import ErrorHandlingSystem.ErrorHandling;
import FileController.FileControl;
import GrammaticalSystem.CompUnit;
import GrammaticalSystem.FuncDef;
import GrammaticalSystem.GrammaticalAnalysis;
import IntermediateCodeSystem.CompUnitMediate;
import IntermediateCodeSystem.ExpAnalyse;
import IntermediateCodeSystem.IntermediateCode;
import LexicalSystem.LexicalAnalysis;
import SymbolTableSystem.FunctionTable;
import SymbolTableSystem.Symbol;
import SymbolTableSystem.SymbolTable;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

public class Compiler {

    public static void main(String[] args) throws IOException {

        int lineNumber = 1;
//        //TODO Debug模式。
//        String text = "";

        FileControl.fileMake();
        SymbolTable.init(); // 初始化单词表。

        try (Scanner sc = new Scanner( new FileReader(FileControl.inPutFileName) )) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine(); // 按行读取字符串
                LexicalAnalysis.lexicalAnalysis( line, lineNumber );
                lineNumber ++;

//                //TODO Debug模式。
//                text += line;
            }
        }

//        //TODO Debug模式。
//        IntermediateCode.writeLlvmIr( text, false );

        CompUnit.analysis();

        CompUnitMediate.analysis();
    }
}

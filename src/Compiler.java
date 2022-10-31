import ErrorHandlingSystem.ErrorHandling;
import FileController.FileControl;
import GrammaticalSystem.CompUnit;
import IntermediateCodeSystem.CompUnitMediate;
import LexicalSystem.LexicalAnalysis;
import SymbolTableSystem.SymbolTable;

import java.io.FileReader;
import java.io.IOException;
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

        //TODO 如果原文件具有报错，那么不进入代码生成阶段。
        if( !ErrorHandling.wrong )
            CompUnit.analysis();

        CompUnitMediate.analysis();
    }
}

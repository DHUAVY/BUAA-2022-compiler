import ErrorHandlingSystem.ErrorHandling;
import FileController.FileControl;
import GrammaticalSystem.CompUnit;
import IntermediateCodeSystem.CompUnitMediate;
import LexicalSystem.LexicalAnalysis;
import SymbolTableSystem.SymbolTable;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import static IntermediateCodeSystem.CompUnitMediate.IOFuncDec;

public class Compiler {

    public static void main(String[] args) throws IOException {

        int lineNumber = 1;

        FileControl.fileMake();
        SymbolTable.init(); // 初始化单词表。
        //TODO 填写四个工具函数。
        IOFuncDec();

        try (Scanner sc = new Scanner( new FileReader(FileControl.inPutFileName) )) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine(); // 按行读取字符串
                LexicalAnalysis.lexicalAnalysis( line, lineNumber );
                lineNumber ++;
            }
        }

        //TODO 如果原文件具有报错，那么不进入代码生成阶段。
        if( !ErrorHandling.wrong )
            CompUnit.analysis();

        CompUnitMediate.analysis();
    }
}

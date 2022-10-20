package ErrorHandlingSystem;

import LexicalSystem.Lexical;

import java.io.IOException;

public class IllegalSymbolError {

    public static void analyse( Lexical lex ) throws IOException {

        String formatString = lex.token;
        int lineNumber = lex.lineNumber;
        int i, len = formatString.length();
        int flag = 0;
        for( i = 0; i < len; i++ ){
            if( (i == 0 || i == len -1) && formatString.charAt(i) == 34 ){
                continue;
            }
            if( formatString.charAt(i) == 32 ||
                    formatString.charAt(i) == 33 ||
                    formatString.charAt(i) == 37 || // %
                    (formatString.charAt(i) >= 40 && formatString.charAt(i) <= 126) ){
                if( formatString.charAt(i) == '\\' ){
                    if( i < len - 1 && formatString.charAt(i+1) != 'n' ){
                        flag = 1;
                        break;
                    }
                }
                else if( formatString.charAt(i) == '%' ){
                    if( i < len - 1 && formatString.charAt(i+1) != 'd' ){
                        flag = 1;
                        break;
                    }
                }
            }
            else{
                flag = 1;
                break;
            }
        }

        if( flag == 1 ){
            ErrorHandling.writeError( lineNumber, ErrorHandling.a );
        }

    }
}

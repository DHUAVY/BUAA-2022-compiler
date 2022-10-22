package IntermediateCodeSystem;

public class TemporaryRegister {

    public static int poi = 1;

    public static String getFreeReg(){
        String str = "$" + poi;
        poi ++;
        return str;
    }
}

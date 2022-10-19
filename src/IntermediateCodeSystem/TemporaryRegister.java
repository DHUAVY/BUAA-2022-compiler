package IntermediateCodeSystem;

public class TemporaryRegister {

    public static int poi = 0;

    public static boolean[] reg = new boolean[8];


    public static String getFreeReg(){
        String str = "$t" + poi;
        poi ++;
        return str;
    }
}

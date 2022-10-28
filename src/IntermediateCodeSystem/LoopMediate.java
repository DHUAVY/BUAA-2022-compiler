package IntermediateCodeSystem;

public class LoopMediate {

    public static int loopTop = 0;
    public static Loop[] loopList = new Loop[10000];

    public static void addLoop( String loopHead, String loopEnd ){
        Loop loop = new Loop(loopHead, loopEnd);
        loopList[loopTop++] = loop;
    }

}

class Loop{
    public String loopHead;
    public String loopEnd;

    public Loop( String loopHead, String loopEnd ){
        this.loopHead = loopHead;
        this.loopEnd = loopEnd;
    }
}


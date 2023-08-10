package IntermediateCodeSystem;

public class LoopMediate {

    public static int loopTop = 0;
    public static Loop[] loopList = new Loop[10000];

    public static void addLoop( String loopHead, String loopEnd, int type ){
        Loop loop = new Loop(loopHead, loopEnd, type);
        loopList[loopTop++] = loop;
    }

}

class Loop{
    public String loopHead;
    public String loopEnd;
    public String ForEnd;   // 为了for最后的continue准备，防止跳过条件。
    public int type;    // StmtMediate.WHILE or StmtMediate.FOR

    public Loop( String loopHead, String loopEnd ){
        this.loopHead = loopHead;
        this.loopEnd = loopEnd;
    }

    public Loop( String loopHead, String loopEnd, int type ){
        this.loopHead = loopHead;
        this.loopEnd = loopEnd;
        this.type = type;
    }
}


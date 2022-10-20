package IntermediateCodeSystem;

public class SymbolMediate {

        public int id; // 当前单词对应的id，即poi。
        public int dimension; // 当前单词所在的维度。
        public String token; // 当前单词所对应的字符串。
        public int type; // 0 -> a, 1 -> a[], 2 -> a[dim1][dim2], -1 -> func
        public boolean con; // 1 -> const, 0 -> var
        public int value; // type = 0，则会有当前对应的value。
        public boolean safe; // 当前单词的取值是否能够使用。非全局变量且未定义初值，函数形参等情况下即为不可使用。
        public int dim1;
        public int dim2;
        public int[] valueList = new int [10000];
        public boolean[] safeList = new boolean[10000];

}

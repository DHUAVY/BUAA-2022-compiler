package IntermediateCodeSystem;

import java.io.IOException;

public class SymbolMediate {

        public int id; // 当前单词对应的id，即poi。
        public int dimension; // 当前单词所在的维度。
        public String token; // 当前单词所对应的字符串。
        public boolean con; // 1 -> const, 0 -> var
        public boolean initial; // 当前单词在当前模块内是否经过初始化。
        public int lltype; // 0 -> i32, 1 -> i8
        // 基础信息

        public int type; // 0 -> a, 1 -> a[], 2 -> a[dim1][dim2], -1 -> func
        public int dim1;
        public int dim2;
        // 维度信息

        public String reg; // 当前对应的寄存器。
        public boolean safe; // 当前单词的取值是否能够使用。非全局变量且未定义初值，函数形参等情况下即为不可使用。
        public int value; // type = 0，则会有当前对应的value。
        public int[] valueList = new int [10000];
        public boolean[] safeList = new boolean[10000];
        // 取值信息

        @Override
        public String toString(){
                String str = "{ \n id = " + this.id + ", dimension = " + this.dimension + ", token = " + this.token + ", con = " + this.con + "\n" +
                                "type = " + this.type + ", dim1 = " + this.dim1 + ", dim2 = " + this.dim2 + ", safe = " + this.safe +
                        "\n}";
                return str;
        }

        public void notSafe(){
                this.safe = false;
                this.value = 0;
                for( int i = 0; i < 10000; i++ ){
                        this.valueList[i] = 0;
                        this.safeList[i] = false;
                }
        }

        public void globalVarChange() throws IOException {
                if( !initial && dimension == 0 && type != 0 ){
                        String newReg = TemporaryRegister.getFreeReg();
                        initial = true;
                        if( type == 1 ){
                                IntermediateCode.changeOneDimensionPatten(
                                        newReg,
                                        reg,
                                        String.valueOf(dim2)
                                );
                        }
                        else if( type == 2 ){
                                IntermediateCode.changeTwoDimensionPatten(
                                        newReg,
                                        reg,
                                        String.valueOf(dim1),
                                        String.valueOf(dim2)
                                );
                        }
                        reg = newReg;
                }
        }
}

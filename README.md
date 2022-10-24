# BUAA-2022-compiler
北航2022编译实验作业


//TODO

define dso_local i32 @sub(i32* %0) #0 {
%2 = alloca i32*, align 8
store i32* %0, i32** %2, align 8
ret i32 0
}

define dso_local i32 @add([6 x i32]* %0) #0 {
%2 = alloca [6 x i32]*, align 8
store [6 x i32]* %0, [6 x i32]** %2, align 8
ret i32 0
}

define dso_local i32 @main() #0 {
%1 = alloca i32, align 4
%2 = alloca [2 x [2 x i32]], align 16
store i32 0, i32* %1, align 4
%3 = bitcast [2 x [2 x i32]]* %2 to i8*
call void @llvm.memcpy.p0i8.p0i8.i64(i8* align 16 %3, i8* align 16 bitcast ([2 x [2 x i32]]* @__const.main.a to i8*), i64 16, i1 false)
ret i32 0
}

int sub( int a[] ){
return 0;
}

int add( int a[][6] ){
return 0;
}

int main(){
int a[2][2]={{1,1},{1,1}};
return 0;
}


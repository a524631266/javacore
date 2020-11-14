### 三大核心组建
1. ClassReader 
ｗａｌｋｅｒ角色，读取数结构数据，接受一个ｖｉｓｔｏｒ操作在ｖ相应的地方做操作

2. ClassWriter

操作完以后写入数据

3. ClassVisitor





### 访问顺序
visit visitSource? visitOuterClass? ( visitAnnotation | visitAttribute )*
( visitInnerClass | visitField | visitMethod )* visitEnd


visit与 visitEnd在类生成周期中只发送一次,因此在这两个方法中可以注入一个新的field

visitSource/OuterClass最多访问一次,
.class public Output 
.super java/lang/Object

.method public <init>()V
 aload_0
 invokenonvirtual java/lang/Object/<init>()V
 return
.end method

.method public static print(I)V
 .limit stack 2
 getstatic java/lang/System/out Ljava/io/PrintStream;
 iload_0 
 invokestatic java/lang/Integer/toString(I)Ljava/lang/String;
 invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
 return
.end method

.method public static read()I
 .limit stack 3
 new java/util/Scanner
 dup
 getstatic java/lang/System/in Ljava/io/InputStream;
 invokespecial java/util/Scanner/<init>(Ljava/io/InputStream;)V
 invokevirtual java/util/Scanner/next()Ljava/lang/String;
 invokestatic java/lang/Integer.parseInt(Ljava/lang/String;)I
 ireturn
.end method
.method public static run()V
 .limit stack 1024
 .limit locals 256
 invokestatic Output/read()I
 istore 0
L0:
 invokestatic Output/read()I
 istore 1
L1:
L3:
 iload 0
 iload 1
 if_icmpne L4
 goto L2
L4:
 iload 0
 iload 1
 if_icmplt L7
 goto L6
L7:
 iload 1
 iload 0
 isub 
 istore 1
 goto L5
L6:
 iload 0
 iload 1
 isub 
 istore 0
L5:
 goto L3
L2:
 iload 0
 invokestatic Output/print(I)V
L9:
 iload 1
 ldc 100
 iadd 
 invokestatic Output/print(I)V
L10:
L12:
 iload 0
 ldc 0
 if_icmpgt L13
 goto L11
L13:
 iload 0
 ldc 1
 isub 
 istore 0
L14:
 iload 0
 invokestatic Output/print(I)V
L15:
 goto L12
L11:
 return
.end method

.method public static main([Ljava/lang/String;)V
 invokestatic Output/run()V
 return
.end method


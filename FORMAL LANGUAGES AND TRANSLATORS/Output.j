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
 ldc 2
 istore 2
 istore 1
 istore 0
 ldc 5
 istore 3
 goto L1
L1:
 invokestatic Output/read()I
 invokestatic Output/read()I
 istore 5
 istore 4
 goto L2
L2:
 iload 4
 iload 5
 if_icmplt L4
 goto L5
L4:
 ldc 10
 istore 6
 goto L7
L7:
 ldc 0
 istore 7
L9:
 iload 7
 ldc 3
 if_icmplt L10
 goto L8
L10:
L12:
 iload 6
 ldc 13
 if_icmplt L13
 goto L11
L13:
 iload 6
 invokestatic Output/print(I)V
 goto L14
L14:
 iload 0
 ldc 10
 if_icmplt L16
 goto L17
L16:
 ldc 80
 istore 0
 goto L19
L19:
 iload 0
 invokestatic Output/print(I)V
 iload 1
 invokestatic Output/print(I)V
 iload 2
 invokestatic Output/print(I)V
 goto L20
L20:
 goto L15
L17:
 ldc 1
 invokestatic Output/print(I)V
 goto L21
L21:
 goto L18
L18:
L15:
 iload 6
 ldc 1
 iadd 
 istore 6
 goto L22
L22:
 goto L12
L11:
 iload 7
 ldc 1
 iadd 
 istore 7
 goto L23
L23:
 iload 7
 invokestatic Output/print(I)V
 goto L24
L24:
 goto L9
L8:
L26:
 iload 7
 iload 3
 if_icmple L27
 goto L25
L27:
 iload 7
 invokestatic Output/print(I)V
 goto L28
L28:
 iload 7
 ldc 1
 iadd 
 istore 7
 goto L29
L29:
 goto L26
L25:
 goto L3
L5:
 iload 4
 invokestatic Output/print(I)V
 goto L30
L30:
 goto L6
L6:
L3:
 goto L0
L0:
 return
.end method

.method public static main([Ljava/lang/String;)V
 invokestatic Output/run()V
 return
.end method


package com.devcheat.bytecode;

import com.devcheat.io.DataReader;
import com.sun.org.apache.bcel.internal.classfile.ClassFormatException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vincent
 * Date: 3/03/13
 * Time: 12:02
 * To change this template use File | Settings | File Templates.
 */
public class ConstantPool {
    public final static int CONSTANT_Class = 7;
    public final static int CONSTANT_Fieldref = 9;
    public final static int CONSTANT_Methodref = 10;
    public final static int CONSTANT_InterfaceMethodref = 11;
    public final static int CONSTANT_String = 8;
    public final static int CONSTANT_Integer = 3;
    public final static int CONSTANT_Float = 4;
    public final static int CONSTANT_Long = 5;
    public final static int CONSTANT_Double = 6;
    public final static int CONSTANT_NameAndType = 12;
    public final static int CONSTANT_Utf8 = 1;
    public final static int CONSTANT_MethodHandle = 15;
    public final static int CONSTANT_MethodType = 16;
    public final static int CONSTANT_InvokeDynamic = 18;

    public ConstantPool(){

    }

    private List<Integer> tags;
    private List<Integer> indices1;
    private List<Integer> indices2;
    private List<Object> constants;

    protected void parse(DataReader stream) {
        int count = stream.readUnsignedShort();
        tags = new ArrayList<Integer>(count);
        indices1 = new ArrayList<Integer>(count);
        indices2 = new ArrayList<Integer>(count);
        constants = new ArrayList<Object>(count);

        for (int i = 1; i < count; i++) {
            int tag = stream.readUnsignedByte();
            tags.add(tag);
            switch (tag) {
                case CONSTANT_Class:
                    indices1.add(stream.readUnsignedShort());
                    break;
                case CONSTANT_Fieldref:
                case CONSTANT_Methodref:
                case CONSTANT_InterfaceMethodref:
                    indices1.add(stream.readUnsignedShort());
                    indices2.add(stream.readUnsignedShort());
                    break;
                case CONSTANT_String:
                    indices1.add(stream.readUnsignedShort());
                    break;
                case CONSTANT_Integer:
                    constants.add(stream.readInt());
                    break;
                case CONSTANT_Float:
                    constants.add(stream.readFloat());
                    break;
                case CONSTANT_Long:
                    constants.add(stream.readLong());
                    tags.add(-CONSTANT_Long);
                    break;
                case CONSTANT_Double:
                    constants.add(stream.readDouble());
                    tags.add(-CONSTANT_Double);
                    break;
                case CONSTANT_NameAndType:
                    indices1.add(stream.readUnsignedShort());
                    indices2.add(stream.readUnsignedShort());
                    break;
                case CONSTANT_Utf8:
                    constants.add(stream.readUTF().intern());
                    // System.out.println("UTF8: "+constants[i]);
                    break;
                default:
                    throw new ClassFormatException("unknown constant tag");
            }
        }
    }
}

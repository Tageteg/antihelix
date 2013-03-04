package com.devcheat.bytecode;

import com.devcheat.io.DataReader;

/**
 * Created with IntelliJ IDEA.
 * User: Vincent
 * Date: 3/03/13
 * Time: 12:12
 * To change this template use File | Settings | File Templates.
 */
public class ClassInfo {

    public ClassInfo(byte[] data){
        parse(new DataReader(data));
    }

    private void parse(DataReader stream) {

    }

    public byte[] toByteArray() {
        return null;
    }
}

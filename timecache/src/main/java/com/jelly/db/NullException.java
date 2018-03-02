package com.jelly.db;

/**
 * Author YC
 * 2018/3/2 0002.
 */

public class NullException extends RuntimeException {
    public  NullException(){
        super();
    }
    public NullException(Exception e){
        super(e);
    }
    public  NullException(String e){
        super(e);
    }
}

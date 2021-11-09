package com.it.hbase.insert;

import java.util.Iterator;

/**
 * @version : 1.0
 * @description
 * @Date 10:03 2017/7/18
 * @auth :
 */
public class HBaseInsertException extends Exception{
    public HBaseInsertException(String message) {
        super(message);
    }

    public final synchronized void addSuppresseds(Iterable<Exception> exceptions){

        if(exceptions != null){
            Iterator<Exception> iterator = exceptions.iterator();
            while (iterator.hasNext()){
                addSuppressed(iterator.next());
            }
        }
    }
}

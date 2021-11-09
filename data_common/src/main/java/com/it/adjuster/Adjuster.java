package com.it.adjuster;

/**
 * Description:
 *      数据调整接口
 * @author
 * @version 1.0
 * @date 2017/7/10 15:17
 */
public interface Adjuster<T, E> {
    E doAdjust(T data);
}
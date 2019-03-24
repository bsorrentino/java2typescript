/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsc.processor;

/**
 * 
 * @author bsorrentino
 *
 * @param <T0>
 * @param <T1>
 * @param <T2>
 */
public class Tuple3<T0,T1,T2> extends Tuple2<T0,T1> {
    final T2 $2;
    
    public Tuple3(T0 $0, T1 $1, T2 $2) {
        super($0, $1);
        this.$2 = $2;
    }
    @Override
    public String toString() {
        return String.format("%s{$0=%s,$1=%s,$2=%s}", 
                getClass().getSimpleName(),
                String.valueOf($0),
                String.valueOf($1),
                String.valueOf($2)
        );
    }
    
}

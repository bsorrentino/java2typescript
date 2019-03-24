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
 */
public class Tuple2<T0,T1> {
    final T0 $0;
    final T1 $1;

    public Tuple2(T0 $0, T1 $1) {
        this.$0 = $0;
        this.$1 = $1;
    }

    @Override
    public String toString() {
        return String.format("%s{$0=%s,$1=%s }", 
                getClass().getSimpleName(),
                String.valueOf($0),
                String.valueOf($1)
        );
    }
    
}

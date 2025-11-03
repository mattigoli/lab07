package it.unibo.inner.test.impl;

import java.util.Iterator;

import it.unibo.inner.api.IterableWithPolicy;
import it.unibo.inner.api.Predicate;

public class IterableWithPolicyImpl<T> implements IterableWithPolicy<T>{

    private final T[] elements;
    private Predicate<T> filter;

    public IterableWithPolicyImpl(T[] elements){
        this(elements, (T e) -> true);
        
    }

    public IterableWithPolicyImpl(T[] elements, Predicate<T> filter){
        this.elements = elements;
        this.filter = filter;
    }

    @Override
    public Iterator<T> iterator() {
        return new IteratorWithPolicy();
    }

    private class IteratorWithPolicy implements Iterator<T>{

        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            while(currentIndex < elements.length){
                if(!filter.test(elements[currentIndex])){
                    currentIndex++;
                } else {
                    return true;
                }
            }
            return false;
        }

        @Override
        public T next() {
            T current = elements[currentIndex];
            while(!filter.test(current) && currentIndex < elements.length){
                currentIndex++;
                current = elements[currentIndex];
            }

            if(filter.test(current)){
                currentIndex++;
                return current;
            }
            return null;
        }
        
    }

    @Override
    public void setIterationPolicy(Predicate<T> filter) {
        this.filter = filter;
    }

}

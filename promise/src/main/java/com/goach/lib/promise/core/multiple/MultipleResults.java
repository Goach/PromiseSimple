package com.goach.lib.promise.core.multiple;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * author: Goach.zhong
 * Date: 2018/12/10 13:38.
 * Des:
 */
public class MultipleResults implements Iterable<OneResult> {
    private final List<OneResult> results;

    public MultipleResults(int size) {
        this.results = new CopyOnWriteArrayList<OneResult>(new OneResult[size]);
    }

    protected void set(int index, OneResult result) {
        results.set(index, result);
    }

    public OneResult get(int index) {
        return results.get(index);
    }

    public Iterator<OneResult> iterator() {
        return results.iterator();
    }

    public int size() {
        return results.size();
    }

    @Override
    public String toString() {
        return "MultipleResults [results=" + results + "]";
    }
}

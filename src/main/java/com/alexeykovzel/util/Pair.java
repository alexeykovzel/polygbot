package com.alexeykovzel.util;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
public class Pair<A, B> implements Serializable {
    private A a;
    private B b;

    public A getFirst() {
        return a;
    }

    public B getSecond() {
        return b;
    }

    @Override
    public String toString() {
        return String.format("Pair{a=%s, b=%s}", a, b);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(a, pair.a) && Objects.equals(b, pair.b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }
}


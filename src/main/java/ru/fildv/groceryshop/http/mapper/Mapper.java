package ru.fildv.groceryshop.http.mapper;

public interface Mapper<F, T> {
    T map(F from);

    default T map(F from, T to) {
        return to;
    }
}

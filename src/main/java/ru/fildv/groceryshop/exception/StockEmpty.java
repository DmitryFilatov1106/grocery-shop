package ru.fildv.groceryshop.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class StockEmpty extends RuntimeException {
    private final List<Integer> deficiency;
}

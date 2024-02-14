package com.github.wesleybritovlk.leasingserviceapi.product;

import org.springframework.web.server.ResponseStatusException;

import java.math.BigInteger;

import static org.springframework.http.HttpStatus.CONFLICT;

public class ProductPolitic {
    public static void returnQuantityToStock(Product findProduct, BigInteger quantityToLeased) {
        findProduct.setQuantityInStock(findProduct.getQuantityInStock().add(quantityToLeased));
    }

    public static void retireQuantityInStock(Product findProduct, BigInteger requestQuantityToLeased) {
        BigInteger quantityInStock = findProduct.getQuantityInStock(), stockHandler;
        if (quantityInStock.compareTo(requestQuantityToLeased) >= 0) {
            stockHandler = quantityInStock.subtract(requestQuantityToLeased);
            findProduct.setQuantityInStock(stockHandler);
        } else {
            var message = "The item quantity is greater than the stock quantity";
            throw new ResponseStatusException(CONFLICT, message);
        }
    }

    public static void updateQuantityInStock(Product findProduct, BigInteger quantityToLeased, BigInteger requestQuantityToLeased) {
        BigInteger quantityInStock = findProduct.getQuantityInStock(), stockHandler;
        if (requestQuantityToLeased.compareTo(quantityToLeased) < 0) {
            stockHandler = quantityToLeased.subtract(requestQuantityToLeased);
            findProduct.setQuantityInStock(quantityInStock.add(stockHandler));
        } else {
            stockHandler = requestQuantityToLeased.subtract(quantityToLeased);
            if (quantityInStock.compareTo(stockHandler) >= 0)
                findProduct.setQuantityInStock(quantityInStock.subtract(stockHandler));
            else {
                var message = "The item quantity is greater than the stock quantity";
                throw new ResponseStatusException(CONFLICT, message);
            }
        }
    }
}
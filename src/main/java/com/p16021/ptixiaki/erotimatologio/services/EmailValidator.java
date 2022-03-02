package com.p16021.ptixiaki.erotimatologio.services;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
public class EmailValidator implements Predicate<String> {

    @Override
    public boolean test(String s) {
        //TODO: regex gia na dexete mo pxxxx@unipi.gr
        return true;
    }
}

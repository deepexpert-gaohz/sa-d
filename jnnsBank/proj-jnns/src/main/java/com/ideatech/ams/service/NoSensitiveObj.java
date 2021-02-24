package com.ideatech.ams.service;
public interface NoSensitiveObj<T> {

    default T noSensitiveObj(){
        return (T) this;
    }
}
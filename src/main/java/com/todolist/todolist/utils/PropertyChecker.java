package com.todolist.todolist.utils;

import org.springframework.stereotype.Service;

@Service
public record PropertyChecker() {

    public Boolean isPropertiesSame(Object requestProperty, Object entityProperty){

        return (requestProperty == null) || (requestProperty.equals(entityProperty));
    }
}

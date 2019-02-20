package com.dm.teamquery.execption;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class InvalidParameterException extends Exception {


    public InvalidParameterException(){
        super();
    }

    public InvalidParameterException (String message) {
        super(message);
        errorList.add(message);
    }

    private  List<String> errorList = new ArrayList<>();

    public List<String> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<String> errorList) {
        this.errorList = errorList;
    }
}
package com.dyc.youthvibe.GetterSetter;

public class GetterSetterEnq {

    private String name, desg, number;

    public GetterSetterEnq(String name, String desg, String number){

            this.name = name;
            this.desg = desg;
            this.number = number;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesg() {
        return desg;
    }

    public void setDesg(String desg) {
        this.desg = desg;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}

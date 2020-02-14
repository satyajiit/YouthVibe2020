package com.dyc.youthvibe.GetterSetter;

public class BlogsModel {

    String id, cllg, msg, name;

    Boolean validate = null;

    public String getName() {
        return name;
    }

    public BlogsModel(String id, String cllg, String msg, String name) {
        this.id = id;
        this.cllg = cllg;
        this.msg = msg;
        this.name = name;
    }

    public Boolean getValidate() {
        return validate;
    }

    public BlogsModel(String id, String cllg, String msg, String name, Boolean validate) {
        this.id = id;
        this.cllg = cllg;
        this.msg = msg;
        this.name = name;
        this.validate = validate;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCllg() {
        return cllg;
    }

    public void setCllg(String cllg) {
        this.cllg = cllg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

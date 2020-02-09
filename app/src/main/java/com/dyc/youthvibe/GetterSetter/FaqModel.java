package com.dyc.youthvibe.GetterSetter;

public class FaqModel {

    private String question, ans;

    public FaqModel(String question, String ans) {
        this.question = question;
        this.ans = ans;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }
}

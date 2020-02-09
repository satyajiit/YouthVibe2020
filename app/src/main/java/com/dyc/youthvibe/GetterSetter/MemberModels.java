package com.dyc.youthvibe.GetterSetter;

public class MemberModels {

    private String memberName, memberDesg, imgUrl, sLink;

    private int color;

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberDesg() {
        return memberDesg;
    }

    public void setMemberDesg(String memberDesg) {
        this.memberDesg = memberDesg;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getsLink() {
        return sLink;
    }


    public void setsLink(String sLink) {
        this.sLink = sLink;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public MemberModels(String memberName, String memberDesg, String imgUrl, String sLink, int color){

        this.imgUrl = imgUrl;
        this.memberName = memberName;
        this.memberDesg = memberDesg;
        this.sLink = sLink;
        this.color = color;

    }

}

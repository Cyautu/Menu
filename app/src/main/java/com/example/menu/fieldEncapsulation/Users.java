package com.example.menu.fieldEncapsulation;

import java.util.Date;

public class Users {
    private int    userID;//用户序号
    private String userName;//账号
    private String userSex;//用户性别
    private Date   userBirthday;//用户出生年月
    private String userNikeName;//用户昵称
    private String userPassword;//用户密码
    private String userPhoto;//用户头像
    private String userPosition;//用户职位
    private String userProjectDept;//用户所在项目部
    private String userCompany;//用户所在公司
    private String userIDNumber;//用户身份证号
    private String userTel;//用户电话号码
    private String userEmail;//用户邮箱
    private String userTrueName;//用户姓名
    private Date   userRegisterDate;//用户注册时间
    private String userIntroduction;//个人简介
    private String Security1;//设置问题1
    private String Answer1;//答案1
    private String Security2;//设置问题2
    private String Answer2;//答案2
    private String Security3;//设置问题3
    private String Answer3;//答案3

    public Users(){}

    public Users(int userID, String userName, String userSex, Date userBirthday, String userNikeName, String userPassword,
                 String userPhoto, String userPosition, String userProjectDept, String userCompany, String userIDNumber, String userTel,
                 String userEmail, String userTrueName, Date userRegisterDate,String userIntroduction,String Security1, String Answer1, String Security2, String Answer2, String Security3, String Answer3){
        this.userID = userID;
        this.userName = userName;
        this.userSex = userSex;
        this.userBirthday = userBirthday;
        this.userNikeName = userNikeName;
        this.userPassword = userPassword;
        this.userPhoto = userPhoto;
        this.userPosition = userPosition;
        this.userProjectDept = userProjectDept;
        this.userCompany = userCompany;
        this.userIDNumber = userIDNumber;
        this.userTel = userTel;
        this.userEmail = userEmail;
        this.userTrueName = userTrueName;
        this.userRegisterDate = userRegisterDate;
        this.userIntroduction = userIntroduction;
        this.Security1 = Security1;
        this.Answer1 = Answer1;
        this.Security2 = Security2;
        this.Answer2 = Answer2;
        this.Security3 = Security3;
        this.Answer3 = Answer3;

    }

    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSex() {
        return userSex;
    }
    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public Date getUserBirthday() {
        return userBirthday;
    }
    public void setUserBirthday(Date userBirthday) {
        this.userBirthday = userBirthday;
    }

    public String getUserNikeName() {
        return userNikeName;
    }
    public void setUserNikeName(String userNikeName) {
        this.userNikeName = userNikeName;
    }

    public String getUserPassword() {
        return userPassword;
    }
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserPhoto() {
        return userPhoto;
    }
    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserPosition() {
        return userPosition;
    }
    public void setUserPosition(String userPosition) {
        this.userPosition = userPosition;
    }

    public String getUserProjectDept() {
        return userProjectDept;
    }
    public void setUserProjectDept(String userProjectDept) {
        this.userProjectDept = userProjectDept;
    }

    public String getUserCompany() {
        return userCompany;
    }
    public void setUserCompany(String userCompany) {
        this.userCompany = userCompany;
    }

    public String getUserIDNumber() {
        return userIDNumber;
    }
    public void setUserIDNumber(String userIDNumber) {
        this.userIDNumber = userIDNumber;
    }

    public String getUserTel() {
        return userTel;
    }
    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    public String getUserEmail() {
        return userEmail;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserTrueName() {
        return userTrueName;
    }
    public void setUserTrueName(String userTrueName) {
        this.userTrueName = userTrueName;
    }

    public Date getUserRegisterDate() {
        return userRegisterDate;
    }
    public void setUserRegisterDate(Date userRegisterDate) {
        this.userRegisterDate = userRegisterDate;
    }

    public String getUserIntroduction() {
        return userIntroduction;
    }
    public void setUserIntroduction(String userIntroduction) {
        this.userIntroduction = userIntroduction;
    }

    public String getSecurity1() {
        return Security1;
    }
    public void setSecurity1(String security1) {
        Security1 = security1;
    }

    public String getAnswer1() {
        return Answer1;
    }
    public void setAnswer1(String answer1) {
        Answer1 = answer1;
    }

    public String getSecurity2() {
        return Security2;
    }
    public void setSecurity2(String security2) {
        Security2 = security2;
    }

    public String getAnswer2() {
        return Answer2;
    }
    public void setAnswer2(String answer2) {
        Answer2 = answer2;
    }

    public String getSecurity3() {
        return Security3;
    }
    public void setSecurity3(String security3) {
        Security3 = security3;
    }

    public String getAnswer3() {
        return Answer3;
    }
    public void setAnswer3(String answer3) {
        Answer3 = answer3;
    }

}

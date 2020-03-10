package com.example.menu.DB;


import com.example.menu.fieldEncapsulation.QuestionsQB;
import com.example.menu.fieldEncapsulation.Training;
import com.example.menu.fieldEncapsulation.Users;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBUrl {
    //数据库链接方法，作为数据库链接的接口
    public static Connection getSQLConnection() {
        String Url=URLBase.getURL(3);//URLBase.getURL(3)数据库链接
        String User="sa";//数据库登录名
        String Pwd="qycxzx123#";//数据库登录密码
        Connection con = null;
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
           /* System.out.println("加载驱动成功");*/
            con = DriverManager.getConnection(Url, User, Pwd);
            /*System.out.println("数据库链接成功");*/
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return con;
    }
    //登录方法,验证用户是否存在
    public static String getLoginInfo(String un){
        String up = "";
        try {
            Connection conn = getSQLConnection();
            String sql = "select * from Users where user_Name = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,un);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                up = rs.getString("user_Password");
            }
            rs.close();
            stmt.close();
            conn.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return up;
    }
    //通过用户名获取用户设置的安全问题
    public ArrayList<Users> getSafeQuestion(String userName){
        ArrayList<Users> questionArrayList = new ArrayList<>();
        String sql = "select Security1,Security2,Security3 from Users where user_Name=?";
        try{
            Connection con=getSQLConnection();
            PreparedStatement ptst = con.prepareStatement(sql);
            ptst.setString(1,userName);
            ResultSet rs=ptst.executeQuery();
            while (rs.next()){
                Users turn = new Users();
                turn.setSecurity1(rs.getString("Security1"));
                turn.setSecurity2(rs.getString("Security2"));
                turn.setSecurity3(rs.getString("Security3"));
                questionArrayList.add(turn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return questionArrayList;
    }
    //验证安全问题
    public boolean getSafeVerification(String userName,String answer1,String answer2,String answer3){
        String sql = "select * from  Users where user_Name=? and Answer1=? and Answer2=? and Answer3=?";
        boolean result=false;
        try{
            Connection con = getSQLConnection();
            PreparedStatement ptst = con.prepareStatement(sql);
            ptst.setString(1,userName);
            ptst.setString(2,answer1);
            ptst.setString(3,answer2);
            ptst.setString(4,answer3);
            ResultSet rs = ptst.executeQuery();
            if (rs.next()){
                result=true;
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    //密码重置
    public void setNewPassword(String userName, String newPassword){
        String sql="update Users set user_Password=? where user_Name=?";
        boolean result=false;
        try {
            Connection con=getSQLConnection();
            PreparedStatement ptst = con.prepareStatement(sql);
            ptst.setString(1,newPassword);
            ptst.setString(2,userName);
            ResultSet rs = ptst.executeQuery();
            if (rs.next()){
                result=true;
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //查询用户的头像和昵称以及用户ID
    public String[] getLoginInformation(String userName){
        String[] result=new String[3];
        String sql="select user_ID,user_NickName,user_Icon from Users where user_Name=?";
        try{
            Connection con = getSQLConnection();
            PreparedStatement ptst = con.prepareStatement(sql);
            ptst.setString(1,userName);
            ResultSet rs=ptst.executeQuery();
            while (rs.next()){
                result[0]=rs.getString("user_ID");
                result[1]=rs.getString("user_Icon");
                result[2]=rs.getString("user_NickName");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    //查询用户的基本信息-昵称、性别、简介
    public ArrayList<Users> getOrdinaryInformation(String userID){
        ArrayList<Users> information = new ArrayList<>();
        String sql = "select user_NickName,user_Sex,user_Introduction from Users where user_ID=?";
        try{
            Connection con=getSQLConnection();
            PreparedStatement ptst = con.prepareStatement(sql);
            ptst.setString(1,userID);
            ResultSet rs=ptst.executeQuery();
            while (rs.next()){
                Users turn = new Users();
                turn.setUserSex(rs.getString("user_Sex"));
                turn.setUserIntroduction(rs.getString("user_Introduction"));
                turn.setUserNikeName(rs.getString("user_NickName"));
                information.add(turn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return information;
    }
    //用户修改基本信息
    public boolean setOrdinaryInformation(String userID,String nickName,String Sex,String introduction){
        String sql="update Users set user_NickName=?,user_Sex=?,user_Introduction=? where user_ID=?";
        boolean result = false;
        try{
            Connection con = getSQLConnection();
            PreparedStatement ptst = con.prepareStatement(sql);
            ptst.setString(1,nickName);
            ptst.setString(2,Sex);
            ptst.setString(3,introduction);
            ptst.setString(4,userID);
            ptst.executeUpdate();
            result=true;
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    //搜索用户
    public static String[] search(String un){
        String[] result={"","","",""};
        try {
            Connection conn = getSQLConnection();
            String sql = "select * from Users where user_Name like'%'+?+'%'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,un);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                result[0] += rs.getString("user_Name")+"丨";
                result[1] += rs.getString("user_Icon")+"丨";
                result[2] += rs.getString("user_Introduction")+"丨";
                result[3] += rs.getString("user_NickName")+"丨";
            }
            rs.close();
            stmt.close();
            conn.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }
    //获取id
    public static String getId(String un){
        String id = "";
        try {
            Connection conn = getSQLConnection();
            String sql = "select * from Users where user_Name = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,un);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                id = rs.getString("user_ID");
            }
            rs.close();
            stmt.close();
            conn.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return id;
    }
    //插入关注记录
    public static void followUsers(String Fans_ID, String Followed_ID){
        try{
            Connection conn = getSQLConnection();
            String sql = "INSERT INTO Follows(Fans_ID,Followed_ID,Follow_Time) VALUES (?,?,getdate())";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,Fans_ID);
            stmt.setString(2,Followed_ID);
            stmt.executeUpdate();
            stmt.close();
            conn.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    //获取关注状态
    public static boolean getFollowState(String fans, String follow){
        boolean state = false;
        try {
            Connection conn = getSQLConnection();
            String sql = "select * from Follows where Fans_ID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,fans);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                if(follow.equals(rs.getString("Followed_ID")))
                    state = true;
            }
            rs.close();
            stmt.close();
            conn.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return state;
    }
    //从follow表中获取自己的粉丝的ID并获取粉丝信息
    public ArrayList<Users>  getFans(String followID){
        ArrayList<Users> result = new ArrayList<>();
        ArrayList<Users> fansID = new ArrayList<>();
        String sql = "select Fans_ID from Follows where Followed_ID=?";
        String sql1 = "select user_Name, user_NickName,user_Icon,user_Introduction from Users where user_ID=?";
        try {//获取粉丝ID
            Connection conn = DBUrl.getSQLConnection();
            PreparedStatement psmt = conn.prepareStatement(sql);
            psmt.setString(1,followID);
            ResultSet rs = psmt.executeQuery();
            while (rs.next()){
                Users fans = new Users();
                fans.setUserID(rs.getInt("Fans_ID"));
                fansID.add(fans);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {//查询粉丝信息
            Connection con = DBUrl.getSQLConnection();
            for (Users fans:fansID){
                PreparedStatement ptst = con.prepareStatement(sql1);
                ptst.setInt(1,fans.getUserID());
                ResultSet re = ptst.executeQuery();
                while (re.next()){
                    Users r = new Users();
                    r.setUserPhoto(re.getString("user_Icon"));
                    r.setUserNikeName(re.getString("user_NickName"));
                    r.setUserIntroduction(re.getString("user_Introduction"));
                    r.setUserName(re.getString("user_Name"));
                    result.add(r);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    //从follow表中获取自己的关注人的ID并获取被关注人信息
    public ArrayList<Users>  getFollows(String userID){
        ArrayList<Users> result = new ArrayList<>();
        ArrayList<Users> followID = new ArrayList<>();
        String sql = "select Followed_ID from Follows where Fans_ID=?";
        String sql1 = "select user_Name, user_NickName,user_Icon,user_Introduction from Users where user_ID=?";
        try {//获取关注人ID
            Connection conn = DBUrl.getSQLConnection();
            PreparedStatement psmt = conn.prepareStatement(sql);
            psmt.setString(1,userID);
            ResultSet rs = psmt.executeQuery();
            while (rs.next()){
                Users fans = new Users();
                fans.setUserID(rs.getInt("Followed_ID"));
                followID.add(fans);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {//查询关注人信息
            Connection con = DBUrl.getSQLConnection();
            for (Users fans:followID){
                /*System.out.println("这些人是我关注的" + fans.getUserID());*/
                PreparedStatement ptst = con.prepareStatement(sql1);
                ptst.setInt(1,fans.getUserID());
                ResultSet re = ptst.executeQuery();
                while (re.next()){
                    Users r = new Users();
                    r.setUserPhoto(re.getString("user_Icon"));
                    r.setUserNikeName(re.getString("user_NickName"));
                    r.setUserIntroduction(re.getString("user_Introduction"));
                    r.setUserName(re.getString("user_Name"));
                    result.add(r);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    //取消关注，删除关注记录
    public static void unFollowUser(String Fans_ID, String Followed_ID) {
        try {
            Connection conn = getSQLConnection();
            String sql = "DELETE FROM Follows WHERE Fans_ID = ? AND Followed_ID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, Fans_ID);
            stmt.setString(2, Followed_ID);
            stmt.executeUpdate();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //考试、练习题库查询
    public ArrayList<QuestionsQB> getQuestion(String TBName){
        ArrayList<QuestionsQB> result = new ArrayList<>();
        int key = URLBase.getQuestionCode(TBName);
        String sql = "select * from "+TBName;
        try {
            Connection con = getSQLConnection();
            PreparedStatement ptst = con.prepareStatement(sql);
            ResultSet rs = ptst.executeQuery();
            switch (key){
                case 0:
                    while (rs.next()){
                        QuestionsQB r = new QuestionsQB();
                        r.setQBID(rs.getInt("QB000ID"));
                        r.setTypeID(rs.getInt("TypeID"));
                        r.setContent(rs.getString("Content_000"));
                        r.setAnswerA(rs.getString("AnswerA_000"));
                        r.setAnswerB(rs.getString("AnswerB_000"));
                        r.setAnswerC(rs.getString("AnswerC_000"));
                        r.setAnswerD(rs.getString("AnswerD_000"));
                        r.setAnswerE(rs.getString("AnswerE_000"));
                        r.setAnswerF(rs.getString("AnswerF_000"));
                        r.setRightAnswer(rs.getString("RightAnswer_000"));
                        r.setAnalyze(rs.getString("Analyze_000"));
                        result.add(r);
                    }
                    break;
                case 1:
                    while (rs.next()){
                        QuestionsQB r = new QuestionsQB();
                        r.setQBID(rs.getInt("QB010ID"));
                        r.setTypeID(rs.getInt("TypeID"));
                        r.setContent(rs.getString("Content_010"));
                        r.setAnswerA(rs.getString("AnswerA_010"));
                        r.setAnswerB(rs.getString("AnswerB_010"));
                        r.setAnswerC(rs.getString("AnswerC_010"));
                        r.setAnswerD(rs.getString("AnswerD_010"));
                        r.setAnswerE(rs.getString("AnswerE_010"));
                        r.setAnswerF(rs.getString("AnswerF_010"));
                        r.setRightAnswer(rs.getString("RightAnswer_010"));
                        r.setAnalyze(rs.getString("Analyze_010"));
                        result.add(r);
                    }
                    break;
                case 2:
                    while (rs.next()){
                        QuestionsQB r = new QuestionsQB();
                        r.setQBID(rs.getInt("QB011ID"));
                        r.setTypeID(rs.getInt("TypeID"));
                        r.setContent(rs.getString("Content_011"));
                        r.setAnswerA(rs.getString("AnswerA_011"));
                        r.setAnswerB(rs.getString("AnswerB_011"));
                        r.setAnswerC(rs.getString("AnswerC_011"));
                        r.setAnswerD(rs.getString("AnswerD_011"));
                        r.setAnswerE(rs.getString("AnswerE_011"));
                        r.setAnswerF(rs.getString("AnswerF_011"));
                        r.setRightAnswer(rs.getString("RightAnswer_011"));
                        r.setAnalyze(rs.getString("Analyze_011"));
                        result.add(r);
                    }
                    break;
                case 3:
                    while (rs.next()){
                        QuestionsQB r = new QuestionsQB();
                        r.setQBID(rs.getInt("QB020ID"));
                        r.setTypeID(rs.getInt("TypeID"));
                        r.setContent(rs.getString("Content_020"));
                        r.setAnswerA(rs.getString("AnswerA_020"));
                        r.setAnswerB(rs.getString("AnswerB_020"));
                        r.setAnswerC(rs.getString("AnswerC_020"));
                        r.setAnswerD(rs.getString("AnswerD_020"));
                        r.setAnswerE(rs.getString("AnswerE_020"));
                        r.setAnswerF(rs.getString("AnswerF_020"));
                        r.setRightAnswer(rs.getString("RightAnswer_020"));
                        r.setAnalyze(rs.getString("Analyze_020"));
                        result.add(r);
                    }
                    break;
                case 4:
                    while (rs.next()){
                        QuestionsQB r = new QuestionsQB();
                        r.setQBID(rs.getInt("QB021ID"));
                        r.setTypeID(rs.getInt("TypeID"));
                        r.setContent(rs.getString("Content_021"));
                        r.setAnswerA(rs.getString("AnswerA_021"));
                        r.setAnswerB(rs.getString("AnswerB_021"));
                        r.setAnswerC(rs.getString("AnswerC_021"));
                        r.setAnswerD(rs.getString("AnswerD_021"));
                        r.setAnswerE(rs.getString("AnswerE_021"));
                        r.setAnswerF(rs.getString("AnswerF_021"));
                        r.setRightAnswer(rs.getString("RightAnswer_021"));
                        r.setAnalyze(rs.getString("Analyze_021"));
                        result.add(r);
                    }
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  result;
    }
    //获取培训内容
    public static ArrayList<Training> getTraining(String key, String value){
        ArrayList<Training> result = new ArrayList<>();
        String sql = "select * from WeeklyTraining where " + key + " = " + "'" + value + "'";
        try {
            Connection con = getSQLConnection();
            PreparedStatement ptst = con.prepareStatement(sql);
            ResultSet rs = ptst.executeQuery();
            while (rs.next()){
                Training t = new Training();
                t.setTrainingID(rs.getInt("TrainingID"));
                t.setTrainingTitle(rs.getString("Training_Title"));
                t.setTrainingTime(rs.getString("Training_Time"));
                t.setPublishTime(rs.getString("PublishTime"));
                t.setTrainingSpeaker(rs.getString("Training_Speaker"));
                t.setOfCompany(rs.getString("Of_Company"));
                t.setOfProjectDep(rs.getString("Of_ProjectDep"));
                t.setTrainingContent(rs.getString("Training_Content"));
                t.setTrainingAccessoryPath(rs.getString("Training_AccessoryPath"));
                t.setTrainingImagePath(rs.getString("Training_ImagePath"));
                t.setTraining_VideoPath(rs.getString("Training_VideoPath"));
                t.setTrainingClickNum(rs.getString("Training_ClickNum"));
                result.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public static ArrayList<Training> getTraining(){
        ArrayList<Training> result = new ArrayList<>();
        String sql = "select * from WeeklyTraining";
        try {
            Connection con = getSQLConnection();
            PreparedStatement ptst = con.prepareStatement(sql);
            ResultSet rs = ptst.executeQuery();
            while (rs.next()){
                Training t = new Training();
                t.setTrainingID(rs.getInt("TrainingID"));
                t.setTrainingTitle(rs.getString("Training_Title"));
                t.setTrainingTime(rs.getString("Training_Time"));
                t.setPublishTime(rs.getString("PublishTime"));
                t.setTrainingSpeaker(rs.getString("Training_Speaker"));
                t.setOfCompany(rs.getString("Of_Company"));
                t.setOfProjectDep(rs.getString("Of_ProjectDep"));
                t.setTrainingContent(rs.getString("Training_Content"));
                t.setTrainingAccessoryPath(rs.getString("Training_AccessoryPath"));
                t.setTrainingImagePath(rs.getString("Training_ImagePath"));
                t.setTraining_VideoPath(rs.getString("Training_VideoPath"));
                t.setTrainingClickNum(rs.getString("Training_ClickNum"));
                result.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    //获取路径
    public static String getTrainingPath(String pathType, int TrainingID){
        String result = null;
        String sql = "select " + pathType + " from WeeklyTraining where TrainingID = '" + TrainingID + "'";
        try {
            Connection con = getSQLConnection();
            PreparedStatement ptst = con.prepareStatement(sql);
            ResultSet rs = ptst.executeQuery();
            while (rs.next()){
                result = (rs.getString(pathType)) ;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    //更新点击量
    public void updateClickNum(int trainingID){
        String sql="update WeeklyTraining set Training_ClickNum += 1 where TrainingID = ?";
        try{
            Connection con = getSQLConnection();
            PreparedStatement ptst = con.prepareStatement(sql);
            ptst.setString(1, String.valueOf(trainingID));
            ptst.executeUpdate();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*public  static void main(String[] args){
        new DBUrl().updateClickNum("66");

    }*/
}
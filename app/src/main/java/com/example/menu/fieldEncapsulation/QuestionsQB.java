package com.example.menu.fieldEncapsulation;

import com.example.menu.R;

public class QuestionsQB {
    private int QBID;//精细化题目ID
    private int TypeID;//题目类型1表示选择，2表示判断，3表示填空，4表示简答
    private String Content;//题目正文内容
    private String AnswerA;//选项A
    private String AnswerB;//选项B
    private String AnswerC;//选项C
    private String AnswerD;//选项D
    private String AnswerE;//选项E
    private String AnswerF;//选项F
    private String RightAnswer;//正确答案
    private String Analyze;//解析
    public QuestionsQB(){}
    public QuestionsQB(int QBID,int TypeID,String Content,String AnswerA,String AnswerB,String AnswerC,String AnswerD,String AnswerE,
                       String AnswerF,String RightAnswer,String Analyze){
        this.QBID = QBID;
        this.TypeID = TypeID;
        this.Content = Content;
        this.AnswerA = AnswerA;
        this.AnswerB = AnswerB;
        this.AnswerC = AnswerC;
        this.AnswerD = AnswerD;
        this.AnswerE = AnswerE;
        this.AnswerF = AnswerF;
        this.RightAnswer = RightAnswer;
        this.Analyze = Analyze;
    }

    public void setQBID(int QBID) {
        this.QBID = QBID;
    }
    public int getQBID() {
        return QBID;
    }

    public void setTypeID(int typeID) {
        TypeID = typeID;
    }
    public int getTypeID() {
        return TypeID;
    }

    public void setContent(String content) {
        Content = content;
    }
    public String getContent() {
        return Content;
    }

    public String getAnswerA() {
        return AnswerA;
    }
    public void setAnswerA(String answerA) {
        AnswerA = answerA;
    }

    public String getAnswerB() {
        return AnswerB;
    }
    public void setAnswerB(String answerB) {
        AnswerB = answerB;
    }

    public String getAnswerC() {
        return AnswerC;
    }
    public void setAnswerC(String answerC) {
        AnswerC = answerC;
    }

    public String getAnswerD() {
        return AnswerD;
    }
    public void setAnswerD(String answerD) {
        AnswerD = answerD;
    }

    public String getAnswerE() {
        return AnswerE;
    }
    public void setAnswerE(String answerE) {
        AnswerE = answerE;
    }

    public String getAnswerF() {
        return AnswerF;
    }
    public void setAnswerF(String answerF) {
        AnswerF = answerF;
    }

    public String getRightAnswer() {
        return RightAnswer;
    }
    public void setRightAnswer(String rightAnswer) {
        RightAnswer = rightAnswer;
    }

    public String getAnalyze() {
        return Analyze;
    }
    public void setAnalyze(String analyze) {
        Analyze = analyze;
    }
}

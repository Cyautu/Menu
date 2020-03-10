package com.example.menu.fieldEncapsulation;

import java.io.Serializable;

public class Training implements Serializable {
    private int TrainingID;                          //培训ID
    private String Training_Title;                      //培训标题
    private String Training_Time;                      //培训时间
    private String PublishTime;                      //培训时间
    private String Training_Speaker;                   //主讲人
    private String Of_Company;                         //所属公司
    private String Of_ProjectDep;                      //所属项目部
    private String Training_Content;                   //培训内容
    private String Training_AccessoryPath;             //附件路径
    private String Training_ImagePath;                 //图片路径
    private String Training_VideoPath;                 //视频路径
    private String Training_ClickNum;                  //点击量

    public Training(){}

    public Training(int TrainingID, String Training_Title, String Training_Time, String PublishTime, String Training_Speaker, String Of_Company,
                    String Of_ProjectDep, String Training_Content, String Training_AccessoryPath, String Training_ImagePath, String Training_VideoPath, String Training_ClickNum){
        this.TrainingID = TrainingID;
        this.Training_Title = Training_Title;
        this.Training_Time = Training_Time;
        this.PublishTime = PublishTime;
        this.Training_Speaker = Training_Speaker;
        this.Of_Company = Of_Company;
        this.Of_ProjectDep = Of_ProjectDep;
        this.Training_Content = Training_Content;
        this.Training_AccessoryPath = Training_AccessoryPath;
        this.Training_ImagePath = Training_ImagePath;
        this.Training_VideoPath = Training_VideoPath;
        this.Training_ClickNum = Training_ClickNum;
    }

    public int getTrainingID(){return TrainingID;}
    public void setTrainingID(int trainingID){this.TrainingID = trainingID;}

    public String getTrainingTitle(){return Training_Title;}
    public void  setTrainingTitle(String training_title){this.Training_Title = training_title;}

    public String getTrainingTime(){return Training_Time;}
    public void setTrainingTime(String training_time){this.Training_Time = training_time;}

    public String getPublishTime(){return PublishTime;}
    public void setPublishTime(String publishTime){this.PublishTime = publishTime;}

    public String getTrainingSpeaker(){return Training_Speaker;}
    public void setTrainingSpeaker(String training_speaker){this.Training_Speaker = training_speaker;}

    public String getOfCompany(){return Of_Company;}
    public void setOfCompany(String of_company){this.Of_Company = of_company;}

    public String getOfProjectDep(){return Of_ProjectDep;}
    public void setOfProjectDep(String of_projectDep){this.Of_ProjectDep = of_projectDep;}

    public String getTrainingContent(){return Training_Content;}
    public void setTrainingContent(String training_content){this.Training_Content = training_content;}

    public String getTrainingAccessoryPath(){return Training_AccessoryPath;}
    public void setTrainingAccessoryPath(String training_accessoryPath){this.Training_AccessoryPath = training_accessoryPath;}

    public String getTrainingImagePath(){return Training_ImagePath;}
    public void setTrainingImagePath(String training_imagePath){this.Training_ImagePath = training_imagePath;}

    public String getTraining_VideoPath(){return Training_VideoPath;}
    public void setTraining_VideoPath(String training_videoPath){this.Training_VideoPath = training_videoPath;}

    public String getTrainingClickNum() { return Training_ClickNum; }
    public void setTrainingClickNum(String training_ClickNum) { Training_ClickNum = training_ClickNum; }
}

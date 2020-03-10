package com.example.menu.ui.mainHome.examination.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.renderscript.ScriptGroup;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import com.example.menu.DB.DBUrl;
import com.example.menu.R;
import com.example.menu.fieldEncapsulation.QuestionsQB;
import com.example.menu.ui.loadingFlash.LoadProgressDialog;
import java.util.ArrayList;
import java.util.Objects;


public class SequentialPracticeActivity extends AppCompatActivity {
    HorizontalScrollView questionNStyle1;//横版题号
    ScrollView questionNStyle2;//展开版题号
    LinearLayout addQuestionN;//添加横版题号
    GridLayout addQuestionN1;//添加展开版题号
    TextView lastClick,lastClick1,questionTotal;//上一次点击的题目、题目总数
    int questionId[];//题号ID
    int j ;
    private ViewPager questionPager;
    private ArrayList<Fragment> listQuestion;
    int queryMsg = 0;
    CheckBox chooseA,chooseB,chooseC,chooseD,chooseE,chooseF;
    private SequentialAdapter adapter;
    Button submitAnswer,openNumberV;//答案提交按钮、展开题号按钮
    EditText inputAnswer;//用户输入的答案框
    private TextView questionTitle,selectionA,selectionB,selectionC,selectionD,selectionE,
            selectionF,rightAnswer,questionAnalyze;
    LinearLayout selectionView,inputView,rightAnswerView;//选项区和手动输入区以及正确答案、解析区域
    private ArrayList<QuestionsQB> questionsQB;
    LoadProgressDialog loadProgressDialog;
    ArrayList<View> viewpagelist;//存储每题的页面
    private LayoutInflater inflter;
    private char[] choose ;//存储用户选择的选项
    int flag,openChange;//正确答案判断数，为1表示该选项是正确答案的其中之一,openChange用于判断题号展开状态，0未展开，1为展开
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sequential_practice);
        inflter = LayoutInflater.from(this);
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        if (info == null){
            Toast.makeText(getApplicationContext(),"请检查你的网络连接···",Toast.LENGTH_SHORT).show();
        }
        questionPager = (ViewPager)findViewById(R.id.sequential_viewPager);
        //加载动画开始
        loadProgressDialog = new LoadProgressDialog(SequentialPracticeActivity.this,"加载题目中…");
        loadProgressDialog.show();

        //题号展开与合拢
        addQuestionN = findViewById(R.id.question_number_view1);
        addQuestionN1 = findViewById(R.id.question_number_view2);
        openNumberV  = findViewById(R.id.button_open_question_number);
        questionNStyle1 = findViewById(R.id.question_number_style1);
        questionNStyle2 = findViewById(R.id.question_number_style2);
        openChange = 0;//设置初始状态为0
        openNumberV.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                switch (openChange){
                    case 0:
                        questionNStyle1.setVisibility(View.INVISIBLE);//虽然隐藏但还占据控件
                        questionNStyle2.setVisibility(View.VISIBLE);//显示展开题号
                        openNumberV.setBackground(getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp));//设置按钮向上
                        openChange = 1;//设置打开状态值
                        break;
                    case 1:
                        questionNStyle1.setVisibility(View.VISIBLE);//虽然隐藏但还占据控件
                        questionNStyle2.setVisibility(View.GONE);//显示展开题号
                        openNumberV.setBackground(getDrawable(R.drawable.ic_keyboard_arrow_down_black_24dp));//设置按钮向下
                        openChange = 0;//设置关闭状态值
                        break;
                }
            }
        });

        //从数据库获取题库
        new Thread(){
            @Override
            public void run() {
                DBUrl dbUrl = new DBUrl();
                questionsQB = dbUrl.getQuestion("QB000");
                Message msg = new Message();
                msg.what = queryMsg;
                handler.sendMessage(msg);
            }
        }.start();
        //返回按钮
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            ((ActionBar) actionBar).setHomeButtonEnabled(true);
            ((ActionBar) actionBar).setDisplayHomeAsUpEnabled(true);
        }
    }
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("SetTextI18n")
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == queryMsg) {
                viewpagelist = new ArrayList<View>();
                questionId = new int[2*questionsQB.size()];
                //左上角显示题目总数
                String total = String.valueOf(questionsQB.size());
                questionTotal = findViewById(R.id.question_total);
                questionTotal.setText(total);
                //循环生成每一题view
                for (int i=0; i < questionsQB.size(); i++){//通过循环生成所有题目的页面
                    View view = inflter.inflate(R.layout.fragment_sequential_page,null);
                    questionTitle = view.findViewById(R.id.question_title);
                    selectionA = view.findViewById(R.id.text_answerA);
                    selectionB = view.findViewById(R.id.text_answerB);
                    selectionC = view.findViewById(R.id.text_answerC);
                    selectionD = view.findViewById(R.id.text_answerD);
                    selectionE = view.findViewById(R.id.text_answerE);
                    selectionF = view.findViewById(R.id.text_answerF);
                    chooseA = view.findViewById(R.id.exam_select_button_A);
                    chooseB = view.findViewById(R.id.exam_select_button_B);
                    chooseC = view.findViewById(R.id.exam_select_button_C);
                    chooseD = view.findViewById(R.id.exam_select_button_D);
                    chooseE = view.findViewById(R.id.exam_select_button_E);
                    chooseF = view.findViewById(R.id.exam_select_button_F);
                    rightAnswer = view.findViewById(R.id.question_right_answer);//正确答案TextView
                    questionAnalyze = view.findViewById(R.id.question_analyze);//解析TextView
                    rightAnswerView = view.findViewById(R.id.right_answer_view);/*答案解析区域*/
                    selectionView = view.findViewById(R.id.selection_view);//选项区域
                    submitAnswer = view.findViewById(R.id.button_submit_answer);//答案提交按钮
                    inputAnswer = view.findViewById(R.id.input_answer_return);//用户答案输入框
                    QuestionsQB q = questionsQB.get(i);
                    inputView = view.findViewById(R.id.input_answer_view);//用户答案输入区域
                    //设置EditText的显示方式为多行文本输入
                    inputAnswer.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    //文本显示的位置在EditText的最上方
                    inputAnswer.setGravity(Gravity.TOP);
                    //改变默认的单行模式
                    inputAnswer.setSingleLine(false);
                    //水平滚动设置为False
                    inputAnswer.setHorizontallyScrolling(false);

                    //为题目设置各种判断
                    switch (q.getTypeID()){
                        //选择题
                        case 1:
                            //单选题
                            if (q.getRightAnswer().length()==1){
                                questionTitle.setText(i+1+"."+"（单选题）"+q.getContent());
                                selectionA.setText(q.getAnswerA());
                                selectionB.setText(q.getAnswerB());
                                selectionC.setText(q.getAnswerC());
                                selectionD.setText(q.getAnswerD());
                                selectionE.setText(q.getAnswerE());
                                selectionF.setText(q.getAnswerF());
                                if (q.getAnswerE()==null){//判断E选项是否为空
                                    chooseE.setVisibility(View.GONE);//隐藏E选项
                                    if (q.getAnswerF()==null){
                                        chooseF.setVisibility(View.GONE);//隐藏F选项
                                    }
                                }
                                inputView.setVisibility(View.GONE);//隐藏简答填空文本输入框
                                submitAnswer.setVisibility(View.GONE);//隐藏答案提交按钮
                                //答案+解析
                                rightAnswer.setText("正确答案："+q.getRightAnswer());
                                if (q.getAnalyze()==null){
                                    questionAnalyze.setText("解析：暂未有解析");
                                }
                                else {
                                    questionAnalyze.setText("解析："+q.getAnalyze());
                                }
                                /*单项选择题点击事件*/
                                //A选项
                                chooseA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        TextView key = view.findViewById(R.id.question_right_answer);
                                        CheckBox A = view.findViewById(R.id.exam_select_button_A);
                                        CheckBox B = view.findViewById(R.id.exam_select_button_B);
                                        CheckBox C = view.findViewById(R.id.exam_select_button_C);
                                        CheckBox D = view.findViewById(R.id.exam_select_button_D);
                                        CheckBox E = view.findViewById(R.id.exam_select_button_E);
                                        CheckBox F = view.findViewById(R.id.exam_select_button_F);
                                        if (q.getRightAnswer().equals("A")){
                                            buttonView.setChecked(true);
                                            key.setTextColor(0xFF1CBF75);
                                            view.findViewById(R.id.exam_select_button_B).setClickable(false);
                                            view.findViewById(R.id.exam_select_button_C).setClickable(false);
                                            view.findViewById(R.id.exam_select_button_D).setClickable(false);
                                            view.findViewById(R.id.exam_select_button_E).setClickable(false);
                                            view.findViewById(R.id.exam_select_button_F).setClickable(false);
                                            view.findViewById(R.id.right_answer_view).setVisibility(View.VISIBLE);
                                        }else {
                                            A.setBackground(getDrawable(R.drawable.selectionafalse));
                                            key.setTextColor(0xFF1CBF75);
                                            switch (q.getRightAnswer()) {
                                                case "B":
                                                    B.setChecked(true);
                                                    break;
                                                case "C":
                                                    C.setChecked(true);
                                                    break;
                                                case "D":
                                                    D.setChecked(true);
                                                    break;
                                                case "E":
                                                    E.setChecked(true);
                                                    break;
                                                default:
                                                    F.setChecked(true);
                                                    break;
                                            }
                                        }
                                    }
                                });
                                //B选项
                                chooseB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        TextView key = view.findViewById(R.id.question_right_answer);
                                        CheckBox A = view.findViewById(R.id.exam_select_button_A);
                                        CheckBox B = view.findViewById(R.id.exam_select_button_B);
                                        CheckBox C = view.findViewById(R.id.exam_select_button_C);
                                        CheckBox D = view.findViewById(R.id.exam_select_button_D);
                                        CheckBox E = view.findViewById(R.id.exam_select_button_E);
                                        CheckBox F = view.findViewById(R.id.exam_select_button_F);
                                        if (q.getRightAnswer().equals("B")){
                                            buttonView.setChecked(true);
                                            key.setTextColor(0xFF1CBF75);
                                            view.findViewById(R.id.exam_select_button_A).setClickable(false);
                                            view.findViewById(R.id.exam_select_button_C).setClickable(false);
                                            view.findViewById(R.id.exam_select_button_D).setClickable(false);
                                            view.findViewById(R.id.exam_select_button_E).setClickable(false);
                                            view.findViewById(R.id.exam_select_button_F).setClickable(false);
                                            view.findViewById(R.id.right_answer_view).setVisibility(View.VISIBLE);
                                        }else {
                                            B.setBackground(getDrawable(R.drawable.selectionbfalse));
                                            key.setTextColor(0xFF1CBF75);
                                            switch (q.getRightAnswer()) {
                                                case "A":
                                                    A.setChecked(true);
                                                    break;
                                                case "C":
                                                    C.setChecked(true);
                                                    break;
                                                case "D":
                                                    D.setChecked(true);
                                                    break;
                                                case "E":
                                                    E.setChecked(true);
                                                    break;
                                                default:
                                                    F.setChecked(true);
                                                    break;
                                            }
                                        }
                                    }
                                });
                                //C选项
                                chooseC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        TextView key = view.findViewById(R.id.question_right_answer);
                                        CheckBox A = view.findViewById(R.id.exam_select_button_A);
                                        CheckBox B = view.findViewById(R.id.exam_select_button_B);
                                        CheckBox C = view.findViewById(R.id.exam_select_button_C);
                                        CheckBox D = view.findViewById(R.id.exam_select_button_D);
                                        CheckBox E = view.findViewById(R.id.exam_select_button_E);
                                        CheckBox F = view.findViewById(R.id.exam_select_button_F);
                                        if (q.getRightAnswer().equals("C")){
                                            buttonView.setChecked(true);
                                            key.setTextColor(0xFF1CBF75);
                                            view.findViewById(R.id.exam_select_button_A).setClickable(false);
                                            view.findViewById(R.id.exam_select_button_B).setClickable(false);
                                            view.findViewById(R.id.exam_select_button_D).setClickable(false);
                                            view.findViewById(R.id.exam_select_button_E).setClickable(false);
                                            view.findViewById(R.id.exam_select_button_F).setClickable(false);
                                            view.findViewById(R.id.right_answer_view).setVisibility(View.VISIBLE);
                                        }else {
                                            C.setBackground(getDrawable(R.drawable.selectioncfalse));
                                            key.setTextColor(0xFF1CBF75);
                                            switch (q.getRightAnswer()) {
                                                case "A":
                                                    A.setChecked(true);
                                                    break;
                                                case "B":
                                                    B.setChecked(true);
                                                    break;
                                                case "D":
                                                    D.setChecked(true);
                                                    break;
                                                case "E":
                                                    E.setChecked(true);
                                                    break;
                                                default:
                                                    F.setChecked(true);
                                                    break;
                                            }
                                        }
                                    }
                                });
                                //D选项
                                chooseD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        TextView key = view.findViewById(R.id.question_right_answer);
                                        CheckBox A = view.findViewById(R.id.exam_select_button_A);
                                        CheckBox B = view.findViewById(R.id.exam_select_button_B);
                                        CheckBox C = view.findViewById(R.id.exam_select_button_C);
                                        CheckBox D = view.findViewById(R.id.exam_select_button_D);
                                        CheckBox E = view.findViewById(R.id.exam_select_button_E);
                                        CheckBox F = view.findViewById(R.id.exam_select_button_F);
                                        if (q.getRightAnswer().equals("D")){
                                            buttonView.setChecked(true);
                                            key.setTextColor(0xFF1CBF75);
                                            view.findViewById(R.id.exam_select_button_A).setClickable(false);
                                            view.findViewById(R.id.exam_select_button_C).setClickable(false);
                                            view.findViewById(R.id.exam_select_button_B).setClickable(false);
                                            view.findViewById(R.id.exam_select_button_E).setClickable(false);
                                            view.findViewById(R.id.exam_select_button_F).setClickable(false);
                                            view.findViewById(R.id.right_answer_view).setVisibility(View.VISIBLE);
                                        }else {
                                            D.setBackground(getDrawable(R.drawable.selectiondfalse));
                                            key.setTextColor(0xFF1CBF75);
                                            switch (q.getRightAnswer()) {
                                                case "A":
                                                    A.setChecked(true);
                                                    break;
                                                case "B":
                                                    B.setChecked(true);
                                                    break;
                                                case "C":
                                                    C.setChecked(true);
                                                    break;
                                                case "E":
                                                    E.setChecked(true);
                                                    break;
                                                default:
                                                    F.setChecked(true);
                                                    break;
                                            }
                                        }
                                    }
                                });
                                //E选项
                                chooseE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        TextView key = view.findViewById(R.id.question_right_answer);
                                        CheckBox A = view.findViewById(R.id.exam_select_button_A);
                                        CheckBox B = view.findViewById(R.id.exam_select_button_B);
                                        CheckBox C = view.findViewById(R.id.exam_select_button_C);
                                        CheckBox D = view.findViewById(R.id.exam_select_button_D);
                                        CheckBox E = view.findViewById(R.id.exam_select_button_E);
                                        CheckBox F = view.findViewById(R.id.exam_select_button_F);
                                        if (q.getRightAnswer().equals("E")){
                                            buttonView.setChecked(true);
                                            key.setTextColor(0xFF1CBF75);
                                            view.findViewById(R.id.exam_select_button_A).setClickable(false);
                                            view.findViewById(R.id.exam_select_button_C).setClickable(false);
                                            view.findViewById(R.id.exam_select_button_B).setClickable(false);
                                            view.findViewById(R.id.exam_select_button_D).setClickable(false);
                                            view.findViewById(R.id.exam_select_button_F).setClickable(false);
                                            view.findViewById(R.id.right_answer_view).setVisibility(View.VISIBLE);
                                        }else {
                                            E.setBackground(getDrawable(R.drawable.selectiondfalse));
                                            key.setTextColor(0xFF1CBF75);
                                            switch (q.getRightAnswer()) {
                                                case "A":
                                                    A.setChecked(true);
                                                    break;
                                                case "B":
                                                    B.setChecked(true);
                                                    break;
                                                case "C":
                                                    C.setChecked(true);
                                                    break;
                                                case "D":
                                                    D.setChecked(true);
                                                    break;
                                                default:
                                                    F.setChecked(true);
                                                    break;
                                            }
                                        }
                                    }
                                });
                                //F选项
                                chooseF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        TextView key = view.findViewById(R.id.question_right_answer);
                                        CheckBox A = view.findViewById(R.id.exam_select_button_A);
                                        CheckBox B = view.findViewById(R.id.exam_select_button_B);
                                        CheckBox C = view.findViewById(R.id.exam_select_button_C);
                                        CheckBox D = view.findViewById(R.id.exam_select_button_D);
                                        CheckBox E = view.findViewById(R.id.exam_select_button_E);
                                        CheckBox F = view.findViewById(R.id.exam_select_button_F);
                                        if (q.getRightAnswer().equals("F")){
                                            buttonView.setChecked(true);
                                            key.setTextColor(0xFF1CBF75);
                                            view.findViewById(R.id.exam_select_button_A).setClickable(false);
                                            view.findViewById(R.id.exam_select_button_C).setClickable(false);
                                            view.findViewById(R.id.exam_select_button_B).setClickable(false);
                                            view.findViewById(R.id.exam_select_button_D).setClickable(false);
                                            view.findViewById(R.id.exam_select_button_E).setClickable(false);
                                            view.findViewById(R.id.right_answer_view).setVisibility(View.VISIBLE);
                                        }else {
                                            F.setBackground(getDrawable(R.drawable.selectiondfalse));
                                            key.setTextColor(0xFF1CBF75);
                                            switch (q.getRightAnswer()) {
                                                case "A":
                                                    A.setChecked(true);
                                                    break;
                                                case "B":
                                                    B.setChecked(true);
                                                    break;
                                                case "C":
                                                    C.setChecked(true);
                                                    break;
                                                case "D":
                                                    D.setChecked(true);
                                                    break;
                                                default:
                                                    E.setChecked(true);
                                                    break;
                                            }
                                        }
                                    }
                                });
                            }
                            //多选题
                            else {
                                    questionTitle.setText( i+1+"."+"（多选题）"+q.getContent());
                                    selectionA.setText(q.getAnswerA());
                                    selectionB.setText(q.getAnswerB());
                                    selectionC.setText(q.getAnswerC());
                                    selectionD.setText(q.getAnswerD());
                                    selectionE.setText(q.getAnswerE());
                                    selectionF.setText(q.getAnswerF());
                                //五个选项 NOPQRS表示ABCDEF没有被选中的默认值
                                //声明的全局变量，每次使用都初始化一次，否则参数一直影响到后续的答案判断
                                choose = new char[]{'N', 'O', 'P', 'Q', 'R','S'};
                                /*设置CheckBox点击事件给choose赋值*/
                                chooseA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if (isChecked){
                                            choose[0] = 'A';
                                        }
                                    }
                                });
                                chooseB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if (isChecked){
                                            choose[1] = 'B';
                                        }
                                    }
                                });
                                chooseC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if (isChecked){
                                            choose[2] = 'C';
                                        }
                                    }
                                });
                                chooseD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if (isChecked){
                                            choose[3] = 'D';
                                        }
                                    }
                                });
                                chooseE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if (isChecked){
                                            choose[4] = 'E';
                                        }
                                    }
                                });
                                chooseF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if (isChecked){
                                            choose[5] = 'F';
                                        }
                                    }
                                });
                                if (q.getAnswerE()==null){
                                    chooseE.setVisibility(View.GONE);
                                    if (q.getAnswerF()==null) {
                                        chooseF.setVisibility(View.GONE);
                                    }
                                }
                                    inputView.setVisibility(View.GONE);//隐藏简答填空文本输入框
                                    submitAnswer.setText("选好了");
                                    //答案+解析
                                rightAnswer.setText("正确答案："+q.getRightAnswer());
                                if (q.getAnalyze()==null){
                                    questionAnalyze.setText("解析：暂未有解析");
                                }
                                else {
                                    questionAnalyze.setText("解析："+q.getAnalyze());
                                }
                                //多选题判断
                                //答案提交按钮监听事件
                                submitAnswer.setOnClickListener(new View.OnClickListener() {//判断答案
                                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                    @Override
                                    public void onClick(View v) {
                                        for (char p:choose){
                                            System.out.println("选中的选项"+p);
                                        }
                                        TextView key = view.findViewById(R.id.question_right_answer);
                                        CheckBox A = view.findViewById(R.id.exam_select_button_A);
                                        CheckBox B = view.findViewById(R.id.exam_select_button_B);
                                        CheckBox C = view.findViewById(R.id.exam_select_button_C);
                                        CheckBox D = view.findViewById(R.id.exam_select_button_D);
                                        CheckBox E = view.findViewById(R.id.exam_select_button_E);
                                        CheckBox F = view.findViewById(R.id.exam_select_button_F);
                                        //将正确答案转化成字符数组
                                        char[] rightKey = q.getRightAnswer().toCharArray();
                                        //如果没有选择，那么提示用户没有选择
                                        if ((choose[0]=='N')&(choose[1]=='O')&(choose[2]=='P')&(choose[3]=='Q')&(choose[4]=='R')&(choose[5]=='S')){
                                            Toast.makeText(SequentialPracticeActivity.this, "亲！请选择呢", Toast.LENGTH_SHORT).show();
                                            }
                                        else {
                                            for (char c : choose) {
                                                for (char value : rightKey) {
                                                    System.out.println("选"+c+"正"+value);
                                                    flag = 0;
                                                    if (c == value) {//答案对上则设置checkBox为true
                                                        flag=1;//表示该选项是正确答案的其中之一
                                                        switch (c) {
                                                            case 'A':
                                                                A.setChecked(true);
                                                                view.findViewById(R.id.exam_select_button_A).setClickable(false);
                                                                break;
                                                            case 'B':
                                                                B.setChecked(true);
                                                                view.findViewById(R.id.exam_select_button_B).setClickable(false);
                                                                break;
                                                            case 'C':
                                                                C.setChecked(true);
                                                                view.findViewById(R.id.exam_select_button_C).setClickable(false);
                                                                break;
                                                            case 'D':
                                                                D.setChecked(true);
                                                                view.findViewById(R.id.exam_select_button_D).setClickable(false);
                                                                break;
                                                            case 'E':
                                                                E.setChecked(true);
                                                                view.findViewById(R.id.exam_select_button_E).setClickable(false);
                                                                break;
                                                            case 'F':
                                                                F.setChecked(true);
                                                                view.findViewById(R.id.exam_select_button_F).setClickable(false);
                                                                break;
                                                        }
                                                        break;
                                                    }
                                                }
                                                System.out.println(flag);
                                                if (flag==0){
                                                    switch (c){
                                                        case 'A':
                                                            A.setBackground(getDrawable(R.drawable.selectionafalse));
                                                            view.findViewById(R.id.exam_select_button_A).setClickable(false);
                                                            break;
                                                        case 'B':
                                                            B.setBackground(getDrawable(R.drawable.selectionbfalse));
                                                            view.findViewById(R.id.exam_select_button_B).setClickable(false);
                                                            break;
                                                        case 'C':
                                                            C.setBackground(getDrawable(R.drawable.selectioncfalse));
                                                            view.findViewById(R.id.exam_select_button_C).setClickable(false);
                                                            break;
                                                        case 'D':
                                                            D.setBackground(getDrawable(R.drawable.selectiondfalse));
                                                            view.findViewById(R.id.exam_select_button_D).setClickable(false);
                                                            break;
                                                        case 'E':
                                                            E.setBackground(getDrawable(R.drawable.selectionefalse));
                                                            view.findViewById(R.id.exam_select_button_E).setClickable(false);
                                                            break;
                                                        case 'F':
                                                            F.setBackground(getDrawable(R.drawable.selectionffalse));
                                                            view.findViewById(R.id.exam_select_button_F).setClickable(false);
                                                            break;
                                                        case 'N'://N-R是为了判断没有被选到的选项设置为不能被点击
                                                            view.findViewById(R.id.exam_select_button_A).setClickable(false);
                                                            for (char r:rightKey){//判断A是否是被漏选的选项
                                                                if ('A'==r){
                                                                    A.setBackground(getDrawable(R.drawable.selectionahalf));
                                                                }
                                                            }
                                                            break;
                                                        case 'O':
                                                            view.findViewById(R.id.exam_select_button_B).setClickable(false);
                                                            for (char r:rightKey){//判断B是否是被漏选的选项
                                                                if ('B'==r){
                                                                    B.setBackground(getDrawable(R.drawable.selectionbhalf));
                                                                }
                                                            }
                                                            break;
                                                        case 'P':
                                                            view.findViewById(R.id.exam_select_button_C).setClickable(false);
                                                            for (char r:rightKey){//判断C是否是被漏选的选项
                                                                if ('C'==r){
                                                                    C.setBackground(getDrawable(R.drawable.selectionchalf));
                                                                }
                                                            }
                                                            break;
                                                        case 'Q':
                                                            view.findViewById(R.id.exam_select_button_D).setClickable(false);
                                                            for (char r:rightKey){//判断D是否是被漏选的选项
                                                                if ('D'==r){
                                                                    D.setBackground(getDrawable(R.drawable.selectiondhalf));
                                                                }
                                                            }
                                                            break;
                                                        case 'R':
                                                            view.findViewById(R.id.exam_select_button_E).setClickable(false);
                                                            for (char r:rightKey){//判断E是否是被漏选的选项
                                                                if ('E'==r){
                                                                    E.setBackground(getDrawable(R.drawable.selectionehalf));
                                                                }
                                                            }
                                                            break;
                                                        case 'S':
                                                            view.findViewById(R.id.exam_select_button_F).setClickable(false);
                                                            for (char r:rightKey){//判断E是否是被漏选的选项
                                                                if ('F'==r){
                                                                    F.setBackground(getDrawable(R.drawable.selectionfhalf));
                                                                }
                                                            }
                                                            break;
                                                    }
                                                }
                                            }
                                            Button submitHide = findViewById(R.id.button_submit_answer);
                                            key.setTextColor(0xFF1CBF75);
                                            submitHide.setVisibility(View.GONE);
                                            view.findViewById(R.id.right_answer_view).setVisibility(View.VISIBLE);
                                            choose = new char[]{'N', 'O', 'P', 'Q', 'R','S'};//初始化，给下一个多选题提供判断
                                        }
                                    }
                                });
                            }
                            break;
                        //判断题
                        case 2:
                            questionTitle.setText(i+1+"."+"（判断题）"+q.getContent());
                            selectionA.setText(q.getAnswerA());
                            selectionB.setText(q.getAnswerB());
                            chooseC.setVisibility(View.GONE);//隐藏CDE选项
                            chooseD.setVisibility(View.GONE);
                            chooseE.setVisibility(View.GONE);
                            chooseF.setVisibility(View.GONE);
                            inputView.setVisibility(View.GONE);//隐藏简答填空文本输入框
                            submitAnswer.setVisibility(View.GONE);//隐藏答案提交按钮
                            //A选项
                            chooseA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    TextView key = view.findViewById(R.id.question_right_answer);
                                    CheckBox A = view.findViewById(R.id.exam_select_button_A);
                                    CheckBox B = view.findViewById(R.id.exam_select_button_B);
                                    if (q.getRightAnswer().equals("A")){
                                        buttonView.setChecked(true);
                                        key.setTextColor(0xFF1CBF75);
                                        view.findViewById(R.id.exam_select_button_B).setClickable(false);
                                        view.findViewById(R.id.right_answer_view).setVisibility(View.VISIBLE);
                                    }else {
                                        A.setBackground(getDrawable(R.drawable.selectionafalse));
                                        key.setTextColor(0xFF1CBF75);
                                        if (q.getRightAnswer().equals("B")){
                                            B.setChecked(true);
                                        }
                                    }
                                }
                            });
                            //B选项
                            chooseB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    TextView key = view.findViewById(R.id.question_right_answer);
                                    CheckBox A = view.findViewById(R.id.exam_select_button_A);
                                    CheckBox B = view.findViewById(R.id.exam_select_button_B);
                                    if (q.getRightAnswer().equals("B")){
                                        buttonView.setChecked(true);
                                        key.setTextColor(0xFF1CBF75);
                                        view.findViewById(R.id.exam_select_button_A).setClickable(false);
                                        view.findViewById(R.id.right_answer_view).setVisibility(View.VISIBLE);
                                    }else {
                                        B.setBackground(getDrawable(R.drawable.selectionbfalse));
                                        key.setTextColor(0xFF1CBF75);
                                        if (q.getRightAnswer().equals("A")){
                                            A.setChecked(true);
                                        }
                                    }
                                }
                            });
                            //答案+解析
                            rightAnswer.setText("正确答案："+q.getRightAnswer());
                            if (q.getAnalyze()==null){
                                questionAnalyze.setText("解析：暂未有解析");
                            }
                            else {
                                questionAnalyze.setText("解析："+q.getAnalyze());
                            }
                            break;
                        case 3:
                            questionTitle.setText( i+1+"."+"（填空题）"+q.getContent());
                            selectionView.setVisibility(View.GONE);//隐藏选择选项区域
                            submitAnswer.setText("看答案");//将提交按钮设为看答案
                            submitAnswer.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    view.findViewById(R.id.right_answer_view).setVisibility(View.VISIBLE);
                                }
                            });
                            //答案+解析
                            rightAnswer.setText("正确答案："+q.getRightAnswer());
                            rightAnswer.setTextColor(0xFF1CBF75);
                            if (q.getAnalyze()==null){
                                questionAnalyze.setText("解析：暂未有解析");
                            }
                            else {
                                questionAnalyze.setText("解析："+q.getAnalyze());
                            }
                            break;
                        case 4:
                            questionTitle.setText( i+1+"."+"（简答题）"+q.getContent());
                            selectionView.setVisibility(View.GONE);//隐藏选择选项区域
                            submitAnswer.setText("看答案");//将提交按钮设为看答案
                            submitAnswer.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    view.findViewById(R.id.right_answer_view).setVisibility(View.VISIBLE);
                                }
                            });
                            //答案+解析
                            rightAnswer.setTextColor(0xFF1CBF75);
                            if (q.getRightAnswer()==null){
                                rightAnswer.setText("正确答案：见解析");
                            }else {
                                rightAnswer.setText("正确答案："+q.getRightAnswer());
                            }
                            if (q.getAnalyze()==null){
                                questionAnalyze.setText("解析：暂未有解析");
                            }
                            else {
                                questionAnalyze.setText("解析："+q.getAnalyze());
                            }
                            break;
                    }
                    viewpagelist.add(view);
                }
                //根据题目总数生成题号
                for (j=0;j<questionsQB.size();j++){
                    @SuppressLint("ResourceType")
                    final TextView addQN = (TextView) LayoutInflater.from(SequentialPracticeActivity.this)
                            .inflate(R.layout.item_question_number,addQuestionN,false);
                    final TextView addQN1 = (TextView) LayoutInflater.from(SequentialPracticeActivity.this)
                            .inflate(R.layout.item_question_number,addQuestionN1,false);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        questionId[j] = View.generateViewId();
                        questionId[j+questionsQB.size()] = View.generateViewId();
                    }
                    //横版
                    addQN.setId(questionId[j]);
                    String number = String.valueOf(j+1);
                    addQN.setText(number);
                    if (j==0){
                        addQN.setTextColor(0xFFFFFFFF);
                        addQN1.setTextColor(0xFFFFFFFF);
                    }
                    addQuestionN.addView(addQN);
                    //展开版
                    addQN1.setId(questionId[j+questionsQB.size()]);
                    addQN1.setText(number);
                    addQuestionN1.addView(addQN1);
                    lastClick = findViewById(questionId[0]);//设置初始值
                    lastClick1 = findViewById(questionId[questionsQB.size()]);//设置初始值
                    //设置监听事件
                    addQN.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("ResourceType")
                        @Override
                        public void onClick(View v) {
                            if (lastClick!=addQN){
                                int count = Integer.valueOf(String.valueOf(addQN.getText()));
                                questionPager.setCurrentItem(count-1);
                                addQN.setTextColor(0xFFFFFFFF);
                                //对应将展开版对应的题号也设为白色
                                TextView ver = findViewById(questionId[count-1+questionsQB.size()]);
                                ver.setTextColor(0xFFFFFFFF);
                                lastClick1.setTextColor(0xFF000000);
                                lastClick1 = ver;

                                lastClick.setTextColor(0xFF000000);
                                lastClick = findViewById(addQN.getId());
                                Toast.makeText(SequentialPracticeActivity.this, "第"+addQN.getText()+"题", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    addQN1.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("ResourceType")
                        @Override
                        public void onClick(View v) {
                            if (lastClick1!=addQN1){
                                int count = Integer.valueOf(String.valueOf(addQN1.getText()));
                                questionPager.setCurrentItem(count-1);
                                addQN1.setTextColor(0xFFFFFFFF);
                                //对应将横版对应的题号也设为白色
                                TextView ver = findViewById(questionId[count-1]);
                                ver.setTextColor(0xFFFFFFFF);
                                lastClick.setTextColor(0xFF000000);
                                lastClick = ver;

                                lastClick1.setTextColor(0xFF000000);
                                lastClick1 = findViewById(addQN1.getId());
                                Toast.makeText(SequentialPracticeActivity.this, "第"+addQN1.getText()+"题", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                //给每题适配页面
                adapter = new SequentialAdapter(viewpagelist);
                questionPager.setAdapter(adapter);
                //结束加载动画
                loadProgressDialog.dismiss();
                questionPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        if (position==(questionsQB.size()-1)){
                            Toast.makeText(SequentialPracticeActivity.this, "这是最后一题了哟", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onPageSelected(int position) {
                        for(int k=0;k<+position;k++){
                                @SuppressLint("WrongViewCast")
                                HorizontalScrollView hz = findViewById(R.id.question_number_style1);
                                hz.setScrollX(124*k);
                                TextView thisQuestion1 = findViewById(questionId[position]);
                                TextView lastQuestion1 = findViewById(questionId[position-1]);
                                TextView thisQuestion2 = findViewById(questionId[position+questionsQB.size()]);
                                TextView lastQuestion2 = findViewById(questionId[position-1+questionsQB.size()]);
                            if (position!=(questionsQB.size()-1)){//判断是否为最后一题
                                TextView nextQuestion1 = findViewById(questionId[position+1]);
                                TextView  nextQuestion2 = findViewById(questionId[position+1+questionsQB.size()]);
                                thisQuestion1.setTextColor(0xFFFFFFFF);//设置横版当前题号为白色
                                lastQuestion1.setTextColor(0xFF000000);//设置上一题号为黑色
                                nextQuestion1.setTextColor(0xFF000000);//设置下一题号为黑色
                                thisQuestion2.setTextColor(0xFFFFFFFF);//设置展开版当前题号为白色
                                lastQuestion2.setTextColor(0xFF000000);//设置上一题号为黑色
                                nextQuestion2.setTextColor(0xFF000000);//设置下一题号为黑色
                                }
                            else {
                                thisQuestion1.setTextColor(0xFFFFFFFF);//设置横版当前题号为白色
                                lastQuestion1.setTextColor(0xFF000000);//设置上一题号为黑色
                                thisQuestion2.setTextColor(0xFFFFFFFF);//设置展开版当前题号为白色
                                lastQuestion2.setTextColor(0xFF000000);//设置上一题号为黑色
                            }


                        }

                    }
                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            }
        }

    };
    @Override
    //返回页面方法，返回上一页
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        exitDialog();
        return true;
    }
    //返回确认对话框
    private void exitDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示：");
        builder.setMessage("退出练习吗？");
        //设置确定按钮
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        //设置取消按钮
        builder.setPositiveButton("继续练习",null);
        //显示提示
        builder.create().show();
    }
}

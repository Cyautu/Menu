package com.example.menu.ui.mainHome.examination.exam.ui.mockexam;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.menu.DB.DBUrl;
import com.example.menu.R;

import com.example.menu.fieldEncapsulation.QuestionsQB;

import com.example.menu.ui.loadingFlash.LoadProgressDialog;
import com.example.menu.ui.mainHome.examination.exam.MockExamActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class MockExamFragment extends Fragment {

    private int time_m = 120, time_s = 0, queryMsg = 0,score = 0;
    private TextView timer;
    private ViewPager viewPager;
    private List<View> list;
    private LayoutInflater inflter;
    private View root;
    private boolean stopTimer = false;
    private String[] rightAnswers = new String[73];
    private ArrayList<QuestionsQB> questionsQB;
    private String[] answers = new String[73];
    private LoadProgressDialog loadProgressDialog;

    public static MockExamFragment newInstance() {
        return new MockExamFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.mock_exam_fragment, container, false);
        this.inflter = inflater;
        this.root = root;
        addQuestionButton();
        Toolbar toolbar = root.findViewById(R.id.toolbar_exam);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(v -> quitTip());
        timer = root.findViewById(R.id.timer);
        handler.postDelayed(runnable, 1000);
        viewPager = root.findViewById(R.id.viewPager);
        loadProgressDialog = new LoadProgressDialog(getActivity(), "正在加载…");
        loadProgressDialog.show();
        ConnectivityManager connectivityManager = (ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        if (info == null){
            Toast.makeText(getActivity(),"请检查你的网络连接···",Toast.LENGTH_SHORT).show();
            loadProgressDialog.dismiss();
        }else {
            new Thread(() -> {
                DBUrl dbUrl = new DBUrl();
                questionsQB = dbUrl.getQuestion("QB000");
                Message msg = new Message();
                msg.what = queryMsg;
                handler.sendMessage(msg);
            }).start();
        }
        return root;
    }

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @SuppressLint("SetTextI18n")
        public void handleMessage(Message msg) {
            if (msg.what == queryMsg) {
                list = new ArrayList<>();
                TextView questionTitle, selectionA, selectionB, selectionC, selectionD, selectionE, selectionF, questionRightAnswer, questionAnalyze;
                //填空题
                ArrayList<Integer> arr = new ArrayList<>();
                for (int i = 0; i < 20; i++) {
                    View view = inflter.inflate(R.layout.fragment_answer, null);
                    questionTitle = view.findViewById(R.id.question_title);
                    questionRightAnswer = view.findViewById(R.id.question_right_answer);
                    questionAnalyze = view.findViewById(R.id.question_analyze);
                    view.findViewById(R.id.choose_item).setVisibility(View.GONE);
                    view.findViewById(R.id.answer).setVisibility(View.GONE);
                    view.findViewById(R.id.answer_detail).setVisibility(View.GONE);
                    int min = 254, max = 288, count = 0; //填空题
                    Random random = new Random();
                    int temp = random.nextInt(max) % (max - min + 1) + min;
                    arr.add(temp);
                    for (int j = 0; j < arr.size() - 1; j++) {
                        if (arr.get(j) == temp) {
                            count++;
                            arr.remove(j);
                        }
                    }
                    if (count > 0) {
                        i--;
                    } else {
                        QuestionsQB q = questionsQB.get(temp);
                        rightAnswers[i] = q.getRightAnswer();
                        questionTitle.setText(i + 1 + "." + q.getContent());
                        questionRightAnswer.setText("正确答案：" + q.getRightAnswer());
                        questionAnalyze.setText(q.getAnalyze());
                        getFillAnswer(view, i);
                        list.add(view);
                    }
                }
                //选择题
                ArrayList<Integer> arr1 = new ArrayList<>();
                for (int i = 0; i < 30; i++) {
                    View view = inflter.inflate(R.layout.fragment_answer, null);
                    questionTitle = view.findViewById(R.id.question_title);
                    questionRightAnswer = view.findViewById(R.id.question_right_answer);
                    questionAnalyze = view.findViewById(R.id.question_analyze);
                    view.findViewById(R.id.answer_fill).setVisibility(View.GONE);
                    view.findViewById(R.id.answer_detail).setVisibility(View.GONE);
                    view.findViewById(R.id.answer).setVisibility(View.GONE);
                    selectionA = view.findViewById(R.id.text_answerA);
                    selectionB = view.findViewById(R.id.text_answerB);
                    selectionC = view.findViewById(R.id.text_answerC);
                    selectionD = view.findViewById(R.id.text_answerD);
                    selectionE = view.findViewById(R.id.text_answerE);
                    selectionF = view.findViewById(R.id.text_answerF);
                    int min = 0, max = 219, count = 0;
                    Random random = new Random();
                    int temp = random.nextInt(max) % (max - min + 1) + min;
                    arr1.add(temp);
                    for (int j = 0; j < arr1.size() - 1; j++) {
                        if (arr1.get(j) == temp) {
                            count++;
                            arr1.remove(j);
                        }
                    }
                    if (count > 0) {
                        i--;
                    } else {
                        QuestionsQB q = questionsQB.get(temp);
                        rightAnswers[i + 20] = q.getRightAnswer();
                        selectionA.setText(q.getAnswerA());
                        selectionB.setText(q.getAnswerB());
                        selectionC.setText(q.getAnswerC());
                        selectionD.setText(q.getAnswerD());
                        if (q.getRightAnswer().length() > 1) {
                            questionTitle.setText(i + 21 + ".(多选题)" + q.getContent());
                            questionRightAnswer.setText("正确答案：" + q.getRightAnswer());
                            questionAnalyze.setText(q.getAnalyze());
                            getMultiStates(view, i + 20);
                            System.out.println("确定要交卷吗" + i + 21);
                            if (q.getAnswerE() != null) {
                                selectionE.setText(q.getAnswerE());
                            } else {
                                view.findViewById(R.id.item_e).setVisibility(View.GONE);
                            }
                            if (q.getAnswerF() != null) {
                                selectionF.setText(q.getAnswerF());
                            } else {
                                view.findViewById(R.id.item_f).setVisibility(View.GONE);
                            }
                        } else {
                            questionTitle.setText(i + 21 + "." + q.getContent());
                            questionRightAnswer.setText("正确答案：" + q.getRightAnswer());
                            questionAnalyze.setText(q.getAnalyze());
                            view.findViewById(R.id.item_e).setVisibility(View.GONE);
                            view.findViewById(R.id.item_f).setVisibility(View.GONE);
                            getCheckStates(view, i + 20);
                        }
                        list.add(view);
                    }
                }
                //判断题
                ArrayList<Integer> arr2 = new ArrayList<>();
                for (int i = 0; i < 20; i++) {
                    View view = inflter.inflate(R.layout.fragment_answer, null);
                    questionTitle = view.findViewById(R.id.question_title);
                    questionRightAnswer = view.findViewById(R.id.question_right_answer);
                    questionAnalyze = view.findViewById(R.id.question_analyze);
                    view.findViewById(R.id.answer_fill).setVisibility(View.GONE);
                    view.findViewById(R.id.item_c).setVisibility(View.GONE);
                    view.findViewById(R.id.item_d).setVisibility(View.GONE);
                    view.findViewById(R.id.item_e).setVisibility(View.GONE);
                    view.findViewById(R.id.item_f).setVisibility(View.GONE);
                    view.findViewById(R.id.answer_detail).setVisibility(View.GONE);
                    selectionA = view.findViewById(R.id.text_answerA);
                    selectionB = view.findViewById(R.id.text_answerB);
                    int min = 220, max = 253, count = 0;
                    Random random = new Random();
                    int temp = random.nextInt(max) % (max - min + 1) + min;
                    arr2.add(temp);
                    for (int j = 0; j < arr2.size() - 1; j++) {
                        if (arr2.get(j) == temp) {
                            count++;
                            arr2.remove(j);
                        }
                    }
                    if (count > 0) {
                        i--;
                    } else {
                        QuestionsQB q = questionsQB.get(temp);
                        rightAnswers[i + 50] = q.getRightAnswer();
                        questionRightAnswer.setText("正确答案：" + q.getRightAnswer());
                        questionAnalyze.setText(q.getAnalyze());
                        questionTitle.setText(i + 51 + "." + q.getContent());
                        selectionA.setText(q.getAnswerA());
                        selectionB.setText(q.getAnswerB());
                        getCheckStates(view, i + 50);
                        list.add(view);
                    }
                }
                //简答题
                ArrayList<Integer> arr3 = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    View view = inflter.inflate(R.layout.fragment_answer, null);
                    questionTitle = view.findViewById(R.id.question_title);
                    questionRightAnswer = view.findViewById(R.id.question_right_answer);
                    questionAnalyze = view.findViewById(R.id.question_analyze);
                    view.findViewById(R.id.choose_item).setVisibility(View.GONE);
                    view.findViewById(R.id.answer_detail).setVisibility(View.GONE);
                    EditText answer_fill = view.findViewById(R.id.answer_fill);
                    answer_fill.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            if (keyCode == KeyEvent.KEYCODE_BACK) {
                                getExit();
                            }
                            return false;
                        }
                    });
                    int min = 289, max = 340, count = 0;
                    Random random = new Random();
                    int temp = random.nextInt(max) % (max - min + 1) + min;
                    arr3.add(temp);
                    for (int j = 0; j < arr3.size() - 1; j++) {
                        if (arr3.get(j) == temp) {
                            count++;
                            arr3.remove(j);
                        }
                    }
                    if (count > 0) {
                        i--;
                    } else {
                        QuestionsQB q = questionsQB.get(temp);
                        questionRightAnswer.setText("");
                        questionAnalyze.setText("详解：" + q.getAnalyze());
                        questionTitle.setText(i + 71 + "." + q.getContent());
                        list.add(view);
                    }
                }
                viewPager.setAdapter(new MockExamViewPagerAdapter((ArrayList<View>) list));
                loadProgressDialog.dismiss();
                //交卷按钮
                root.findViewById(R.id.hand_in).setOnClickListener(v -> {
                    int doneQuestionNum = 0;
                    for (int i = 0; i < 70; i++) {
                        if (!"".equals(answers[i]) && answers[i] != null) {
                            doneQuestionNum++;
                        }
                    }
                    if (doneQuestionNum == 70) {
                        AlertDialog dialog = new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                                .setMessage("确定要交卷吗？")
                                .setPositiveButton("再检查检查", (dialog1, which) -> {
                                })
                                .setNeutralButton("交卷", (dialog1, which) -> {
                                    checkAnswers();
                                    changeItemStates();
                                })
                                .create();
                        dialog.show();
                    } else {
                        AlertDialog dialog = new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                                .setMessage("你还有题没做完，确定要交卷吗？")
                                .setPositiveButton("继续答题", (dialog1, which) -> {
                                })
                                .setNeutralButton("交卷", (dialog1, which) -> {
                                    checkAnswers();
                                    changeItemStates();
                                })
                                .create();
                        dialog.show();
                    }
                });
                //翻页监听
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        TextView num = root.findViewById(R.id.num);
                        num.setText((position + 1) + "/73");
                        if (!stopTimer) {
                            addQuestionButton();
                        }
                        if (position > 19 && position < 70)
                            hideInput();
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            }
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MockExamViewModel mViewModel = ViewModelProviders.of(this).get(MockExamViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onResume() {
        super.onResume();
        getExit();
    }

    //计时器
    private Runnable runnable = new Runnable() {

        @SuppressLint("SetTextI18n")
        @Override
        public void run() {
            if (!stopTimer) {
                String s1 = "", s2 = "";
                time_s--;
                if (time_s < 0) {
                    time_s = 59;
                    time_m--;
                }
                if (time_m < 10) {
                    s1 = "0";
                }
                if (time_s < 10) {
                    s2 = "0";
                }
                timer.setText(s1 + time_m + " : " + s2 + time_s);
                handler.postDelayed(this, 1000);
            }
        }
    };

    //退出提示
    private void quitTip() {
        TextView title = new TextView(Objects.requireNonNull(getActivity()));
        title.setText("退出提示");
        title.setPadding(0, 25, 0, 0);
        title.setGravity(Gravity.CENTER);
        title.setTextSize(18);
        title.setTextColor(Color.BLACK);
        AlertDialog dialog = new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                .setCustomTitle(title)
                .setMessage("确定要退出考试吗？")
                .setPositiveButton("继续答题", (dialog1, which) -> {
                })
                .setNeutralButton("退出", (dialog1, which) -> {
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), MockExamActivity.class));
                })
                .create();
        dialog.show();
    }

    //单选按钮
    private void getCheckStates(View view, int questionNum) {
        CheckBox A, B, C, D;
        A = view.findViewById(R.id.exam_select_button_A);
        B = view.findViewById(R.id.exam_select_button_B);
        C = view.findViewById(R.id.exam_select_button_C);
        D = view.findViewById(R.id.exam_select_button_D);
        A.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    B.setChecked(false);
                    C.setChecked(false);
                    D.setChecked(false);
                    answers[questionNum] = "A";
                    viewPager.setCurrentItem(questionNum + 1);
                } else {
                    answers[questionNum] = null;
                }
            }
        });
        B.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    A.setChecked(false);
                    C.setChecked(false);
                    D.setChecked(false);
                    answers[questionNum] = "B";
                    viewPager.setCurrentItem(questionNum + 1);
                } else {
                    answers[questionNum] = null;
                }
            }
        });
        C.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    B.setChecked(false);
                    A.setChecked(false);
                    D.setChecked(false);
                    answers[questionNum] = "C";
                    viewPager.setCurrentItem(questionNum + 1);
                } else {
                    answers[questionNum] = null;
                }
            }
        });
        D.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    B.setChecked(false);
                    C.setChecked(false);
                    A.setChecked(false);
                    answers[questionNum] = "D";
                    viewPager.setCurrentItem(questionNum + 1);
                } else {
                    answers[questionNum] = null;
                }
            }
        });
    }

    //多选按钮
    private void getMultiStates(View view, int questionNum) {
        CheckBox A, B, C, D, E, F;
        A = view.findViewById(R.id.exam_select_button_A);
        B = view.findViewById(R.id.exam_select_button_B);
        C = view.findViewById(R.id.exam_select_button_C);
        D = view.findViewById(R.id.exam_select_button_D);
        E = view.findViewById(R.id.exam_select_button_E);
        F = view.findViewById(R.id.exam_select_button_F);
        A.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                answers[questionNum] += "A";
                answers[questionNum] = answers[questionNum].replace("null", "");
            } else {
                answers[questionNum] = answers[questionNum].replace("A", "");
            }
        });
        B.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                answers[questionNum] += "B";
                answers[questionNum] = answers[questionNum].replace("null", "");
            } else {
                answers[questionNum] = answers[questionNum].replace("B", "");
            }
        });
        C.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                answers[questionNum] += "C";
                answers[questionNum] = answers[questionNum].replace("null", "");
            } else {
                answers[questionNum] = answers[questionNum].replace("C", "");
            }
        });
        D.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                answers[questionNum] += "D";
                answers[questionNum] = answers[questionNum].replace("null", "");
            } else {
                answers[questionNum] = answers[questionNum].replace("D", "");
            }
        });
        E.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                answers[questionNum] += "E";
                answers[questionNum] = answers[questionNum].replace("null", "");
            } else {
                answers[questionNum] = answers[questionNum].replace("E", "");
            }
        });
        F.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                answers[questionNum] += "F";
                answers[questionNum] = answers[questionNum].replace("null", "");
            } else {
                answers[questionNum] = answers[questionNum].replace("F", "");
            }
        });
    }

    //填空
    private void getFillAnswer(View view, int questionNum) {
        EditText answer_fill = view.findViewById(R.id.answer_fill);
        answer_fill.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    getExit();
                }
                return false;
            }
        });
        answer_fill.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                answers[questionNum] = answer_fill.getText().toString();
            }
        });
    }

    //重写物理返回按键
    private void getExit() {
        Objects.requireNonNull(getView()).setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((view, i, keyEvent) -> {
            if (keyEvent.getAction() == KeyEvent.ACTION_UP && i == KeyEvent.KEYCODE_BACK) {
                quitTip();
                return true;
            }
            return false;
        });
    }

    //题号导航按钮
    private void addQuestionButton() {
        GridLayout gridLayout = root.findViewById(R.id.gridLayout);
        gridLayout.removeAllViews();
        if (stopTimer) {
            for (int i = 0; i < 73; i++) {
                GridLayout.Spec rowSpec = GridLayout.spec(i / 4, 1f);
                GridLayout.Spec columnSpec = GridLayout.spec(i % 4, 1f);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
                params.setGravity(Gravity.CENTER);
                params.setMargins(0, 10, 0, 0);
                if (i / 4 == 0)
                    params.bottomMargin = getResources().getDimensionPixelSize(R.dimen.dp_2);
                if (i % 4 == 1) {
                    params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dp_2);
                    params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dp_2);
                }
                Button btn = new Button(getActivity());
                if (answers[i] != null && answers[i].equals(rightAnswers[i])) {
                    btn.setBackgroundColor(getResources().getColor(R.color.rightAnswer));
                    btn.setTextColor(Color.WHITE);
                } else {
                    btn.setBackgroundColor(getResources().getColor(R.color.wrongAnswer));
                    btn.setTextColor(Color.WHITE);
                }
                btn.setText(String.valueOf(i + 1));
                int finalI = i;
                btn.setOnClickListener(v -> {
                    viewPager.setCurrentItem(finalI);
                });
                gridLayout.addView(btn, params);
            }
        } else {
            for (int i = 0; i < 73; i++) {
                GridLayout.Spec rowSpec = GridLayout.spec(i / 4, 1f);
                GridLayout.Spec columnSpec = GridLayout.spec(i % 4, 1f);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
                params.setGravity(Gravity.CENTER);
                params.setMargins(0, 10, 0, 0);
                if (i / 4 == 0)
                    params.bottomMargin = getResources().getDimensionPixelSize(R.dimen.dp_2);
                if (i % 4 == 1) {
                    params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dp_2);
                    params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dp_2);
                }
                Button btn = new Button(getActivity());
                if (answers[i] != null) {
                    btn.setBackgroundColor(Color.DKGRAY);
                    btn.setTextColor(Color.WHITE);
                }
                btn.setText(String.valueOf(i + 1));
                int finalI = i;
                btn.setOnClickListener(v -> {
                    viewPager.setCurrentItem(finalI);
                });
                gridLayout.addView(btn, params);
            }
        }
    }

    //答案比对
    private void checkAnswers() {
        int score = 0;
        for (int i = 0; i < 70; i++) {
            if (i > 19) {
                if (answers[i] == null || "".equals(answers[i])) {
                    continue;
                } else {
                    //冒泡法为多选题选项排序
                    for (int a = 0; a < answers[i].length() - 1; a++) {
                        for (int j = 0; j < answers[i].length() - 1; j++) {
                            if (answers[i].substring(j, j + 1).compareTo(answers[i].substring(j + 1, j + 2)) > 0) {
                                answers[i] = answers[i].substring(0, j) + answers[i].substring(j + 1, j + 2) + answers[i].substring(j, j + 1) + answers[i].substring(j + 2, answers[i].length());
                            }
                        }
                    }
                }
            }
            if (answers[i] != null && answers[i].equals(rightAnswers[i])) {
                score += 1;
            }
        }
        this.score = score;
        AlertDialog dialog = new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                .setMessage("你的得分是" + score)
                .create();
        dialog.show();
    }

    //隐藏软键盘
    private void hideInput() {
        InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(INPUT_METHOD_SERVICE);
        View v = getActivity().getWindow().peekDecorView();
        if (null != v) {
            if (imm != null) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }
    }

    //提交答案后改变控件状态
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("SetTextI18n")
    private void changeItemStates() {
        root.findViewById(R.id.hand_in).setVisibility(View.GONE);
        TextView s = root.findViewById(R.id.score_get);
        s.setText("得分：" + score);
        s.setVisibility(View.VISIBLE);
        TextView num = root.findViewById(R.id.num);
        num.setText(R.string.progress);
        stopTimer = true;
        addQuestionButton();
        if (time_s != 0) {
            time_s = 60 - time_s;
            time_m = 119 - time_m;
        } else {
            time_s = 0;
            time_m = 120 - time_m;
        }
        timer.setText("总共用时：" + time_m + "分" + time_s + "秒");
        for (int i = 0; i < 73; i++) {
            list.get(i).findViewById(R.id.answer_detail).setVisibility(View.VISIBLE);
            TextView yourAnswer;
            yourAnswer = list.get(i).findViewById(R.id.answer);
            if (answers[i] != null) {
                yourAnswer.setText("你的答案：" + answers[i]);
            }
            yourAnswer.setVisibility(View.VISIBLE);
            CheckBox A, B, C, D, E, F;
            A = list.get(i).findViewById(R.id.exam_select_button_A);
            B = list.get(i).findViewById(R.id.exam_select_button_B);
            C = list.get(i).findViewById(R.id.exam_select_button_C);
            D = list.get(i).findViewById(R.id.exam_select_button_D);
            E = list.get(i).findViewById(R.id.exam_select_button_E);
            F = list.get(i).findViewById(R.id.exam_select_button_F);
            A.setClickable(false);
            B.setClickable(false);
            C.setClickable(false);
            D.setClickable(false);
            E.setClickable(false);
            F.setClickable(false);
            //选择判断题
            if (i > 19 && i < 70) {
                String aFlag = "", bFlag = "", cFlag = "", dFlag = "", eFlag = "", fFlag = "";
                for (int j = 0; j < rightAnswers[i].length(); j++) {
                    switch (rightAnswers[i].substring(j, j + 1)) {
                        case "A":
                            aFlag = "r";
                            break;
                        case "B":
                            bFlag = "r";
                            break;
                        case "C":
                            cFlag = "r";
                            break;
                        case "D":
                            dFlag = "r";
                            break;
                        case "E":
                            eFlag = "r";
                            break;
                        case "F":
                            fFlag = "r";
                            break;
                    }
                }
                if (answers[i] != null && !"".equals(answers[i])) {
                    for (int j = 0; j < answers[i].length(); j++) {
                        switch (answers[i].substring(j, j + 1)) {
                            case "A":
                                aFlag += "a";
                                break;
                            case "B":
                                bFlag += "a";
                                break;
                            case "C":
                                cFlag += "a";
                                break;
                            case "D":
                                dFlag += "a";
                                break;
                            case "E":
                                eFlag += "a";
                                break;
                            case "F":
                                fFlag += "a";
                                break;
                        }

                    }
                }
                switch (aFlag) {
                    case "r":
                        if (rightAnswers[i].length() > 1) {
                            A.setBackground(getResources().getDrawable(R.drawable.selectionahalf));
                        } else {
                            A.setBackground(getResources().getDrawable(R.drawable.selectionapressed));
                        }
                        break;
                    case "ra":
                        A.setBackground(getResources().getDrawable(R.drawable.selectionapressed));
                        break;
                    case "a":
                        A.setBackground(getResources().getDrawable(R.drawable.selectionafalse));
                        break;
                    case "":
                        A.setBackground(getResources().getDrawable(R.drawable.selectiona));
                        break;
                }
                switch (bFlag) {
                    case "r":
                        if (rightAnswers[i].length() > 1) {
                            B.setBackground(getResources().getDrawable(R.drawable.selectionbhalf));
                        } else {
                            B.setBackground(getResources().getDrawable(R.drawable.selectionbpressed));
                        }
                        break;
                    case "ra":
                        B.setBackground(getResources().getDrawable(R.drawable.selectionbpressed));
                        break;
                    case "a":
                        B.setBackground(getResources().getDrawable(R.drawable.selectionbfalse));
                        break;
                    case "":
                        B.setBackground(getResources().getDrawable(R.drawable.selectionb));
                        break;
                }
                switch (cFlag) {
                    case "r":
                        if (rightAnswers[i].length() > 1) {
                            C.setBackground(getResources().getDrawable(R.drawable.selectionchalf));
                        } else {
                            C.setBackground(getResources().getDrawable(R.drawable.selectioncpressed));
                        }
                        break;
                    case "ra":
                        C.setBackground(getResources().getDrawable(R.drawable.selectioncpressed));
                        break;
                    case "a":
                        C.setBackground(getResources().getDrawable(R.drawable.selectioncfalse));
                        break;
                    case "":
                        C.setBackground(getResources().getDrawable(R.drawable.selectionc));
                        break;
                }
                switch (dFlag) {
                    case "r":
                        if (rightAnswers[i].length() > 1) {
                            D.setBackground(getResources().getDrawable(R.drawable.selectiondhalf));
                        } else {
                            D.setBackground(getResources().getDrawable(R.drawable.selectiondpressed));
                        }
                        break;
                    case "ra":
                        D.setBackground(getResources().getDrawable(R.drawable.selectiondpressed));
                        break;
                    case "a":
                        D.setBackground(getResources().getDrawable(R.drawable.selectiondfalse));
                        break;
                    case "":
                        D.setBackground(getResources().getDrawable(R.drawable.selectiond));
                        break;
                }
                switch (eFlag) {
                    case "r":
                        if (rightAnswers[i].length() > 1) {
                            E.setBackground(getResources().getDrawable(R.drawable.selectionehalf));
                        } else {
                            E.setBackground(getResources().getDrawable(R.drawable.selectionepressed));
                        }
                        break;
                    case "ra":
                        E.setBackground(getResources().getDrawable(R.drawable.selectionepressed));
                        break;
                    case "a":
                        E.setBackground(getResources().getDrawable(R.drawable.selectionefalse));
                        break;
                    case "":
                        E.setBackground(getResources().getDrawable(R.drawable.selectione));
                        break;
                }
                switch (fFlag) {
                    case "r":
                        if (rightAnswers[i].length() > 1) {
                            F.setBackground(getResources().getDrawable(R.drawable.selectionfhalf));
                        } else {
                            F.setBackground(getResources().getDrawable(R.drawable.selectionfpress));
                        }
                        break;
                    case "ra":
                        F.setBackground(getResources().getDrawable(R.drawable.selectionfpress));
                        break;
                    case "a":
                        F.setBackground(getResources().getDrawable(R.drawable.selectionffalse));
                        break;
                    case "":
                        F.setBackground(getResources().getDrawable(R.drawable.selectionf));
                        break;
                }
            }
        }
        viewPager.setAdapter(new MockExamViewPagerAdapter((ArrayList<View>) list));
    }
}
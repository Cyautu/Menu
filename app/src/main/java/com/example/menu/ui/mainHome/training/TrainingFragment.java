package com.example.menu.ui.mainHome.training;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.example.menu.DB.DBUrl;
import com.example.menu.R;
import com.example.menu.fieldEncapsulation.Training;
import com.example.menu.service.userIcon.DownloadUserIconSerice;
import com.example.menu.ui.loadingFlash.LoadProgressDialog;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.Objects;

import static android.view.View.GONE;
import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;

public class TrainingFragment extends Fragment {

    private ArrayList<Training> trainingList = new ArrayList<>();
    private ArrayList<String> branchList = new ArrayList<>();
    private ArrayList<String> projectList = new ArrayList<>();
    private ArrayList<Training> oriTrainingList = new ArrayList<>();
    private Context context;
    private int queryMsg = 0;
    private RecyclerView recyclerView;
    private LoadProgressDialog loadProgressDialog;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TrainingViewModel trainingViewModel = ViewModelProviders.of(this).get(TrainingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_training, container, false);
        AppBarLayout appBarLayout = root.findViewById(R.id.select_bar);
        appBarLayout.setExpanded(false);
        context = getActivity();
        this.root = root;
        recyclerView = root.findViewById(R.id.recyclerView);
        loadProgressDialog = new LoadProgressDialog(Objects.requireNonNull(getActivity()),"正在刷新…");
        loadProgressDialog.show();
        new Thread(() -> {
            trainingList = DBUrl.getTraining();
            oriTrainingList = trainingList;
            Message msg = new Message();
            msg.what = queryMsg;
            handler.sendMessage(msg);
        }).start();
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        if (info == null){
            Toast.makeText(context,"请检查你的网络连接···",Toast.LENGTH_SHORT).show();
        }
        return root;
    }
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == queryMsg) {
                initRecyclerView();
                loadProgressDialog.dismiss();
                branchList.clear();
                branchList.add("全部");
                ListView selectList = root.findViewById(R.id.selectList);
                TextView branch = root.findViewById(R.id.branch);
                TextView project = root.findViewById(R.id.project);
                project.setClickable(false);
                for (int i=0; i<oriTrainingList.size(); i++){
                    if (!branchList.contains(oriTrainingList.get(i).getOfCompany())){
                        branchList.add(oriTrainingList.get(i).getOfCompany());
                    }
                }
                branch.setOnClickListener(v -> {
                    branch.setBackgroundColor(getResources().getColor(R.color.blue));
                    recyclerView.setVisibility(View.GONE);
                    selectList.setVisibility(View.VISIBLE);
                    selectList.setAdapter(new ArrayAdapter<>(Objects.requireNonNull(getActivity()),R.layout.support_simple_spinner_dropdown_item,branchList));
                    selectList.setOnItemClickListener((parent, view, position, id) -> {
                        project.setText("全部");
                        branch.setBackgroundColor(Color.argb(0,0,0,0));
                        project.setBackgroundColor(Color.argb(0,0,0,0));
                        branch.setText(branchList.get(position));
                        recyclerView.setVisibility(View.VISIBLE);
                        selectList.setVisibility(GONE);
                        loadProgressDialog = new LoadProgressDialog(Objects.requireNonNull(getActivity()),"正在刷新…");
                        loadProgressDialog.show();
                        new Thread(){
                            @Override
                            public void run() {
                                if (branchList.get(position).equals("全部"))
                                {
                                    trainingList = DBUrl.getTraining();
                                }else {
                                    trainingList = DBUrl.getTraining("Of_Company",branchList.get(position));
                                }
                                getActivity().runOnUiThread(TrainingFragment.this::initRecyclerView);
                            }
                        }.start();
                        loadProgressDialog.dismiss();
                        if (branchList.get(position).equals("全部")){
                            project.setClickable(false);
                        }else {
                            projectList.clear();
                            projectList.add("全部");
                            for (int i=0; i<oriTrainingList.size(); i++){
                                if ( oriTrainingList.get(i).getOfCompany() != null && oriTrainingList.get(i).getOfCompany().equals(branchList.get(position))){
                                    if (!projectList.contains(oriTrainingList.get(i).getOfProjectDep())){
                                        projectList.add(oriTrainingList.get(i).getOfProjectDep());
                                    }
                                }
                            }
                            project.setOnClickListener(v1 -> {
                                project.setBackgroundColor(getResources().getColor(R.color.blue));
                                recyclerView.setVisibility(GONE);
                                selectList.setVisibility(View.VISIBLE);
                                selectList.setAdapter(new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item,projectList));
                                selectList.setOnItemClickListener((parent1, view1, position1, id1) -> {
                                    branch.setBackgroundColor(Color.argb(0,0,0,0));
                                    project.setBackgroundColor(Color.argb(0,0,0,0));
                                    project.setText(projectList.get(position1));
                                    recyclerView.setVisibility(View.VISIBLE);
                                    selectList.setVisibility(GONE);
                                    loadProgressDialog = new LoadProgressDialog(Objects.requireNonNull(getActivity()),"正在刷新…");
                                    loadProgressDialog.show();
                                    new Thread(){
                                        @Override
                                        public void run() {
                                            if (projectList.get(position1).equals("全部"))
                                            {
                                                trainingList = DBUrl.getTraining("Of_Company",branch.getText().toString());
                                            }else {
                                                trainingList = DBUrl.getTraining("Of_ProjectDep",projectList.get(position1));
                                            }
                                            getActivity().runOnUiThread(TrainingFragment.this::initRecyclerView);
                                        }
                                    }.start();
                                    loadProgressDialog.dismiss();
                                });
                            });
                        }
                    });
                });
            }
        }
    };
    private void initRecyclerView(){
        ListAdapter listAdapter = new ListAdapter(context, trainingList);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setStackFromEnd(true);
        manager.setReverseLayout(true);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(listAdapter);
        listAdapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, ArrayList<Training> list) {
                new Thread(() -> new DBUrl().updateClickNum(list.get(position).getTrainingID())).start();
                Intent intent = new Intent();
                intent.putExtra("position", position);
                intent.putExtra("list", list);
                intent.setClass(Objects.requireNonNull(getActivity()), TrainingDetailActivity.class);
                startActivity(intent);
            }
            @Override
            public void onItemLongClick(View view, int position) {
                //预留
            }
        });
    }
}
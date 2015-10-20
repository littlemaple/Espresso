package com.rainbow.blue.espresso.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.rainbow.blue.espresso.R;
import com.rainbow.blue.espresso.adapter.MessageAdapter;
import com.rainbow.blue.espresso.bean.Message;

import org.json.JSONArray;

import java.util.Collections;

import jp.wasabeef.recyclerview.animators.LandingAnimator;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener {

    public static final String tag = "MainActivityFragment";
    private static final String url = "http://medzone.sinaapp.com/api/";
    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private Button btnAdd;
    private Button btnDel;


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("main", "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        btnAdd = (Button) rootView.findViewById(R.id.button);
        btnDel = (Button) rootView.findViewById(R.id.button2);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_view);
        adapter = new MessageAdapter(getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setItemAnimator(new LandingAnimator());
        recyclerView.setAdapter(adapter);
        btnAdd.setOnClickListener(this);
        btnDel.setOnClickListener(this);
        bindTouch();
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                                    @Override
                                                    public void onRefresh() {
                                                        Volley.newRequestQueue(getContext()).add(new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                                                            @Override
                                                            public void onResponse(JSONArray response) {
                                                                Log.d(tag, "onResponse:" + response.toString());
                                                                adapter.refresh(Message.createMessage(response));
                                                                swipeRefreshLayout.setRefreshing(false);
                                                            }
                                                        }, new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {
                                                                Log.d(tag, "onErrorResponse:" + error.toString());
                                                                swipeRefreshLayout.setRefreshing(false);
                                                            }
                                                        }));
                                                    }
                                                }
        );
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("main", "onAttach");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("main", "onActivityCreated");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                adapter.add(Message.getDefault());
                break;
            case R.id.button2:
                adapter.remove(0);
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("main", "onSaveInstanceState");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("main", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("main", "onStop");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("main", "onStart");
    }

    public void bindTouch() {
        ItemTouchHelper.Callback callback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.RIGHT) {
            /**
             * @param recyclerView
             * @param viewHolder 拖动的ViewHolder
             * @param target 目标位置的ViewHolder
             * @return
             */
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();//得到拖动ViewHolder的position
                int toPosition = target.getAdapterPosition();//得到目标ViewHolder的position
                if (fromPosition < toPosition) {
                    //分别把中间所有的item的位置重新交换
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(adapter.getDataSet(), i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(adapter.getDataSet(), i, i - 1);
                    }
                }
                adapter.notifyItemMoved(fromPosition, toPosition);
                //返回true表示执行拖动
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                adapter.remove(position);
                adapter.notifyItemRemoved(position);
            }
        };
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);
    }
}

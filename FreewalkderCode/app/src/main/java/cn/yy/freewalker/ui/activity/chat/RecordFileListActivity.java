package cn.yy.freewalker.ui.activity.chat;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.yy.freewalker.R;
import cn.yy.freewalker.ui.activity.BaseActivity;
import cn.yy.freewalker.ui.adapter.RecordFileListAdapter;
import cn.yy.freewalker.ui.widget.listview.ListViewRecordFile;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/15 20:42
 */
public class RecordFileListActivity extends BaseActivity implements RecordFileListAdapter.onListItemDeleteListener,
        AdapterView.OnItemClickListener {


    @BindView(R.id.lv_record_file)
    ListViewRecordFile lvRecordFile;

    RecordFileListAdapter adapter;
    List<String> listFile = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat_record_file_list;
    }

    @Override
    protected void myHandleMsg(Message msg) {

    }


    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onDelete(int index) {
        listFile.remove(index);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = new Bundle();
        bundle.putString("title", listFile.get(position));
        startActivity(RecordListActivity.class, bundle);
    }

    @Override
    protected void initData() {
        //Test START
        //Add device
        listFile.add("aaaa");
        listFile.add("aaffa");
        listFile.add("hello");
        listFile.add("better");
        //Test END


        adapter = new RecordFileListAdapter(RecordFileListActivity.this, listFile);
        adapter.setListener(this);
        lvRecordFile.setOnItemClickListener(this);
        lvRecordFile.setAdapter(adapter);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void doMyCreate() {

    }

    @Override
    protected void causeGC() {

    }


}

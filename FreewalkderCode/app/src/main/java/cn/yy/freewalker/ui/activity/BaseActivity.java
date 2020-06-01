package cn.yy.freewalker.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/18.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected MyHandler mHandler;


    protected class MyHandler extends Handler {
        private WeakReference<BaseActivity> mOuter;

        private MyHandler(BaseActivity activity){
            mOuter = new WeakReference<BaseActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BaseActivity outer = mOuter.get();
            if(outer != null) {
                myHandleMsg(msg);
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.putParcelable("android:support:fragments", null);
        }
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        mHandler = new MyHandler(this);
        initData();
        initViews();
        doMyCreate();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        causeGC();
    }


    protected void startActivity(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), cls);
        startActivity(intent);
    }

    protected void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    protected void startActivityForResult(Class<?> cls, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), cls);
        startActivityForResult(intent, requestCode);
    }

    protected void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), cls);
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }


    protected abstract int getLayoutId();
    //消息管理
    protected abstract void myHandleMsg(Message msg);

    //初始化数据
    protected abstract void initData();

    //初始化页面
    protected abstract void initViews();

    //执行初始化后的事情
    protected abstract void doMyCreate();

    //释放内存
    protected abstract void causeGC();

}

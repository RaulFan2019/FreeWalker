package cn.yy.freewalker.ui.fragment.face;

import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qmuiteam.qmui.qqface.QQFace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import cn.yy.freewalker.R;
import cn.yy.freewalker.adapter.ChatFaceBinder;
import cn.yy.freewalker.bean.ChatFaceBean;
import cn.yy.freewalker.ui.fragment.BaseFragment;
import cn.yy.freewalker.ui.widget.faceView.QDQQFaceManager;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * @author zhao
 * @version 1.0
 * @date 2020/6/9 下午10:14
 */
public class FaceInputFragment extends BaseFragment {

    @BindView(R.id.rv_chat_face)
    RecyclerView mFaceRv;

    private MultiTypeAdapter mFaceAdapter;

    private OnOutputListener listener;

    private ArrayList<Object> mFaceItems = new ArrayList<>();



    /* 构造函数 */
    public static FaceInputFragment newInstance() {
        FaceInputFragment fragment = new FaceInputFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_face_input;
    }

    @Override
    protected void myHandleMsg(Message msg) {

    }

    @Override
    protected void initParams() {
        initFace();

        mFaceRv.setLayoutManager(new GridLayoutManager(getActivity(),8));
        mFaceRv.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener(){

            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {
                view.setOnClickListener(v -> {
                    int position = mFaceRv.getChildAdapterPosition(view);
                    Object bean = mFaceItems.get(position);
                    if(bean instanceof ChatFaceBean){
                        if(listener != null) {
                            listener.output((ChatFaceBean) bean);
                        }
                    }
                });
            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {

            }
        });

        mFaceRv.setAdapter(mFaceAdapter);

    }

    private void initFace(){
        mFaceAdapter = new MultiTypeAdapter();
        mFaceAdapter.register(ChatFaceBean.class,new ChatFaceBinder());

        List<QQFace> faceList = QDQQFaceManager.getInstance().getQQFaceList();
        for (QQFace item : faceList){
            mFaceItems.add(new ChatFaceBean(item.getRes(),item.getName()));
        }
        mFaceAdapter.setItems(mFaceItems);
    }

    @Override
    protected void causeGC() {

    }

    @Override
    protected void onVisible() {

    }

    @Override
    protected void onInVisible() {

    }

    public void setOnOutputListener(OnOutputListener listener){
        this.listener = listener;
    }

    public interface OnOutputListener{
        void output(ChatFaceBean bean);
    }
}

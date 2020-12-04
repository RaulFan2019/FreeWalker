package cn.yy.freewalker.ui.widget.common;

import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import cn.yy.freewalker.R;

/**
 * @author Raul.Fan
 * @email 35686324@qq.com
 * @date 2020/6/8 23:45
 */

public class InputPwdBox extends RelativeLayout {


    private Context context;
    private TextView[] textViews;
    private static int MAXlength = 6;

    public String getInputContent() {
        if (inputContent == null){
            return null;
        }
        if (inputContent.length() > MAXlength){
            return inputContent.substring(0,MAXlength);
        }else {
            return inputContent;
        }

    }

    public void setInputContent(String inputContent) {
        this.inputContent = inputContent;
    }

    private String inputContent;

    private EditText etCode;
    private List<String> codes = new ArrayList<>();
    private InputMethodManager imm;
    private Animation shake;

    public InputPwdBox(Context context) {
        super(context);
        this.context = context;
        loadView();
    }

    public InputPwdBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        loadView();
    }

    public InputPwdBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        loadView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public InputPwdBox(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        loadView();
    }

    private void loadView() {
        //抖动效果
//        shake = AnimationUtils.loadAnimation(context, R.anim.shake);
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = LayoutInflater.from(context).inflate(R.layout.widget_input_pwd_box, this);
        initView(view);
        initEvent();
    }

    private void initView(View view) {
        textViews = new TextView[MAXlength];
        textViews[0] = (TextView) view.findViewById(R.id.tv_code1);
        textViews[1] = (TextView) view.findViewById(R.id.tv_code2);
        textViews[2] = (TextView) view.findViewById(R.id.tv_code3);
        textViews[3] = (TextView) view.findViewById(R.id.tv_code4);
        textViews[4] = (TextView) view.findViewById(R.id.tv_code5);
        textViews[5] = (TextView) view.findViewById(R.id.tv_code6);
        etCode = (EditText) view.findViewById(R.id.et_code);
    }

    private void initEvent() {
        //验证码输入
        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                inputContent = etCode.getText().toString();
                if (inputCompleteListener != null) {
                    if (inputContent.length() >= MAXlength) {
                        inputCompleteListener.inputComplete(inputContent);
                    }
                }
                for (int i = 0; i < MAXlength; i++) {
                    if (i < inputContent.length()) {
                        textViews[i].setText(String.valueOf(inputContent.charAt(i)));
                    } else {
                        textViews[i].setText("");
                    }
                }


            }
        });

    }

    /**
     * 设置高亮颜色
     */
    private void setColor(boolean isShow, int color) {

        for (int i = 0; i < MAXlength; i++) {
            textViews[i].setTextColor(color);
            if (isShow) {
                textViews[i].setBackground(context.getResources().getDrawable(R.drawable.bg_box_tv));
                //抖动
                textViews[i].startAnimation(shake);
            } else {
                textViews[i].setBackground(context.getResources().getDrawable(R.drawable.bg_box_tv));

            }
        }

    }

    /**
     * 获得手机号验证码
     *
     * @return 验证码
     */
    public String getPhoneCode() {
        StringBuilder sb = new StringBuilder();
        for (String code : codes) {
            sb.append(code);
        }
        return sb.toString();
    }


    private InputCompleteListener inputCompleteListener;

    public void setInputCompleteListener(InputCompleteListener inputCompleteListener) {
        this.inputCompleteListener = inputCompleteListener;
    }

    public interface InputCompleteListener {
        void inputComplete(String code);
    }
}

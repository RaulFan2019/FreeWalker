package cn.yy.freewalker.ui.widget.faceView;

import android.content.Context;
import android.util.AttributeSet;

import com.qmuiteam.qmui.qqface.QMUIQQFaceCompiler;
import com.qmuiteam.qmui.qqface.QMUIQQFaceView;

/**
 * @author zhao
 * @version 1.0
 * @date 2020/6/7 下午9:32
 */
public class QQFaceTextView extends QMUIQQFaceView {
    public QQFaceTextView(Context context) {
        super(context);
    }

    public QQFaceTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCompiler(QMUIQQFaceCompiler.getInstance(QDQQFaceManager.getInstance()));
    }
}

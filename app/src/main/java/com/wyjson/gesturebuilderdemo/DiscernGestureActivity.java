package com.wyjson.gesturebuilderdemo;

import android.app.Activity;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.util.List;

/**
 * 识别手势
 *
 * @author Wyjson
 * @version 1
 * @date 2019-09-03 20:44
 * https://github.com/Miserlou/Android-SDK-Samples/tree/master/GestureBuilder/src/com/android/gesture/builder
 * <p>
 * https://www.iteye.com/blog/yinger-fei-1261503
 * https://www.cnblogs.com/ldq2016/p/5290933.html
 * http://blog.sina.com.cn/s/blog_4b93170a0100oxw0.html
 */
public class DiscernGestureActivity extends Activity {

    private Gesture mGesture;
    private GestureLibrary mGestureLibrary;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.discern_gesture);

        tvResult = findViewById(R.id.tv_result);
        tvResult.setMovementMethod(ScrollingMovementMethod.getInstance());

        mGestureLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
        mGestureLibrary.load();

        GestureOverlayView overlay = (GestureOverlayView) findViewById(R.id.gestures_overlay);
        //设置手势可多笔画绘制，默认情况为单笔画绘制
//        overlay.setGestureStrokeType(GestureOverlayView.GESTURE_STROKE_TYPE_MULTIPLE);
        //设置手势的颜色
        overlay.setGestureColor(ContextCompat.getColor(this, R.color.gesture_discern_draw_color));
        //设置还没未能形成手势绘制是的颜色
        overlay.setUncertainGestureColor(ContextCompat.getColor(this, R.color.uncertain_gesture_discern_draw_color));
//        overlay.setUncertainGestureColor(Color.TRANSPARENT);
        //设置手势的粗细
//        overlay.setGestureStrokeWidth(4);

        /*手势绘制完成后淡出屏幕的时间间隔，即绘制完手指离开屏幕后相隔多长时间手势从屏幕上消失；
         * 可以理解为手势绘制完成手指离开屏幕后到调用onGesturePerformed的时间间隔
         * 默认值为420毫秒，这里设置为2秒
         */
        overlay.setFadeOffset(500);

        //绑定监听器
        overlay.addOnGesturePerformedListener(new GestureOverlayView.OnGesturePerformedListener() {
            @Override
            public void onGesturePerformed(GestureOverlayView gestureOverlayView, Gesture gesture) {
                Toast.makeText(DiscernGestureActivity.this, "完成?", Toast.LENGTH_SHORT).show();
            }
        });

        overlay.addOnGesturingListener(new GestureOverlayView.OnGesturingListener() {
            @Override
            public void onGesturingStarted(GestureOverlayView gestureOverlayView) {
                mGesture = null;
            }

            @Override
            public void onGesturingEnded(GestureOverlayView overlay) {
                mGesture = overlay.getGesture();
                if (mGesture.getLength() < CreateGestureActivity.LENGTH_THRESHOLD) {
                    overlay.clear(false);
                } else {
                    List<Prediction> predictions = mGestureLibrary.recognize(mGesture);

                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < predictions.size(); i++) {
                        sb.append(i + " : " + predictions.get(i).name + " : " + predictions.get(i).score);
                        sb.append("\n");
                    }
                    tvResult.setText(sb.toString());
                }
            }
        });

    }

    @SuppressWarnings({"UnusedDeclaration"})
    public void cancelGesture(View v) {
        finish();
    }

}
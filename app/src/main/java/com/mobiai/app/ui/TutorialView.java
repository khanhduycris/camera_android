package com.mobiai.app.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.mobiai.R;
import com.mobiai.app.utils.TrackingEvent;
import com.mobiai.base.basecode.storage.SharedPreferenceUtils;

import java.util.ArrayList;
import java.util.List;

public class TutorialView extends FrameLayout{

    ConstraintLayout tutorial1;
    ConstraintLayout tutorial2;
    ConstraintLayout tutorial3;
    ConstraintLayout tutorial4;
    ConstraintLayout tutorial5;



    private final OnClickListener closeListener = v -> {
        SharedPreferenceUtils.INSTANCE.setShowedTutorialFistOpen(false);
        setVisibility(GONE);

    };

    public void initView(){
        (new Handler(Looper.getMainLooper())).post(new Runnable() {
            @Override
            public void run() {

                tutorial1 = findViewById(R.id.tutorial1);
                tutorial2 = findViewById(R.id.tutorial2);
                tutorial3 = findViewById(R.id.tutorial3);
                tutorial4 = findViewById(R.id.tutorial4);
                tutorial5 = findViewById(R.id.tutorial5);

                tutorialViews.add(tutorial1);
                tutorialViews.add(tutorial2);
                tutorialViews.add(tutorial3);
                tutorialViews.add(tutorial4);
                tutorialViews.add(tutorial5);

                findViewById(R.id.img_Exit_Tutorial1).setOnClickListener(closeListener);
                tutorial1.setOnClickListener(v -> setTutorialIndex(1));
                findViewById(R.id.tv_Next_Tutorial1).setOnClickListener(v -> setTutorialIndex(1));

                findViewById(R.id.img_Exit_Tutorial2).setOnClickListener(closeListener);
                tutorial2.setOnClickListener(v -> setTutorialIndex(2));
                findViewById(R.id.tv_Next_Tutorial2).setOnClickListener(v -> setTutorialIndex(2));

                findViewById(R.id.img_Exit_Tutorial3).setOnClickListener(closeListener);
                tutorial3.setOnClickListener(v -> setTutorialIndex(3));
                findViewById(R.id.tv_Next_Tutorial3).setOnClickListener(v -> setTutorialIndex(3));

                findViewById(R.id.img_Exit_Tutorial4).setOnClickListener(closeListener);
                tutorial4.setOnClickListener(v -> setTutorialIndex(4));
                findViewById(R.id.tv_Next_Tutorial4).setOnClickListener(v -> setTutorialIndex(4));

                findViewById(R.id.img_Exit_Tutorial5).setOnClickListener(closeListener);
                findViewById(R.id.btn_Enjoy_App).setOnClickListener(closeListener);

            }
        });
    }

    List<View> tutorialViews = new ArrayList<>();

    private void setTutorialIndex(int index) {

        for (View v : tutorialViews) {
            if(v != null) {
                if((tutorialViews.indexOf(v)) == index){
                    v.setVisibility(VISIBLE);
                }
                else{
                    v.setVisibility(GONE);
                }

            }
        }

        TrackingEvent.INSTANCE.logEvent(TrackingEvent.CLICK_NEXT_TUTORIAL);

    }



    public TutorialView(@NonNull Context context) {
        super(context);
        initView();


    }

    public TutorialView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TutorialView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public TutorialView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }
}

package com.iishanto.cpalarm;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iishanto.cpalarm.Codeforces.CfApi;
import com.iishanto.cpalarm.CustomViews.AnalogClock;
import com.iishanto.cpalarm.EventRecycler.Adapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Window window=getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.white));

        getWindow().setStatusBarColor(this.getResources().getColor(R.color.white));

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        ToolsSingleton.getInstance().setMainContext(getBaseContext());
        ToolsSingleton.getInstance().getMediaPlayer(this).stop();

        setContentView(R.layout.activity_main);

        RecyclerView rcv=findViewById(R.id.activityList);
        rcv.setLayoutManager(new LinearLayoutManager(this));

        Adapter adpt=new Adapter();



        AnalogClock ang=findViewById(R.id.analogClock);
        rcv.setAdapter(adpt);
        CfApi cf=new CfApi(adpt,ang.anim());

        ImageButton reload=findViewById(R.id.reload);

        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cf.isRefreshing()) return;
                adpt.clear();
                cf.refresh();
            }
        });

    }
}
package com.imac.voice_app.util.mainmenu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.imac.voice_app.R;
import com.imac.voice_app.view.mainmenu.MainMenu;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainMenu mainMenu = new MainMenu(this, new MainMenu.OnClickEvent() {
            @Override
            public void onClick(View view, int position) {

            }
        });
    }
}

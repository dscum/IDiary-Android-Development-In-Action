package com.example.idiaryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //find views in our xml file
        Toolbar toolbar=findViewById(R.id.main_activity_materialToolbar);
        toolbar.inflateMenu(R.menu.main_activity_toolbar_menu);

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser =mAuth.getCurrentUser();
        toolbar.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(firebaseUser!=null){
                    mAuth.signOut();
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                }
                return false;
            }
        });
    }
}
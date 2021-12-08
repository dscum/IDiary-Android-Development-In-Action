package com.example.idiaryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private ArrayList<Diary> diariesList;
    private DiaryAdapter diaryAdapter;
    private LottieAnimationView lottieAnimationView;
    private TextView noDiaryTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //find views in our xml file
        Toolbar toolbar=findViewById(R.id.main_activity_materialToolbar);
        toolbar.inflateMenu(R.menu.main_activity_toolbar_menu);
        ExtendedFloatingActionButton addDiaryButton = findViewById(R.id.add_diary_button);
        noDiaryTextView = findViewById(R.id.no_diary_text_view);
        lottieAnimationView = findViewById(R.id.lottie_animation);
        recyclerView=findViewById(R.id.recycler_view);
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser =mAuth.getCurrentUser();
        if(firebaseUser!=null) {
            //need user unique Id to retrieve diaries based on each user node in firebase database
            getDiaries(firebaseUser.getUid());
        }
        //in order to enable our receycler view we need to initialize a horizontal linear layout for it
        //which this can be done by calling LinearLayoutManager
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);
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
        //action to navigate to our new activity [AddDiary Activity]
        addDiaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firebaseUser!=null){
                    startActivity(new Intent(getApplicationContext(),AddDiary.class));
                }
            }
        });
    }
    private void getDiaries(String userId){
        //initialize diariesList
        diariesList=new ArrayList<>();
        //initialize adapter and we set the DiaryAdapter class to RecyclerView
        diaryAdapter=new DiaryAdapter(getApplicationContext(), diariesList);
        recyclerView.setAdapter(diaryAdapter);
        // make reference to real-time database
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                //snapshot in this method is holding the info that belongs to each diary
                //by getting each snapshot value we can assign it to a Diary object that we created
                //first need to check if any snapshot exist or in other words there is any diary available
                if (snapshot.exists()){
                    // snapshot consist of all existing diaries in our database
                    //we need to loop through all of them and add them one by one to our diary list
                    // we also need to clear our diary list before for loop otherwise later when a new diary is added
                    // the list of diary populates two times and it duplicates items in our list
                    diariesList.clear();
                    for(DataSnapshot snap:snapshot.getChildren()){
                        Diary diary=snap.getValue(Diary.class);
                        //then we add each diary to our list of diaries
                        diariesList.add(diary);
                    }
                    // we reverse the list to display the newest diaries in our recyclerview
                    Collections.reverse(diariesList);
                    //then we need to inform the diary adapter a new Diary is added to the list so it makes the necessary changes
                    diaryAdapter.notifyDataSetChanged();

                } else{
                    //if there is no diary we can add a text view that says no diary yet
                    //or we can add an animation to our app for a better user experience
                    //or both can be done
                    lottieAnimationView.setVisibility(View.VISIBLE);
                    noDiaryTextView.setVisibility(View.VISIBLE);
                    lottieAnimationView.playAnimation();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}
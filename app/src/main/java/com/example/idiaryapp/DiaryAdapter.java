package com.example.idiaryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;


public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder> {
    //adapter does not contain any context on its own unlike activities
    //so we need to pass a context when making instance of DiaryAdapter.
    final Context context;
    final ArrayList<Diary> diariesList; //we will pass lists of Diary to display all diary together

    public DiaryAdapter(Context context, ArrayList<Diary> diariesList){
        this.context = context;
        this.diariesList = diariesList;
    }
    @NonNull
    @NotNull
    @Override
    public DiaryAdapter.DiaryViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        // just like activities that they are connected to their xml files by setContentView()
        //in RecyclerView Adapater we need to connect the diary layout
        //which this connection happens by inflating a layout and it requires a context
        //which the context will be passed to constructor of DiaryAdapater class from the parent layout that our Recycler exist in
        //in our case the recycler view is in our main activity
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.diary_layout, parent, false);
        //this view will be returned to DiaryViewHolder class
        //from its name is obvious that it holds the content of whatever we want to display in our Diary layout.
        return new DiaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DiaryAdapter.DiaryViewHolder holder, int position) {
        // this method is to assign contents to the views we found in DiaryViewHolder class
        //holder variable containts the views initialized or found in DiaryViewHolder class
        //for that we need to get the content from list of Diaries and assign them to different positions in our recyclerview adapter
       Diary diary=diariesList.get(position);
       //first we set the content for title and note since both of these are available in both of diary type
       holder.diaryTitle.setText(diary.getTitle());
       holder.diaryNote.setText(diary.getNote());
       //then we check the diary type to set the content for image and name of place correctly
       String diaryType=diary.getType();
       if(diaryType.equals("text")){
           holder.diaryLoc.setVisibility(View.GONE);
           //default image if there isn't any image attached
           Glide.with(context)
                   .load(R.drawable.login_back_ground_image)
                   .transform(new RoundedCorners(15),new CenterCrop())
                   .transition(DrawableTransitionOptions.withCrossFade())
                   .into(holder.diaryImage);

//           holder.diaryImage.setVisibility(View.GONE);

       }else if(diaryType.equals("image")){
           holder.diaryLoc.setText(diary.getPlaceName());
           String imageURL=diary.getImage();
           //now we check to make sure imageURL is not empty just to avoid app to crash
           if(!imageURL.isEmpty()){
               Glide.with(context)
                       .load(imageURL)
                       .transform(new RoundedCorners(15),new CenterCrop())
                       .transition(DrawableTransitionOptions.withCrossFade())
                       .into(holder.diaryImage);
           }

        }
    }

    @Override
    public int getItemCount() {
        return diariesList.size();
    }

    public static class DiaryViewHolder extends RecyclerView.ViewHolder {
        private final TextView diaryTitle;
        private final TextView diaryNote;
        private final TextView diaryLoc;
        private final ImageView diaryImage;
        public DiaryViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            //initialize the views in diary_layout xml here
            diaryNote=itemView.findViewById(R.id.adapter_diary_note);
            diaryTitle=itemView.findViewById(R.id.adapter_diary_title);
            diaryLoc=itemView.findViewById(R.id.adapter_diary_location);
            diaryImage=itemView.findViewById(R.id.adapter_diary_image);


        }
    }
}

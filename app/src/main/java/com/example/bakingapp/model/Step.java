package com.example.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "step", indices = {@Index("recipe_id")},
        foreignKeys = @ForeignKey(
        entity = Recipe.class,
        parentColumns = "id",
        childColumns = "recipe_id",
        onDelete = CASCADE))

public class Step implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "step_order")
    private int stepOrder;
    @ColumnInfo(name = "short_description")
    private String shortDescription;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "video_url")
    private String videoURL;
    @ColumnInfo(name = "thumbnail_url")
    private String thumbnailURL;
    @ColumnInfo(name = "recipe_id")
    private int recipeId;


    public Step (int id, int stepOrder, String shortDescription, String description, String videoURL,
                 String thumbnailURL, int recipeId){
        this.id = id;
        this.stepOrder = stepOrder;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
        this.recipeId = recipeId;
    }

    @Ignore
    protected Step(Parcel in) {
        id = in.readInt();
        stepOrder = in.readInt();
        shortDescription  = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
        recipeId = in.readInt();
    }

    @Ignore
    public Step (int id, int stepOrder, String shortDescription, String description, String videoURL, String thumbnailURL){
        this.id = id;
        this.stepOrder = stepOrder;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    public int getId() {
        return id;
    }

    public int getStepOrder() {
        return stepOrder;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public int getRecipeId() {
        return recipeId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(id);
        parcel.writeInt(stepOrder);
        parcel.writeString(shortDescription);
        parcel.writeString(description);
        parcel.writeString(videoURL);
        parcel.writeString(thumbnailURL);
        parcel.writeInt(recipeId);
    }

}

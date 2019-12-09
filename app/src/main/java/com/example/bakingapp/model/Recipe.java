package com.example.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "recipe")
public class Recipe implements Parcelable {

    @PrimaryKey(autoGenerate = false)
    private int id;
    @ColumnInfo(name = "name")
    private String name;
    @Ignore
    private List<Ingredient> ingredients;
    @Ignore
    private List<Step> steps;
    @ColumnInfo(name = "serving")
    private int serving;
    @ColumnInfo(name = "image")
    private String image;


    public Recipe (int id, String name, int serving, String image){
        this.id = id;
        this.name = name;
        this.serving = serving;
        this.image = image;
    }

    @Ignore
    protected Recipe(Parcel in) {
        id = in.readInt();
        name  = in.readString();
        ingredients = in.readParcelableList(ingredients, Ingredient.class.getClassLoader());
        steps = in.readParcelableList(steps, Step.class.getClassLoader());
        serving = in.readInt();
        image = in.readString();
    }

    @Ignore
    public Recipe (int id, String name, ArrayList<Ingredient> ingredients, ArrayList<Step> steps,
                   int serving, String image){
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.serving = serving;
        this.image = image;
    }


    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public int getServing() {
        return serving;
    }

    public String getImage() {
        return image;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeList(ingredients);
        parcel.writeList(steps);
        parcel.writeInt(serving);
        parcel.writeString(image);
    }
}

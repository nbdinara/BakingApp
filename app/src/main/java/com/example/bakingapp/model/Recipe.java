package com.example.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "recipe")
public class Recipe implements Parcelable {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "recipe_id")
    public int recipe_id;
    @ColumnInfo(name = "name")
    private String name;
    @Ignore
    private ArrayList<Ingredient> ingredients;
    @Ignore
    private ArrayList<Step> steps;
    @ColumnInfo(name = "serving")
    private int serving;
    @ColumnInfo(name = "image")
    private String image;


    public Recipe (int recipe_id, String name, int serving, String image){
        this.recipe_id = recipe_id;
        this.name = name;
        this.serving = serving;
        this.image = image;
    }

    @Ignore
    protected Recipe(Parcel in) {
        recipe_id = in.readInt();
        name  = in.readString();
        ingredients = in.readArrayList(Ingredient.class.getClassLoader());
        steps = in.readArrayList(Step.class.getClassLoader());
        serving = in.readInt();
        recipe_id = in.readInt();
        image = in.readString();
    }

    @Ignore
    public Recipe (int recipe_id, String name, ArrayList<Ingredient> ingredients, ArrayList<Step> steps,
                   int serving, String image){
        this.recipe_id = recipe_id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.serving = serving;
        this.image = image;
    }

    @Ignore
    public Recipe (String name, int serving, String image){
        this.name = name;
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


    public int getRecipeId() {
        return recipe_id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public ArrayList<Step> getSteps() {
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

        parcel.writeInt(recipe_id);
        parcel.writeString(name);
        parcel.writeList(ingredients);
        parcel.writeList(steps);
        parcel.writeInt(serving);
        parcel.writeString(image);
    }
}

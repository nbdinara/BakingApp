package com.example.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Recipe implements Parcelable {

    private int id;
    private String name;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Step> steps;
    private int serving;
    private String image;

    protected Recipe(Parcel in) {
        id = in.readInt();
        name  = in.readString();
        ingredients = in.readArrayList(Ingredient.class.getClassLoader());
        steps = in.readArrayList(Step.class.getClassLoader());
        serving = in.readInt();
        id = in.readInt();
        image = in.readString();
    }

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

        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeList(ingredients);
        parcel.writeList(steps);
        parcel.writeInt(serving);
        parcel.writeString(image);
    }
}

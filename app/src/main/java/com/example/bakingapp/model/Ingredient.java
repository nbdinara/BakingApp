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

@Entity(tableName = "ingredient", indices = {@Index("recipe_id")},
        foreignKeys = @ForeignKey(
        entity = Recipe.class,
        parentColumns = "recipe_id",
        childColumns = "recipe_id",
        onDelete = CASCADE))

public class Ingredient  implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "quantity")
    private double quantity;
    @ColumnInfo(name = "measure")
    private String measure;
    @ColumnInfo(name = "ingredient")
    private String ingredient;
    @ColumnInfo(name = "recipe_id")
    private int recipeId;


    public Ingredient(int id, double quantity, String measure, String ingredient, int recipeId){
        this.id = id;
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
        this.recipeId = recipeId;
    }

    @Ignore
    public Ingredient(double quantity, String measure, String ingredient){
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    @Ignore
    public Ingredient(double quantity, String measure, String ingredient, int recipeId){
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
        this.recipeId = recipeId;

    }

    @Ignore
    protected Ingredient(Parcel in) {
        this.id = in.readInt();
        quantity = in.readDouble();
        measure  = in.readString();
        ingredient = in.readString();
        this.recipeId = in.readInt();

    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


    public double getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public int getId() {
        return id;
    }

    public int getRecipeId() {
        return recipeId;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeDouble(quantity);
        parcel.writeString(measure);
        parcel.writeString(ingredient);
        parcel.writeInt(recipeId);
    }
}

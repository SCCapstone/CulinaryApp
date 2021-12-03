package com.github.CulinaryApp.views;

import androidx.appcompat.app.AppCompatActivity;
import com.github.CulinaryApp.R;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.w3c.dom.Text;
import de.hdodenhof.circleimageview.CircleImageView;

public class RecipesViewAdapter {

    /*  This class adapts the layouts for the individual recipes to container layout -
     *    Container for RecyclerView is activity_recipes.xml  */

    private static final String TAG = "RecylcerViewAdapter";

    CircleImageView imageOfRecipe;
    TextView nameOfRecipe;
    RelativeLayout parentLayout;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            // bind ViewHolder obj to elm's in recipe (singular) xml
            imageOfRecipe = itemView.findViewById(R.id.recipe_image);
            nameOfRecipe = itemView.findViewById(R.id.recipe_name);
            parentLayout = itemView.findViewById(R.id.individual_recipe_item_layout);
        }
    }
}
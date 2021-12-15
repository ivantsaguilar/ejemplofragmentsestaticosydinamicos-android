package myteam.androides.react.app.com.myapplication.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import myteam.androides.react.app.com.myapplication.Fragments.ItemsFragment;
import myteam.androides.react.app.com.myapplication.R;

/**
 * Created by ivants on 13/12/16.
 * Activity lista para el fragment
 */

public class ItemList extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemlist);

        Bundle bundle = getIntent().getExtras();

         if(savedInstanceState == null){

             ItemsFragment fragment = new ItemsFragment();
             fragment.setArguments(bundle);
             getSupportFragmentManager().beginTransaction().
                     add(R.id.list_item, fragment).
                     commit();
         }
    }
}

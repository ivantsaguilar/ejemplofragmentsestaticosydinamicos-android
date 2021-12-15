package myteam.androides.react.app.com.myapplication.Activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import myteam.androides.react.app.com.myapplication.Fragments.ItemsFragment;
import myteam.androides.react.app.com.myapplication.Fragments.ListaFragment;
import myteam.androides.react.app.com.myapplication.R;


/**
 * Base de datos exportada desde un manejador de base de datos
 * Recycler con una base de datos
 * Adaptador con cursores para la extraccion de los datos con querys
 * Consultas a la base de datos desde un fragment
 */
public class MainActivity extends AppCompatActivity implements ListaFragment.CallBacks,ListaFragment.Refresh{

    //Este nos va a decir cuando existen 2 fragments
    public static boolean TwoPain;
    private static final String ELEMENTS_TAG = "ELEMENTS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.list_item) != null) {
            TwoPain = true;

            //Vamos a remplazar el contenido en blanco del FrameLayout con un nuevo fragment
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.list_item, new ItemsFragment(), ELEMENTS_TAG).
                        commit();
            } else {
                TwoPain = false;
            }
        }
    }

    public boolean getTwoPane(){
        return this.TwoPain;
    }

    @Override
    public void onItemSelected(String nombreLista, String lista) {
        if (TwoPain){
            //Inicia un fragmento
            Bundle bundle = new Bundle();
            bundle.putString("nombreLista",nombreLista);
            bundle.putString("lista",lista);
            ItemsFragment itemsFragment = new ItemsFragment();
            itemsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.list_item, itemsFragment).
                    commit();
        }else{
            //Inicia una actividad
            Intent intent = new Intent(this, ItemList.class);
            intent.putExtra("nombreLista",nombreLista);
            intent.putExtra("lista",lista);
            startActivity(intent);
        }

    }

    //Refrescamos la pantalla mandando un dato a la clase ItemFragment mediante el bundle
    @Override
    public void refreshList() {
        Bundle bundle = new Bundle();
        ItemsFragment itemsFragment = new ItemsFragment();
        bundle.putBoolean("restart",true);
        itemsFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().
                replace(R.id.list_item,
                        itemsFragment).
                commit();
    }
}
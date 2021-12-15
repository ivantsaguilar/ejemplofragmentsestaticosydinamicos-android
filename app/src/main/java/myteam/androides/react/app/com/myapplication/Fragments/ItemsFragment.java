package myteam.androides.react.app.com.myapplication.Fragments;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

import myteam.androides.react.app.com.myapplication.Helpers.DataBaseHelper;
import myteam.androides.react.app.com.myapplication.R;

/**
 * Created by ivants on 13/12/16.
 */

public class ItemsFragment extends Fragment {
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    ListView listView;
    TextView addItem;
    SimpleCursorAdapter cursorAdapter;
    CursorLoader cursorLoader;
    Cursor myCursor;
    String lista;
    DataBaseHelper myDataBaseHelper;
    String[] from;
    int[] to;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView  = inflater.inflate(R.layout.items_fragment,container,false);

        /**
        toolbar = (Toolbar)rootView.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        toolbar.setTitle("nombre del item");
        activity.setSupportActionBar(toolbar);

        collapsingToolbarLayout = (CollapsingToolbarLayout)rootView.findViewById(R.id.collapsing);
        collapsingToolbarLayout.setTitle("");
        */

        listView = (ListView)rootView.findViewById(R.id.list_item);
        addItem = (TextView)rootView.findViewById(R.id.addItem);

        Bundle bundle = getArguments();

        if(bundle != null) {
            boolean restart = bundle.getBoolean("restart");

            if (restart) {
                listView.setAdapter(null);

            } else {

                lista = (String) bundle.get("lista");
                DataBaseHelper myDBHelper = new DataBaseHelper(getActivity());

                try {
                    myDBHelper.createDataBase();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    myDBHelper.openDataBase();
                    myCursor = myDBHelper.fetchItemsList(lista);
                    myDBHelper.closeDataBase();

                    if (myCursor != null) {
                        //Los parametros de from y to deben de coincidir para que la DB
                        //muestre bien los datos
                        from = new String[]{"_id", "item"};
                        to = new int[]{R.id.id_item_list, R.id.title_item};

                        cursorAdapter = new SimpleCursorAdapter(getActivity().getApplicationContext(),
                                R.layout.item_list,
                                myCursor,
                                from,
                                to,
                                0);
                        listView.setAdapter(cursorAdapter);
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Lista vacia", Toast.LENGTH_SHORT).show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        //Habilitamos el click para agregar elementos
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity().getApplicationContext(),"click agregar",Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Agregar un item");
                final EditText input = new EditText(getActivity());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                //boton positivo y negativo
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity().getApplicationContext());
                        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
                        ContentValues valores = new ContentValues();
                        valores.put("item",input.getText().toString());

                        db.insert(lista,null,valores);
                        db.close();

                        //Actualizamos la lista
                        try{
                            dataBaseHelper.openDataBase();
                            Cursor cursor = dataBaseHelper.fetchItemsList(lista);
                            dataBaseHelper.closeDataBase();

                            String[] from = new String[]{"_id","item"};
                            int[] to = new int[]{R.id.id_item_list,R.id.title_item};
                            cursorAdapter =
                                    new SimpleCursorAdapter(getActivity().getApplicationContext(),
                                    R.layout.item_list,
                                    cursor,
                                    from,
                                    to,
                                    0);
                            listView.setAdapter(cursorAdapter);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                builder.show();
            }
        });

        //Habilitamos el click a los elementos de la lista
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //Toast.makeText(getActivity(),"click lista",Toast.LENGTH_SHORT).show();
                //Obtengo el id del elemento
                final TextView ID = (TextView) view.findViewById(R.id.id_item_list);

                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("Eliminar");
                dialog.setMessage("¿Seguro que deseas eliminar el item?");
                dialog.setNegativeButton("Cancelar",null); //No quiero que haga nada
                dialog.setPositiveButton("Aceptar", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Toast.makeText(getActivity(),"eliminar",Toast.LENGTH_SHORT).show();

                        myDataBaseHelper = new DataBaseHelper(getActivity().getApplicationContext());
                        SQLiteDatabase db = myDataBaseHelper.getWritableDatabase();
                        String where = "_id = '" + ID.getText().toString() + "'";

                        Toast.makeText(getActivity().getApplicationContext(),
                                ID.getText().toString(),Toast.LENGTH_SHORT).show();

                        db.delete(lista,where,null);
                        db.close();

                        //Actualizamos la lista
                        try{
                            myDataBaseHelper.openDataBase();
                            Cursor cursor = myDataBaseHelper.fetchItemsList(lista);
                            String[] from = new String[]{"_id","item"};
                            int[] to = new int[]{R.id.id_item_list,R.id.title_item};
                            cursorAdapter = new SimpleCursorAdapter(getActivity().getApplicationContext(),
                            R.layout.item_list,
                                    cursor,
                                    from,
                                    to,
                                    0);
                            myDataBaseHelper.closeDataBase();
                            listView.setAdapter(cursorAdapter);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });

                //Mostramos el dialog
                dialog.show();
            }
        });

        return rootView;
    }
}

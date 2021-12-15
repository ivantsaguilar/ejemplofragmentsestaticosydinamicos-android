package myteam.androides.react.app.com.myapplication.Fragments;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import myteam.androides.react.app.com.myapplication.Activities.MainActivity;
import myteam.androides.react.app.com.myapplication.Adapters.ListaAdapter;
import myteam.androides.react.app.com.myapplication.Helpers.DataBaseHelper;
import myteam.androides.react.app.com.myapplication.R;
import myteam.androides.react.app.com.myapplication.RecyclerItemClickListener;

/**
 * Created by ivants on 10/12/16.
 */

public class ListaFragment extends Fragment{

    View rootView;
    RecyclerView recyclerView;
    ListaAdapter listaAdapter;
    DataBaseHelper myDBHelper;
    FloatingActionButton fab;
    String listarel;
    TextView delete;


    private static final String TAG = "RecyclerViewFragment";

    /**String[] data = new String[]{"Elemento1","Elemento2","Elemento3","Elemento4","Elemento5",
                                "Elemento1","Elemento2","Elemento3","Elemento4","Elemento5",
                                "Elemento1","Elemento2","Elemento3","Elemento4","Elemento5",
                                "Elemento1","Elemento2","Elemento3","Elemento4","Elemento5"};
    */

    //Metodo que se hará cargo de la comunicacion
    public interface CallBacks{
        public void onItemSelected(String nombreLista, String lista);
    }

    public interface Refresh{
        public void refreshList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.lista_fragment,container,false);
        rootView.setTag(TAG);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.lista);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        fab = (FloatingActionButton)rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialogItem = new AlertDialog.Builder(getActivity());
                final EditText input = new EditText(getActivity());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                dialogItem.setView(input);
                dialogItem.setMessage("Nombre de la nueva lista").
                        setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();

                            }
                        }).
                        setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try{
                                    myDBHelper.openDataBase();
                                    Cursor cursor = myDBHelper.fetchAllList();
                                    //Verifico que el cursor contenga elementos
                                    if(cursor != null){
                                        cursor.moveToFirst();
                                        int id_ = cursor.getInt(cursor.getColumnIndex(cursor.getColumnName(0)))+1;
                                        //listarel = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
                                        listarel = ""+id_;
                                        cursor.close();

                                        //Toast.makeText(getActivity(), "_id's" + id_, Toast.LENGTH_SHORT).show();
                                        myDBHelper.closeDataBase();
                                    }
                                }catch (SQLException e){
                                    e.printStackTrace();
                                }

                                DataBaseHelper myDBHelper2 = new DataBaseHelper(getActivity().getApplicationContext());
                                SQLiteDatabase db = myDBHelper2.getWritableDatabase();
                                ContentValues values2 = new ContentValues();
                                values2.put("nombre",input.getText().toString());
                                values2.put("lista_rel","Lista"+ listarel);
                                String lista = "Lista" + listarel;

                                db.insert("listas",null,values2);
                                db.execSQL("CREATE TABLE '"+ lista + "' ('_id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, 'item' TEXT)");

                                ContentValues v = new ContentValues();
                                v.put("item","Item A");
                                db.insert(lista,null,v);
                                db.close();

                                try{
                                    myDBHelper.openDataBase();
                                    Cursor cursorRefresh = myDBHelper.fetchAllList();
                                    myDBHelper.closeDataBase();
                                    if(cursorRefresh != null){
                                        listaAdapter = new ListaAdapter(getActivity().getApplicationContext(),
                                                cursorRefresh);
                                        recyclerView.setAdapter(listaAdapter);
                                        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity().getApplicationContext(),
                                                new OnItemClickListener()));
                                    }
                                }catch (SQLException e){
                                    e.printStackTrace();
                                }
                            }
                        });
                AlertDialog dialog = dialogItem.create();
                dialog.show();

            }

        });

        //Necesitamos crear un metodo para dar click a los elementos del recycler view
        //Nos traemos la base de datos
        myDBHelper = new DataBaseHelper(getActivity().getApplicationContext());
        //Verificamos la base de datos, en nuestro caso la copiamos
        //Estamos trabajando con archivos "IOException"
        try {
            myDBHelper.createDataBase();
        } catch (IOException e) {
            throw  new Error("No se pudo crear la DB");
        }

        //Creamo el dataBaseHelper
        //Hacemos la consulta en donde se trae todos los elementos
        //Estamos trabajando con la DB "SQLException
        try{
            //Siempre tenemos que abrir la base de datos
            myDBHelper.openDataBase();
            Cursor cursor = myDBHelper.fetchAllList();
            if(cursor != null){
                listaAdapter = new ListaAdapter(getActivity().getApplicationContext(),cursor);
                //Llenamos el recycler
                recyclerView.setAdapter(listaAdapter);
                recyclerView.addOnItemTouchListener(
                        new RecyclerItemClickListener(
                                getActivity().getApplicationContext(),
                                new OnItemClickListener()));
            }
            myDBHelper.closeDataBase();
        }catch (SQLException e){
            throw new Error("No se pudo realizar la consulta");
        }
        return rootView;
    }

    private class OnItemClickListener extends RecyclerItemClickListener.SimpleOnItemClickListener{

        //Click simple
        @Override
        public void onItemClick(View childView, int position) {
            TextView textView = (TextView)childView.findViewById(R.id.title);
            TextView listaNombre = (TextView)childView.findViewById(R.id.listaNombre);

            ((CallBacks)getActivity()).onItemSelected(
                    textView.getText().toString(),
                    listaNombre.getText().toString());
            //Toast.makeText(getActivity().getApplicationContext(),"1. " + textView.getText().toString() + "\n" + "2." + listaNombre.getText().toString(),Toast.LENGTH_SHORT).show();
        }

        //CLick largo
        @Override
        public void onItemLongPress(View childView, int position) {
            delete = (TextView)childView.findViewById(R.id.listaNombre);

            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setTitle("Eliminar lista");
            dialog.setMessage("¿Seguro que deseas eliminar esta lista?");
            dialog.setNegativeButton("Cancelar",null);
            dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    myDBHelper = new DataBaseHelper(getActivity().getApplicationContext());
                    SQLiteDatabase db = myDBHelper.getWritableDatabase();
                    String where = "lista_rel" + "=?";
                    String[] whereArgs = new String[]{delete.getText().toString()};

                    db.delete("listas",where,whereArgs);
                    db.execSQL("DROP TABLE IF EXISTS "+delete.getText().toString());
                    db.close();

                    Toast.makeText(getActivity(), "Se elimino la : " + delete.getText().toString(), Toast.LENGTH_SHORT).show();

                    //Actualizamos la vista
                    try{
                        myDBHelper.openDataBase();
                        Cursor cursor = myDBHelper.fetchAllList();
                        if(cursor != null){
                            listaAdapter = new ListaAdapter(getActivity().getApplicationContext(),cursor);
                            recyclerView.setAdapter(listaAdapter);
                            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),new OnItemClickListener()));
                        }

                        //Refrescamos la vista para la tableta
                        if(MainActivity.TwoPain){
                            ((Refresh)getActivity()).refreshList();

                        }

                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                }
            });
            dialog.show();

        }
    }
}

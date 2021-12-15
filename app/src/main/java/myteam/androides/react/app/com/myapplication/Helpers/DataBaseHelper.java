package myteam.androides.react.app.com.myapplication.Helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by ivants on 11/12/16.
 */

public class DataBaseHelper extends SQLiteOpenHelper{

    //Le decimos en que paquete y como se llama nuestro archivo
    private static String DBPath="/data/data/myteam.androides.react.app.com.myapplication/databases/";
    private static String DB_Name = "dbListas.sqlite";
    private final Context myContext;
    private SQLiteDatabase myDB;


    public DataBaseHelper(Context context){
        super(context,DB_Name,null,1);
        this.myContext = context;
    }

    public void createDataBase() throws IOException{
        //¿Ya existe la base de datos?
        boolean dbExist = checkDataBase();
        SQLiteDatabase db_Read = null;

        //Si la DB existe no hacemos nada porque ya existe
        if(dbExist){

        }else{
            db_Read = this.getReadableDatabase();
            db_Read.close();
            //No sabemos si la copia se esta haciendo correctamente
            try{
                copyDataBase();
            }catch (Exception e){
                throw new Error("Error en la copia de la base de datos");
            }
        }
    }

    public boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DBPath + DB_Name;
            checkDB = SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READWRITE);
        }catch (Exception e){
            File dbFile = new File(DBPath + DB_Name);
            return  dbFile.exists();
        }

        //Si es distinta a nulo significa que si existe, la abrio y tenemos que cerrarla
        if(checkDB != null){
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }

    private void copyDataBase() throws IOException{

        //Vamos a copiar el archivo
        InputStream myInput = myContext.getAssets().open(DB_Name);
        String outFileName = DBPath + DB_Name;
        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;

        //-1 ya no hay nada
        while((length = myInput.read(buffer)) != -1){
            //Aqui hacemos la copia
            if(length > 0){
                myOutput.write(buffer,0,length);
            }
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException{

        String myPath = DBPath + DB_Name;
        myDB = SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READONLY);
    }

    public synchronized void closeDataBase(){

        if(myDB != null){
            myDB.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Yo no voy a usar el metodo onCreate porque ya tengo la base de datos en un archivo
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //Coidgo de la actualizacion de la base de datos
        //Verificamos si oldVersion es distinto del newVersion ponemos un if
        try{
            createDataBase();
            //Debo poner la forma en la que voy a crear,copiar la nueva base de datos
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public Cursor fetchAllList() throws SQLException{
        //Tenemos que regresar el cursor resultado del query
        Cursor cursor = myDB.rawQuery("SELECT * FROM listas ORDER BY _id DESC",null);
        if(cursor != null){
            //Nos debemos de asegurar que el cursor este apuntando a la primera posicion
            cursor.moveToFirst();
        }

        return cursor;
    }

    public Cursor fetchItemsList(String lista) throws SQLException{
        Cursor cursor = myDB.rawQuery("SELECT * FROM " + lista + " ORDER BY _id DESC", null);
        if(cursor != null)
            cursor.moveToFirst();

        return  cursor;
    }
}

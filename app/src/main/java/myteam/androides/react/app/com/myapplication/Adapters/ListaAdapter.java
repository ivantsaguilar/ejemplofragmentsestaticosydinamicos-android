package myteam.androides.react.app.com.myapplication.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import myteam.androides.react.app.com.myapplication.R;

/**
 * Created by ivants on 10/12/16.
 */

public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.ViewHolder>{
    Context context;
    CursorAdapter cursorAdapter;

    //Constructor
    public ListaAdapter(Context context, Cursor cursor){
        this.context = context;
        cursorAdapter = new CursorAdapter(context,cursor,0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View retView = inflater.inflate(R.layout.item,parent,false);
                return retView;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView textView = (TextView)view.findViewById(R.id.title);
                TextView listaNombre = (TextView)view.findViewById(R.id.listaNombre);

                textView.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));
                listaNombre.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2))));
            }
        };
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //holder.titlee.setText(data[position]);
        cursorAdapter.getCursor().moveToPosition(position);
        //Asociamos el adaptador del cursor
        cursorAdapter.bindView(holder.itemView,context,cursorAdapter.getCursor());
    }

    @Override
    public int getItemCount() {
        return cursorAdapter.getCount();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView titlee,listaNombre;

        public ViewHolder(View itemView) {
            super(itemView);
            titlee = (TextView)itemView.findViewById(R.id.title);
            listaNombre = (TextView)itemView.findViewById(R.id.listaNombre);
        }

    }
}



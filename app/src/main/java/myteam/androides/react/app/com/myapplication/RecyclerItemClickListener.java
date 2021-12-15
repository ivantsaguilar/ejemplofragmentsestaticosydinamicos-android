package myteam.androides.react.app.com.myapplication;

import android.content.Context;
import android.gesture.Gesture;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by ivants on 2/01/17.
 */

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener{

    //Definimos nuestro propio metodo en una interface
    protected OnItemClickListener listener;
    private GestureDetector gestureDetector;

    private View childView;
    private int childViewPosition;

    public RecyclerItemClickListener(Context context,OnItemClickListener listener){
        this.gestureDetector = new GestureDetector(context, new GestureListener());
        this.listener = listener;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        childView = rv.findChildViewUnder(e.getX(),e.getY());
        //childViewPosition = rv.getChildPosition(childView);
        childViewPosition = rv.getChildAdapterPosition(childView);

        return childView != null && gestureDetector.onTouchEvent(e);
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public interface OnItemClickListener{
        //Click corto
        public void onItemClick(View childView, int position);
        //Click largo
        public void onItemLongPress(View chilView,int position);
    }

    public static abstract class SimpleOnItemClickListener implements OnItemClickListener{
        public void OnItemClick(View childView, int position){

        }

        public void OnItemLongPress(View chilView, int position){

        }

    }

    public class GestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            if(childView != null){
                //mando a llamar al metodo que yo acabo de crear en la interface
                listener.onItemClick(childView,childViewPosition);
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent event) {
            if(childView != null){
                //llamo al metodo que acabo de crear en la interface
                listener.onItemLongPress(childView,childViewPosition);
            }
            super.onLongPress(event);
        }

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }
    }
}

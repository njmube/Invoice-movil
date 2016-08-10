package ec.bigdata.facturaelectronicamovil.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import ec.bigdata.facturaelectronicamovil.interfaz.RecyclerViewClickInterface;

/**
 * Created by DavidLeonardo on 26/7/2016.
 */
public class RecyclerViewTouchListener implements RecyclerView.OnItemTouchListener {

    private GestureDetector gestureDetector;

    private RecyclerViewClickInterface recyclerViewClickInterface;

    public RecyclerViewTouchListener(Context context, final RecyclerView recyclerView, final RecyclerViewClickInterface recyclerViewClickInterface) {
        this.recyclerViewClickInterface = recyclerViewClickInterface;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {

                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && recyclerViewClickInterface != null) {
                    recyclerViewClickInterface.onLongClick(child, recyclerView.getChildPosition(child));

                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (child != null && recyclerViewClickInterface != null && gestureDetector.onTouchEvent(e)) {
            recyclerViewClickInterface.onClick(child, rv.getChildPosition(child));


        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}

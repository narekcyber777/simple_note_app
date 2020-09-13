package com.example.noteapp.list_view_create;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.noteapp.R;
import com.example.noteapp.listeners.BackAwardsListener;
import com.example.noteapp.listeners.OnSwipeTouchListener;
import com.example.noteapp.model.DataModel;
import com.example.noteapp.ui.EditTextActivity;
import com.example.noteapp.ui.MainActivity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class NoteListAdapter extends ArrayAdapter<DataModel> implements View.OnClickListener, View.OnLongClickListener {
    final private Map<Integer, TextView> allTextviews;
    private ArrayList<DataModel> dates;
    private Context context;
    private BackAwardsListener backAwardsListener;
    private int selectedPosition = -1;
    private TextView permanentTextview;
    private int next = -1;

    public NoteListAdapter(@NonNull ArrayList<DataModel> dates, Context context) {
        super(context, R.layout.list_node, dates);


        this.dates = dates;
        this.context = context;
        backAwardsListener = (BackAwardsListener) context;

        allTextviews = new LinkedHashMap<>();

    }

    @Override
    public void onClick(View v) {

        final TextView textview = allTextviews.get(v.getTag());

        switch (v.getId()) {

            case R.id.text:
                Log.d("NAR", "Nur");

                if (next != -1) {
                    allTextviews.get(next).setVisibility(View.INVISIBLE);
                }

                next = (Integer) v.getTag();

                if (textview != null) {
                    textview.setVisibility(View.VISIBLE);

                }

                break;

        }

    }


    private final void createDialog(final DataModel dataModel) {

        new AlertDialog.Builder(context)
                .setTitle("Edit Note")
                .setMessage("Do you want to do changes here?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, EditTextActivity.class);
                        intent.putExtra("data", dataModel);
                        ((MainActivity) context).startActivityForResult(intent, 111);
                    }
                }).setNegativeButton("No", null).show();

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        final DataModel dataModel = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder;
        final View result;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.list_node, parent, false);
            viewHolder.textView = convertView.findViewById(R.id.text_close);
            viewHolder.editText = convertView.findViewById(R.id.text);
            result = convertView;
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;

        }
        viewHolder.textView.setText(dataModel.getShowText());

        viewHolder.textView.setOnLongClickListener(this);
        viewHolder.editText.setText(dataModel.getShowText());
        viewHolder.editText.setOnClickListener(this);
        viewHolder.editText.setOnLongClickListener(this);


        viewHolder.textView.setTag(position);
        viewHolder.editText.setTag(position);

        allTextviews.put(position, viewHolder.textView);

        final View finalConvertView = convertView;
        viewHolder.textView.setOnTouchListener(new OnSwipeTouchListener(context, position) {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean foo = super.onTouch(v, event);


                if (event.getAction() != MotionEvent.ACTION_MOVE && onDoubleTouch()) {
                    int position = (Integer) v.getTag();
                    DataModel object = getItem(position);

                    object.setId(position);
                    if (object != null) {

                        createDialog(object);
                        Log.d("NAR", "Beytur");
                    }
                }


                return foo;
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();

                Log.d("NAR", "" + position);

                animImage(context, finalConvertView, position);

            }

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
            }


        });

        return convertView;


    }

    private void animImage(final Context context, final View view, final int selectedPosition) {
        // Load the animation like this
        final Animation animRightToLeft = AnimationUtils.loadAnimation(context, R.anim.to_right);

        view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        // Start the animation like this
        view.startAnimation(animRightToLeft);
        animRightToLeft.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {


            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if (animation.hasEnded()) {
                    Log.d("NAR", "ENDED");
                    backAwardsListener.takeBack("DELETE", selectedPosition);
                } else {
                    view.clearAnimation();
                    animation.cancel();
                }


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private static class ViewHolder {
        private TextView textView;
        private TextView editText;
    }


}

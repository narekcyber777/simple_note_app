package com.example.noteapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.noteapp.R;
import com.example.noteapp.io_help.ObjectSerializer;
import com.example.noteapp.list_view_create.NoteListAdapter;
import com.example.noteapp.listeners.BackAwardsListener;
import com.example.noteapp.model.DataModel;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, BackAwardsListener {
    private ListView noteList;
    private NoteListAdapter noteListAdapter;
    private ArrayList<DataModel> dataModels;
    private TextView text_close;
    private EditText text;
    private SharedPreferences myPrefer;

    private BackAwardsListener backAwardsListener;


    @SuppressWarnings("raw,unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        noteList = findViewById(R.id.note_list);

        text_close = findViewById(R.id.text_close);
        myPrefer = this.getSharedPreferences("com.example.noteapp", Context.MODE_PRIVATE)
        ;
        if (myPrefer.getString("datas", "").isEmpty()) {
            dataModels = new ArrayList<>();

        } else {
            try {
                dataModels = (ArrayList<DataModel>) ObjectSerializer.deserialize(myPrefer.getString("datas", ""));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        noteListAdapter = new NoteListAdapter(dataModels, this);
        noteList.setAdapter(noteListAdapter);
        noteList.setOnItemClickListener(getItemClicker());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.add_note:
                Intent intent = new Intent(MainActivity.this, EditTextActivity.class);
                intent.putExtra("data", new DataModel(-1, ""));
                startActivityForResult(intent, 111);
                return true;

            default:
                return false;
        }
    }


    private final AdapterView.OnItemClickListener getItemClicker() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        };
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 111:
                if (resultCode == RESULT_OK) {
                    Log.d("Nar", "DJANGO");
                    if (data != null && data.getSerializableExtra("data") != null) {
                        DataModel dataModel = (DataModel) data.getSerializableExtra("data");

                        if (dataModel.getId() == -1) dataModels.add(dataModel);
                        else dataModels.get(dataModel.getId()).setShowText(dataModel.getShowText());


                        noteListAdapter.notifyDataSetChanged();
                    }
                }


                break;


        }
    }

    @Override
    public void takeBack(String value, int currentPositon) {
        Log.d("NAR", value + ':' + currentPositon + "");

        switch (value) {
            case "DELETE":
                Log.d("NAR", "delted");

                dataModels.remove(currentPositon);

                noteListAdapter.notifyDataSetChanged();
                if (currentPositon == 0)
                    myPrefer.edit().clear();
                break;


        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            myPrefer.edit().putString("datas", ObjectSerializer.serialize(dataModels)).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

    }
}

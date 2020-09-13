package com.example.noteapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.noteapp.R;
import com.example.noteapp.model.DataModel;

public class EditTextActivity extends AppCompatActivity {
    private DataModel dataModel;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);
        editText = findViewById(R.id.editText);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (getIntent().getSerializableExtra("data") != null) {
            Log.d("NAR", "TOR00");
            dataModel = (DataModel) getIntent().getSerializableExtra("data");
            editText.setText(dataModel.getShowText());

        }


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {

        if (dataModel != null) {
            saveText();
            Log.d("NAR", "TOR");
            Intent intent = new Intent(EditTextActivity.this, MainActivity.class);
            intent.putExtra("data", dataModel);
            setResult(RESULT_OK, intent);
            finish();

            super.onBackPressed();
        }
    }


    private final void saveText() {
        try {
            dataModel.setShowText(editText.getText().toString());
        } catch (NullPointerException e) {
            dataModel.setShowText("");
        }
    }

}

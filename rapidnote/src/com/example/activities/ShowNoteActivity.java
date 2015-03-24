package com.example.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.rapidnote.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ShowNoteActivity extends Activity {
    EditText editText_title;
    JSONObject jsonRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_note);
        findViewById();
        getDate();
    }

    private void findViewById() {
        editText_title = (EditText) findViewById(R.id.edtv_show_note);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getDate() {
        Intent intent = getIntent();
        try {
            jsonRoot = new JSONObject(intent.getStringExtra("json"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setTitle();
    }

    private void setTitle() {
        try {
            String s=jsonRoot.getString("title");
            System.out.print(s);
            editText_title.setText(jsonRoot.getString("title"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

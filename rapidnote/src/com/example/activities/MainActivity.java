package com.example.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.example.adapt.GridviewAdapter;
import com.example.rapidnote.R;


public class MainActivity extends Activity implements View.OnClickListener {
    private Button btn_add_note;
    private GridView gridView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        setOnclickListener();
        init();
    }


    private void init() {
        gridView.setAdapter(new GridviewAdapter(this));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,ShowNoteActivity.class);
                intent.putExtra("json",parent.getAdapter().getItem(position).toString());
                startActivity(intent);
            }
        });

    }

    private void setOnclickListener() {
        btn_add_note.setOnClickListener(this);
    }

    private void findView() {
        btn_add_note = (Button) findViewById(R.id.btn_add_note);
        gridView = (GridView) findViewById(R.id.gridView);
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_add_note:
                intent.setClass(this, EditNoteActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

}

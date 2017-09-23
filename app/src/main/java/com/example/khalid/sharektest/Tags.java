package com.example.khalid.sharektest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

/**
 * Created by Abdelrahman on 6/12/2017.
 */

public class Tags extends AppCompatActivity  {
    EditText search;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView=(ListView)findViewById(R.id.Search_Common);
        search = (EditText) findViewById(R.id.Tags_editText_search);
        search.setSingleLine();

        search.setOnKeyListener(onSoftKeyboardDonePress);

        String[] CommonTags = new String[] { "books",
                "games",
                "home_tools",
                "accessories",
                "food",
                "service",
                "tools",
                "electronics",
                "coworking_space"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Tags.this,
                R.layout.commontag_row, R.id.commonTag_title_textView, CommonTags);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){






            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {

                int itemPosition     = position;
                // ListView Clicked item value
                String  itemValue    = (String) listView.getItemAtPosition(position);
                Intent intent = new Intent(Tags.this, CommonTags.class);
                intent.putExtra("value", itemValue);
                startActivity(intent);
            }

        });


    }
    private View.OnKeyListener onSoftKeyboardDonePress=new View.OnKeyListener()
    {
        public boolean onKey(View v, int keyCode, KeyEvent event)
        {
            if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
            {
                // code to hide the soft keyboard
                Intent intent = new Intent(Tags.this, CommonTags.class);
                intent.putExtra("value", search.getText().toString().toLowerCase());
                startActivity(intent);
            }
            return false;
        }
    };
/*kp
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, CommonTags.class);
        intent.putExtra("value", search.getText().toString());
        startActivity(intent);
    }*/


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Tags.this, HomePage.class);
        startActivity(intent);

    }


}


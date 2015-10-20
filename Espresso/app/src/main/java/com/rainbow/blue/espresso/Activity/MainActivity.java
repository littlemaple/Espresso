package com.rainbow.blue.espresso.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.rainbow.blue.espresso.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://www.freehao123.com/"));
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_webview) {
            startActivity(new Intent(MainActivity.this, WebviewActivity.class));
            return true;
        }
        if (id == R.id.action_list) {
            startActivity(new Intent(MainActivity.this, CollapsedActivity.class));
            return true;
        }
        if (id == R.id.action_chart) {
            startActivity(new Intent(MainActivity.this, GraphicViewActivity.class));
            return true;
        }
        if (id == R.id.action_pic) {
            startActivity(new Intent(MainActivity.this, LocalImageActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

package sg.edu.rp.c346.id19045784.c302_magic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<Color> adapter;
    private ArrayList<Color> list;
    private AsyncHttpClient client;
    private Boolean Check = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listViewColor);
        list = new ArrayList<Color>();
        adapter = new ArrayAdapter<Color>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        client = new AsyncHttpClient();

        //TODO: read loginId and apiKey from SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String longinId = prefs.getString("loginID", "");
        String apikey = prefs.getString("apiKey", "");



        // TODO: if loginId and apikey is empty, go back to LoginActivity
        if (longinId.equalsIgnoreCase("")){
            Check = true;
        }
        else if (apikey.equalsIgnoreCase("")){
            Check = true;
        }

        if (Check == true){
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        }

        //TODO: Point X - call getMenuCategories.php to populate the list view
        RequestParams params = new RequestParams();
        params.add("loginId", longinId);
        params.add("apikey", apikey);

        client.post("http://10.0.2.2/C302_Assignment/19045784_getColours.php",params,  new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    Log.i("JSON Results: ", response.toString());

                    for (int i = 0; i < response.length(); i ++) {
                        JSONObject jsonobj = response.getJSONObject(i);

                        String id = jsonobj.getString("colourId");
                        String colorName = jsonobj.getString("colourName");

                        list.add(new Color(id, colorName));

                    }
                    adapter.notifyDataSetChanged();

                }
                catch (JSONException e){
                    e.printStackTrace();
                }

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Color selected = list.get(position);
                //TODO: make Intent to DisplayMenuItemsActivity passing the categoryId

                Intent i  = new Intent(MainActivity.this, CardActivity_19045784.class);
                i.putExtra("position", selected.getId());
               startActivity(i);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String role = prefs.getString("role", "");

        if (role.equals("admin")) {
            getMenuInflater().inflate(R.menu.submain, menu);
        }
        else {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_logout) {
            // TODO: Clear SharedPreferences
            SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
            preference.edit().clear();
            preference.edit().commit();

            // TODO: Redirect back to login screen
            Intent intentlogout = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intentlogout);
            return true;
        }
        else if (id == R.id.menu_addCard){
            Intent i = new Intent(this, CreateCardActivity_19045784.class);
            startActivity(i);
            return true;
        }
        else if (id == R.id.menu_cardsByColor){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}

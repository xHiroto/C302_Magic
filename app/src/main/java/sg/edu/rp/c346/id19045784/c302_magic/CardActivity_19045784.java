package sg.edu.rp.c346.id19045784.c302_magic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

public class CardActivity_19045784 extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<ColorItem> adapter;
    private ArrayList<ColorItem> list;
    private AsyncHttpClient client;
    String colorId;
    String longinId;
    String apikey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_19045784);

        listView = (ListView) findViewById(R.id.listViewColorItem);
        list = new ArrayList<ColorItem>();
        adapter = new ArrayAdapter<ColorItem>(CardActivity_19045784.this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        client = new AsyncHttpClient();


        Intent intent = getIntent();
        colorId = intent.getStringExtra("position");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(CardActivity_19045784.this);
        longinId = prefs.getString("loginID", "");
        apikey = prefs.getString("apiKey", "");



    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.clear();


        RequestParams params = new RequestParams();
        params.add("loginId", longinId);
        params.add("apikey", apikey);
        params.add("colorId", colorId);

        client.post("http://10.0.2.2/C302_Assignment/19045784_getCardsByColour.php",params,  new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    Log.i("JSON Results: ", response.toString());

                    for (int i = 0; i < response.length(); i ++) {
                        JSONObject jsonobj = response.getJSONObject(i);

                        String id = jsonobj.getString("cardId");
                        String cardName = jsonobj.getString("cardName");
                        String colourId = jsonobj.getString("colourId");
                        String typeId = jsonobj.getString("typeId");
                        String price = jsonobj.getString("price");
                        String quantity = jsonobj.getString("quantity");

                        list.add(new ColorItem(id,cardName, colourId, typeId, price, quantity));


                    }
                    adapter.notifyDataSetChanged();

                }
                catch (JSONException e){
                    e.printStackTrace();
                }

            }
        });

        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(CardActivity_19045784.this);
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
        if (id == R.id.menu_addCard) {
            Intent i = new Intent(this, CreateCardActivity_19045784.class);
            startActivity(i);
            return true;
        }
        else if(id == R.id.menu_cardsByColor){
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            return true;
        }
        else if (id == R.id.menu_logout){
            SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
            preference.edit().clear();
            preference.edit().commit();

            // TODO: Redirect back to login screen
            Intent intentlogout = new Intent(this, LoginActivity.class);
            startActivity(intentlogout);
            return true;
        }//
        return super.onOptionsItemSelected(item);
    }
}
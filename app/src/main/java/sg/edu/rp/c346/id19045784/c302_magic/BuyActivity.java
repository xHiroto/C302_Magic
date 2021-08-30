package sg.edu.rp.c346.id19045784.c302_magic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

//Student Name: Javier Lim
//Student ID: 19045784
//Class Day: 5

public class BuyActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<CardItem> adapter;
    private ArrayList<CardItem> list;
    String longinId;
    String apikey;
    private AsyncHttpClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        listView = (ListView) findViewById(R.id.listViewCards);
        list = new ArrayList<CardItem>();
        adapter = new ArrayAdapter<CardItem>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        client = new AsyncHttpClient();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(BuyActivity.this);
        longinId = prefs.getString("loginID", "");
        apikey = prefs.getString("apiKey", "");
        adapter.clear();

        RequestParams params = new RequestParams();
        params.add("loginId", longinId);
        params.add("apikey", apikey);


        client.post("http://10.0.2.2/C302_Assignment/getAllCards.php",params,  new JsonHttpResponseHandler() {

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
                        Double dPrice = Double.parseDouble(price);
                        Integer iQuantity = Integer.parseInt(quantity);

                        if (iQuantity != 0) {
                            list.add(new CardItem(id, colourId, typeId, cardName, dPrice, iQuantity));
                        }


                    }
                    adapter.notifyDataSetChanged();

                }
                catch (JSONException e){
                    e.printStackTrace();
                }

            }
        });

        adapter.notifyDataSetChanged();
        
        
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CardItem selected = list.get(position);
                Double price = selected.getPrice();
                Double quantity = selected.getQuantity();
                Double total = price * quantity;
                String cardId = selected.getCardId();

                RequestParams params = new RequestParams();
                params.add("loginId", longinId);
                params.add("apikey", apikey);
                params.add("cardId", cardId);
                params.add("total", String.format("%.2f", total));

                client.post("http://10.0.2.2/C302_Assignment/buyCard.php", params, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);


                        try {
                            Log.i("JSON create Results: ", response.toString());

                            String result =  response.getString("message");

                            if (result.equals("true")){
                               Toast.makeText(BuyActivity.this, "You bought " + quantity+ " copies of " + selected.getCardName() + " for $" + String.format("%.2f", total), Toast.LENGTH_SHORT).show();
                            }

                            else {
                                Toast.makeText(BuyActivity.this, "Failed to buy", Toast.LENGTH_SHORT).show();
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });


                Intent intent = new Intent(BuyActivity.this, BuyActivity.class);
                startActivity(intent);


            }

        });
    }
}

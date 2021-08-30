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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class CreateCardActivity_19045784 extends AppCompatActivity {

    EditText etName, etColourId, etTypeId, etPrice, etQuantity;
    Button btnAdd;
    private AsyncHttpClient client;
    String categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_card_19045784);

        etName = findViewById(R.id.etName);
        etColourId = findViewById(R.id.etColorID);
        etTypeId = findViewById(R.id.etTypeID);
        etPrice = findViewById(R.id.etPrice);
        etQuantity = findViewById(R.id.etQuantity);
        btnAdd = findViewById(R.id.btnAdd);

        client = new AsyncHttpClient();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(CreateCardActivity_19045784.this);
        String longinId = prefs.getString("loginID", "");
        String apikey = prefs.getString("apiKey", "");



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (errorCheck()) {
                    RequestParams params = new RequestParams();
                    params.add("loginId", longinId);
                    params.add("apikey", apikey);
                    params.add("cardName", etName.getText().toString());
                    params.add("colorId", etColourId.getText().toString());
                    params.add("typeId", etTypeId.getText().toString());
                    params.add("price", etPrice.getText().toString());
                    params.add("quantity", etQuantity.getText().toString());

                    client.post("http://10.0.2.2/C302_Assignment/19045784_createCard.php", params, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);


                            try {
                                Log.i("JSON create Results: ", response.toString());

                                Toast.makeText(CreateCardActivity_19045784.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CreateCardActivity_19045784.this, MainActivity.class);
                                startActivity(intent);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.submain, menu);
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


        private Boolean errorCheck() {
            if (etName.getText().toString().isEmpty()) {
                Toast.makeText(this, "Name cannot be empty!", Toast.LENGTH_SHORT).show();
                return false;
            } else if (etColourId.getText().toString().isEmpty()) {
                Toast.makeText(this, "Color ID cannot be empty!", Toast.LENGTH_SHORT).show();
                return false;

            } else if (etTypeId.getText().toString().isEmpty()) {
                Toast.makeText(this, "Type ID cannot be empty!", Toast.LENGTH_SHORT).show();
                return false;

            } else if (etPrice.getText().toString().isEmpty()) {
                Toast.makeText(this, "Price cannot be empty!", Toast.LENGTH_SHORT).show();
                return false;

            } else if (etQuantity.getText().toString().isEmpty()) {
                Toast.makeText(this, "Quantity cannot be empty!", Toast.LENGTH_SHORT).show();
                return false;

            } else {
                if (Integer.parseInt(etColourId.getText().toString()) < 1 || Integer.parseInt(etColourId.getText().toString()) > 5) {
                    Toast.makeText(this, "Color ID can only be from 1 to 5", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (Integer.parseInt(etTypeId.getText().toString()) < 1 || Integer.parseInt(etTypeId.getText().toString()) > 4) {
                    Toast.makeText(this, "Type ID can only be from 1 to 4", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (Double.parseDouble(etPrice.getText().toString()) < 0) {
                    Toast.makeText(this, "Price cannot be 0 or less", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (Integer.parseInt(etQuantity.getText().toString()) < 0) {
                    Toast.makeText(this, "Quantity cannot be 0 or less", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            return true;
        }
    }

package com.example.mehar.whatstheweather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText city;
    TextView weather;



    public class DownloadTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            String result="";
            URL url;
            HttpURLConnection urlConnection = null;
            try{
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection)url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data =reader.read();
                while(data!=-1){
                    char current=(char)data;
                    result+= current;
                    data = reader.read();
                }
                return result;

            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Could not find weather :(",Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                String state = "";
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                Log.i("Weather Content",weatherInfo);
                JSONArray arr = new JSONArray(weatherInfo);
                String main="",description="";
                for(int i=0;i<arr.length();i++){
                    JSONObject jsonpart = arr.getJSONObject(i);
                    main = jsonpart.getString("main");
                    description = jsonpart.getString("description");
                }
                if(!main.equals("") && !description.equals("") ){
                    state = main + " : " + description + "\r\n";
                    weather = (TextView) findViewById(R.id.info);
                    weather.setText(state);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Could not find weather :(",Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Could not find weather :(",Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void showWeather(View view){
        city = (EditText)findViewById(R.id.cityName);
        DownloadTask task = new DownloadTask();
        String intial = "http://api.openweathermap.org/data/2.5/weather?q=";String apiId = "&appid=3c5d6123f9b2ee4899bd163c976fb1c0";
        String cityName = city.getText().toString();
        task.execute(intial+cityName+apiId);
        InputMethodManager mgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(city.getWindowToken(),0);
    }
}

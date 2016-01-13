package com.testing.parsingjson;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private TextView tvData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvData = (TextView)findViewById(R.id.text_holder);


                new JSONTask().execute("http://de-coding-test.s3.amazonaws.com/books.json");

    }

    public class JSONTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String...params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line= "";
                while ((line=reader.readLine()) !=null) {
                    buffer.append(line);
                }
                String totalJSON = buffer.toString();

                try {
                    JSONArray parentArray = new JSONArray(totalJSON);

                        JSONObject jsonObject = parentArray.getJSONObject(0);


                        String title = jsonObject.getString("title");
                        String imageURL = jsonObject.getString("imageURL");




                    return buffer.toString();

                } catch (JSONException e) {
                    e.printStackTrace();

                }


            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch(IOException e) {
                    e.printStackTrace();

                }
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            tvData.setText(result);
        }
    }

}
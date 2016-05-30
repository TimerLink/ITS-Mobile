package cn.edu.hit.itsmobile.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import com.alibaba.fastjson.JSON;

import cn.edu.hit.itsmobile.model.JSONData;
import android.os.AsyncTask;

public class QueryManager extends AsyncTask<Void, Long, JSONData>{
    private final static String HTTP_HOST = "http://104.236.182.43:8124/";
    
    private String id;
    private HttpURLConnection con = null;
    private OnQueryCompleteListener listener;
    
    public QueryManager(String id) {
        this.id = id;
    }
    
    public QueryManager setOnQueryCompleteListner(OnQueryCompleteListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    protected JSONData doInBackground(Void... params) {
        try {
            URL url = new URL(HTTP_HOST + "?id=" + URLEncoder.encode(this.id, "UTF-8"));
            con = (HttpURLConnection)url.openConnection();
            con.setDoOutput(true);
            con.setConnectTimeout(10000);
            con.setRequestMethod("GET");
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = "", result = "";
            for (line = br.readLine(); line != null; line = br.readLine()) {
                result += line;
            }
            if(result == null || result.equals(""))
                return null;
            JSONData data = JSON.parseObject(result, JSONData.class);
            return data;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONData result) {
        super.onPostExecute(result);
        if(listener != null)
            listener.onFinish(result);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if(con != null)
            con.disconnect();
    }
    
    public interface OnQueryCompleteListener {
        public void onFinish(JSONData result);
    }

}

package cn.edu.hit.itsmobile.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.alibaba.fastjson.JSON;

import cn.edu.hit.itsmobile.model.JSONData;
import cn.edu.hit.itsmobile.model.Person;
import cn.edu.hit.itsmobile.model.SensorData;

import android.os.AsyncTask;
import android.util.Log;

/**
 * HTTP通信
 */
public class QueryManager extends AsyncTask<Void, Long, JSONData> {
    private final static String HTTP_HOST = "http://192.168.0.119:8080/HttpClient";//104.236.182.43
//    private final static String HTTP_HOST = "http://123.206.85.17:8124/";//104.236.182.43

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

    /**
     * 获取 JSON数据:line,result,在后台执行
     * @param params
     * @return
     */
    @Override
    protected JSONData doInBackground(Void... params) {
        try {
            URL url = new URL(HTTP_HOST + "?l=120&b=21");
            con = (HttpURLConnection)url.openConnection();
            con.setConnectTimeout(10000);//最大连接延迟
            con.setRequestMethod("GET");//从服务器端获取数据
//            Log.e("method", String.valueOf(con.getRequestMethod()));
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = "", result = "";
            for (line = br.readLine(); line != null; line = br.readLine()) {
                result += line;//读取结果最终全部放在result中
            }
            if(result == null || result.equals(""))
                return null;
//            List<Person> datas = JSON.parseArray(result, Person.class);
            JSONData data = JSON.parseObject(result,JSONData.class);
            Log.e("data", data.toString());
            return data;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 提交结果
     * @param result
     */
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

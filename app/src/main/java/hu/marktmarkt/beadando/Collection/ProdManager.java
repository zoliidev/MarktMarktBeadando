package hu.marktmarkt.beadando.Collection;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Map;

import hu.marktmarkt.beadando.Model.Product;

public class ProdManager {

    private final Context context;

    public ProdManager(Context context){
        this.context = context;
    }

    public interface VolleyCallBack {
        void onSuccess();
    }

    public void populateProds(String url, ArrayList<Product> resultList, Map<String, String> data, final VolleyCallBack callBack){
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest getProd = new StringRequest(Request.Method.POST, url, response -> {
            JSONArray result;
            try {
                result = new JSONArray(response);

                for (int i = 0; i < result.length(); i++) {
                    String obj = result.getString(i);
                    String[] darab = obj.split("@");
                    int discount = Integer.parseInt(darab[5]);
                    resultList.add(new Product(Integer.parseInt(darab[0]), darab[1], Integer.parseInt(darab[2]), darab[3], darab[4], discount));
                    //Log.i("ArrayList", products.get(i).toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            callBack.onSuccess();

        }, error -> Toast.makeText(context, error.getMessage() + "", Toast.LENGTH_LONG).show()) {
            protected Map<String, String> getParams() {
                return data;
            }
        };

        requestQueue.add(getProd);
    }
}

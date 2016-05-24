package net.cnheider.flappynerd;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import static com.android.volley.VolleyLog.TAG;

public class NerdDriver {
  private static NerdDriver mInstance;
  private static String mNerdID;
  private static String mGameID;
  private static RequestQueue mRequestQueue;
  private static ImageLoader mImageLoader;
  private static String mUrl = "http://development.flappynerd.net";
  private static Context mCtx;

  private NerdDriver(Context ctx){
    mCtx = ctx;
    mRequestQueue = getRequestQueue();
    TelephonyManager telephonyManager = (TelephonyManager)ctx.getSystemService(Context.TELEPHONY_SERVICE);
    mNerdID = telephonyManager.getDeviceId();
  }

  private RequestQueue getRequestQueue() {
    if (mRequestQueue == null) {
      // getApplicationContext() is key, it keeps you from leaking the
      // Activity or BroadcastReceiver if someone passes one in.
      mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
    }
    return mRequestQueue;
  }

  public static synchronized NerdDriver getInstance(Context context) {
    if (mInstance == null) {
      mInstance = new NerdDriver(context);
    }
    return mInstance;
  }

  public boolean signin(String name){

    JSONObject nerd = new JSONObject();
    try {
      nerd.put("name", name);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    JsonObjectRequest jsonRequest = new JsonObjectRequest(
        Request.Method.POST,
        mUrl+"/nerd/signin/"+mNerdID,
        nerd,
        new Response.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject response) {
            Log.d(TAG, "onResponse: "+response);
          }
        },
        new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
            Log.d(TAG, "onErrorResponse: ", error);
          }
        }
    );
    JsonObjectRequest jsonRequest2 = new JsonObjectRequest(
        Request.Method.PUT,
        mUrl+"/nerd/name/"+mNerdID,
        nerd,
        new Response.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject response) {
            Log.d(TAG, "onResponse: "+response);
          }
        },
        new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
            Log.d(TAG, "onErrorResponse: ", error);
          }
        }
    );

    mRequestQueue.add(jsonRequest);
    mRequestQueue.add(jsonRequest2);

    return true;
  }

  public boolean join(String game_id){
    mGameID = game_id;

    JSONObject json = new JSONObject();
    try {
      json.put("game_id", Integer.valueOf(game_id));
    } catch (JSONException e) {
      e.printStackTrace();
    }

    JsonObjectRequest jsonRequest = new JsonObjectRequest(
        Request.Method.PUT,
        mUrl+"/nerd/play/"+mNerdID,
        json,
        new Response.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject response) {
            Log.d(TAG, "onResponse: "+response);
          }
        },
        new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
            Log.d(TAG, "onErrorResponse: ", error);
          }
        }
    );

    mRequestQueue.add(jsonRequest);

    return true;
  }

  public boolean avatar(int avatar_id){
    JSONObject json = new JSONObject();
    try {
      json.put("avatar_url", "https://github.com/cnHeider/FlappyNerd/raw/master/app/src/main/res/mipmap-xxxhdpi/logo"+avatar_id+".png");
    } catch (JSONException e) {
      e.printStackTrace();
    }

    JsonObjectRequest jsonRequest = new JsonObjectRequest(
        Request.Method.PUT,
        mUrl+"/nerd/avatar/"+mNerdID,
        json,
        new Response.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject response) {
            Log.d(TAG, "onResponse: "+response);
          }
        },
        new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
            Log.d(TAG, "onErrorResponse: ", error);
          }
        }
    );

    mRequestQueue.add(jsonRequest);

    return true;
  }

  public boolean flap(long air_time){
    if (mGameID == "")
      return false;

    JSONObject flap = new JSONObject();
    try {
      flap.put("nerd_id", mNerdID);
      flap.put("air_time", air_time);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    JsonObjectRequest jsonRequest = new JsonObjectRequest(
        Request.Method.POST,
        mUrl+"/game/flap/"+mGameID,
        flap,
        new Response.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject response) {
            Log.d(TAG, "onResponse: "+response);
          }
        },
        new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
            Log.d(TAG, "onErrorResponse: ", error);
          }
        }
    );

    mRequestQueue.add(jsonRequest);

    return true;
  }

  private  <T> void addToRequestQueue(Request<T> req) {
    getRequestQueue().add(req);
  }

  private ImageLoader getImageLoader() {
    return mImageLoader;
  }
}

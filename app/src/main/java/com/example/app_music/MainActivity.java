package com.example.app_music;

import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;



import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    // private static final String url = "http://192.168.56.1/albumfinal.json";
SearchView searchView;
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private AlbumAdapter albumAdapter;
    RequestQueue queue;
    GridLayoutManager lLayout;
    //private String url="https://app-1486707345.000webhostapp.com/mrbean.json";
    private ArrayList<Album> list=new ArrayList<>();
//    private ArrayList<Currency> curr=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        list=new ArrayList<>();
        albumAdapter=new AlbumAdapter(this,list);
        lLayout = new GridLayoutManager(MainActivity.this, 2);
        recyclerView.setLayoutManager(lLayout);
        recyclerView.setAdapter(albumAdapter);

        queue = AppController.getInstance().getRequestQueue();

        preparealbums();

    }

    public  void preparealbums()
    {
        String url_deployment = "http://music-app.epizy.com/API/list_song.php";
        String url_development="http://192.168.8.102/projectMusic/API/list_song.php";


        JsonArrayRequest songReq = new JsonArrayRequest(url_development,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        Log.d(TAG, response.toString());


                        for (int i = 0; i < response.length(); i++) {
                            try
                            {
                                JSONObject obj = response.getJSONObject(i);
                                Album song = new Album(obj.getString("song"),obj.getString("url"),
                                        obj.getString("artists"),obj.getString("cover_image"));





                                list.add(song);
                                Log.d("song bro :" ,song.song);

                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        albumAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());


            }
        });

        // Adding request to request queue
        queue.add(songReq);

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        MenuItem menuItem=menu.findItem(R.id.action_search);
        SearchView searchView= (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setQueryHint("Search Song name");
        searchView.setOnQueryTextListener(this);
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        searchView = (SearchView) menu.findItem(R.id.action_search)
//                .getActionView();
//        searchView.setSearchableInfo(searchManager
//                .getSearchableInfo(getComponentName()));
//        searchView.setMaxWidth(Integer.MAX_VALUE);
//
//        // listening to search query text change
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                // filter recycler view when query submitted
//                albumAdapter.getFilter().filter(query);
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String query) {
//                // filter recycler view when text is changed
//                albumAdapter.getFilter().filter(query);
//                return false;
//            }
//        });
        return true;
    }




    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText=newText.toLowerCase();
        ArrayList<Album> arrayListp=new ArrayList<>();
        for(Album album:list){
            String name=album.getSong().toLowerCase();
            if(name.contains(newText))
                arrayListp.add(album);
        }
        albumAdapter.setfilter(arrayListp);
        return true;    }
}

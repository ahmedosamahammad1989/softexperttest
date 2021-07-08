package com.demo.softexperttest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Movie;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public Integer pageNumber = 1;
    List<Cars> carsList = new ArrayList<Cars>();
    public static ImageLoader imageLoader;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ApiInterface apiService;
    RecyclerviewItemAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();

        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        mSwipeRefreshLayout.setOnRefreshListener(this);
//        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .resetViewBeforeLoading(true)
//                .displayer(new OvaledBitmapDisplayer()).build();
                .displayer(new FadeInBitmapDisplayer(200, true, true, false))
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .resetViewBeforeLoading(true).considerExifParams(false)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .showImageOnLoading(android.R.color.transparent)
                .showImageForEmptyUri(R.drawable.ic_launcher_background)
                .showImageOnFail(R.drawable.ic_launcher_background)
                .build();
        File cacheDir = StorageUtils.getCacheDirectory(context);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                .diskCacheExtraOptions(480, 800, null)
                .threadPoolSize(3) // default
                .threadPriority(Thread.NORM_PRIORITY - 1) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default
//                .diskCacheSize(50 * 1024 * 1024)
//                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(context)) // default
                .imageDecoder(new BaseImageDecoder(true)) // default
                .defaultDisplayImageOptions(options) // default
                .writeDebugLogs()
                .build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new RecyclerviewItemAdapter(carsList);
        recyclerView.setAdapter(recyclerAdapter);

        apiService = ApiClient.getClient().create(ApiInterface.class);

        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                if(mSwipeRefreshLayout != null) {
                    mSwipeRefreshLayout.setRefreshing(true);
                }
                // TODO Fetching data from server
                pageNumber++;
                refreshContents();
            }
        });
    }

    public void refreshContents(){
        Call<HashMap<String, Object>> call = apiService.getCars("cars?page=" +pageNumber);

        call.enqueue(new Callback<HashMap<String, Object>>() {
            @Override
            public void onResponse(Call<HashMap<String, Object>> call, Response<HashMap<String, Object>> response) {
                List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) response.body().get("data");
                if (list != null) {
                    for (int i=0;i<list.size();i++){
                        Cars car = new Cars();
                        Map<String, Object> map = list.get(i);
                        car.setId(map.get("id").toString());
                        car.setBrand(map.get("brand").toString());
                        car.setConstructionYear(map.get("constractionYear") != null ? map.get("constractionYear").toString() : "");
                        car.setIsUsed(map.get("isUsed") != null ? (map.get("isUsed").equals("true") ? "Used" : "New") : "");
                        car.setImageUrl(map.get("imageUrl") != null ? map.get("imageUrl").toString() : "");
                        carsList.add(car);
                    }
                    Log.d("TAG","Response = "+carsList);
                    recyclerAdapter.setCarsList(carsList);
                    recyclerAdapter.notifyDataSetChanged();
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<HashMap<String, Object>> call, Throwable t) {
                Log.d("TAG","Response = "+t.toString());
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        pageNumber++;
        refreshContents();
    }
}
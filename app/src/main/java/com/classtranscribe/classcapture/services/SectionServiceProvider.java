package com.classtranscribe.classcapture.services;

import android.content.Context;

import com.classtranscribe.classcapture.R;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import java.net.CookieManager;
import java.net.CookiePolicy;

import io.realm.RealmObject;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by sourabhdesai on 9/17/15.
 * Singleton wrapper over SectionService instance created by Retrofit api
 * Docs for retrofit showing how this is done:
 *  http://square.github.io/retrofit/#restadapter-configuration
 */
public class SectionServiceProvider {
    private static SectionService ourInstance = null;

    public static SectionService getInstance(Context context) {
        if (ourInstance != null) {
            return ourInstance;
        }

        // GSON converter with DateTime Type Adapter
        Gson gson = GSONUtils.getConfiguredGsonBuilder()
            .create();

        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new DeviceIDRequestInterceptor(context));

        // Set cookie logic for OkHTTP client
        PersistentCookieStore cookieStore = new PersistentCookieStore(context);
        CookieManager cookieManager = new CookieManager(cookieStore, CookiePolicy.ACCEPT_ALL);
        client.setCookieHandler(cookieManager);

        // Create rest adapter from RetroFit. Initialize endpoint
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(context.getString(R.string.api_base_url))
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

        ourInstance = retrofit.create(SectionService.class);

        return ourInstance;
    }
}

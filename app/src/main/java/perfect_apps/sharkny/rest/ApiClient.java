package perfect_apps.sharkny.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mostafa on 06/06/16.
 */
public class ApiClient {

    // Base URL used in api
    public static final String BASE_URL = "http://sharkny.net/";
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}

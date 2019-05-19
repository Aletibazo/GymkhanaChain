package com.gymkhanachain.app.client;



import com.gymkhanachain.app.commons.GymkConstants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GymkhanasRestService {

    //String API_ROUTE = "/gymkhanas";

    // Obtener gymkhanas (MainActivity)
    @GET(GymkConstants.API)
    Call<List<Gymkhana>> getNearbyGymks(@Query("lat_sup")Double lat_sup, @Query("long_sup")Double long_sup, @Query("lat_inf")Double lat_inf, @Query("long_inf")Double long_inf);

    // Obtener gymkhanas de un usuario
    @GET(GymkConstants.API + "/user/{user}")
    Call<List<Gymkhana>> getUserGymkanas(@Path("user") String user);

    //Operaciones CRUD con Gymkhanas
    // Obtener una gymkhana
    @GET(GymkConstants.API + "/{id}")
    Call<Gymkhana> getGymkanaById(@Path("id") int id);

    // Añadir una gymkhana
    @POST(GymkConstants.API)
    Call<String> doPost(@Body Gymkhana gymkhana);

    // Modificar una gymkhana
    @POST(GymkConstants.API + "/{id}")
    Call<Void> modGymkana(@Path("id") int id, @Body Gymkhana gymkhana);

    //Borrar una gymkhana
    @DELETE(GymkConstants.API + "/{id}")
    Call<Void> deleteGymkhana(@Path("id") int id);
}

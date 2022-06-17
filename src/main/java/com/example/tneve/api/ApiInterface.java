package com.example.tneve.api;

import com.example.tneve.model.Attend;
import com.example.tneve.model.Event;
import com.example.tneve.model.FriendshipResponse;
import com.example.tneve.model.User;
import com.example.tneve.model.UserObject;
import com.example.tneve.model.Message;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {

    // USER
    @POST("users")
    Call<User> registerUser(@Body User user);

    @POST("users/login")
    Call<ApiToken> authenticationUser(@Body UserObject userObject);

    @GET("users/search/")
    Call<List<User>> searchUser(@Header("Authorization") String apiToken, @Query("s") String name);

    @GET("users/{ID}")
    Call<List<User>> getUser(@Header("Authorization") String token, @Path("ID") long userId);

    @DELETE("users")
    Call<ResponseBody> deleteUser(@Header("Authorization") String token);

    @PUT("users")
    Call<User> updateUser(@Body User user, @Header("Authorization") String token);

    // EVENTS
    @POST("events")
    Call<Event> createEvent(@Header("Authorization") String apiToken, @Body Event event);

    @GET("events")
    Call<List<Event>> searchEvents(@Query("t") String type);

    @GET("events")
    Call<List<Event>> getEvents(@Header("Authorization") String token);

    @DELETE("events/{ID}")
    Call<ResponseBody> deleteEvent(@Header("Authorization") String token, @Path("ID") long eventId);

    @GET("users/{ID}/assistances/")
    Call<List<Event>> getJoinedEvents(@Header("Authorization") String token, @Path("ID") long userId);

    @GET("users/{ID}/assistances/future")
    Call<List<Event>> getJoinedFutureEvents(@Header("Authorization") String token, @Path("ID") long userId);

    @GET("users/{ID}/assistances/finished")
    Call<List<Event>> getJoinedPastEvents(@Header("Authorization") String token, @Path("ID") long userId);

    @FormUrlEncoded
    @POST("events/{ID}/assistances")
    Call<Attend> confirmAttend(@Header("Authorization") String token,
                               @Path("ID") long eventId,
                               @Field("punctuation") int rating,
                               @Field("commentary") String comment);

    @GET("events/{ID}/assistances")
    Call<List<Attend>> getEventAttendances(@Header("Authorization") String token, @Path("ID") long eventId);


    @DELETE("assistances/{user_id}/{event_id}")
    Call<ResponseBody> cancelAttendance(@Path("user_id") long userId, @Path("event_id") long eventId, @Header("Authorization") String token);

    // ========================================================================
    // FRIENDS
    // ========================================================================

    /**
     *
     * @param token
     * @return
     */
    @GET("friends")
    Call<List<User>> getFriends(@Header("Authorization") String token);

    /**
     * Request a new friendship
     *
     * @param token
     * @param userId
     * @return
     */
    @POST("friends/{USER_ID}")
    Call<FriendshipResponse> requestFriendship(@Header("Authorization") String token, @Path("USER_ID") long userId);

    /**
     * Accepts friendship request from external user to authenticated user
     *
     * @param token
     * @param userId
     * @return
     */
    @PUT("friends/{USER_ID}")
    Call<FriendshipResponse> acceptFriendship(@Header("Authorization") String token, @Path("USER_ID") long userId);

    /**
     * Rejects friendship request from external user to authenticated user
     *
     * @param token
     * @param userId
     * @return
     */
    @DELETE("friends/{USER_ID}")
    Call<FriendshipResponse> rejectFriendship(@Header("Authorization") String token, @Path("USER_ID") long userId);

    /**
     *  Gets all external users that have sent a friendship request to the authenticated user
     *
     * @param token
     * @return
     */
    @GET("friends/requests")
    Call<List<User>> getFriendshipRequests(@Header("Authorization") String token);

    // ========================================================================
    // MESSAGES
    // ========================================================================

    @GET("messages/users")
    Call<List<User>> getMessages(@Header("Authorization") String token);

    @POST("messages")
    Call<Message> sendMessage(@Header("Authorization") String token, @Body Message message);

    @GET("messages/{USER_ID}")
    Call<List<Message>> getMessages(@Header("Authorization") String token, @Path("USER_ID") long userId);
}

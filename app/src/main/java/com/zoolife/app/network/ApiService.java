package com.zoolife.app.network;

import com.google.gson.JsonObject;
import com.zoolife.app.ResponseModel.AddComment.AddCommentResponseModel;
import com.zoolife.app.ResponseModel.AddDelivery.AddDeliveryResponseModel;
import com.zoolife.app.ResponseModel.AddPost.AddPostResponseModel;
import com.zoolife.app.ResponseModel.AllPost.AllPostResponseModel;
import com.zoolife.app.ResponseModel.Articles.AllArticlesResponseModel;
import com.zoolife.app.ResponseModel.Bid.BidModel;
import com.zoolife.app.ResponseModel.Category.CategoryResponseModel;
import com.zoolife.app.ResponseModel.ChangePassword.ChangePasswordResponseModel;
import com.zoolife.app.ResponseModel.CityNameResponseModel.CityNameResponseModel;
import com.zoolife.app.ResponseModel.FavModel.FavResponseModel;
import com.zoolife.app.ResponseModel.GetFavourites.GetFavouritesResponse;
import com.zoolife.app.ResponseModel.GetPost.GetPostResponseModel;
import com.zoolife.app.ResponseModel.GetUserProfile.GetUserProfileResponseModel;
import com.zoolife.app.ResponseModel.NoDataResponseModel;
import com.zoolife.app.ResponseModel.OTP.OTPResponseModel;
import com.zoolife.app.ResponseModel.SearchPost.SearchResponseModel;
import com.zoolife.app.ResponseModel.ShowComment.ViewCommentsResponseModel;
import com.zoolife.app.ResponseModel.SubCategory.SubCategoryResponseModel;
import com.zoolife.app.ResponseModel.UpdateDeviceInfo.UpdateDeviceInfoResponse;
import com.zoolife.app.ResponseModel.UpdateProfile.UpdateProfileResponseModel;
import com.zoolife.app.ResponseModel.UserPost.UserAllPostResponseModel;
import com.zoolife.app.ResponseModel.UserPost.UserAllPostResponseModelNew;
import com.zoolife.app.models.AuctionModel;
import com.zoolife.app.models.SliderModel;
import com.zoolife.app.models.notification.NotificationModel;
import com.zoolife.app.models.related_ad_home.RelatedAdHomeModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiService {


    /*@FormUrlEncoded
    @POST("/auth")
    Call<JsonObject> signIn(
            @Field("pass") String pass,
            @Field("username") String username,
            @Field("password") String password
    );*/
    @FormUrlEncoded
    @POST("loginapi")
    Call<JsonObject> signIn(
            @Field("phone") String phone,
            @Field("password") String password
    );


    @FormUrlEncoded
    @POST("registerapi")
    Call<JsonObject> signUp(
            @Field("username") String fullname,
            @Field("phone") String phone,
            @Field("password") String password
    );


    /*@FormUrlEncoded
    @POST("/auth")
    Call<OTPResponseModel> otpVerify(
            @Field("pass") String pass,
            @Field("otp") String otp,
            @Field("username") String username
    );
*/
    @FormUrlEncoded
    @POST("verify_otp")
    Call<OTPResponseModel> otpVerify(
            @Field("phone") String phone,
            @Field("otp") String otp
    );

    @FormUrlEncoded
    @POST("resend_otp")
    Call<OTPResponseModel> resendOTP(
            @Field("phone") String phone
            );


   /* @FormUrlEncoded
    @POST("/auth")
    Call<JsonObject> resetPassword(
            @Field("pass") String pass,
            @Field("username") String username
    );*/

    @FormUrlEncoded
    @POST("reset_password")
    Call<JsonObject> resetPassword(
            @Field("phone") String pass
    );


   /* @FormUrlEncoded
    @POST("/auth")
    Call<ChangePasswordResponseModel> updatePassword(
            @Field("pass") String pass,
            @Field("password") String password,
            @Field("username") String username
    );*/

    @FormUrlEncoded
    @POST("update_password")
    Call<ChangePasswordResponseModel> updatePassword(
            @Field("password") String pass,
            @Field("phone") String phone
    );



    /*@FormUrlEncoded
//    @POST("/category")
    @POST("/category")
    Call<CategoryResponseModel> getCategory(
            @Field("pass") String pass

    );*/


    //Checked Working
    @POST("category")
    Call<CategoryResponseModel> getCategory(


    );



//    @Headers("X-Localization: en")
    @POST("category")
    Call<CategoryResponseModel> getCategoryEnglish();


    // @POST("/home")category_id
    @GET("itemsapi")
    Call<AllPostResponseModel> getAllPost();


    @POST("auction_items")
    Call<AuctionModel> getAuctionList();

//https://newzoolifeapi.zoolifeshop.com/get_items_by_subcategory
    @GET
    Call<AllPostResponseModel> getAllPostNew(@Url String url);

    @FormUrlEncoded
    @POST("get_items_by_subcategory")
    Call<AllPostResponseModel> getAllPostBySubCategory(@Field("category_id") int category_id);


    @FormUrlEncoded
    @POST
    Call<AllPostResponseModel> getAllPostNewBySubCategory(
            @Url String url,
            @Field("category_id") int category_id);

//     @FormUrlEncoded
//     @POST("/home")
//     Call<AllCategoryPostResponseModel> getPostByCategory(
//             @Field("pass") String pass


//     );


//     @FormUrlEncoded
//     @POST("/home")
//     Call<SearchResponseModel> getSearch(
//             @Field("pass") String pass,
//             @Field("search") String search

//     );


   /* @FormUrlEncoded
    @POST("/category")
    Call<SubCategoryResponseModel> getSubCategory(
            @Field("pass") String pass,
            @Field("category_id") int category_id

    );*/


    @FormUrlEncoded
    @POST("get_sub_category")
    Call<SubCategoryResponseModel> getSubCategory(
            @Field("category_id") int category_id

    );

    @FormUrlEncoded
    @POST("sliders")
    Call<SliderModel> getSliders(
            @Field("pass") String allSliders
    );

    @FormUrlEncoded
    @POST("sliders")
    Call<ResponseBody> getSliders1(
            @Field("pass") String allSliders
    );





//     @FormUrlEncoded
//     @POST("/notification")
//     Call<NotificationsResponseModel> getNotifications(
//             @Field("pass") String pass,
//             @Field("username") String username

//     );


//     @FormUrlEncoded
//     @POST("/get_all_notify")
//     Call<NotificationModel> getNotifications(
//             @Field("user_id") String userId
//     );

    @FormUrlEncoded
    //@POST("/reportapi")
    @POST("reportapi")
    Call<AddPostResponseModel> reportApi(
            @Field("pass") String pass,
            @Field("ads_id") String adsId,
            @Field("user_id") String userId,
            @Field("content") String content
    );


//     @Multipart
//     @POST("/item")
//     Call<AddPostResponseModel> addPost(
//             @Query("pass") String pass,
//             @Query("username") String username,
//             @Query("location") String location,
//             @Query("title") String title,
//             @Query("description") String description,
//             @Query("category") int category,
//             @Query("sub_category") int sub_category,
//             @Query("showComments") int showComments,
//             @Query("showPhone") int showPhone,
//             @Query("showMessage") int showMessage,
//             @Part MultipartBody.Part MediaFile
//     );

    //@POST("/item")
    @POST("add_post")
    Call<AddPostResponseModel> addPost1(
            @Body RequestBody file
    );


//     @POST("/sliders")
//     Call<RelatedAdHomeModel> getRelatedAdds(
//             @Body RequestBody file
//     );


    @Multipart
    //@POST("/home")
    @POST("home")
    Call<String> uploadImage(
            @Query("pass") String pass,
            @Query("username") String username,
            @Part("images[]") String image,
            @Part MultipartBody.Part MediaFile
    );


//     @FormUrlEncoded
//     @POST("/item")
//     Call<UserAllPostResponseModel> getAllUserPost(
//             @Field("pass") String pass,
//             @Field("username") String username

//     );


//     @FormUrlEncoded
//     @POST("/user")
//     Call<UpdateProfileResponseModel> updateProfile(
//             @Field("pass") String pass,
//             @Field("username") String username,
//             @Field("surname") String surname,
//             @Field("fullname") String fullname,
//             @Field("language") String language,
//             @Field("bYear") String bYear,
//             @Field("bMonth") String bMonth,
//             @Field("bDay") String bDay,
//             @Field("country") String country,
//             @Field("country_id") int country_id,
//             @Field("city") String city,
//             @Field("city_id") int city_id

//     );

//     @FormUrlEncoded
//     @POST("/user")
//     Call<GetUserProfileResponseModel> getProfile(
//             @Field("pass") String pass,
//             @Field("username") String username
//     );


//     @FormUrlEncoded
//     @POST("/home")
//     Call<UpdateDeviceInfoResponse> updateDeviceInfo(
//             @Field("pass") String pass,
//             @Field("user_id") String userId,
//             @Field("device_token") String deviceToken,
//             @Field("device_type") String deviceType

//     );


//     @FormUrlEncoded
//     @POST("/comments")
//     Call<AddCommentResponseModel> addComment(
//             @Field("pass") String pass,
//             @Field("userId") int userId,
//             @Field("itemId") int itemId,
//             @Field("message") String message

//     );

    @FormUrlEncoded
    @POST("add_comments")
    Call<AddCommentResponseModel> addComment(
            @Field("user_id") int userId,
            @Field("id") int id,
            @Field("message") String message

    );

    @FormUrlEncoded
    @POST("delete_comment")
    Call<NoDataResponseModel> deleteComment(
            @Field("itemId") int itemId,
            @Field("userId") int userId,
            @Field("commentId") int commentId
    );

    @FormUrlEncoded
    @POST("list_comments_by_item")
    Call<ViewCommentsResponseModel> listCommentByItem(
            @Field("id") String itemId
    );

    @FormUrlEncoded
    @POST("delete_favorites")
    Call<NoDataResponseModel> deleteFavorites(
            @Field("favoriteId") int favoriteId
    );

    @FormUrlEncoded
    @POST("favoruit_item")
    Call<FavResponseModel> favoruitItem(
            @Field("user_id") String userId,
            @Field("id") int Itemid,
            @Field("status") int status
    );

    @FormUrlEncoded
    @POST("fav_item_by_user")
    Call<GetFavouritesResponse> favItembyUser(
            @Field("user_id") String userId
    );

    @FormUrlEncoded
    @POST("favoruit_list_by_item")
    Call<NoDataResponseModel> favoruitListbyItem(
            @Field("user_id") int userId,
            @Field("id") int itemId
    );

    @FormUrlEncoded
    @POST("list_likes")
    Call<NoDataResponseModel> listLikes(
            @Field("phone") String phone,
            @Field("fromUserId") int fromUserId,
            @Field("itemId") int itemId
    );

    @FormUrlEncoded
    @POST("like_item")
    Call<NoDataResponseModel> likeItem(
            @Field("id") int id,
            @Field("user_id") int userId
    );

    @FormUrlEncoded
    @POST("delete_like_item")
    Call<NoDataResponseModel> deleteLikeItem(
            @Field("phone") String phone,
            @Field("fromUserId") int fromUserId,
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("abuse_item")
    Call<NoDataResponseModel> abuseItem(
            @Field("user_id") int userId,
            @Field("itemId") int itemId
    );

    @FormUrlEncoded
    @POST("list_abused_items")
    Call<NoDataResponseModel> listAbusedItems(
            @Field("phone") String phone,
            @Field("fromUserId") int fromUserId
    );

    @FormUrlEncoded
    @POST("delete_abused_item")
    Call<NoDataResponseModel> deleteAbusedItem(
            @Field("user_id") int userId,
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("get_post_by_user")
    Call<UserAllPostResponseModelNew> getAucationByUser(
            @Field("user_id") String userId,
            @Field("post_type") String posttype

    );
    @FormUrlEncoded
    @POST("get_post_by_user")
    Call<UserAllPostResponseModelNew> getPostbyUserNew(
            @Field("user_id") String userId
    );

    @FormUrlEncoded
    @POST("deactivate_item")
    Call<NoDataResponseModel> deactivateItem(
            @Field("user_id") int userId,
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("activate_item")
    Call<NoDataResponseModel> activateItem(
            @Field("user_id") int userId,
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("delete_item")
    Call<NoDataResponseModel> deleteItem(
            @Field("user_id") String userId,
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("delete_item_images")
    Call<NoDataResponseModel> deleteItemImages(
            @Field("id") int id
    );

    //@FormUrlEncoded
    //@POST("/add_post")
    //Call<AddPostResponseModel> addPost(
    //@Body RequestBody ("user_id") int userId,
    //@Field("fromUserId") int fromUserId,
    //@Field("priority") int priority,
    //@Field("imgUrl") File imgUrl,
    //@Field("showComments") int showComments,
    //@Field("category") int category,
    //@Field("subCategory") int subCategory,
    //@Field("itemTitle") String itemTitle,
    //@Field("itemDesc") String itemDesc,
    //@Field("showPhoneNumber") int showPhoneNumber,
    //@Field("showMessage") int showMessage,
    //@Field("city") String city,
    //@Field("country") String country,
    //@Field("images[]") File images,
    //@Part MultipartBody.Part MediaFile
    //);

    @POST("update_post")
    Call<NoDataResponseModel> updatePost(
            @Body RequestBody file
    );

    @FormUrlEncoded
    @POST("get_single_category")
    Call<NoDataResponseModel> getSingleCategory(
            @Field("category_id") int category_id
    );

    @FormUrlEncoded
    @POST("category")
    Call<NoDataResponseModel> category(
            @Field("category_id") int category_id
    );

//Dashboard API call here

    @FormUrlEncoded
    @POST("item_search")
    Call<SearchResponseModel> itemSearch(
            @Field("user_id") String user_id,
            @Field("search") String search
    );

    @FormUrlEncoded
    @POST("get_all_item_by_category")
    Call<AllPostResponseModel> getAllItembyCategory(
            @Field("category_id") int category_id,
            @Field("city") String city,
            @Field("subCategory") int subCategory,
            @Field("removeAt") int removeAt
    );


    @FormUrlEncoded
    @POST
    Call<AllPostResponseModel> getAllItembyCategoryNew(
            @Url String url,
            @Field("category_id") int category_id,
            @Field("city") String city,
            @Field("subCategory") int subCategory,
            @Field("removeAt") int removeAt
    );

    @FormUrlEncoded
    @POST("get_item")
    Call<GetPostResponseModel> getItem(
            @Field("user_id") int user_id,
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("get_item")
    Call<ResponseBody> getItem3(
            @Field("user_id") int user_id,
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("get_item")
    Call<GetPostResponseModel> getItem2(
            @Field("id") int id

    );

    //Item Search API call here

    @FormUrlEncoded
    @POST("get_user_profile")
    Call<GetUserProfileResponseModel> getUserProfile(
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("update_user_profile")
    Call<UpdateProfileResponseModel> updateUserProfile(
            @Field("user_id") String user_id,
            @Field("country") String country,
            @Field("city") String city,
            @Field("fullname") String fullname
    );

    @FormUrlEncoded
    @POST("update_user_device_token")
    Call<UpdateDeviceInfoResponse> updateUserDeviceToken(
            @Field("user_id") String user_id,
            @Field("device_token") String device_token,
            @Field("device_type") String device_type
    );

    //check this one
    @FormUrlEncoded
    @POST("get_chat_list_by_user")
    Call<NoDataResponseModel> getChatListbyUser(
            @Field("user_id") int user_id
    );

    @FormUrlEncoded
    @POST("get_single_chat_by_user")
    Call<NoDataResponseModel> getSingleChatbyUser(
            @Field("user_id") int user_id,
            @Field("chat_id") int chat_id
    );

    @FormUrlEncoded
    @POST("send_message")
    Call<NoDataResponseModel> sendMessage(
            @Field("user_id") int user_id,
            @Field("message") String message,
            @Field("to_user_id") int to_user_id,
            @Field("chat_id") int chat_id
    );

    @FormUrlEncoded
    @POST("send_new_message")
    Call<NoDataResponseModel> sendNewMessage(
            @Field("user_id") int user_id,
            @Field("message") String message,
            @Field("to_user_id") int to_user_id,
            @Field("chat_id") int chat_id
    );

    @FormUrlEncoded
    @POST("delete_message")
    Call<NoDataResponseModel> sendNewMessage(
            @Field("user_id") int user_id,
            @Field("chat_id") int chat_id,
            @Field("message_id") int message_id
    );

    @FormUrlEncoded
    @POST("get_by_item_report")
    Call<NoDataResponseModel> getbyItemReport(
            @Field("itemId") int itemId
    );

    @FormUrlEncoded
    @POST("get_all_notify")
    Call<NotificationModel> getAllNotify(
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("add_delivery")
    Call<AddDeliveryResponseModel> addDelivery(
            @Field("user_id") String fromUserId,
            @Field("itemTitle") String itemTitle,
            @Field("itemDesc") String itemDesc,
            @Field("category") String category,
            @Field("subCategory") String subcategory,
            @Field("showPhoneNumber") String showPhoneNumber,
            @Field("showComments") String showComments,
            @Field("showMessage") String showMessage,
            @Field("city") String city,
            @Field("country") String country,
            @Field("phone") String imgUrl
    );

    @FormUrlEncoded
    @POST("add_msgs")
    Call<NoDataResponseModel> addMsgs(
            @Field("user_id") int user_id
    );

    @FormUrlEncoded
    @POST("read_msg")
    Call<NoDataResponseModel> readMsgs(
            @Field("user_id") int user_id,
            @Field("read_msg") int read_msg
    );

    @FormUrlEncoded
    @POST("sliders")
    Call<RelatedAdHomeModel> sliders(
            @Field("pass") String pass
    );


    @FormUrlEncoded
    @POST("like_item")
    Call<AddCommentResponseModel> likeItem(
            @Field("pass") String pass,
            @Field("user_d") int userId,
            @Field("id") int itemId,
            @Field("status") String status

    );


    @FormUrlEncoded
    @POST("comments")
    Call<ViewCommentsResponseModel> viewComments(
            @Field("pass") String pass,
            @Field("itemId") String itemId
    );


    @FormUrlEncoded
    @POST("home")
    Call<GetPostResponseModel> getPost(
            @Field("pass") String pass,
            @Field("id") String itemId
    );


    @FormUrlEncoded
    @POST("home")
    Call<GetPostResponseModel> getPostWithLogin(
            @Field("pass") String pass,
            @Field("id") String itemId,
            @Field("username") String username
    );

    @FormUrlEncoded
    @POST("deliveries")
    Call<UserAllPostResponseModel> getAllDelivery(
            @Field("user_id") int userId
    );


    @FormUrlEncoded
    @POST//("deliveries")
    Call<UserAllPostResponseModel> getAllDeliveryNextPage(
            @Url String url,
            @Field("user_id") int userId
    );

    @FormUrlEncoded
    @POST("delete_deliver")
    Call<UserAllPostResponseModel> deleteDelivery(
            @Field("user_id") String userId,
            @Field("id") String id

    );

//     @FormUrlEncoded
//     @POST("/item")
//     Call<UserAllPostResponseModel> addDelivery(
//             @Field("pass") String pass,
//             @Field("username") String username,
//             @Field("location") String location,
//             @Field("title") String itemTitle,
//             @Field("description") String description,
//             @Field("category") String category,
//             @Field("sub_category") String subcategory,
//             @Field("showComments") String showComments,
//             @Field("showPhone") String showPhone,
//             @Field("showMessage") String showMessage,
//             @Field("country") String country,
//             @Field("city") String city

//     );
/*
    @FormUrlEncoded
    @POST("/articles")
    Call<AllArticlesResponseModel> getAllArticles(
            @Field("pass") String pass
    );*/

    @FormUrlEncoded
    @POST("articles")
    Call<AllArticlesResponseModel> getAllArticles(
            @Field("pass") String pass
    );


    @FormUrlEncoded
    @POST("favorites")
    Call<FavResponseModel> doFavAd(
            @Field("pass") String pass,
            @Field("userId") String userId,
            @Field("itemId") String itemId,
            @Field("status") String status
    );

//     @FormUrlEncoded
//     @POST("/favorites")
//     Call<GetFavouritesResponse> getMyFav(
//             @Field("pass") String pass,
//             @Field("userId") String userId
//     );

//     @FormUrlEncoded
//     @POST("/item")
//     Call<NoDataResponseModel> deleteItem(
//             @Field("pass") String pass,
//             @Field("username") String username,
//             @Field("id") String id
//     );

    //     @FormUrlEncoded
//     @POST("/item")
//     Call<NoDataResponseModel> deleteItemImage(
//             @Field("pass") String pass,
//             @Field("id") String id
//     );
    @FormUrlEncoded
    @POST("delete_item_images")
    Call<NoDataResponseModel> deleteItemImage(
            @Field("id") String id
    );

    @GET("cities")
    Call<CityNameResponseModel> getAllCityNames(
    );

    @FormUrlEncoded
    @POST("add_bid")
    Call<NoDataResponseModel> add_bid(
            @Field("item_id") int itemId,
            @Field("fromUserId") int userId,
            @Field("amount") String amount
    );

    @FormUrlEncoded
    @POST("auction_items")
    Call<BidModel> auction_items(
            @Field("item_id") String itemId
    );
    @FormUrlEncoded
    @POST("bids")
    Call<BidModel> getBide(
            @Field("item_id") String itemId
    );
}

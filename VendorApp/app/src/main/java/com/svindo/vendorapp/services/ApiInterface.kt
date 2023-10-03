package com.svindo.vendorapp.services

import androidx.annotation.NonNull
import com.svindo.vendorapp.modelclass.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {


    @FormUrlEncoded
    @POST("Auth/GenerateOtp")
    fun Gen_otp(
        @Field("mobile_number") mobile_number: String,
    ): Call<Mobileotp_Response>


    @FormUrlEncoded
    @POST("Auth/VerifyOtp")
    fun Verify_otp(
        @Field("mobile_number") mobile_number: String,
        @Field("otp") otp: String,
        @Field("fcm_token") fcm_token: String,
    ): Call<Verify_otp_Response>


    @POST("Home/wallet")
    fun WalletDetailsInterface(
        @Header("Sessionid") content_type:String?,
    ): Call<WalletModal>


    @POST("Home/ad_wallet")
    fun AdWalletDetails(
        @Header("Sessionid") content_type:String?,
    ): Call<WalletModal>




    @FormUrlEncoded
    @POST("Home/vendor_withdraw_request")
    fun WithDrawlAmount(
        @Header("Sessionid") content_type:String?,
        @Field("withdraw_amount") withdraw_amount: String,
    ): Call<Verify_otp_Response>




    @Multipart
    @NonNull
    @POST("Home/profile_details_update")
    fun UploadBusinessRegFilesInterface(
        @HeaderMap headers: Map<String, String>,
        @Part("type") type: RequestBody,
        @Part("pan_number") pan_number: RequestBody,
        @Part image1: MultipartBody.Part,

    ): Call<Verify_otp_Response>



    @Multipart
    @NonNull
    @POST("Auth/create_vendor_user")
    fun businessdetails(
       //@Header("Sessionid")  content_type:String?,
        @Part("type") type: RequestBody,
        @Part("business_name") business_name: RequestBody,
        @Part("mobile")contact_mob_num: RequestBody,
        @Part("email")store_email_id: RequestBody,
        @Part("category_id") category_id: RequestBody,
        @Part("address")address: RequestBody,
        @Part("location")location: RequestBody,
        @Part("name") name: RequestBody,
        @Part("zone_id")zone_id: RequestBody,
        @Part image: MultipartBody.Part,
        @Part("serviceitem")serviceitem: RequestBody,
        @Part("business_phone")business_phone: RequestBody,
        ): Call<Verify_otp_Response>


    @Multipart
    @NonNull
    @POST("Home/profile_details_update")
    fun Editbusinessdetails(
        @Header("Sessionid")  content_type:String?,
        @Part("type") type: RequestBody,
        @Part("name") name: RequestBody,
        @Part("business_name") business_name: RequestBody,
        @Part("mobile")contact_mob_num: RequestBody,
        @Part("email")store_email_id: RequestBody,
        @Part("category_id") category_id: RequestBody,
        @Part("address")address: RequestBody,
        @Part("location")location: RequestBody,
        @Part image: MultipartBody.Part,
        @Part image1: MultipartBody.Part,
    ): Call<Verify_otp_Response>


    @Multipart
    @NonNull
    @POST("Home/profile_details_update")
    fun Editbusinessdetails_one_image(
        @Header("Sessionid")  content_type:String?,
        @Part("type") type: RequestBody,
        @Part("name") name: RequestBody,
        @Part("business_name") business_name: RequestBody,
        @Part("mobile")contact_mob_num: RequestBody,
        @Part("email")store_email_id: RequestBody,
        @Part("category_id") category_id: RequestBody,
        @Part("address")address: RequestBody,
        @Part("location")location: RequestBody,
        @Part image: MultipartBody.Part,
    ): Call<Verify_otp_Response>


    @FormUrlEncoded
    @POST("Home/profile_details_update")
    fun Editbusinessdetails_files_null(
        @Header("Sessionid")  content_type:String?,
        @Field("type") type: String,
        @Field("name") name: String,
        @Field("business_name") business_name: String,
        @Field("mobile")contact_mob_num: String,
        @Field("email")store_email_id: String,
        @Field("category_id") category_id: String,
        @Field("address")address: String,
        @Field("location")location: String,
    ): Call<Verify_otp_Response>



    @FormUrlEncoded
    @POST("Home/profile_details_update")
    fun bankaccountdetails(
       @Header("Sessionid") content_type: String?,
        @Field("type") type: String,
        @Field("account_hold_name") account_hold_name: String,
        @Field("ifsc") ifsc: String,
        @Field("bank_name") bank_name: String,
        @Field("account_number") account_number: String,
    ): Call<Bankdetails_Response>




    @FormUrlEncoded
    @POST("Home/profile_details_update")
    fun contactdetails(
      @Header("Sessionid")  content_type:String?,
        @Field("type") type: String,
        @Field("name") emergency_contact_name: String,
        @Field("mobile_number") emergency_mobile_number: String,
    ): Call<Bankdetails_Response>



    @POST("Home")
    fun Dashboarddetails(
        @Header("Sessionid") Sessionid: String?,
    ): Call<DashboardResponseModal>



    @POST("Home/customer_feedback")
    fun Customerfeedbackdetails(
        @Header("Sessionid") Sessionid: String?,
    ): Call<CustomerFeedbackModal>

    @POST("Home/notifications")
    fun notificationdetails(
        @Header("Sessionid")  content_type:String?,
    ): Call<NotificationModal>


    @POST("Home/insights")
    fun  InsightsDetails(
        @Header("Sessionid")  content_type:String?,
    ): Call<InsightsModalClass>

    @POST("Home/store_products_list")
    fun  ProductDetails(
        @Header("Sessionid")  content_type:String?,
    ): Call<ProductsModal>


    @POST("Home/store_products_category_list")
    fun  CategoriesDetails(
        @Header("Sessionid")  content_type:String?,
    ): Call<ProductCategoryModal>



    @POST("Home/vendor_spotlights")
    fun  SpotlightDetails(
        @Header("Sessionid")  content_type:String?,
    ): Call<SpotligtsModal>


    @POST("Home/coupons_list")
    fun  CouponListDeatils(
        @Header("Sessionid")  content_type:String?,
    ): Call<CouponsListModal>

    @FormUrlEncoded
    @POST("Home/add_coupon")
    fun AddCoupondetails(
        @Header("Sessionid") content_type: String?,
        @Field("code") code: String,
        @Field("description") description: String,
        @Field("min_amount") min_amount: String,
        @Field("maximum_amount") maximum_amount: String,
        @Field("discount_percentage") discount_percentage: String,
        @Field("validity_days") validity_days: String,
    ): Call<AddCouponModal>




    @POST("Home/units")
    fun  Unitdetails(
        @Header("Sessionid")  content_type:String?,
    ): Call<UnitModal>


    @FormUrlEncoded
    @POST("Home/catalog_products")
    fun  CatalougeProductList(
        @Header("Sessionid")  content_type:String?,
        @Field("category_id") category_id: String,
    ): Call<CustomSpinAdapter>


    @POST("Home/main_categories")
    fun  categoriesListforReg(
        @Header("Sessionid")  content_type:String?,
    ): Call<CustomSpinAdapter>

    @POST("Home/main_categories")
    fun MainCategoreis(
        @Header("Sessionid")  content_type:String?,
    ): Call<MainCategoryModal>



    @POST("https://svindo.com/Common_controller/get_location_zone")
    fun Zones(
    ): Call<ZonesModalClass>

    @POST("https://svindo.com/Common_controller/main_categories")
    fun  session_freecategoriesListforReg(
     //   @Header("Sessionid")  content_type:String?,
    ): Call<CustomSpinAdapter>

    @FormUrlEncoded
    @POST("https://svindo.com/Common_controller/get_location_zone")
    fun SubZones(
        @Field("id") id: String,
    ): Call<ZonesModalClass>


    @FormUrlEncoded
    @POST("Home/catalog_product_details")
    fun  fetchItemDetails(
        @Header("Sessionid")  content_type:String?,
        @Field("product_id") product_id: String,
    ): Call<ProductCategoryDetails>


    @FormUrlEncoded
    @POST("Home/sub_categorires")
    fun SubCategoryDetails(
        @Header("Sessionid")  content_type:String?,
        @Field("category_id") category_id: String,
    ): Call<SubCategoryModal>


    @POST("Home/sub_categorires")
    fun SizesListDetails(
        @Header("Sessionid")  content_type:String?,
    ): Call<SubCategoryModal>



    @FormUrlEncoded
    @POST("Home/add_vendor_print_product")
    fun AddPrintingProductdetails(
        @Header("Sessionid") content_type: String?,
        @Field("mrp") mrp: String,
        @Field("sale_price") sale_price: String,
        @Field("quantity") quantity: String,
        @Field("unit_id") unit_id: String,
        @Field("avaiable_stock") avaiable_stock: String,
        @Field("product_id") product_id: String,
        @Field("instant_delivery") instant_delivery: String,
        @Field("delivery_days") delivery_days: String,
        @Field("general_delivery") general_delivery: String,
        @Field("self_pickup") self_pickup: String,
        @Field("is_cod") is_cod: String,
        @Field("is_replace") is_replace: String,
        @Field("is_refund") is_refund: String,
        @Field("is_shop_exchange") is_shop_exchange: String,
        @Field("colour_data") colour_data: String,
        @Field("fabric_data") fabric_data: String,
        @Field("pattern_data") pattern_data: String,
        @Field("prices_data") prices_data: String,
        @Field("sizes_data") sizes_data: String,
        @Field("addon_data") addon_data: String,
    ): Call<Bankdetails_Response>




    @Multipart
    @NonNull
    @POST("Home/add_product_to_catalog")
    fun AddNewProductDetails(
        @Header("Sessionid") content_type: String?,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("origin") origin: RequestBody,
        @Part("category_id") category_id: RequestBody,
        @Part("mrp") mrp: RequestBody,
        @Part("sale_price") sale_price: RequestBody,
        @Part("quantity") quantity: RequestBody,
        @Part("unit_id") unit_id: RequestBody,
        @Part("avaiable_stock") avaiable_stock: RequestBody,
        @Part("instant_delivery") instant_delivery: RequestBody,
        @Part("general_delivery") general_delivery: RequestBody,
        @Part("self_pickup") self_pickup: RequestBody,
        @Part("is_refund") is_refund: RequestBody,
        @Part("is_shop_exchange") is_shop_exchange: RequestBody,
        @Part("delivery_days") delivery_days: RequestBody,
        @Part("is_cod") is_cod: RequestBody,
        @Part("is_replace") is_replace: RequestBody,
        @Part("subcategory_id") subcategory_id: RequestBody,
        @Part("min_quantity") min_quantity: RequestBody,
        @Part("size_id") size_id: RequestBody,
        @Part("is_printable") is_printable: RequestBody,
        @Part("hsn_code") hsn_code: RequestBody,
        @Part image : MultipartBody.Part,
        @Part image1 : MultipartBody.Part,
        @Part image2: MultipartBody.Part,
        @Part image3 : MultipartBody.Part,
     //   @Part image4: MultipartBody.Part,
    ): Call<Bankdetails_Response>


    @FormUrlEncoded
    @POST("Home/add_vendor_product")
    fun CatProductDetails(
        @Header("Sessionid") content_type: String?,
        @Field("product_id") product_id: String,
        @Field("category_id") category_id: String,
        @Field("mrp") mrp: String,
        @Field("sale_price") sale_price: String,
        @Field("quantity") quantity: String,
        @Field("unit_id") unit_id: String,
        @Field("avaiable_stock") avaiable_stock: String,
        @Field("instant_delivery") instant_delivery: String,
        @Field("delivery_days") delivery_days: String,
        @Field("general_delivery") general_delivery: String,
        @Field("self_pickup") self_pickup: String,
        @Field("is_cod") is_cod: String,
        @Field("is_replace") is_replace: String,
        @Field("is_refund") is_refund: String,
        @Field("is_shop_exchange") is_shop_exchange: String,
        @Field("addon_data") addon_data: String,
        @Field("is_printable") is_printable: String,
        @Field("min_quanity") min_quanity: String,
        @Field("size_id") size_id: String,
    ): Call<Bankdetails_Response>




    @POST("Home/user_profile_details")
    fun AccountDetails(
        @Header("Sessionid")  content_type:String?,
    ): Call<AccountsModal>


    @POST("Home/user_profile_details")
    fun BusinessDetails(
        @Header("Sessionid")content_type:String?,
    ): Call<EditBusinessDetailsModal>


    @FormUrlEncoded
    @POST("Home/individual_product_details")
    fun ProductDeatilsresponse(
        @Header("Sessionid")content_type:String?,
        @Field("product_id")id: String,
    ): Call<ProductDeatilsResponse>


    @Multipart
    @NonNull
    @POST("Home/add_notification")
    fun AddNotifications(
        @Header("Sessionid")  content_type:String?,
        @Part("description")description: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<Verify_otp_Response>



    @FormUrlEncoded
    @POST("Home/vendor_status_update")
    fun StatusDetails(
        @Header("Sessionid")  content_type:String?,
        @Field("type") type: String,
        @Field("product_id") product_id: String,
        @Field("value") value: String,
    ): Call<VendorStatusUpadateModal>


    @FormUrlEncoded
    @POST("Home/vendor_status_update")
    fun CategoryStatusDetails(
        @Header("Sessionid")  content_type:String?,
        @Field("type") type: String,
        @Field("category_id") category_id: String,
        @Field("value") value: String,
    ): Call<VendorStatusUpadateModal>


    @FormUrlEncoded
    @POST("Home/product_delete")
    fun ProductDelete(
        @Header("Sessionid")  content_type:String?,
        @Field("product_id") product_id: String,
    ): Call<Bankdetails_Response>


    @FormUrlEncoded
    @POST("Home/banner_delete")
    fun BannerDelete(
        @Header("Sessionid")  content_type:String?,
        @Field("banner_id") product_id: String,
    ): Call<Verify_otp_Response>


    @FormUrlEncoded
    @POST("Home/spotlight_delete")
    fun SpotlightDelete(
        @Header("Sessionid")  content_type:String?,
        @Field("spotlight_id") spotlight_id: String,
    ): Call<Bankdetails_Response>

    @FormUrlEncoded
    @POST("Home/spotlight_boosted")
    fun SpotlightBoosting(
        @Header("Sessionid")  content_type:String?,
        @Field("spotlight_id") spotlight_id: String,
        @Field("value") value: String,
        @Field("amount") amount: String,
    ): Call<Bankdetails_Response>

    @FormUrlEncoded
    @POST("Home/addon_delete")
    fun AddonDelete(
        @Header("Sessionid")  content_type:String?,
        @Field("addon_id") addon_id: String,
    ): Call<Verify_otp_Response>


    @Multipart
    @NonNull
    @POST("Home/profile_details_update")
    fun FssaiDetails(
       @Header("Sessionid")  content_type:String?,
       @Part("type") type: RequestBody,
       @Part("code")code: RequestBody,
       @Part image1: MultipartBody.Part,
    ): Call<Bankdetails_Response>



    @Multipart
    @NonNull
    @POST("Home/profile_details_update")
    fun GSTINDeatils(
        @Header("Sessionid")  content_type:String?,
        @Part("type") type: RequestBody,
        @Part("code")code: RequestBody,
        @Part image1: MultipartBody.Part,
    ): Call<Bankdetails_Response>


    @FormUrlEncoded
    @POST("Home/orderslist")
    fun  OrdersDetails(
        @Header("Sessionid") content_type:String?,
        @Field("type") type: String,
        @Field("status") status: String,
    ): Call<OrdersListModal>



    @FormUrlEncoded
    @POST("Home/add_addons")
    fun  AddAddonsDetails(
        @Header("Sessionid") content_type:String?,
        @Field("name") name: String,
        @Field("description") description: String,
        @Field("price") price: String,
        @Field("id") id: String,
    ): Call<Bankdetails_Response>


    @POST("Home/addon_icons")
    fun AddonsIconsDetails(
        @Header("Sessionid") content_type:String?,
    ): Call<AddonIconsModal>



    @POST("Home/addons_list")
    fun  AddAddonsListDetails(
        @Header("Sessionid") content_type:String?,
    ): Call<AddonsListModal>


    @POST("Home/banners_list")
    fun  BannersListDetails(
        @Header("Sessionid") content_type:String?,
    ): Call<BannersListModal>


    @Multipart
    @NonNull
    @POST("Home/add_banners")
    fun AddBanners(
        @Header("Sessionid")  content_type:String?,
        @Part("redirect_type")redirect_type: RequestBody,
        @Part("product_id")product_id: RequestBody,
        @Part("max_amount")max_amount: RequestBody,
        @Part("banner_name")banner_name: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<Verify_otp_Response>



    @FormUrlEncoded
    @POST("Home/add_spotlight")
    fun  AddSpotlight(
        @Header("Sessionid")  content_type:String?,
        @Field("product_id") product_id: String,
    ): Call<Verify_otp_Response>


    @FormUrlEncoded
    @POST("Home/orderdetails")
    fun  AcceptOrdersDetails(
        @Header("Sessionid")  content_type:String?,
        @Field("order_id") order_id: String,
    ): Call<OrderAcceptModal>


    @FormUrlEncoded
    @POST("Home/order_status")
    fun OrderStatusDeatils(
        @Header("Sessionid")  content_type:String?,
        @Field("order_id") order_id: String,
        @Field("status") status: String,
    ): Call<Verify_otp_Response>

    @FormUrlEncoded
    @POST("Home/order_addons")
    fun OrderAddonsDeatils(
        @Header("Sessionid")  content_type:String?,
        @Field("id") id: String,
    ): Call<ViewAddonsListModal>


    @POST("Home/check_free_delivery")
    fun CashBackStatus(
        @Header("Sessionid")  content_type:String?,
    ): Call<CashbackStatusModal>


    @FormUrlEncoded
    @POST("Home/vendor_status_update")
    fun LocationStatusDetails(
        @Header("Sessionid")  content_type:String?,
        @Field("type") type: String,
        @Field("option") option: String,
    ): Call<CashbackStatusModal>


    @FormUrlEncoded
    @POST("Home/vendor_status_update")
    fun ShopStatusDetails(
        @Header("Sessionid")  content_type:String?,
        @Field("type") type: String,
        @Field("option") option: String,
    ): Call<CashbackStatusModal>


    @FormUrlEncoded
    @POST("Home/shop_boost")
    fun ShopboostDetails(
        @Header("Sessionid")  content_type:String?,
        @Field("value") value: String,
        @Field("amount") amount: String,
    ): Call<Verify_otp_Response>



    @FormUrlEncoded
    @POST("Home/individual_product_details_edit")
    fun ProductDetailsEdit(
        @Header("Sessionid") content_type: String?,
        @Field("id") product_id: String,
        @Field("sale_price") sale_price: String,
        @Field("max_quantity") max_quantity: String,
        @Field("min_quanity") min_quantity: String,
        @Field("available_stock_count") available_stock_count: String,
        @Field("instant_delivery") instant_delivery: String,
        @Field("self_pickup") self_pickup: String,
        @Field("general_delivery") general_delivery: String,
        @Field("delivery_required_days") delivery_days: String,
        @Field("return") returnvalue: String,
        @Field("cod") cod: String,
        @Field("replacment") replacment: String,
        @Field("shop_exchange")shop_exchange: String,
        @Field("mrp_price") mrp_price: String,
    ): Call<Bankdetails_Response>





}
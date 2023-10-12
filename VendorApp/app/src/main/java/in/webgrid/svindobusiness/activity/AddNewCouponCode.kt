package `in`.webgrid.svindobusiness.activity


//@SuppressLint("StaticFieldLeak")
//lateinit var addNewCouponCodeBinding: ActivityAddNewCouponCodeBinding
//class AddNewCouponCode : AppCompatActivity() {
//    private lateinit var addcoupopnResponse:AddCouponModal
//    private lateinit var sharedPreference: SharedPreference
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        addNewCouponCodeBinding = ActivityAddNewCouponCodeBinding.inflate(layoutInflater)
//        setContentView(addNewCouponCodeBinding.root)
//
//          sharedPreference= SharedPreference(this)
//
//        val loginButton = findViewById<ImageView>(R.id.backbutton)
//        loginButton.setOnClickListener { this.onBackPressed()
//        }

//        addNewCouponCodeBinding.addcouponcodesubmitbutton.setOnClickListener {


//            val couponCode = addNewCouponCodeBinding.couponcode.text.toString().trim()
//            val minamount = addNewCouponCodeBinding.minamt.text.toString().trim()
//            val maximumamount = addNewCouponCodeBinding.maxamt.text.toString().trim()
//            val discountpercentage = addNewCouponCodeBinding.dicountamt.text.toString().trim()
//            val validitydays = addNewCouponCodeBinding.validitydays.text.toString().trim()
//            val description = addNewCouponCodeBinding.coupondescpt.text.toString().trim()
//
//            if (couponCode.isNotEmpty() && minamount.isNotEmpty() && maximumamount.isNotEmpty() && discountpercentage.isNotEmpty()  && description.isNotEmpty() && validitydays.isNotEmpty()){
//                AddCouponDetails(
//                 addNewCouponCodeBinding.couponcode.text.toString().trim(),
//                    addNewCouponCodeBinding.coupondescpt.text.toString().trim(),
//             addNewCouponCodeBinding.minamt.text.toString().trim(),
//                    addNewCouponCodeBinding.maxamt.text.toString().trim(),
//              addNewCouponCodeBinding.dicountamt.text.toString().trim(),
//             addNewCouponCodeBinding.validitydays.text.toString().trim(),
//                )
//            } else {
//                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
//            }
       // }
   // }
//    fun AddCouponDetails(
//        couponCode: String,
//        description: String,
//        minamount: String,
//        maximumamount: String,
//        discountpercentage: String,
//        validitydays: String,
//
//        ) {
//
//        val loginService = ApiClient.buildService(ApiInterface::class.java)
//        val requestCall = loginService.AddCoupondetails(sharedPreference.getValueString("token"),couponCode,description,minamount,maximumamount,discountpercentage,validitydays)
//
//        requestCall.enqueue(object : Callback<AddCouponModal>{
//            @SuppressLint("SuspiciousIndentation")
//            override fun onResponse(
//                call: Call<AddCouponModal>,
//                response: Response<AddCouponModal>
//            ) {
//                when {
//                    response.isSuccessful -> {//status code between 200 to 29
//                        addcoupopnResponse = response.body()!!
//                        if (addcoupopnResponse.error=="0") {
//                            val i = Intent(this@AddNewCouponCode,CouponsActivity::class.java)
//                            startActivity(i)
//                        } else{
//                            showToast(addcoupopnResponse.message)
//                        }
//                    }
//                    response.code() == 401 -> {//unauthorised
//                        showToast(getString(R.string.session_exp))
//                    }
//                    else -> {//Application-level failure
//                        //status code in the range of 300's, 400's, and 500's
//                        showToast(getString(R.string.server_error))
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<AddCouponModal>, t: Throwable) {
//                showToast(getString(R.string.session_exp))
//            }
//
//        })

  //  }

//
//}
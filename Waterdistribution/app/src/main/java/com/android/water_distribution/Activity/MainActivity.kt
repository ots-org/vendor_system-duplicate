package com.android.water_distribution.Activity

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import com.google.android.material.navigation.NavigationView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest

import com.android.water_distribution.R
import com.android.water_distribution.Utility.CommonFunctions
import com.android.water_distribution.Utility.NotificationUtils
import com.android.water_distribution.app.Config
import com.android.water_distribution.fragment.NavigationFragment
import com.android.water_distribution.fragment.NotificationFragment
import com.android.water_distribution.fragment.ProductsFragment
import com.android.water_distribution.service.UserService
import com.google.firebase.messaging.FirebaseMessaging
import locationprovider.davidserrano.com.LocationProvider
import org.json.JSONObject

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    internal lateinit var mToolbar: Toolbar
    internal lateinit var drawer: androidx.drawerlayout.widget.DrawerLayout
    internal lateinit var toggle: ActionBarDrawerToggle
    internal var action: ActionBar? = null
    private var className = ""
    internal lateinit var sideMenuLayoutId: RelativeLayout

    private var mRegistrationBroadcastReceiver: BroadcastReceiver? = null
    internal var fragmentManager: androidx.fragment.app.FragmentManager? = null
    internal var fragmentManagernav: androidx.fragment.app.FragmentManager? = null

    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    internal var presentHomeFrag = true
    internal var iSDrawerOpen = false

    internal var latS = "0"
    internal var lonS = "0"

    private val bothLocationsRequestCode = 132
    private val SETTINGS_ACTION = 123
    private lateinit var lProvider: LocationProvider
    var sharedPreferences : SharedPreferences? = null
    var toolbarTitle : TextView? = null

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        mToolbar = findViewById(R.id.toolbar)
        drawer = findViewById(R.id.drawer_layout)
        sideMenuLayoutId = findViewById(R.id.sideMenuLayoutId)

        setSupportActionBar(mToolbar)

        sideMenuLayoutId.layoutParams.width = (windowManager.defaultDisplay.width / 1.4).toInt()

        toggle = ActionBarDrawerToggle(this, drawer, mToolbar, R.string.app_name, R.string.app_name)
        drawer.addDrawerListener(toggle)

        // Ask the user for permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permission = ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)

            if (permission != PackageManager.PERMISSION_GRANTED) {
                // permission denied, boo!

                requestPermissions(
                        PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE
                )
            }

        }

        sharedPreferences = applicationContext.getSharedPreferences("water_management", 0)
        val sharedPreferencesFCM = applicationContext.getSharedPreferences(Config.SHARED_PREF, 0)

        if (sharedPreferencesFCM.contains("regId")) {
            CommonFunctions.appendLog("\n\r\n FCM user Registration Token : " + sharedPreferencesFCM.getString("regId", "") + "\n")
        }


        if (supportActionBar != null) {
            action = supportActionBar
            action!!.setDisplayHomeAsUpEnabled(true)
            action!!.setHomeButtonEnabled(true)
            action!!.setDisplayShowTitleEnabled(false)
            action!!.setDisplayShowCustomEnabled(true)
            action!!.setHomeAsUpIndicator(R.mipmap.ic_hamburger)

            val viewActionBar = layoutInflater.inflate(R.layout.view_custom_toolbar, null)
            val params = ActionBar.LayoutParams(//Center the textview in the ActionBar !
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER)
            toolbarTitle = viewActionBar.findViewById<View>(R.id.toolbar_title) as TextView
            if (sharedPreferences?.contains("userRoleId")!! && sharedPreferences?.getString("userRoleId", "")!!.equals("1", ignoreCase = true)){
                toolbarTitle?.text = "Notifications"
            }
            else {
                toolbarTitle?.text = "Products"
            }
            action!!.setCustomView(viewActionBar, params)
            mToolbar.navigationIcon!!.setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_ATOP)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        }

        fragmentManager = supportFragmentManager
        fragmentManagernav = supportFragmentManager

        /*if (sharedPreferences.contains("userRoleId") && sharedPreferences.getString("userRoleId","").equalsIgnoreCase("1")){
            menuItemVisibility(true);
        }
        else {
            menuItemVisibility(false);
        }*/
        val actionBarDrawerToggle = object : ActionBarDrawerToggle(
                this,
                drawer,
                mToolbar,
                R.string.open,
                R.string.close
        ) {

            /**
             * Called when a drawer has settled in a completely closed
             * state.
             */
            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                iSDrawerOpen = false
                invalidateOptionsMenu()
                syncState()
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                iSDrawerOpen = true
                invalidateOptionsMenu()
                syncState()
            }

            override fun onDrawerStateChanged(newState: Int) {
                super.onDrawerStateChanged(newState)
            }

        }
        drawer.setDrawerListener(actionBarDrawerToggle)

        actionBarDrawerToggle.drawerArrowDrawable.color = resources.getColor(R.color.white)

        if (sharedPreferences?.contains("userRoleId")!! && sharedPreferences?.getString("userRoleId", "")!!.equals("1", ignoreCase = true)){
         displayView(1)
        }
        else {
            displayView(0)
        }
        changeNAVFragment(NavigationFragment.newInstance(), false)

        mRegistrationBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {

                // checking for type intent filter
                if (intent.action == Config.REGISTRATION_COMPLETE) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL)

                    //displayFirebaseRegId();

                } else if (intent.action == Config.PUSH_NOTIFICATION) {
                    // new push notification is received

                    val message = intent.getStringExtra("message")

                    Toast.makeText(applicationContext, "Push notification: $message", Toast.LENGTH_LONG).show()

                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkLocationPermission()) {
                startLocationUpdates()
            }
        } else {
            startLocationUpdates()
        }

        if (ContextCompat.checkSelfPermission(this@MainActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION)
        } else {
            permissionGranted()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            startForegroundService(Intent(this, UserService::class.java))
        }
        else {
            startService(Intent(this, UserService::class.java))
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the
                //showFileChooser();
            }
        }
        else if (requestCode == bothLocationsRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates()
                    permissionGranted()
                }
            } else {
                showExplanation()
            }
            return
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }

    }

    override fun onResume() {
        super.onResume()

        // register GCM registration complete receiver
        androidx.localbroadcastmanager.content.LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver!!,
                IntentFilter(Config.REGISTRATION_COMPLETE))

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        androidx.localbroadcastmanager.content.LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver!!,
                IntentFilter(Config.PUSH_NOTIFICATION))

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(applicationContext)
    }

    override fun onPause() {
        androidx.localbroadcastmanager.content.LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver!!)
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (sharedPreferences?.contains("userRoleId")!! && !sharedPreferences?.getString("userRoleId", "")!!.equals("1", ignoreCase = true)) {
            menuInflater.inflate(R.menu.content_main, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // Respond to the action bar's Up/Home button
            R.id.cart -> {
                val intent = Intent(this@MainActivity, CardListActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun menuItemVisibility(visible: Boolean) {
        /*Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_item_add_user).setVisible(visible);
        nav_Menu.findItem(R.id.nav_item_add_product).setVisible(visible);*/
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return true
    }

    /*@Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/

    public fun loadNotificationsScreen(){
        toolbarTitle?.setText("Notifications")
        displayView(1)
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    private fun displayView(position: Int) {
        // update the main content by replacing fragments
        var fragment: androidx.fragment.app.Fragment? = null
        // Clear all back stack.
        val backStackCount = supportFragmentManager.backStackEntryCount
        for (i in 0 until backStackCount) {
            val backStackId = supportFragmentManager.getBackStackEntryAt(i).id
            //getSupportFragmentManager().popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        when (position) {

            0 -> {
                fragment = ProductsFragment.newInstance()
                className = ProductsFragment::class.java.simpleName
            }

             1-> {
                 fragment = NotificationFragment.newInstance()
                 className = NotificationFragment::class.java.simpleName
             }

            /*case 2:
                fragment = FrgTarget.newInstance();
                className=FrgTarget.class.getSimpleName();
                break;

            case 3:
                fragment = FrgSchedules.newInstance();
                className=FrgSchedules.class.getSimpleName();
                break;

            case 4:
                fragment = FrgEditPatientBasicProfile.newInstance();
                className=FrgEditPatientBasicProfile.class.getSimpleName();
                break;*/

            else -> {
            }
        }

        fragment?.let {
            changeFragment(it)

                    /*getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment, className)
                    .addToBackStack(className).commitAllowingStateLoss();*/
        } ?: // error in creating fragment
        Log.e("MainActivity", "Error in creating fragment")
    }

    fun changeFragment(fragment: androidx.fragment.app.Fragment) {

        if (fragmentManager != null && fragment.javaClass.simpleName != "FragmentProductDetails")
            fragmentManager!!.beginTransaction()
                    .replace(R.id.container, fragment, fragment.javaClass.simpleName)
                    .addToBackStack(fragment.javaClass.simpleName).commitAllowingStateLoss()
        if (fragment.javaClass.simpleName.equals(ProductsFragment::class.java.simpleName, ignoreCase = true)) {
            presentHomeFrag = true
        }
        else if (fragment.javaClass.simpleName.equals(NotificationFragment::class.java.simpleName, ignoreCase = true) && sharedPreferences?.contains("userRoleId")!! && sharedPreferences?.getString("userRoleId", "")!!.equals("1", ignoreCase = true)) {
            presentHomeFrag = true
        }
        else {
            presentHomeFrag = false
        }
        hereClose()
    }

    fun closeDrawer() {
        Handler().postDelayed({ hereClose() }, 1200)

    }

    internal fun hereClose() {
        drawer.closeDrawer(Gravity.LEFT)
    }

    override fun onBackPressed() {

        if (iSDrawerOpen) {

            navBackPress()

        } else {
            if (fragmentManager!!.getBackStackEntryAt(fragmentManager!!.backStackEntryCount - 1).name!!.equals(ProductsFragment::class.java.simpleName, ignoreCase = true) || presentHomeFrag) {

                finish()

            } else {

                fragmentManager!!.popBackStack()


            }
        }
    }

    fun navBackPress() {

        Log.d("drawerOpen", "$iSDrawerOpen//")

        if (iSDrawerOpen) {

            hereClose()
        }
    }


        fun changeNAVFragment(fragment: androidx.fragment.app.Fragment, child: Boolean) {
            if (fragmentManagernav != null) {

                if (child) {
                    fragmentManagernav!!.beginTransaction()/*.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)*/
                            .replace(R.id.navContainer, fragment, fragment.javaClass.simpleName)
                            .commitAllowingStateLoss()
                } else {
                    fragmentManagernav!!.beginTransaction()
                            .replace(R.id.navContainer, fragment, fragment.javaClass.simpleName)
                            .commitAllowingStateLoss()
                }
            }
        }


    internal fun permissionGranted() {

                getMemberOrders()
                //handler.postDelayed(runnable,30000);
                /*}else if (handler!=null){
                    startStop.setText("Start");
                    handler.removeCallbacks(runnable);*/
    }

    private fun startLocationUpdates() {
        val callback = object : LocationProvider.LocationCallback {
            override fun locationRequestStopped() {
                printOutput("Stopped requesting location")
                //enableLocationButton()
            }

            override fun onNewLocationAvailable(latitude: Float, longitude: Float) {
                printOutput("New location available - Lat: $latitude / Lon: $longitude")
                latS = latitude.toString()
                lonS = longitude.toString()
                Log.d("loc", "lat:$latS/lon:$lonS")
            }

            override fun locationServicesNotEnabled() {
                printOutput("Location services are not enabled - please turn them on and try again")
                //enableLocationButton()
            }

            override fun updateLocationInBackground(latitude: Float, longitude: Float) {
                printOutput("Location updated in background - Lat: $latitude / Lon: $longitude")
                latS = latitude.toString()
                lonS = longitude.toString()
                Log.d("loc", "lat:$latS/lon:$lonS")
            }

            override fun networkListenerInitialised() {
                printOutput("Network listener started")
            }
        }

        lProvider = LocationProvider.Builder()
                .setContext(this)
                .setListener(callback)
                .create()

        lifecycle.addObserver(lProvider)

        //disableLocationButton()

        printOutput("\nStarting location updates...")
        lProvider.requestLocation()
    }

    private fun printOutput(s: String) {
        Log.d("Update",s);
    }

    private fun checkLocationPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                showExplanation()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), bothLocationsRequestCode)
            }
            false
        } else {
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SETTINGS_ACTION && resultCode == Activity.RESULT_OK) {
            startLocationUpdates()
        }
    }

    private fun showExplanation() {
        AlertDialog.Builder(this)
                .setTitle("GPS alert")
                .setMessage("Enable GPS")
                .setPositiveButton("OK") { dialogInterface, i ->
                    dialogInterface.dismiss()
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivityForResult(intent, SETTINGS_ACTION)
                }
                .setNegativeButton("Cancel") { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
                .show()
    }

    internal fun getMemberOrders() {
        /*showProgress("Loading..")
        val jsonStr = "{\"RequestHeader\": {\"AppInstanceID\": \"1.0.0.1.B\",\"SourceID\": \"INSTAG\"},\"RequestData\": {\n" +
                "\"MemberID\": \"" + memId + "\"," +
                "\"Latitude\": \"" + latS + "\"," +
                "\"Longitude\": \"" + lonS + "\"" +
                "  }}"

        var jsonObject: JSONObject? = null

        try {
            jsonObject = JSONObject(jsonStr)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        Log.d("jsonstr", jsonStr)

        val url = "http://uatapi.shopmygrocery.com/Service1.svc/json/INSTAG/InsertMemberCurrentLocation"
        val jsonObjReq = JsonObjectRequest(Request.Method.POST,
                url, jsonObject, Response.Listener { response ->
            Log.d("response Cart Json>", response!!.toString())
            dismissProgress()
            if (response != null) {

            }
        }, Response.ErrorListener { error ->
            dismissProgress()
            if (error != null && error.message != null) {
                Toast.makeText(this@MainActivity,
                        error.message, Toast.LENGTH_SHORT).show()
            }
            // hide the progress dialog
        })

        jsonObjReq.setShouldCache(false)

        // Adding request to request queue
        MyAppController.getInstance().addToRequestQueue(jsonObjReq)*/
    }

    companion object {

        val MY_PERMISSIONS_REQUEST_LOCATION = 99
    }

}

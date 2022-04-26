package com.example.taxibookinguserapplication.Map

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taxibookinguserapplication.Login.LoginActivity
import com.example.taxibookinguserapplication.Main.PrivacyPolicyActivity
import com.example.taxibookinguserapplication.Main.Term_ConditionActivity
import com.example.taxibookinguserapplication.Main.TripHistory
import com.example.taxibookinguserapplication.Main.ViewProfile
import com.example.taxibookinguserapplication.Map.Adapter.NavigationRVAdapter
import com.example.taxibookinguserapplication.Map.Fragemnets.Manual_PickUp_Fragment
import com.example.taxibookinguserapplication.Map.Fragemnets.PickupFragments
import com.example.taxibookinguserapplication.Map.Model.ClickListener
import com.example.taxibookinguserapplication.Map.Model.NavigationItemModel
import com.example.taxibookinguserapplication.Map.Model.RecyclerTouchListener
import com.example.taxibookinguserapplication.R
import com.example.taxibookinguserapplication.util.SharedPreferenceUtils
import com.rehablab.util.ConstantUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_manual_pick_up.*
import kotlinx.android.synthetic.main.activity_pick_up.*

class Manual_Pick_up : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    private lateinit var adapter: NavigationRVAdapter
    lateinit var navigation_rv: RecyclerView
    lateinit var ivClose1: ImageView
    var Image_Url:String=""


    private var items = arrayListOf(
        NavigationItemModel(R.drawable.home, "Account"),
        NavigationItemModel(R.drawable.wallet, "Wallet"),
        NavigationItemModel(R.drawable.trips, "Trips"),
        NavigationItemModel(R.drawable.tc, "Terms & Conditions"),
        NavigationItemModel(R.drawable.privacy, "Privacy Policy")
        // NavigationItemModel(R.drawable.home, "Profile"),
        // NavigationItemModel(R.drawable.back, "Like us on facebook")
    )
    private var items1 = arrayListOf(
        NavigationItemModel(R.drawable.home, "Account"),
        NavigationItemModel(R.drawable.wallet, "Wallet"),
        NavigationItemModel(R.drawable.trips, "Trips"),
        NavigationItemModel(R.drawable.tc, "Terms & Conditions"),
        NavigationItemModel(R.drawable.privacy, "Privacy Policy")
        // NavigationItemModel(R.drawable.home, "Profile"),
        // NavigationItemModel(R.drawable.back, "Like us on facebook")
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual_pick_up)
        drawerLayout = findViewById(R.id.drawer_layout_manual)
        navigation_rv=findViewById(R.id.navigation_rv1_manual)
        var ivMenu=findViewById<ImageView>(R.id.ivMenu1_manual)
        ivClose1=findViewById(R.id.ivClose_manual)

        Image_Url=
            SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.Image_Url,"").toString()

        if (Image_Url.equals(""))
        {
            val  picasso= Picasso.get()
            picasso.load(R.drawable.driverimg).into(navigation_user_pic_manual)
        }
        else
        {
            val  picasso= Picasso.get()
            picasso.load(Image_Url).into(navigation_user_pic_manual)
        }

        Logout_Linear_Layout_manual.setOnClickListener {
            SharedPreferenceUtils.getInstance(this)?.clear()
            val intent= Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val bundle = Bundle()
        bundle.putString("fragmentName", "Settings Fragment")
        /* bundle.putString("Location",locat)
         bundle.putString("Late",latii)
         bundle.putString("Long",lan)*/
        val settingsFragment = Manual_PickUp_Fragment()
        settingsFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_main_content_id_manual, settingsFragment).commit()


        getSupportActionBar()?.setDisplayShowTitleEnabled(false);
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(false);


        navigation_rv.layoutManager = LinearLayoutManager(this)
        navigation_rv.setHasFixedSize(true)
        ivMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }



        navigation_rv.addOnItemTouchListener(RecyclerTouchListener(this, object : ClickListener {
            override fun onClick(view: View, position: Int) {
                when (position) {
                    0 -> {
                        val intent = Intent(this@Manual_Pick_up, ViewProfile::class.java)
                        startActivity(intent)

                    }
                    1 -> {
                        //  drawerLayout.closeDrawer(GravityCompat.START)
                        /*val intent = Intent(this@Pick_up, TripDetails::class.java)
                        startActivity(intent)*/
                    }
                    2 -> {
                        val intent = Intent(this@Manual_Pick_up, TripHistory::class.java)
                        startActivity(intent)
                    }
                    3 -> {
                        val intent = Intent(this@Manual_Pick_up, Term_ConditionActivity::class.java)
                        startActivity(intent)
                        // # Books Fragment

                    }
                    4 -> {
                        // # Profile Activity
                        val intent = Intent(this@Manual_Pick_up, PrivacyPolicyActivity::class.java)
                        startActivity(intent)
                    }
                    5 -> {
                        drawerLayout.closeDrawer(GravityCompat.START)


                    }
                    6 -> {


                    }
                }

                updateAdapter(position)
                if (position != 6 && position != 4) {

                }
                Handler().postDelayed({
                    drawerLayout.closeDrawer(GravityCompat.START)
                }, 200)
            }
        }))


        updateAdapter(0)





        val toggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ) {
            override fun onDrawerClosed(drawerView: View) {

                super.onDrawerClosed(drawerView)
                try {
                    val inputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                } catch (e: Exception) {
                    e.stackTrace
                }
            }

            override fun onDrawerOpened(drawerView: View) {

                super.onDrawerOpened(drawerView)
                try {
                    val inputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
                } catch (e: Exception) {
                    e.stackTrace
                }
            }
        }
        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()



    }

    private fun updateAdapter(highlightItemPos: Int) {
        adapter = NavigationRVAdapter(items,items1, highlightItemPos)
        navigation_rv.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {

            if (supportFragmentManager.backStackEntryCount > 0) {
                // Go to the previous fragment
                supportFragmentManager.popBackStack()
            } else {
                // Exit the app
                super.onBackPressed()
            }
        }
    }

    fun click1(){
        drawerLayout.openDrawer(GravityCompat.START)
    }


}
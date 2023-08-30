package com.uc.weacare2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home_navigation.*

class HomeNavigation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_navigation)

        bottomnavigti.setOnNavigationItemSelectedListener(onBottomNavListener)
        bottomnavigti.selectedItemId = R.id.weatherFragment;

        var fr = supportFragmentManager.beginTransaction()
        fr.add(R.id.v_fragment, HomeFragment())
        fr.commit()
    }

    private val onBottomNavListener = BottomNavigationView.OnNavigationItemSelectedListener { i ->
        var selectFr: Fragment = HomeFragment()

        when(i.itemId){
            R.id.donateFragment -> {
                selectFr = Donation1()
            }
            R.id.weatherFragment -> {
                selectFr = HomeFragment()
            }
            R.id.profileFragment -> {
                selectFr = ProfileActivity()
            }
        }
        var fr = supportFragmentManager.beginTransaction()
        fr.replace(R.id.v_fragment, selectFr)
        fr.commit()
        true
    }
}
package com.tencent.wx.livestream.hsbcgithubapp.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tencent.wx.livestream.hsbcgithubapp.R

/**
 * The one and only activity in the app, serve as the host for fragments.
 * The App contains three screens: Search, Profile and Login
 * User navigates between Search and Profile screen in the bottom navigation bar
 * If User is not logged in, he/she can go to Login screen via Profile screen
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment
        ) as NavHostFragment
        val navController = navHostFragment.navController

        // Setup the bottom navigation view with navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setupWithNavController(navController)

        requestStoragePermission()
    }

    private fun requestStoragePermission() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED)) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                    val builder: AlertDialog.Builder? = this.let {
                        AlertDialog.Builder(it)
                    }
                    builder?.setMessage("确定不让App访问你的手机存储吗？")
                    builder?.setPositiveButton("可以访问") { _, _ ->
                        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
                    }
                    builder?.create()?.show()
                }
            }
        }
    }
}

package ca.blogspot.johnchenprogramming.whoami

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.transition.Fade
import android.transition.Transition



class Activity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_2)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        getWindow().setExitTransition(null);
        val fade = Fade()
        fade.excludeTarget(android.R.id.navigationBarBackground, true)
        fade.excludeTarget(android.R.id.statusBarBackground, true)
        fade.excludeTarget(R.id.activity_2, true)
        /*fade.excludeTarget(R.id.fe_appbar_layout, true);*/
        window.enterTransition = fade

    }

    override fun onBackPressed() {
        super.onBackPressed()
        //overridePendingTransition(0, 0)
    }
}

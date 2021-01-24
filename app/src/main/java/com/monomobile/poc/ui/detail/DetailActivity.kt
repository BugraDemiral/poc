package com.monomobile.poc.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.monomobile.poc.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener {
            finish()
        }

        intent.extras?.let {
            Picasso.get().load(it.getString("img")).into(imageView2)
            textViewName.text = it.getString("name")
            textViewNickname.text = it.getString("nickname")
            textViewSeasonAppearance.text = it.getString("appearance")
            textViewOccupation.text = it.getString("occupation")
            textViewStatus.text = it.getString("status")
        }
    }
}
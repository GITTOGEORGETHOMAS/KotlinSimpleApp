package com.example.mysampleapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = Runnable {
        val nextItem = (viewPager.currentItem + 1) % (viewPager.adapter?.itemCount ?: 1)
        viewPager.setCurrentItem(nextItem, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        viewPager = findViewById(R.id.viewPager)

        val fragments: ArrayList<Fragment> = arrayListOf(
            OnboardingFragment1(),
            OnboardingFragment2(),
            OnboardingFragment3()
        )

        val adapter = OnboardingPagerAdapter(this, fragments)
        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                startAutoSlide()

                if (position == (viewPager.adapter?.itemCount ?: 1) - 1) {
                    navigateToSignIn()
                }
            }
        })

        startAutoSlide()
    }

    private fun startAutoSlide() {
        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, 5000)
    }

    private fun stopAutoSlide() {
        handler.removeCallbacks(runnable)
    }

    override fun onPause() {
        super.onPause()
        stopAutoSlide()
    }

    override fun onResume() {
        super.onResume()
        startAutoSlide()
    }

    private fun navigateToSignIn() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

    private inner class OnboardingPagerAdapter(
        fa: AppCompatActivity,
        private val fragments: ArrayList<Fragment>
    ) : FragmentStateAdapter(fa) {

        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment = fragments[position]
    }
}

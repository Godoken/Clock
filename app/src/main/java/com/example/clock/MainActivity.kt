package com.example.clock

import android.animation.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnTouchListener, View.OnClickListener {
    private var liveMove: MutableLiveData<Boolean> = MutableLiveData()
    private var rotation: Float = 0.0f

    lateinit var animator: Animator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hand_hour.setOnTouchListener(this)
        hand_minute.setOnTouchListener(this)

        submit_button.setOnClickListener(this)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        v.performClick()

        val set = AnimatorSet()

        liveMove.observe(this, Observer<Boolean> {
            rotation = (v.rotation + 30.0f).minus((v.rotation + 30.0f).rem(30))
            animator = ObjectAnimator.ofFloat(v, View.ROTATION, v.rotation, rotation)
            animator.duration = 100
            set.play(animator)
            set.start()
        })

        when (event.action) {
            MotionEvent.ACTION_DOWN -> { }
            MotionEvent.ACTION_MOVE -> { liveMove.value = true}
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> { }
        }
        liveMove.removeObservers(this)
        return true
    }

    override fun onClick(v: View) {

    }

}

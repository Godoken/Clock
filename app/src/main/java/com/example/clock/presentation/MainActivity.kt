package com.example.clock.presentation

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.clock.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnTouchListener, View.OnClickListener,
    InterfaceView {

    private var liveMove: MutableLiveData<Boolean> = MutableLiveData()

    lateinit var animator: Animator

    var rotationHour: Float = 0.0f
    var rotationMinute: Float = 0.0f

    private lateinit var presenter: Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = PresenterFactory.createPresenter()

        hand_hour.setOnTouchListener(this)
        hand_minute.setOnTouchListener(this)

        submit_button.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.detachView()
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        v.performClick()
        when (v.id) {
            hand_hour.id -> rotationHour = animate(v, event)
            hand_minute.id -> rotationMinute = animate(v, event)
        }
        return true
    }

    private fun animate(v: View, event: MotionEvent) : Float {
        var rotation = 0.0f
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
        return rotation
    }

    override fun onClick(v: View) {
        presenter.isSubmit(time_text.text.toString(), rotationHour, rotationMinute).observe(this, Observer <Boolean> {
            if (it){
                message_text.text = getString(R.string.success_message)
                presenter.beginNewTask().observe(this, Observer<String> {
                    time_text.text = it
                    message_text.text = getString(R.string.task_message)
                })

            } else {
                message_text.text = getString(R.string.failure_message)
            }
        })
    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }
}

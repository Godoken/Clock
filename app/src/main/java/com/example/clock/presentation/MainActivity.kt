package com.example.clock.presentation

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.clock.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnTouchListener, View.OnClickListener,
    InterfaceView {

    private var liveMove: MutableLiveData<Boolean> = MutableLiveData()

    var rotationHour: Float = 0.0f
    var rotationMinute: Float = 0.0f

    private var previousX: Float = 0.0f
    private var previousY: Float = 0.0f

    var rotation: Int = 0

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
        var localRotation = 0.0f

        liveMove.observe(this, Observer<Boolean> {
            localRotation = (v.rotation + rotation).minus((v.rotation + rotation).rem(30))

            ViewCompat.animate(v)
                .setDuration(110)
                .rotation(localRotation)
                .start()
        })

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                previousX = event.x
                previousY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                if (rotation == 0) {
                    rotation = presenter.chooseKindRotation(event.x, event.y, previousX, previousY)
                    previousX = event.x
                    previousY = event.y
                }
                liveMove.value = true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                rotation = 0
            }
        }
        liveMove.removeObservers(this)
        return localRotation
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

        clock.visibility = View.GONE
        hand_hour.visibility = View.GONE
        hand_minute.visibility = View.GONE
        time_text.visibility = View.GONE
        submit_button.visibility = View.GONE
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE

        clock.visibility = View.VISIBLE
        hand_hour.visibility = View.VISIBLE
        hand_minute.visibility = View.VISIBLE
        time_text.visibility = View.VISIBLE
        submit_button.visibility = View.VISIBLE
    }
}

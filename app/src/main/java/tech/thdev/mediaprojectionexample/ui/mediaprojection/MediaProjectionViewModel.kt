package tech.thdev.mediaprojectionexample.ui.mediaprojection

import android.os.Message
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tech.thdev.media_projection_library.constant.MediaProjectionStatus

class MediaProjectionViewModel : ViewModel() {

    private var isStarted: Boolean = false

    private val _init = MutableLiveData<Unit>()
    val init: LiveData<Unit> get() = _init
    private val _stop = MutableLiveData<Unit>()
    val stop: LiveData<Unit> get() = _stop

    private val _statusInitialize = MutableLiveData<Unit>()
    val statusInitialize: LiveData<Unit> get() = _statusInitialize
    private val _statusStarted = MutableLiveData<Unit>()
    val statusStarted: LiveData<Unit> get() = _statusStarted
    private val _statusStop = MutableLiveData<Unit>()
    val statusStop: LiveData<Unit> get() = _statusStop
    private val _statusFail = MutableLiveData<Unit>()
    val statusFail: LiveData<Unit> get() = _statusFail
    private val _statusReject = MutableLiveData<Unit>()
    val statusReject: LiveData<Unit> get() = _statusReject

    private val _message = MutableLiveData<Message>()
    val message: LiveData<Message> get() = _message

    fun restartMediaProjection() {
        if (isStarted.not()) {
            _init.value = Unit
        } else {
            _stop.value = Unit
        }
    }

    private fun sendMessage(status: MediaProjectionStatus) {
        _message.value = Message.obtain().also { it.obj = status }
    }

    fun onMediaProjectionListener(status: MediaProjectionStatus) {
        when (status) {
            MediaProjectionStatus.OnInitialize -> {
                _statusInitialize.value = Unit
            }
            MediaProjectionStatus.OnStarted -> {
                isStarted = true
                _statusStarted.value = Unit
            }
            MediaProjectionStatus.OnStop -> {
                isStarted = false
                _statusStop.value = Unit
            }
            MediaProjectionStatus.OnFail -> {
                isStarted = false
                _statusFail.value = Unit
            }
            MediaProjectionStatus.OnReject -> {
                isStarted = false
                _statusReject.value = Unit
            }
        }
        sendMessage(status)
    }
}
package com.mobiai.app.utils

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

open class BaseEvent

class ShowTutorialEvents() : BaseEvent()
class ReloadAllGallery() : BaseEvent()

fun listenEvent(
    onSuccess: (e: BaseEvent) -> Unit,
    onError: (th: Throwable) -> Unit = {}
): Disposable {
    return RxBus.events(BaseEvent::class.java)
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            onSuccess(it)
        }, {
            onError(it)
        })
}

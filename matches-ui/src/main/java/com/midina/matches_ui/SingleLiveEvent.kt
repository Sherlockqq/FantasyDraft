package com.midina.matches_ui

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveEvent<T> : MutableLiveData<T>() {

    private val pending = AtomicBoolean(false)
    private val observers = mutableSetOf<Observer<in T>>()

    private val internalObserver = Observer<T>{ t ->
        if(pending.compareAndSet(true,false)){
            observers.forEach { observer ->
                observer.onChanged(t)
            }
        }
    }

    @MainThread
    override fun observe(owner : LifecycleOwner, observer: Observer<in T>){

        //Observe the internal MutableLiveData
        super.observe(owner, Observer { t ->
            if(pending.compareAndSet(true,false)){
                observer.onChanged(t)
            }
        })

        //get all Observers
        observers.add(observer)

        if(!hasObservers()){
            super.observe(owner, internalObserver)
        }else{
            Log.i("TAG","observe: Multiple observers registered " +
                    "but only one will be notified of changed")
        }
    }

    override fun removeObservers(owner: LifecycleOwner) {
        observers.clear()
        super.removeObservers(owner)
    }

    override fun removeObserver(observer: Observer<in T>) {
        observers.remove(observer)
        super.removeObserver(observer)
    }

    @MainThread
    override fun setValue(value: T?) {
        pending.set(true)
        super.setValue(value)
    }

}
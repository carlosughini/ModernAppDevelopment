package com.example.carlo.mordenappdevelopment.ui.view

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import com.example.carlo.mordenappdevelopment.androidmanagers.NetManager
import com.example.carlo.mordenappdevelopment.data.GitRepoRepository
import com.example.carlo.mordenappdevelopment.data.OnRepositoryReadyCallback
import com.example.carlo.mordenappdevelopment.extensions.plusAssign
import com.example.carlo.mordenappdevelopment.ui.uimodels.Repository
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlin.collections.ArrayList

class MainViewModel(application: Application) : AndroidViewModel(application) {

    var gitRepoRepository: GitRepoRepository = GitRepoRepository(NetManager(getApplication()))
    val text = ObservableField("old Data")
    val isLoading = ObservableField(false)
    private val compositeDisposable = CompositeDisposable()

    var repositories = MutableLiveData<ArrayList<Repository>>()
    lateinit var disposable: Disposable

    fun loadRepositories() {
        isLoading.set(true)
        compositeDisposable += gitRepoRepository
            .getRepositories()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith<DisposableObserver<ArrayList<Repository>>>(object : DisposableObserver<ArrayList<Repository>>(){


            override fun onNext(data: ArrayList<Repository>) {
                repositories.value = data
            }

            override fun onComplete() {
                isLoading.set(false)
            }

            override fun onError(e: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }
}
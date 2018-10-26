package com.example.carlo.mordenappdevelopment.data

import android.content.Context
import android.os.Handler
import com.example.carlo.mordenappdevelopment.androidmanagers.NetManager
import com.example.carlo.mordenappdevelopment.ui.uimodels.Repository
import io.reactivex.Observable

class GitRepoRepository(val netManager: NetManager) {

    val localDataSource = GitRepoLocalDataSource()
    val remoteDataSource = GitRepoRemoteDataSource()

    fun getRepositories() : Observable<ArrayList<Repository>> {

        netManager.isConnectedToInternet?.let {
            if (it) {
                return remoteDataSource.getRepositories().flatMap {
                    return@flatMap localDataSource.saveRepositories(it)
                        .toSingleDefault(it)
                        .toObservable()
                }
            }
        }

        return localDataSource.getRepositories()

    }
}

interface OnRepositoryReadyCallback {
    fun onDataReady(data: ArrayList<Repository>)
}
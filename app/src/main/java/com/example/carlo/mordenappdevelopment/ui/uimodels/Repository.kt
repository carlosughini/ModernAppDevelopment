package com.example.carlo.mordenappdevelopment.ui.uimodels

import android.databinding.BaseObservable
import android.databinding.Bindable

class Repository(repositoryName: String?,
                 var repositoryOwner: String?,
                 var numberOfStars: Int? ,
                 var hasIssues: Boolean = false) : BaseObservable() {

    @Bindable
    var repositoryName : String = ""
    set (value) {
        field = value
        notifyChange()
    }

}
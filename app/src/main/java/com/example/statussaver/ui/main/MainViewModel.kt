package com.example.statussaver.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ithebk.statussaver.BuildConfig
import com.example.statussaver.data.STATUS_TYPE
import com.example.statussaver.data.Status
import java.io.File

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext
    private val _statusList = MutableLiveData<List<Status>>()
    val statusList: LiveData<List<Status>>
        get() = _statusList;
    fun init() {
        val mainPath: String? = context.getExternalFilesDir(null)?.absolutePath
        if (mainPath != null) {
            val extraPortion = ("Android/data/" + BuildConfig.APPLICATION_ID
                    + File.separator + "files")
            val validPath = mainPath.replace(extraPortion, "")
            val statusPath = validPath + "WhatsApp/Media/.Statuses"
            val validFile = File(statusPath)
            if (validFile.listFiles() != null) {
                val files: MutableList<File> = validFile.listFiles().toMutableList()
                //files.filter { it.extension == "jpg" }
                files.sortByDescending { it.lastModified() }
                val statusList: MutableList<Status> = mutableListOf()
                files.iterator().forEach {
                    var extension: STATUS_TYPE? = null;
                    if (it.extension == "jpg") {
                        extension = STATUS_TYPE.IMAGE;
                    } else if (it.extension == "mp4") {
                        extension = STATUS_TYPE.VIDEO
                    }
                    if (extension != null) {
                        statusList.add(
                            Status(
                                it.absolutePath,
                                extension
                            )
                        );
                    }

                }
                _statusList.postValue(statusList);
            }
            else {
                println("Failing")
            }
        }
    }
}
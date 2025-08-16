package com.tayler.playvalu.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.tayler.playvalu.model.MusicModel
import java.io.File
import java.util.concurrent.TimeUnit

@SuppressLint("DefaultLocale")
fun formatTimePlayer(time :Int): String{
    val timeFinal = java.lang.String.format("%02d:%02d ", TimeUnit.MILLISECONDS.toMinutes(time.toLong()),
        TimeUnit.MILLISECONDS.toSeconds(time.toLong()) - TimeUnit.MINUTES.toSeconds(
            TimeUnit.MILLISECONDS.toMinutes(
                time.toLong()))
    )
    return timeFinal
}

fun validateApiAndroidR() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R

fun getFileMusicDeprecated():ArrayList<MusicModel>{
    val listFilter : ArrayList<MusicModel> = ArrayList()
    val songs = getMusic(Environment.getExternalStorageDirectory())
    for (item in songs){
        listFilter.add(MusicModel(name = item.name,path = item.path))
    }
    return  listFilter
}

fun getMusic(root: File): ArrayList<File> {
    val filesMusic: ArrayList<File> = ArrayList()
    val files = root.listFiles()
    files?.let {
        for (item in it) {
            if (item.isDirectory && !item.isHidden) {
                filesMusic.addAll(getMusic(item))
            } else {
                if (item.name.endsWith(".mp3")) {
                    filesMusic.add(item)
                }
            }
        }
    }
    return filesMusic
}

fun Context.getFileMusic():ArrayList<MusicModel>{
    val listFilter : ArrayList<MusicModel> = ArrayList()
    val resolver: ContentResolver = contentResolver
    val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.DATA,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST
    )
    val cursor: Cursor? = resolver.query(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        projection,
        null,
        null,
        null
    )
    cursor?.use {
        val dataColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
        val titleColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
        while (it.moveToNext()) {
            val data = it.getString(dataColumn)
            val title = it.getString(titleColumn)
            if (data.endsWith(".mp3")){
                listFilter.add(MusicModel(name = title,path = data))
            }
        }
    }

    cursor?.close()
    return listFilter
}
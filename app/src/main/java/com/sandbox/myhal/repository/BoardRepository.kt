package com.sandbox.myhal.repository

import android.net.Uri
import com.sandbox.myhal.activities.BaseActivity
import com.sandbox.myhal.activities.BoardActivity
import com.sandbox.myhal.activities.CreateBoardActivity
import com.sandbox.myhal.activities.TaskListActivity
import com.sandbox.myhal.models.Board

interface BoardRepository {

    fun createBoard(activity: CreateBoardActivity, board: Board)
    fun uploadBoardImage(activity: CreateBoardActivity, fileUri: Uri?)
    fun getBoardsList(activity: BoardActivity, customerId: String)
    fun getBoardDetails(activity: TaskListActivity, documentId: String)
    fun addUpdateTaskList(activity: TaskListActivity, board: Board )

}
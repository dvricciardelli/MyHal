package com.sandbox.myhal.repository

import android.app.Activity
import android.net.Uri
import com.sandbox.myhal.activities.*
import com.sandbox.myhal.models.Board
import com.sandbox.myhal.models.User

interface BoardRepository {

    fun createBoard(activity: CreateBoardActivity, board: Board)
    fun uploadBoardImage(activity: CreateBoardActivity, fileUri: Uri?)
    fun getBoardsList(activity: BoardActivity, customerId: String)
    fun getBoardDetails(activity: TaskListActivity, documentId: String)
    fun addUpdateTaskList(activity: Activity, board: Board )
    fun assignMemberToBoard(activity: MembersActivity, board: Board, user: User)

}
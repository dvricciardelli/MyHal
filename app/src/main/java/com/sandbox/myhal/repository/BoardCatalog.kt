package com.sandbox.myhal.repository

import android.net.Uri
import com.sandbox.myhal.activities.BaseActivity
import com.sandbox.myhal.activities.BoardActivity
import com.sandbox.myhal.activities.CreateBoardActivity
import com.sandbox.myhal.activities.TaskListActivity
import com.sandbox.myhal.models.Board

class BoardCatalog(boardRepository: BoardRepository): BoardRepository  {

    val mBoardRepository: BoardRepository = boardRepository

    override fun createBoard(activity: CreateBoardActivity, board: Board){
        mBoardRepository.createBoard(activity, board)
    }

    override fun uploadBoardImage(activity: CreateBoardActivity, fileUri: Uri?) {
        mBoardRepository.uploadBoardImage(activity, fileUri)
    }

    override fun getBoardsList(activity: BoardActivity, customerId: String) {
        mBoardRepository.getBoardsList(activity, customerId)
    }

    override fun getBoardDetails(activity: TaskListActivity, documentId: String) {
        mBoardRepository.getBoardDetails(activity, documentId)
    }

    override fun addUpdateTaskList(activity: TaskListActivity, board: Board) {
        mBoardRepository.addUpdateTaskList(activity, board)
    }

}
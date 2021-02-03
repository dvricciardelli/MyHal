package com.sandbox.myhal.repository

import android.app.Activity
import android.net.Uri
import com.sandbox.myhal.activities.*
import com.sandbox.myhal.models.Board
import com.sandbox.myhal.models.User

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

    override fun addUpdateTaskList(activity: Activity, board: Board) {
        mBoardRepository.addUpdateTaskList(activity, board)
    }

    override fun assignMemberToBoard(activity: MembersActivity, board: Board, user: User) {
        return mBoardRepository.assignMemberToBoard(activity, board, user)
    }



}
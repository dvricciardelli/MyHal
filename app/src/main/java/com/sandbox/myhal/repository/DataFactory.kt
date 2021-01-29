package com.sandbox.myhal.repository

 object DataFactory {

     fun createCustomer(): CustomerRepository = FirestoreCustomerRepository()
     fun createBoard(): BoardRepository = FirestoreBoardRepository()

 }
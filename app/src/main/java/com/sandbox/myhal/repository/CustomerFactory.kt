package com.sandbox.myhal.repository

 object CustomerFactory {

        fun create(): CustomerRepository = FirestoreCustomerRepository()

 }
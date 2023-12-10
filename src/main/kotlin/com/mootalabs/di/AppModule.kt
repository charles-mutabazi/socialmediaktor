package com.mootalabs.di

import com.mootalabs.dao.user.UserDao
import com.mootalabs.dao.user.UserDaoImpl
import com.mootalabs.repository.user.UserRepository
import com.mootalabs.repository.user.UserRepositoryImpl
import org.koin.dsl.module

val appModule = module {
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<UserDao> { UserDaoImpl() }
}
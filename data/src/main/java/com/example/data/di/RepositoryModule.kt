package com.example.data.di

import com.example.data.remote.CharactersAPI
import com.example.data.repositories.CharactersRepositoryImp
import com.example.domain.repositories.ICharactersRepository

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//@InstallIn(SingletonComponent::class)
//@Module
//abstract class RepositoryModule {
//    @Binds
//    @Singleton
//    abstract fun bindCharactersRepo(charactersRepository: CharactersRepositoryImp): ICharactersRepository
//}
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideCharactersRepository(
        charactersAPI: CharactersAPI
    ): ICharactersRepository {
        return CharactersRepositoryImp(charactersAPI)
    }
}
package com.currencyconvertor.di

import com.currencyconvertor.data.repository.CurrencyConversionRepoImpl
import com.currencyconvertor.domain.repository.CurrencyConversionRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {
    @Binds
    abstract fun provideRepo(impl: CurrencyConversionRepoImpl): CurrencyConversionRepo
}
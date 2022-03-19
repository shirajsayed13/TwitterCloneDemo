package com.shiraj.data.repositories

import com.shiraj.domain.TweetsUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindTweetsRepository(impl: TweetsUseCaseImpl): TweetsUseCase
}
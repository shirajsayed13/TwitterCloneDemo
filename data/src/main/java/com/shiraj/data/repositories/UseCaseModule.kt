package com.shiraj.data.repositories

import com.shiraj.domain.usecase.PostTweetUseCase
import com.shiraj.domain.usecase.TweetsUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindTweetsRepository(impl: TweetsUseCaseImpl): TweetsUseCase

    @Binds
    abstract fun bindPostTweetRepository(impl: PostTweetUseCaseImpl): PostTweetUseCase
}


package com.tnmd.learningenglishapp.model.service.module

import com.tnmd.learningenglishapp.model.service.AccountService
import com.tnmd.learningenglishapp.model.service.imple.AccountServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
  @Binds abstract fun provideAccountService(impl: AccountServiceImpl): AccountService
}

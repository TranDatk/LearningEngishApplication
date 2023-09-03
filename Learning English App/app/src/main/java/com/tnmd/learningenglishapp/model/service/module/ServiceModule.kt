

package com.tnmd.learningenglishapp.model.service.module

import com.tnmd.learningenglishapp.model.service.AccountService
import com.tnmd.learningenglishapp.model.service.AuthenticationService
import com.tnmd.learningenglishapp.model.service.CoursesService
import com.tnmd.learningenglishapp.model.service.LearnerService
import com.tnmd.learningenglishapp.model.service.LogService
import com.tnmd.learningenglishapp.model.service.WordsService
import com.tnmd.learningenglishapp.model.service.imple.AccountServiceImpl
import com.tnmd.learningenglishapp.model.service.imple.AuthenticationServiceImpl
import com.tnmd.learningenglishapp.model.service.imple.CoursesServiceImpl
import com.tnmd.learningenglishapp.model.service.imple.LearnerServiceImpl
import com.tnmd.learningenglishapp.model.service.imple.WordsServiceImpl
import com.tnmd.learningenglishapp.service.impl.LogServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
  @Binds abstract fun provideAuthenticationService(impl: AuthenticationServiceImpl): AuthenticationService

  @Binds abstract fun provideLogService(impl: LogServiceImpl): LogService

  @Binds abstract fun provideCoursesService(impl: CoursesServiceImpl) : CoursesService

  @Binds abstract fun provideWordsService(impl: WordsServiceImpl) : WordsService

  @Binds abstract fun provideLeanerService(impl: LearnerServiceImpl) : LearnerService

  @Binds abstract fun provideAccountService(impl: AccountServiceImpl) : AccountService
}

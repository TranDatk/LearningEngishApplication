

package com.tnmd.learningenglishapp.model.service.module

import com.tnmd.learningenglishapp.model.service.AccountAccountLevelService
import com.tnmd.learningenglishapp.model.service.AccountLevelService
import com.tnmd.learningenglishapp.model.service.AccountService
import com.tnmd.learningenglishapp.model.service.AuthenticationService
import com.tnmd.learningenglishapp.model.service.CoursesService
import com.tnmd.learningenglishapp.model.service.LearnerService
import com.tnmd.learningenglishapp.model.service.LogService
import com.tnmd.learningenglishapp.model.service.ProcessesService
import com.tnmd.learningenglishapp.model.service.ScheduleService
import com.tnmd.learningenglishapp.model.service.ScoresService
import com.tnmd.learningenglishapp.model.service.WordsService
import com.tnmd.learningenglishapp.model.service.Words_CoursesService
import com.tnmd.learningenglishapp.model.service.imple.AccountAccountLevelServiceImpl
import com.tnmd.learningenglishapp.model.service.imple.AccountLevelServiceImpl
import com.tnmd.learningenglishapp.model.service.imple.AccountServiceImpl
import com.tnmd.learningenglishapp.model.service.imple.AuthenticationServiceImpl
import com.tnmd.learningenglishapp.model.service.imple.CoursesServiceImpl
import com.tnmd.learningenglishapp.model.service.imple.LearnerServiceImpl
import com.tnmd.learningenglishapp.model.service.imple.ProcessesServiceImpl
import com.tnmd.learningenglishapp.model.service.imple.ScheduleServiceImpl
import com.tnmd.learningenglishapp.model.service.imple.ScoresServiceImpl
import com.tnmd.learningenglishapp.model.service.imple.WordsServiceImpl
import com.tnmd.learningenglishapp.model.service.imple.Words_CoursesServiceImpl
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
  @Binds abstract fun provideProcessesService(impl: ProcessesServiceImpl) : ProcessesService
  @Binds abstract fun provideWords_CoursesService(impl: Words_CoursesServiceImpl) : Words_CoursesService
  @Binds abstract fun provideScoreService(impl: ScoresServiceImpl) : ScoresService
  @Binds abstract fun provideScheduleService(impl: ScheduleServiceImpl) : ScheduleService

  @Binds abstract fun accountLevelService(impl: AccountLevelServiceImpl) : AccountLevelService

  @Binds abstract fun accountAccountLevelService(impl: AccountAccountLevelServiceImpl) : AccountAccountLevelService
}

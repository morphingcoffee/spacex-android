package com.morphingcoffee.spacex.di

import com.morphingcoffee.spacex.BuildConfig
import com.morphingcoffee.spacex.data.remote.IFetchCompanyService
import com.morphingcoffee.spacex.data.remote.IFetchLaunchesService
import com.morphingcoffee.spacex.domain.interfaces.ICompanyRepository
import com.morphingcoffee.spacex.domain.interfaces.ILaunchesRepository
import com.morphingcoffee.spacex.domain.usecase.IGetCompanyUseCase
import com.morphingcoffee.spacex.domain.usecase.IGetLaunchesUseCase
import com.morphingcoffee.spacex.domain.usecase.impl.GetCompanyUseCase
import com.morphingcoffee.spacex.domain.usecase.impl.GetLaunchesUseCase
import com.morphingcoffee.spacex.presentation.CompanyInfoViewModel
import com.morphingcoffee.spacex.presentation.LaunchesViewModel
import com.morphingcoffee.spacex.repository.CompanyRepository
import com.morphingcoffee.spacex.repository.LaunchesRepository
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class KoinModules {
    companion object {
        private fun presentationModule(): Module = module {
            viewModel { CompanyInfoViewModel(get()) }
            viewModel { LaunchesViewModel(get()) }
        }

        private fun domainModule(): Module = module {
            factory<IGetCompanyUseCase> { GetCompanyUseCase(get()) }
            factory<IGetLaunchesUseCase> { GetLaunchesUseCase(get()) }
        }

        private fun repositoryModule(): Module = module {
            factory<ICompanyRepository> { CompanyRepository(get()) }
            factory<ILaunchesRepository> { LaunchesRepository(get()) }
        }

        private fun dataModule(): Module = module {
            factory<IFetchCompanyService> { get<Retrofit>().create(IFetchCompanyService::class.java) }
            factory<IFetchLaunchesService> { get<Retrofit>().create(IFetchLaunchesService::class.java) }

            factory<Moshi> {
                Moshi.Builder()
                    .build()
            }

            factory<MoshiConverterFactory> { MoshiConverterFactory.create(get()) }

            // FIXME stricten logging after dev
            factory<HttpLoggingInterceptor> {
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            }
            factory<OkHttpClient> {
                OkHttpClient.Builder()
                    .addInterceptor(get<HttpLoggingInterceptor>())
                    .build()
            }

            single<Retrofit> {
                Retrofit.Builder()
                    .client(get<OkHttpClient>())
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(get<MoshiConverterFactory>())
                    .build()
            }
        }

        // TODO decide if UseCases should simply be part of domain
//        private fun useCaseModule(): Module = module { }
        // TODO consider if more layers would be beneficial
        //private fun frameworkModule(): Module = module { }

        fun all(): List<Module> =
            presentationModule() + domainModule() + repositoryModule() + dataModule()
    }
}
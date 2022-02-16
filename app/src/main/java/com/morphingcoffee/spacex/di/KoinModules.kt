package com.morphingcoffee.spacex.di

import com.morphingcoffee.spacex.BuildConfig
import com.morphingcoffee.spacex.data.remote.FetchCompanyService
import com.morphingcoffee.spacex.presentation.CompanyViewModel
import com.morphingcoffee.spacex.presentation.LaunchesViewModel
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
            viewModel { CompanyViewModel() }
            viewModel { LaunchesViewModel() }
        }

        private fun domainModule(): Module = module {
        }

        private fun repositoryModule(): Module = module {
        }

        private fun dataModule(): Module = module {
            factory<FetchCompanyService> { get<Retrofit>().create(FetchCompanyService::class.java) }
            factory<Moshi> {
                Moshi.Builder()
                    .build()
            }
            factory<MoshiConverterFactory> { MoshiConverterFactory.create(get()) }
            factory<OkHttpClient> {
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor())
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
package com.morphingcoffee.spacex.di

import android.content.Context
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.room.Room
import coil.ImageLoader
import coil.request.ImageRequest
import com.morphingcoffee.spacex.BuildConfig
import com.morphingcoffee.spacex.R
import com.morphingcoffee.spacex.data.local.AppDB
import com.morphingcoffee.spacex.data.remote.IFetchCompanyService
import com.morphingcoffee.spacex.data.remote.IFetchLaunchesService
import com.morphingcoffee.spacex.domain.interfaces.ICompanyRepository
import com.morphingcoffee.spacex.domain.interfaces.ILaunchesRepository
import com.morphingcoffee.spacex.domain.model.Launch
import com.morphingcoffee.spacex.domain.usecase.IGetCompanyUseCase
import com.morphingcoffee.spacex.domain.usecase.IGetLaunchesUseCase
import com.morphingcoffee.spacex.domain.usecase.impl.GetCompanyUseCase
import com.morphingcoffee.spacex.domain.usecase.impl.GetLaunchesUseCase
import com.morphingcoffee.spacex.presentation.CompanyViewModel
import com.morphingcoffee.spacex.presentation.LaunchesViewModel
import com.morphingcoffee.spacex.presentation.recyclerview.LaunchesAdapter
import com.morphingcoffee.spacex.presentation.recyclerview.LaunchesDiffUtilCallback
import com.morphingcoffee.spacex.repository.CompanyRepository
import com.morphingcoffee.spacex.repository.ICurrentUnixTimeProvider
import com.morphingcoffee.spacex.repository.LaunchesRepository
import com.morphingcoffee.spacex.repository.model.LaunchesCachingConfig
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

        private const val DB_IDENTIFIER = "spacex_app_db"

        /** 60 second launches cache **/
        private const val LAUNCHES_CACHE_VALIDITY_IN_MILLIS = 60000L

        private fun presentationModule(): Module = module {
            viewModel { CompanyViewModel(get()) }
            viewModel { LaunchesViewModel(get()) }

            factory<LaunchesAdapter> { LaunchesAdapter(get(), get(), get(), get()) }
            factory<DiffUtil.ItemCallback<Launch>> { LaunchesDiffUtilCallback() }
            factory<AsyncDifferConfig<Launch>> { AsyncDifferConfig.Builder<Launch>(get()).build() }
            factory<ImageRequest.Builder> { ImageRequest.Builder(get<Context>()) }
            single<ImageLoader> {
                ImageLoader.Builder(get())
                    .error(R.drawable.rocket)
                    .placeholder(R.drawable.rocket)
                    .fallback(R.drawable.rocket)
                    .crossfade(200)
                    .build()
            }
        }

        private fun domainModule(): Module = module {
            factory<IGetCompanyUseCase> { GetCompanyUseCase(get()) }
            factory<IGetLaunchesUseCase> { GetLaunchesUseCase(get()) }
        }

        private fun repositoryModule(): Module = module {
            factory<LaunchesCachingConfig> { LaunchesCachingConfig(launchesCacheValidityInMillis = LAUNCHES_CACHE_VALIDITY_IN_MILLIS) }
            factory<ICompanyRepository> { CompanyRepository(get(), get()) }
            factory<ILaunchesRepository> { LaunchesRepository(get(), get(), get(), get()) }
            factory<ICurrentUnixTimeProvider> { ICurrentUnixTimeProvider { System.currentTimeMillis() } }
        }

        private fun dataModule(): Module = module {
            single<AppDB> { Room.databaseBuilder(get(), AppDB::class.java, DB_IDENTIFIER).build() }
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

        fun all(): List<Module> =
            presentationModule() + domainModule() + repositoryModule() + dataModule()
    }
}
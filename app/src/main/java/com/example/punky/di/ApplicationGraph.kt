package com.example.punky.di

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import com.example.punky.ui.beerdeatils.BeerDetailsFragment
import com.example.punky.ui.beerlist.BeerListFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component( modules = [
    ApplicationModule::class,
    RoomModule::class,
    ApplicationBinds::class,
    ViewModelBuilderModule::class,
])
interface ApplicationGraph {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application
        ) : ApplicationGraph
    }


    @ExperimentalPagingApi
    fun inject(into: BeerListFragment)
    fun inject(into: BeerDetailsFragment)
}
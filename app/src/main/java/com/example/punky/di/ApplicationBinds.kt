package com.example.punky.di

import androidx.lifecycle.ViewModel
import com.example.punky.ui.beerlist.BeerListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ApplicationBinds {

    @Binds
    @IntoMap
    @ViewModelKey(BeerListViewModel::class)
    internal abstract fun bindBeerListViewModel(
        viewModel: BeerListViewModel
    ): ViewModel
}
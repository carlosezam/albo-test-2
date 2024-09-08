package com.example.punky.di

import androidx.lifecycle.ViewModel
import com.example.punky.ui.beerdeatils.BeerDetailsViewModel
import com.example.punky.ui.beerdeatils.BeerDetailsViewModelImpl
import com.example.punky.ui.beerlist.BeerListViewModel
import com.example.punky.ui.beerlist.BeerListViewModelImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap


@Module
@InstallIn(SingletonComponent::class)
abstract class ApplicationBinds {

    @Binds
    internal abstract fun bindsBeerListViewModelImpl( impl: BeerListViewModelImpl ): BeerListViewModel

    /*@Binds
    @IntoMap
    @ViewModelKey(BeerListViewModel::class)
    internal abstract fun bindsBeerListViewModel(
        viewModel: BeerListViewModel
    ): ViewModel*/


    @Binds
    internal abstract fun bindsBeerDetailsModelImpl(impl: BeerDetailsViewModelImpl): BeerDetailsViewModel

    /*@Binds
    @IntoMap
    @ViewModelKey(BeerDetailsViewModel::class)
    internal abstract fun bindsBeerDetailsViewModel(
        viewModel: BeerDetailsViewModel
    ): ViewModel*/
}
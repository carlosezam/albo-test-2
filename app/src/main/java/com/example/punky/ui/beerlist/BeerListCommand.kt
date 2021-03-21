package com.example.punky.ui.beerlist

sealed class BeerListCommand(){
    data class OpenBeerDetails( val beerId: Int ) : BeerListCommand()
}

package com.example.punky.ui.beerlist

sealed class BeerListCommand(){
    data class OpenBeerDetails( val beer: BeerItem, val position: Int ) : BeerListCommand()
}

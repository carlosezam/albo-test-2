package com.ezam.rickandmorty.utils

class ThrottleClick(val interval: Long = 1000L, val onClick: () -> Unit) : () -> Unit {

    private var lastClickTime = 0L

    override fun invoke() {
        if( System.currentTimeMillis() - lastClickTime > interval) {
            lastClickTime = System.currentTimeMillis()
            onClick()
        }
    }

}
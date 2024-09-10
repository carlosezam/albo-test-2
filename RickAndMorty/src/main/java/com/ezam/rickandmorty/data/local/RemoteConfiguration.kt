package com.ezam.rickandmorty.data.local

import com.ezam.rickandmorty.domain.Configuration
import com.ezam.rickandmorty.domain.IdGeneratorType
import com.punky.core.data.RemoteConfig
import com.punky.core.utils.enumFromName
import javax.inject.Inject

class RemoteConfiguration @Inject constructor(
    private val remoteConfig: RemoteConfig
) : Configuration {

    override val idGenerator: IdGeneratorType
        get() = enumFromName<IdGeneratorType>(remoteConfig.getString(ID_GENERATOR))
            ?: IdGeneratorType.RANDOM

    companion object {
        internal const val ID_GENERATOR = "id_generator"
    }
}
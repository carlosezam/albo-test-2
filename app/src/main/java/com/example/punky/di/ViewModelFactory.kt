package com.example.punky.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.MapKey
import dagger.Module
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

@Module
abstract class ViewModelBuilderModule {

    @Binds
    internal abstract fun bindViewModelFactory(
        factory: AppViewModelFactory
    ) : ViewModelProvider.Factory

}

@MapKey
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ViewModelKey( val value: KClass<out ViewModel>)



class AppViewModelFactory @Inject constructor(
    private val creators: @JvmSuppressWildcards Map< Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        var creator: Provider<out ViewModel>? = creators[modelClass]

        if( creator == null ) {
            for((key,value) in creators){
                if(modelClass.isAssignableFrom(key)){
                    creator =  value
                    break
                }
            }
        }

        if( creator == null ){
            throw IllegalArgumentException("unknow model class " + modelClass)
        }

        try {
            @Suppress("UNCHECKED_CAST")
            return creator.get() as T
            //return modelClass.getConstructor(TransportRepository::class.java).newInstance(transportRepository)
        } catch ( e: Exception ){
            throw  RuntimeException(e)
        }

    }


}

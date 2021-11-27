## Punky 

Este proyecto se trata de un Master-Detail, en la vista inicial se muestra un listado de cevezas con detalles minimos
como nombre, imagen y tagline. Al dar clic sobre un item se puede observar los detalles. Cuenta con soporte offline y modo nocturno.

Datos: [PunkApi](https://api.punkapi.com/v2/)

## Arquitectura
Se usa la [arquitectura recomendada](https://developer.android.com/jetpack/guide?hl=es-419#overview) por Android.
Cuenta con una capa de repositorio ubicado en el folder ```data```,
esta a su vez se divide en 2 data sources: ```network``` (quien se encarga de hacer las peticiones a la api) y ```local``` (maneja la persistencia de los datos).

Dentro del folder ```di``` se encuentra el grafo de dependencias donde se configuran los objetos a ser injectados.

El folder ```ui``` contiene los viewmodels y fragments de la app, está divido por pantallas ```BeerList``` y ```BeerDetails```


## Técnologias
El proyecto está construido en Kotlin y hace uso de Android Jetpack:
- [NavigationComponent](https://developer.android.com/guide/navigation/navigation-getting-started)
- [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)
- [Room](https://developer.android.com/training/data-storage/room)
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
- [ConstraintLayout](https://developer.android.com/training/constraint-layout?hl=es-419)

Además de otas librerias:
- [kotlin Coroutines](https://kotlinlang.org/docs/coroutines-guide.html)
- [Dagger 2](https://developer.android.com/training/dependency-injection/dagger-android?hl=es-419)


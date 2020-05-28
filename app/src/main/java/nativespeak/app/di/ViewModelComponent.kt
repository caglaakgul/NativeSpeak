package nativespeak.app.di

import dagger.Component

@Component(
    modules = arrayOf(
        ViewModelModule::class
    )
)
interface ViewModelComponent {
    // inject your view models


}
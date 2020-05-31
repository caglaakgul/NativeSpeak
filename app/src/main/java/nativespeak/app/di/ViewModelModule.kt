package nativespeak.app.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import nativespeak.app.base.viewmodel.ViewModelFactory
import nativespeak.app.di.scopes.PerApplication
import nativespeak.app.ui.discover.DiscoverViewModel
import nativespeak.app.ui.login.LoginViewModel
import nativespeak.app.ui.message.MessageViewModel
import nativespeak.app.ui.register.RegisterViewModel
import nativespeak.app.ui.settings.SettingsViewModel
import nativespeak.app.ui.splash.SplashViewModel


@Module
internal abstract class ViewModelModule {
    @Binds
    @PerApplication
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory


    @Binds
    @IntoMap
    @ViewModelKey(MessageViewModel::class)
    abstract fun bindsMessageViewModel(viewModel: MessageViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    abstract fun bindsRegisterViewModel(viewModel: RegisterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DiscoverViewModel::class)
    abstract fun bindsDiscoverViewModel(viewModel: DiscoverViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindsLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindsSettingsViewModel(viewModel: SettingsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindsSplashViewModel(viewModel: SplashViewModel): ViewModel
}
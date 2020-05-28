package nativespeak.app.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import nativespeak.app.di.scopes.PerActivity
import nativespeak.app.ui.discover.DiscoverActivity
import nativespeak.app.ui.login.LoginActivity
import nativespeak.app.ui.message.MessageActivity
import nativespeak.app.ui.register.RegisterActivity
import nativespeak.app.ui.settings.SettingsActivity

@Module
abstract class ActivityModule {

    @PerActivity
    @ContributesAndroidInjector
    abstract fun bindMessageActivity(): MessageActivity


    @PerActivity
    @ContributesAndroidInjector
    abstract fun bindRegisterActivity(): RegisterActivity

    @PerActivity
    @ContributesAndroidInjector
    abstract fun bindDiscoverActivity(): DiscoverActivity

    @PerActivity
    @ContributesAndroidInjector
    abstract fun bindLoginActivity(): LoginActivity

    @PerActivity
    @ContributesAndroidInjector
    abstract fun bindSettingsActivity(): SettingsActivity
}
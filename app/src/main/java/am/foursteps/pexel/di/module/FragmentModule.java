package am.foursteps.pexel.di.module;



import am.foursteps.pexel.ui.main.fragment.FavoriteFragment;
import am.foursteps.pexel.ui.main.fragment.ItemFragment;
import am.foursteps.pexel.ui.main.fragment.ListFragment;
import am.foursteps.pexel.ui.main.fragment.SearchFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract ListFragment contributeListFragment();

    @ContributesAndroidInjector
    abstract ItemFragment contributeItemFragment();

    @ContributesAndroidInjector
    abstract SearchFragment contributeSearchFragment();

    @ContributesAndroidInjector
    abstract FavoriteFragment contributeFavoriteFragment();
}

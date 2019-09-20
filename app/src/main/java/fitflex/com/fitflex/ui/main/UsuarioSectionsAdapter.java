package fitflex.com.fitflex.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import fitflex.com.fitflex.R;
import fitflex.com.fitflex.activity.VisualizarTreinos;
import fitflex.com.fitflex.activity.CalculoImcFragment;

public class UsuarioSectionsAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{ R.string.visualizarTreinos, R.string.imc};

    private final Context mContext;

    public UsuarioSectionsAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch ( position ) {
            case 0:

                return new VisualizarTreinos();

            case 1:
                return new CalculoImcFragment();

        }

        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 4 total pages.
        return 2;
    }
}

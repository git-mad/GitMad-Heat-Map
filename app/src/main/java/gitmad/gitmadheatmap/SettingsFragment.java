package gitmad.gitmadheatmap;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    private TextView tvFullName;
    private TextView tvEmail;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        tvFullName = v.findViewById(R.id.settings_text_full_name);
        tvEmail = v.findViewById(R.id.settings_text_email);

        // Get username from shared preferences.
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(getString(R.string.pref_preferences), Context.MODE_PRIVATE);
        String firstName = sharedPreferences.getString(getString(R.string.pref_first_name), "");
        String lastName = sharedPreferences.getString(getString(R.string.pref_last_name), "");
        String email = sharedPreferences.getString(getString(R.string.pref_user_email), "");
        String fullName = firstName + " " + lastName;

        tvFullName.setText(fullName);
        tvEmail.setText(email);

        return v;
    }

}

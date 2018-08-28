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
public class FragmentSettings extends Fragment {

    private TextView tvFullName;
    private TextView tvEmail;

    public static FragmentSettings newInstance() {
        return new FragmentSettings();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
//        setNameValues( view );

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        tvFullName = view.findViewById(R.id.settings_text_full_name);
        tvEmail = view.findViewById(R.id.settings_text_email);

        // Get username from shared preferences.
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(getString(R.string.pref_preferences), Context.MODE_PRIVATE);
        String firstName = sharedPreferences.getString(getString(R.string.pref_first_name), "");
        String lastName = sharedPreferences.getString(getString(R.string.pref_last_name), "");
        String email = sharedPreferences.getString(getString(R.string.pref_user_email), "");
        String fullName = firstName + " " + lastName;

        tvFullName.setText(fullName);
        tvEmail.setText(email);
    }

}

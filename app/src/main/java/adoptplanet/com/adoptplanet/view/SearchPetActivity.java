package adoptplanet.com.adoptplanet.view;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.widget.TintImageView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import adoptplanet.com.adoptplanet.R;
import adoptplanet.com.adoptplanet.controller.PetListviewAdapter;
import adoptplanet.com.adoptplanet.model.CurrentUser;
import adoptplanet.com.adoptplanet.model.Pet;
import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchPetActivity extends ActionBarActivity {

    @Bind(R.id.search_pet_toolbar) Toolbar toolbar;
    @Bind(R.id.search_pet_listview) ListView listview;

    public static final String TAG = "SearchPetActivity";

    public static final int MALE = 1;
    public static final int FEMALE = 2;

    public static final int BABY_ID = 1;
    public static final int YOUNG_ID = 2;
    public static final int ADULT_ID = 4;
    public static final int SENIOR_ID = 8;

    private static final int baby_min = 0;
    private static final int baby_max = 2;

    private static final int young_min = baby_max;
    private static final int young_max = 4;

    private static final int adult_min = young_max;
    private static final int adult_max = 6;

    private static final int senior_min = adult_max;
    private static final int senior_max = 7;


    private int drive_min = 0;
    private int drive_max = 300;
    private int gender = MALE;
    private int age = YOUNG_ID;
    private String breed = "";


    private int drive_max_t = 300;
    private int gender_t = MALE;
    private int age_t = YOUNG_ID;
    private String breed_t = "";

    private Dialog d;

    private ArrayList<Pet> pet_list = new ArrayList<>();

    private PetListviewAdapter adapter;

    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_pet);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        bar.setTitle("");
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeAsUpIndicator(R.drawable.z_icon_back);

        adapter = new PetListviewAdapter(this, pet_list);
        listview.setAdapter(adapter);

        refreshData(index);
    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        MenuItem searchViewMenuItem = menu.findItem(R.id.menu_search_search_field);
//        SearchView mSearchView = (SearchView) searchViewMenuItem.getActionView();
//        //int searchImgId = getResources().getIdentifier("android:id/search_button", null, null);
//        //ImageView v = (ImageView) mSearchView.findViewById(searchImgId);
//        Log.d(TAG, "\nChild[0]: " + ((LinearLayout)mSearchView.getChildAt(0)).getChildAt(0));
//        Log.d(TAG, "\nChild[0]: " + ((LinearLayout)mSearchView.getChildAt(0)).getChildAt(1));
//        Log.d(TAG, "\nChild[0]: " + ((LinearLayout)mSearchView.getChildAt(0)).getChildAt(2));
//        //v.setImageResource(R.drawable.z_search);
//        //mSearchView.setOnQueryTextListener(this);
//        ((TintImageView)((LinearLayout) mSearchView.getChildAt(0)).getChildAt(1))
//                .setImageResource(R.drawable.z_search);
//        return super.onPrepareOptionsMenu(menu);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_pet, menu);


        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search_search_field).getActionView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                onQueryTextChange(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null)
                    adapter.getFilter().filter(newText.toLowerCase());
                return false;
            }
        });
//        int searchIconId = searchView.getContext().getResources().
//                getIdentifier("android:id/search_button", null, null);
//        ImageView searchIcon = (ImageView) searchView.findViewById(searchIconId);
//        searchIcon.setImageResource(R.drawable.z_search);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

//        if (id == R.id.action_settings) {
//            return true;
//        }

        if (id == R.id.menu_search_settings){

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setCancelable(true)
                    .setView(setAlertLayout())
                    .create();

            d = dialog;

            dialog.show();
        }

        if (id == R.id.menu_search_search_field){

        }

        if (id == android.R.id.home){
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private LinearLayout setAlertLayout(){
        gender_t = gender;
        age_t = age;
        drive_max_t = drive_max;

        LayoutInflater inflater = getLayoutInflater();
        LinearLayout m_lay = (LinearLayout) inflater.inflate(R.layout.alert_search_options, null);

        final Button b_20 = (Button) m_lay.findViewById(R.id.alert_search_20_b);
        final Button b_50 = (Button) m_lay.findViewById(R.id.alert_search_50_b);
        final Button b_100 = (Button) m_lay.findViewById(R.id.alert_search_100_b);
        final Button b_300 = (Button) m_lay.findViewById(R.id.alert_search_300_b);
        final Button b_300p = (Button) m_lay.findViewById(R.id.alert_search_300p_b);
        // ...
        final Button b_male = (Button) m_lay.findViewById(R.id.alert_search_male_b);
        final Button b_female = (Button) m_lay.findViewById(R.id.alert_search_female_b);
        // ...
        final Button b_b = (Button) m_lay.findViewById(R.id.alert_search_baby_b);
        final Button b_y = (Button) m_lay.findViewById(R.id.alert_search_young_b);
        final Button b_a = (Button) m_lay.findViewById(R.id.alert_search_adult_b);
        final Button b_s = (Button) m_lay.findViewById(R.id.alert_search_senior_b);
        // ...
        final Button set_b = (Button) m_lay.findViewById(R.id.alert_search_setb);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                if (id == R.id.alert_search_setb){
                    gender = gender_t;
                    age = age_t;
                    drive_max = drive_max_t;
                    index = 0;
                    refreshData(index);
                    Log.d(TAG, "\nGender: " + gender + "\nAge: " + age + "\nDrive" + drive_max);
                    d.dismiss();
                }
                else if (id == R.id.alert_search_20_b){
                    drive_max_t = 20;
                    makeActive(b_20);
                    makeUnactive(b_50);
                    makeUnactive(b_100);
                    makeUnactive(b_300);
                    makeUnactive(b_300p);
                }
                else if (id == R.id.alert_search_50_b){
                    drive_max_t = 50;
                    makeActive(b_50);
                    makeUnactive(b_20);
                    makeUnactive(b_100);
                    makeUnactive(b_300);
                    makeUnactive(b_300p);
                }
                else if (id == R.id.alert_search_100_b){
                    drive_max_t = 100;
                    makeActive(b_100);
                    makeUnactive(b_50);
                    makeUnactive(b_20);
                    makeUnactive(b_300);
                    makeUnactive(b_300p);
                }
                else if (id == R.id.alert_search_300_b){
                    drive_max_t = 300;
                    makeActive(b_300);
                    makeUnactive(b_50);
                    makeUnactive(b_100);
                    makeUnactive(b_20);
                    makeUnactive(b_300p);
                }
                else if (id == R.id.alert_search_300p_b){
                    drive_max_t = -1;
                    makeActive(b_300p);
                    makeUnactive(b_50);
                    makeUnactive(b_100);
                    makeUnactive(b_300);
                    makeUnactive(b_20);
                }
                else if (id == R.id.alert_search_male_b){
                    gender_t = MALE;
                    makeActive(b_male);
                    makeUnactive(b_female);
                }
                else if (id == R.id.alert_search_female_b){
                    gender_t = FEMALE;
                    makeActive(b_female);
                    makeUnactive(b_male);
                }
                else if (id == R.id.alert_search_senior_b){
                    age_t = SENIOR_ID;
                    makeActive(b_s);
                    makeUnactive(b_b);
                    makeUnactive(b_y);
                    makeUnactive(b_a);
                }
                else if (id == R.id.alert_search_baby_b){
                    age_t = BABY_ID;
                    makeActive(b_b);
                    makeUnactive(b_s);
                    makeUnactive(b_y);
                    makeUnactive(b_a);
                }
                else if (id == R.id.alert_search_young_b){
                    age_t = YOUNG_ID;
                    makeActive(b_y);
                    makeUnactive(b_b);
                    makeUnactive(b_s);
                    makeUnactive(b_a);
                }
                else if (id == R.id.alert_search_adult_b){
                    age_t = ADULT_ID;
                    makeActive(b_a);
                    makeUnactive(b_b);
                    makeUnactive(b_y);
                    makeUnactive(b_s);
                }
                else if (id == R.id.alert_search_breedlay){
                    // todo breed
                }
            }
        };

        set_b.setOnClickListener(listener);
        b_20.setOnClickListener(listener);
        b_100.setOnClickListener(listener);
        b_50.setOnClickListener(listener);
        b_300.setOnClickListener(listener);
        b_300p.setOnClickListener(listener);
        b_male.setOnClickListener(listener);
        b_female.setOnClickListener(listener);
        b_s.setOnClickListener(listener);
        b_a.setOnClickListener(listener);
        b_b.setOnClickListener(listener);
        b_y.setOnClickListener(listener);

        return m_lay;
    }

    public void refreshData(int index){

        if (index == 0)pet_list.clear();

        ParseQuery<ParseObject> pets_parse = ParseQuery.getQuery("Pet");
        pets_parse.orderByAscending("name");
        pets_parse.setLimit(CurrentUser.LOAD_LIMIT);

        pets_parse.setSkip(CurrentUser.LOAD_LIMIT * index);
        //if (drive_max != -1)pets_parse.whereLessThanOrEqualTo("distanc")
        // todo distance filter

        pets_parse.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> mList, ParseException e) {
                for (ParseObject pet_parse : mList) {
                    Pet pet = new Pet(pet_parse);

                    if (pet.owner_id != null && pet.owner_id.length() != 0
                            && pet.name != null && pet.name.length() != 0){
                        pet_list.add(pet);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void makeUnactive(Button b){
        b.setTextColor(getResources().getColor(R.color.gray_l));
        b.setBackground(getResources().getDrawable(R.drawable.z_layout_borders));
    }

    private void makeActive(Button b){
        b.setTextColor(getResources().getColor(R.color.white));
        b.setBackground(getResources().getDrawable(R.drawable.z_layout_borders_active));
    }

    // DISTANCE CALCULATOR
    public final static double AVERAGE_RADIUS_OF_EARTH = 6371;
    public int calculateDistance(double userLat, double userLng,
                                 double venueLat, double venueLng) {

        double latDistance = Math.toRadians(userLat - venueLat);
        double lngDistance = Math.toRadians(userLng - venueLng);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(venueLat))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (int) (Math.round(AVERAGE_RADIUS_OF_EARTH * c));
    }
}

package com.zionsongsapp.Activities;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zionsongsapp.Adapters.SongsListAdapter;
import com.zionsongsapp.utils.SongObject;
import com.zionsongsapp.utils.ZionCategoryAndSongsList;

import android.support.design.widget.FloatingActionButton;

import java.util.ArrayList;

/**
 * Created by Moukthika on 1/31/2016.
 */
public class ZionSongsListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private ListView songsList;
    public static String[] songsArray;
    private SongsListAdapter adapter;
    private int minIndex;
    private FloatingActionButton right_FBA;
    public static ArrayList<SongObject> songsArrayList;
    private static Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zion_songslist);

        mContext = this;

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.support_actionbar_customlayout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        songsList = (ListView) findViewById(R.id.songs_list);
        songsList.setOnItemClickListener(this);
        ((FloatingActionButton) findViewById(R.id.songs_right_flotingbutton)).setOnClickListener(this);

        songsArray = getIntent().getStringArrayExtra("SONGS_ARRAY");
        minIndex = getIntent().getIntExtra("MIN_INDEX", 0);
        ((TextView)findViewById(R.id.content_title)).setText(getIntent().getStringExtra("CATEGORY_NAME").replace("_"," "));
        ((TextView) findViewById(R.id.content_title)).setTypeface(Typeface.createFromAsset(getAssets(), "font/ROBOTO-CONDENSED_0.TTF"));

        songsArrayList = new ArrayList<SongObject>();
        for(int i = 0; i < songsArray.length; i++){

            SongObject object = new SongObject();
            object.setIndexNumber(minIndex+i);
            object.setSongName(songsArray[i]);

            songsArrayList.add(i, object);
        }

        if(adapter == null)
            adapter = new SongsListAdapter(this);

        songsList.setAdapter(adapter);
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p/>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        view.setSelected(true);
        Intent intent = new Intent(this, ZionSongViewActivity.class);
        intent.putExtra("SONG_NAME", SongsListAdapter.mArray.get(position).getSongName().trim());
        startActivity(intent);
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    SearchView searchView;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.zion_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(this.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();

        if (null != searchView) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(true);
        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String query) {
                // this is your adapter that will be filtered
                // Toast.makeText(ZionSongsCategoriesListActivity.this, newText, Toast.LENGTH_SHORT).show();
                adapter.filterData(query);
                return false;
            }

            public boolean onQueryTextSubmit(String query) {
                // Here u can get the value "query" which is entered in the search box.
                // Toast.makeText(ZionSongsCategoriesListActivity.this, query, Toast.LENGTH_SHORT).show();
                adapter.filterData(query);
                return false;
            }

            public boolean onClose() {
                adapter.filterData("");
                return false;
            }
        };

        searchView.setOnQueryTextListener(queryTextListener);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This method is called whenever the user chooses to navigate Up within your application's
     * activity hierarchy from the action bar.
     * <p/>
     * <p>If a parent was specified in the manifest for this activity or an activity-alias to it,
     * default Up navigation will be handled automatically. See
     * {@link #getSupportParentActivityIntent()} for how to specify the parent. If any activity
     * along the parent chain requires extra Intent arguments, the Activity subclass
     * should override the method {@link #onPrepareSupportNavigateUpTaskStack(TaskStackBuilder)}
     * to supply those arguments.</p>
     * <p/>
     * <p>See <a href="{@docRoot}guide/topics/fundamentals/tasks-and-back-stack.html">Tasks and
     * Back Stack</a> from the developer guide and
     * <a href="{@docRoot}design/patterns/navigation.html">Navigation</a> from the design guide
     * for more information about navigating within your app.</p>
     * <p/>
     * <p>See the {@link TaskStackBuilder} class and the Activity methods
     * {@link #getSupportParentActivityIntent()}, {@link #supportShouldUpRecreateTask(Intent)}, and
     * {@link #supportNavigateUpTo(Intent)} for help implementing custom Up navigation.</p>
     *
     * @return true if Up navigation completed successfully and this Activity was finished,
     * false otherwise.
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     * <p/>
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //******************** Popup menu functionality ****************************//
        View menuItemView = ((Activity) this).findViewById(R.id.action_list);
        //Creating the instance of PopupMenu
        final PopupMenu popup = new PopupMenu(this, menuItemView);

        for(int i = 0;i < getResources().getStringArray(R.array.Zion_Categories).length;i++)
            popup.getMenu().add(getResources().getStringArray(R.array.Zion_Categories)[i].trim().replace("_"," "));

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                for(int index = 0; index < getResources().getStringArray(R.array.Zion_Categories).length; index++){

                    if(getResources().getStringArray(R.array.Zion_Categories)[index].replace("_"," ").equals(item.getTitle())){

                        songsArray = ZionCategoryAndSongsList.getCategoryArray(ZionSongsListActivity.this, index);
                        ((TextView)findViewById(R.id.content_title)).setText(item.getTitle());

                        if(songsArrayList.size() > 0)
                            songsArrayList.clear();

                        for(int i = 0; i < songsArray.length; i++){

                            SongObject object = new SongObject();
                            object.setIndexNumber(ZionSongsCategoriesListActivity.minIndexArray[index]+i);
                            object.setSongName(songsArray[i]);

                            songsArrayList.add(i, object);
                        }

                        adapter.notifyDataSetChanged();

                        //adapter = new SongsListAdapter(ZionSongsListActivity.this);
                        //songsList.setAdapter(adapter);
                    }
                }
                return true;
            }
        });
        popup.show();
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        if(searchView.isIconified())
            searchView.setIconified(false);
        else
            searchView.setIconified(true);

//        if(v.getId() == R.id.songs_right_flotingbutton){
//
//            //Toast.makeText(this, "right fba", Toast.LENGTH_SHORT).show();
//            //finish();
//        }
    }

    public static void finishActivity(){
        ((ZionSongsListActivity) mContext).finish();
    }
}

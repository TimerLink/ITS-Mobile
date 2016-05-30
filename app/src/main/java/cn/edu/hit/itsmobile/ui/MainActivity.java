package cn.edu.hit.itsmobile.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SimpleAdapter;
import cn.edu.hit.itsmobile.R;
import cn.edu.hit.itsmobile.UnitTest;

public class MainActivity extends Activity {
	public final static int ACTION_SHOW_BUS_STATION = 1;
	public final static int ACTION_SHOW_BUS_LINE = 2;
    public final static int ACTION_SHOW_NEARBY_STATION = 3;
    public final static int ACTION_SHOW_SENSOR_ROUTE = 4;
	
	private boolean showSearchMenu = false;
	private String[] mDrawerTitles;
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	
	private ListView mDrawerList;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private InputMethodManager inputMethodManager;
	private SearchView searchView;
	
	private onSearchQueryListener searchListener;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		fragments.add(new MapFragment());
		fragments.add(new SearchFragment());
		fragments.add(new SettingsFragment());
		
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        
        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        mDrawerTitles = getResources().getStringArray(R.array.drawer_menu_title);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerList.setAdapter(new SimpleAdapter(
        		this, getDrawerMenuArray(), 
        		R.layout.list_item_drawer, 
        		new String[]{"icon", "title"}, 
        		new int[]{R.id.imageview, R.id.textview}
        		)
        );
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerToggle = new ActionBarDrawerToggle(
        		this, 
        		mDrawerLayout, 
                R.drawable.ic_drawer,
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {}; 
        mDrawerLayout.setDrawerListener(mDrawerToggle);
		
		if (savedInstanceState == null) {
            selectItem(0);
        }
		
	    inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	    
	    new UnitTest(this).run();
    }

	public List<Map<String,Object>> getDrawerMenuArray(){
	    int[] mDrawerIcons = {R.drawable.ic_map, 
	            R.drawable.ic_search, 
	            R.drawable.ic_settings};
    	List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
    	for (int i = 0; i < mDrawerTitles.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("icon", mDrawerIcons[i]);
			map.put("title", mDrawerTitles[i]);
			list.add(map);
		}
    	return list;
    }
	
	public void setOnSearchQueryListener(onSearchQueryListener listener) {
		this.searchListener = listener;
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);
		
		searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
		searchView.setIconifiedByDefault(true);
		searchView.setOnCloseListener(new OnCloseListener() {
			
			@Override
			public boolean onClose() {
				return true;
			}
		});

		LinearLayout searchViewInner1 = (LinearLayout)searchView.getChildAt(0);
		LinearLayout searchViewInner2 = (LinearLayout)searchViewInner1.getChildAt(2);
		final LinearLayout searchViewInner3 = (LinearLayout)searchViewInner2.getChildAt(1);

		searchView.setOnQueryTextFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)
					searchViewInner3.setBackgroundResource(R.drawable.textfield_search_selected);
				else
					searchViewInner3.setBackgroundResource(R.drawable.textfield_search_default);
			}
		});
		//开启搜索
		searchView.setOnQueryTextListener(new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String query) {
				hideSoftInput();
				if(searchListener != null)
					searchListener.onQuery(query);
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});
		
		return super.onCreateOptionsMenu(menu); 
    } 
    
    @Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem searchMenu = menu.findItem(R.id.menu_search);
		searchMenu.setVisible(showSearchMenu);
		searchView.setIconified(!showSearchMenu);
		if(!showSearchMenu){
			hideSoftInput();
			searchView.clearFocus();
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void selectItem(int position) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainFrameLayout, fragments.get(position)).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mDrawerTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);

        showSearchMenu = fragments.get(position) instanceof SearchFragment;
        invalidateOptionsMenu();
    }
    
	private class DrawerItemClickListener implements ListView.OnItemClickListener { 
		@Override 
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectItem(position);
		} 
	}

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
    @Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}

	/*
     * ���������
     */
    private void hideSoftInput() {
    	if (inputMethodManager != null) {
    		View v = getCurrentFocus();
    		if (v == null) {
    			return;
    		}

    		inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    	}
    }

	@Override
	protected void onResume() {
		final Intent intent = getIntent();
		if(intent != null){
			int action = intent.getIntExtra("action", 0);
			selectItem(0);
			switch (action) {
			case ACTION_SHOW_BUS_STATION:
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						((MapFragment)fragments.get(0)).addMarker(intent.getBundleExtra("bundle"));
					}
				}, 100);
				break;
			case ACTION_SHOW_BUS_LINE:
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						((MapFragment)fragments.get(0)).showBusLine();
					}
				}, 100);
				break;
			case ACTION_SHOW_NEARBY_STATION:
				((MapFragment)fragments.get(0)).showNearbyBusStations();
				break;
            case ACTION_SHOW_SENSOR_ROUTE:
                new Handler().postDelayed(new Runnable() {
                    
                    @Override
                    public void run() {
                        ((MapFragment)fragments.get(0)).showSensorData();
                    }
                }, 100);
                break;
			default:
				break;
			}
		}
		setIntent(null);
		super.onResume();
	}
	
	public interface onSearchQueryListener {
		public void onQuery(String query);
	}
}

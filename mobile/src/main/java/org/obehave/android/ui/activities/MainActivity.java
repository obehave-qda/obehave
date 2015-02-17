package org.obehave.android.ui.activities;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.google.common.eventbus.Subscribe;
import org.obehave.android.R;
import org.obehave.android.services.ApplicationService;
import org.obehave.android.ui.adapters.SectionsPagerAdapter;
import org.obehave.android.ui.events.ActionSelectedEvent;
import org.obehave.android.ui.events.SubjectModifierSelectedEvent;
import org.obehave.android.ui.events.SubjectSelectedEvent;
import org.obehave.android.ui.exceptions.UiException;
import org.obehave.android.ui.fragments.ActionFragment;
import org.obehave.android.ui.fragments.SubjectFragment;
import org.obehave.android.ui.fragments.SubjectModifierFragment;
import org.obehave.android.ui.util.ErrorDialog;
import org.obehave.events.EventBusHolder;
import org.obehave.exceptions.FactoryException;
import org.obehave.model.Action;
import org.obehave.model.Subject;
import org.obehave.model.modifier.ModifierFactory;

import java.util.List;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    private static final int CODING_FRAGMENT_POSITION = 2;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;


    @Subscribe
    public  void onSubjectSelected(SubjectSelectedEvent event){
        Subject subject = event.getSubject();
        Log.d(LOG_TAG, "onSubjectSelected");
        Log.d(LOG_TAG, subject.getDisplayString());
        changeCodingFragment(ActionFragment.newInstance(CODING_FRAGMENT_POSITION, ApplicationService.getAllActions()));
        ApplicationService.selectItem(event.getSubject());
    }

    @Subscribe
    public  void onActionSelected(ActionSelectedEvent event){
        Action action = event.getAction();
        Log.d(LOG_TAG, "onActionSelected");
        Log.d(LOG_TAG, action.getDisplayString());
        try {
            ApplicationService.selectItem(action);
            ModifierFactory modifierFactory = ApplicationService.getModifierFactoryOfSelectedAction();
            if (modifierFactory == null) {
                ApplicationService.createCoding();
            } else if (modifierFactory.getType() == ModifierFactory.Type.SUBJECT_MODIFIER_FACTORY) {
                changeCodingFragment(SubjectModifierFragment.newInstance(CODING_FRAGMENT_POSITION, (modifierFactory).getValidSubjects()));
            }
        }
        catch(UiException ex){
            ErrorDialog ed = new ErrorDialog(ex.getMessage(), this);
            ed.invoke();
        }
    }

    @Subscribe
    public void onSubjectModifierSelected(SubjectModifierSelectedEvent event){
        List<Subject> subjects =  event.getSubjects();
        try {
            if (subjects.isEmpty()) {
                throw new UiException("Es muss mindestens ein Subjekt gewählt werden.");
            }

            ModifierFactory subjectModifierFactory = (ModifierFactory) ApplicationService.getSelectedAction().getModifierFactory();
            ApplicationService.selectItem(subjectModifierFactory.create(subjects.get(0).getName()));
            ApplicationService.createCoding();
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            changeCodingFragment(SubjectFragment.newInstance(CODING_FRAGMENT_POSITION, ApplicationService.getAllSubjects()));
        }
        catch(UiException exception){
            ErrorDialog ed = new ErrorDialog(exception, this);
            ed.invoke();
        } catch (FactoryException e) {
            ErrorDialog ed = new ErrorDialog(e.getMessage(), this);
            ed.invoke();
            e.printStackTrace();
        }
    }

    private void changeCodingFragment(Fragment fragment){
        Log.d(LOG_TAG, "Fragment - Change Coding Fragment");
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.root_frame, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusHolder.register(this);
        setContentView(R.layout.activity_main);

        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
                if(position == CODING_FRAGMENT_POSITION){ // CodingFragment
                    Log.i(LOG_TAG, ""+ ApplicationService.getAllSubjects().size());
                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); // drop placeholder fragment
                    changeCodingFragment(SubjectFragment.newInstance(CODING_FRAGMENT_POSITION, ApplicationService.getAllSubjects()));
                }
            }
        });

        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            String title = "";
            Integer resourceTitle = mSectionsPagerAdapter.getPageTitleResource(i);
            if(resourceTitle != null){
                title = getString(resourceTitle);
            }

            actionBar.addTab(
                    actionBar.newTab()
                            .setText(title)
                            .setTabListener(this));
        }


        // TODO: Change Filename;
        ApplicationService.importFile("filename.txt");
       // changeCodingFragment(SubjectFragment.newInstance(1, ApplicationService.getAllSubjects()));
        // replacing Sub

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_open_coding_schema, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

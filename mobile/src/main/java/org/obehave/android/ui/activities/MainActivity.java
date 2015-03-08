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
import org.obehave.android.application.MyApplication;
import org.obehave.android.database.DataHolder;
import org.obehave.android.events.NodeSelectedEvent;
import org.obehave.android.ui.adapters.SectionsPagerAdapter;
import org.obehave.android.ui.events.*;
import org.obehave.android.ui.exceptions.UiException;
import org.obehave.android.ui.fragments.*;
import org.obehave.android.util.ErrorDialog;
import org.obehave.events.EventBusHolder;
import org.obehave.exceptions.FactoryException;
import org.obehave.model.Action;
import org.obehave.model.Node;
import org.obehave.model.Subject;
import org.obehave.model.modifier.ModifierFactory;

import java.util.List;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    private static final int CODING_FRAGMENT_POSITION = 2;
    private static final String ARG_CURRENT_SUBJECT_NODE = "subject_node";
    private static final String ARG_FILENAME = "filename";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Node currentSubjectNode;

    private String activeFragmentTag = "SubjectFragment";

    @Subscribe
    public void onSubjectSelected(SubjectSelectedEvent event) {
        Subject subject = event.getSubject();
        Log.d(LOG_TAG, "onSubjectSelected");
        Log.d(LOG_TAG, subject.getDisplayString());
        changeCodingFragment(ActionFragment.newInstance(CODING_FRAGMENT_POSITION, DataHolder.action().getData(null), DataHolder.action().getChildren(null)));
        MyApplication.selectItem(event.getSubject());
    }

    @Subscribe
    public void onActionSelected(ActionSelectedEvent event) {
        Action action = event.getAction();
        Log.d(LOG_TAG, "onActionSelected");
        Log.d(LOG_TAG, action.getDisplayString());
        try {
            MyApplication.selectItem(action);
            ModifierFactory modifierFactory = MyApplication.getModifierFactoryOfSelectedAction();
            if (modifierFactory == null) {
                MyApplication.createCoding();
            } else if (modifierFactory.getType() == ModifierFactory.Type.SUBJECT_MODIFIER_FACTORY) {
                changeCodingFragment(SubjectModifierFragment.newInstance(CODING_FRAGMENT_POSITION, (modifierFactory).getValidSubjects()));
            } else if (modifierFactory.getType() == ModifierFactory.Type.ENUMERATION_MODIFIER_FACTORY) {
                changeCodingFragment(EnumerationModifierFragment.newInstance(CODING_FRAGMENT_POSITION, (modifierFactory).getValidValues()));
            } else if (modifierFactory.getType() == ModifierFactory.Type.DECIMAL_RANGE_MODIFIER_FACTORY) {
                changeCodingFragment(DecimalRangeModifierFragment.newInstance(CODING_FRAGMENT_POSITION, modifierFactory.getFrom(), modifierFactory.getTo()));
            }
        } catch (UiException ex) {
            ErrorDialog ed = new ErrorDialog(ex.getMessage(), this);
            ed.invoke();
        }
    }

    @Subscribe
    public void onSubjectModifierSelected(SubjectModifierSelectedEvent event) {
        List<Subject> subjects = event.getSubjects();
        try {
            if (subjects.isEmpty()) {
                throw new UiException("Es muss mindestens ein Subjekt gewählt werden.");
            }

            ModifierFactory subjectModifierFactory = (ModifierFactory) MyApplication.getSelectedAction().getModifierFactory();
            MyApplication.selectItem(subjectModifierFactory.create(subjects.get(0).getName()));
            MyApplication.createCoding();
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            changeToSubjectFragment(null);

        } catch (UiException exception) {
            ErrorDialog ed = new ErrorDialog(exception, this);
            ed.invoke();
        } catch (FactoryException e) {
            ErrorDialog ed = new ErrorDialog(e.getMessage(), this);
            ed.invoke();
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onEnumerationModifierSelected(EnumerationModifierSelectedEvent event) {
        List<String> values = event.getValues();
        try {
            if (values.isEmpty()) {
                throw new UiException("Es muss mindestens ein Wert gewählt werden.");
            }

            ModifierFactory enumerationModifierFactory = MyApplication.getSelectedAction().getModifierFactory();
            MyApplication.selectItem(enumerationModifierFactory.create(values.get(0)));
            MyApplication.createCoding();
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            changeToSubjectFragment(null);
        } catch (UiException exception) {
            ErrorDialog ed = new ErrorDialog(exception, this);
            ed.invoke();
        } catch (FactoryException e) {
            ErrorDialog ed = new ErrorDialog(e.getMessage(), this);
            ed.invoke();
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onTimerStartEvent(TimerStartEvent event) {
        Log.d(LOG_TAG, "onTimerStartEvent");
        MyApplication.startTimer();
    }

    @Subscribe
    public void onTimerStopEvent(TimerStopEvent event) {
        Log.d(LOG_TAG, "onTimerStopEvent");
        MyApplication.stopTimer();
    }

    @Subscribe
    public void onDecimalRangeModifierSelected(DecimalRangeModifierSelectedEvent event) {
        String value = event.getValue();
        try {
            ModifierFactory modifierFactory = MyApplication.getSelectedAction().getModifierFactory();
            MyApplication.selectItem(modifierFactory.create(value));
            MyApplication.createCoding();
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            changeToSubjectFragment(null);

        } catch (FactoryException e) {
            ErrorDialog ed = new ErrorDialog(e.getMessage(), this);
            ed.invoke();
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onNodeSelected(NodeSelectedEvent event) {
        if (event.getNodeType() == NodeSelectedEvent.NodeType.SUBJECT) {
            changeToSubjectFragment(event.getNode());
        } else if (event.getNodeType() == NodeSelectedEvent.NodeType.ACTION) {
            changeCodingFragment(ActionFragment.newInstance(CODING_FRAGMENT_POSITION, DataHolder.action().getData(null), DataHolder.action().getChildren(null)));
        }
    }

    private void changeCodingFragment(Fragment fragment) {
        Log.d(LOG_TAG, "Fragment - Change Coding Fragment");
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.root_frame, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void changeToSubjectFragment(Node node) {
        Log.d(LOG_TAG, "changeToSubjectFragment");
        if (node == null) {
            node = DataHolder.subject().getRootNode();
        }
        currentSubjectNode = node;
        Fragment fragment = SubjectFragment.newInstance(CODING_FRAGMENT_POSITION, node);
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


        if (savedInstanceState == null) {
            currentSubjectNode = DataHolder.subject().getRootNode();
        }
        else {
            currentSubjectNode = (Node) savedInstanceState.getSerializable(ARG_CURRENT_SUBJECT_NODE);
        }

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
                if (position == CODING_FRAGMENT_POSITION) { // CodingFragment
                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    changeToSubjectFragment(currentSubjectNode);

                }
            }
        });

        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            String title = "";
            Integer resourceTitle = mSectionsPagerAdapter.getPageTitleResource(i);
            if (resourceTitle != null) {
                title = getString(resourceTitle);
            }

            actionBar.addTab(
                    actionBar.newTab()
                            .setText(title)
                            .setTabListener(this));
        }


        // TODO: Change Filename;


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
        /* TODO: Check if timer is stopped  */
        MyApplication.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ARG_CURRENT_SUBJECT_NODE, currentSubjectNode);
    }

}

package com.example.administrador.myapplication.controllers;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.administrador.myapplication.R;
import com.example.administrador.myapplication.models.entities.ServiceOrder;
import com.example.administrador.myapplication.models.persistence.ServiceOrderContract;
import com.example.administrador.myapplication.util.AppUtil;
import com.melnykov.fab.FloatingActionButton;

import org.apache.http.protocol.HTTP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceOrderListActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    public static final int REQUEST_CODE_ADD = 1;
    public static final int REQUEST_CODE_EDIT = 2;
    private RecyclerView mServiceOrders;
    private ServiceOrderListAdapter mServiceOrdersAdapter;
    private Map<String, String> mFilterColumns = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_order_list_material);

        this.bindElements();
    }

    private void bindElements() {
        mServiceOrders = AppUtil.get(findViewById(R.id.recyclerViewServiceOrders));
        mServiceOrders.setHasFixedSize(true);
        mServiceOrders.setLayoutManager(new LinearLayoutManager(this));

        final FloatingActionButton fabAdd = AppUtil.get(findViewById(R.id.fabAdd));
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent goToAddActivity = new Intent(ServiceOrderListActivity.this, ServiceOrderActivity.class);
                startActivityForResult(goToAddActivity, REQUEST_CODE_ADD);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.updateRecyclerItens(ServiceOrder.getAll());
    }

    private void updateRecyclerItens(List<ServiceOrder> serviceOrders) {
        if (mServiceOrdersAdapter == null) {
            mServiceOrdersAdapter = new ServiceOrderListAdapter(serviceOrders);
            mServiceOrders.setAdapter(mServiceOrdersAdapter);
        } else {
            mServiceOrdersAdapter.setItens(serviceOrders);
            mServiceOrdersAdapter.notifyDataSetChanged();
        }
    }

    private void updateRecyclerItens() {
        List<ServiceOrder> serviceOrders;
        if(mServiceOrdersAdapter != null) {
            if(mServiceOrdersAdapter.getFilters().values().size() > 0) {
                serviceOrders = ServiceOrder.filterBy(mServiceOrdersAdapter.getFilters());
            } else {
                serviceOrders = ServiceOrder.getAll();
            }
        } else {
            serviceOrders = ServiceOrder.getAll();
        }
        updateRecyclerItens(serviceOrders);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_ADD) {
                Toast.makeText(this, R.string.msg_add_success, Toast.LENGTH_LONG).show();
                // Force onPrepareOptionsMenu call
                supportInvalidateOptionsMenu();
            } else if (requestCode == REQUEST_CODE_EDIT) {
                Toast.makeText(this, R.string.msg_edit_success, Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        final ServiceOrder serviceOrder = mServiceOrdersAdapter.getSelectedItem();
        switch (item.getItemId()) {
            case R.id.actionEdit:
                final Intent goToEditActivity = new Intent(ServiceOrderListActivity.this, ServiceOrderActivity.class);
                goToEditActivity.putExtra(ServiceOrderActivity.EXTRA_SERVICE_ORDER, serviceOrder);
                goToEditActivity.putExtra(ServiceOrderActivity.EXTRA_START_BENCHMARK, SystemClock.elapsedRealtime());
                super.startActivityForResult(goToEditActivity, REQUEST_CODE_EDIT);
                return true;
            case R.id.actionStore:
                serviceOrder.setActive(!serviceOrder.isActive()); // alternate state for stored
                String title;
                String message = serviceOrder.isActive() ? getString(R.string.msg_activate_success): getString(R.string.msg_store_success);
                serviceOrder.save();
                Toast.makeText(ServiceOrderListActivity.this, message, Toast.LENGTH_LONG).show();
                updateRecyclerItens();
                return true;
            case R.id.actionDelete:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.lbl_confirm)
                        .setMessage(R.string.msg_delete)
                        .setPositiveButton(R.string.lbl_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Delete and show a message
                                serviceOrder.delete();
                                Toast.makeText(ServiceOrderListActivity.this, R.string.msg_delete_success, Toast.LENGTH_LONG).show();
                                // Update recycler view dataset
                                updateRecyclerItens(ServiceOrder.getAll());
                                // Force onPrepareOptionsMenu call
                                supportInvalidateOptionsMenu();
                            }
                        })
                        .setNeutralButton(R.string.lbl_no, null)
                        .create().show();
                return true;
            case R.id.actionCall:
                // Best Practices: http://stackoverflow.com/questions/4275678/how-to-make-phone-call-using-intent-in-android
                final Intent goToSOPhoneCall = new Intent(Intent.ACTION_CALL /* or Intent.ACTION_DIAL (no manifest permission needed) */);
                goToSOPhoneCall.setData(Uri.parse("tel:" + serviceOrder.getPhone()));
                startActivity(goToSOPhoneCall);
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_service_order_list_actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * @see <a href="http://developer.android.com/guide/components/intents-filters.html">Forcing an app chooser</a>
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final Map<String, String[]> filterApplied = mServiceOrdersAdapter.getFilters();
        switch (item.getItemId()) {
            case R.id.actionShare:
                // Create the text message with a string
                final Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, ServiceOrder.getAll().toString());
                sendIntent.setType(HTTP.PLAIN_TEXT_TYPE);

                // Create intent to show the chooser dialog
                final Intent chooser = Intent.createChooser(sendIntent, getString(R.string.lbl_share_option));

                // Verify the original intent will resolve to at least one activity
                if (sendIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(chooser);
                }
                return true;
            case R.id.actionFilterStored:
                configureActiveFilter(item, filterApplied, ServiceOrderListAdapter.FILTER_BY_STORED);
                updateRecyclerItens();
                return true;
            case R.id.actionFilterActive:
                configureActiveFilter(item, filterApplied, ServiceOrderListAdapter.FILTER_BY_ACTIVATED);
                updateRecyclerItens();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void configureActiveFilter(MenuItem item, Map<String, String[]> filterApplied, String filterValue) {
        String[] newValues;
        if(filterApplied.containsKey(ServiceOrderContract.ACTIVE)) {
            List<String> oldValues = new ArrayList<>();
            oldValues.addAll(Arrays.asList(filterApplied.get(ServiceOrderContract.ACTIVE)));
            filterApplied.remove(ServiceOrderContract.ACTIVE);
            if(!oldValues.remove(filterValue)) {
                oldValues.add(filterValue);
                item.setIcon(R.drawable.ic_action_toggle_check_box);
            } else {
                item.setIcon(R.drawable.ic_action_toggle_check_box_outline_blank);
            }
            newValues = new String[oldValues.size()];
            newValues = oldValues.toArray(newValues);
        } else {
            newValues = new String[1];
            newValues[0] = filterValue;
            item.setIcon(R.drawable.ic_action_toggle_check_box);
        }
        if(newValues.length > 0)
            filterApplied.put(ServiceOrderContract.ACTIVE, newValues);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem menuShare = menu.findItem(R.id.actionShare);
        final boolean menuShareVisible = mServiceOrdersAdapter.getItemCount() > 0;
        menuShare.setEnabled(menuShareVisible).setVisible(menuShareVisible);
        return super.onPrepareOptionsMenu(menu);
    }
}

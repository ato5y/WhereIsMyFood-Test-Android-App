package com.amit.a20170803.whereismyfood;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amit.a20170803.whereismyfood.APIS.MenuAPI;
import com.amit.a20170803.whereismyfood.APIS.OnTaskComplete;
import com.amit.a20170803.whereismyfood.Enums.ActionEnum;
import com.amit.a20170803.whereismyfood.Interface.ValueChangedListener;
import com.amit.a20170803.whereismyfood.WSClasses.Menus;
import com.amit.a20170803.whereismyfood.adapter.NumberPicker;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MenuActivity extends AppCompatActivity implements OnTaskComplete {
    ListView listMenus;
    MenuAPI menuAPI;
    ArrayList<Menus> menus;
    ArrayList<Menus> myCart;
    String branchId;
    String loggedUserId;
    CustomAdapter myCustomAdapter;

    private SearchView searchView;
    private MenuItem searchMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        /*NumberPicker numberPicker = (NumberPicker) findViewById(R.id.number_picker_default);
        numberPicker.setMax(15);
        numberPicker.setMin(5);
        numberPicker.setUnit(2);
        numberPicker.setValue(10);*/
        loggedUserId = (String) getIntent().getSerializableExtra("UserId");
        if (getIntent().getSerializableExtra("MyCart") == null){
            myCart= new ArrayList<Menus>();
        }else{
            myCart = (ArrayList<Menus>) getIntent().getSerializableExtra("MyCart");
        }

        listMenus = (ListView) findViewById(R.id.list_menu);
        branchId = (String) getIntent().getSerializableExtra("BranchId");
        menuAPI = new MenuAPI(new ProgressDialog(this),this,branchId);
        menuAPI.execute();
    }

    @Override
    public void onComplete() {
        try {
            try {

                menus = menuAPI.get();
                 myCustomAdapter = new CustomAdapter(this,android.R.layout.simple_list_item_1,menus);
                listMenus.setAdapter(myCustomAdapter);


            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public class CustomAdapter extends ArrayAdapter
    {
        private LayoutInflater inflater;

        public CustomAdapter(Context context, int resource, ArrayList<Menus> objects)
        {
            super(context, resource, objects);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            if (inflater == null)
            {
                inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            if (convertView == null)
            {
                convertView = inflater.inflate(R.layout.custom_list_menu, null);
            }
            final NumberPicker numberPicker = (NumberPicker) convertView.findViewById(R.id.number_picker_default);
            numberPicker.setMax(1);
            numberPicker.setMin(0);
            numberPicker.setUnit(1);
            numberPicker.setValue(0);

            //final CheckBox checked = (CheckBox) convertView.findViewById(R.id.check_flag);
            TextView plNameTV = (TextView) convertView.findViewById(R.id.pl_name);
            TextView plPriceTV = (TextView) convertView.findViewById(R.id.pl_price);
            TextView plTypeTV = (TextView) convertView.findViewById(R.id.pl_type);


            plNameTV.setText(menus.get(position).getName());
            plPriceTV.setText(menus.get(position).getPrice()+" L.E");
            plTypeTV.setText(menus.get(position).getType());
            //checked.setChecked(menus.get(position).isChecked());

            numberPicker.setValueChangedListener(new ValueChangedListener() {
                @Override
                public void valueChanged(int value, ActionEnum action) {
                    menus.get(position).setQty(numberPicker.getValue());
                    //System.out.println(" sssssssssssss "+numberPicker.getValue());
                }
            });

            /*checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    menus.get(position).setChecked(checked.isChecked());

                    System.out.println(" sssssssssssss "+position);
                }
            });*/

            return convertView;
        }
    }
/*
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_contacts, menu);
        // Action View
        //MenuItem searchItem = menu.findItem(R.id.action_search);
        //SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        // Configure the search info and add any event listeners
        //return super.onCreateOptionsMenu(menu);
        return true;
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_my_contacts, menu);

        /*SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);*/

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int x = 0;
        //myCart.clear();
        //handle presses on the action bar items
        switch (item.getItemId()) {

            case R.id.action_add_contact:
                for (int i = 0;i<menus.size();i++){
                    //if(menus.get(i).isChecked()){
                    if(menus.get(i).getQty()>0){
                        myCart.add(menus.get(i));
                        x++;
                    }
                }
                if(x>0){
                    Intent goTosecondPage = new Intent(this, PickUpMapActivity.class);
                    goTosecondPage.putExtra("MyCart",myCart);
                    goTosecondPage.putExtra("UserId",loggedUserId);
                    startActivity(goTosecondPage);
                }
                //startActivity(new Intent(this, AddContact.class));
                //Toast.makeText(this,"add"+menus.size(),Toast.LENGTH_LONG ).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

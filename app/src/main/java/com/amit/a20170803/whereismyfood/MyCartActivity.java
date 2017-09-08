package com.amit.a20170803.whereismyfood;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.TestLooperManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.amit.a20170803.whereismyfood.APIS.AddOrderAPI;
import com.amit.a20170803.whereismyfood.APIS.OnTaskComplete;
import com.amit.a20170803.whereismyfood.WSClasses.Menus;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MyCartActivity extends AppCompatActivity implements OnTaskComplete{
    ArrayList<Menus> myCart;
    ListView listMyCart;
    TextView totalTxt;
    String loggedUserId;
    int totalVar=0;
    String pickAddress;
    AddOrderAPI addOrderAPI;
    double lat;
    double lng;
    String message = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Intent goTosecondPage = new Intent(MyCartActivity.this, RestaurantActivity.class);
                goTosecondPage.putExtra("MyCart",myCart);
                goTosecondPage.putExtra("UserId",loggedUserId);
                startActivity(goTosecondPage);
            }
        });

        listMyCart = (ListView) findViewById(R.id.list_my_cart);
        totalTxt = (TextView) findViewById(R.id.total_txt);

        pickAddress = (String) getIntent().getSerializableExtra("Address");
        loggedUserId = (String) getIntent().getSerializableExtra("UserId");
        lat =  (double) getIntent().getSerializableExtra("Lat");
        lng = (double) getIntent().getSerializableExtra("Lng");
        myCart = (ArrayList<Menus>) getIntent().getSerializableExtra("MyCart");
        CustomAdapter myCustomAdapter = new CustomAdapter(this,android.R.layout.simple_list_item_1,myCart);
        listMyCart.setAdapter(myCustomAdapter);

        for(int i=0;i<myCart.size();i++){
           totalVar = totalVar + Integer.parseInt(myCart.get(i).getPrice());
        }
        totalTxt.setText(totalVar+" L.E");
        System.out.println("sssssssssssssss last "+loggedUserId);
        System.out.println("sssssssssssssss add "+pickAddress);
    }

    @Override
    public void onComplete() {
        try {
            message = addOrderAPI.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        if (message.equals("1")){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Order sent Successfully")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent goTosecondPage = new Intent(MyCartActivity.this, RestaurantActivity.class);
                            //goTosecondPage.putExtra("MyCart","");
                            goTosecondPage.putExtra("UserId",loggedUserId);
                            startActivity(goTosecondPage);
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }else{

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Error in request!")
                    .setCancelable(true)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();


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
                convertView = inflater.inflate(R.layout.custom_cart_list , null);
            }

            TextView nameTV = (TextView) convertView.findViewById(R.id.pl_name_txt);
            TextView priceTV = (TextView)convertView.findViewById(R.id.pl_price_txt);
            Button delete = (Button) convertView.findViewById(R.id.delete_btn);

            nameTV.setText(myCart.get(position).getName() );
            priceTV.setText(myCart.get(position).getPrice()+"  L.E" );

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myCart.remove(position);
                    notifyDataSetChanged();
                    totalVar = 0;
                    for(int i=0;i<myCart.size();i++){
                        totalVar = totalVar + Integer.parseInt(myCart.get(i).getPrice());
                    }
                    totalTxt.setText(totalVar+" L.E");
                }
            });


            return convertView;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_my_cart, menu);

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
        String x;
        //myCart.clear();
        //handle presses on the action bar items
        switch (item.getItemId()) {

            case R.id.action_add_contact:
                x  =  myCart.get(0).getId();
                for(int i=0;i<myCart.size();i++){
                    if(1==myCart.size()){
                        break;
                    }else{
                        x  = x +","+ myCart.get(i).getId();
                    }
                }
                addOrderAPI = new AddOrderAPI(new ProgressDialog(this),this);
                addOrderAPI.execute("addOrder",loggedUserId,x,pickAddress,lat+"",lng+"");
                //startActivity(new Intent(this, AddContact.class));
                //Toast.makeText(this,"add"+menus.size(),Toast.LENGTH_LONG ).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

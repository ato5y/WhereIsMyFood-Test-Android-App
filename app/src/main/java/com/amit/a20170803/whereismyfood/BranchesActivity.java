package com.amit.a20170803.whereismyfood;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.amit.a20170803.whereismyfood.APIS.BranchesAPI;
import com.amit.a20170803.whereismyfood.APIS.OnTaskComplete;
import com.amit.a20170803.whereismyfood.WSClasses.Branches;
import com.amit.a20170803.whereismyfood.WSClasses.Menus;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class BranchesActivity extends AppCompatActivity implements OnTaskComplete , View.OnClickListener ,AdapterView.OnItemClickListener {
    ListView listBranches;
    BranchesAPI branchesAPI;
    ArrayList<Branches> branches;
    String resId;
    String loggedUserId;
    Button showMapBtn;
    ArrayList<Menus> myCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branches);

        listBranches = (ListView) findViewById(R.id.branches_list);
        showMapBtn = (Button) findViewById(R.id.show_map);

        loggedUserId = (String) getIntent().getSerializableExtra("UserId");
        myCart = (ArrayList<Menus>) getIntent().getSerializableExtra("MyCart");
        resId = (String) getIntent().getSerializableExtra("ResId");
        branchesAPI = new BranchesAPI(new ProgressDialog(this),this,resId);
        branchesAPI.execute();

        showMapBtn.setOnClickListener(this);
        listBranches.setOnItemClickListener(this);
    }

    @Override
    public void onComplete() {

        try {
            try {

                branches = branchesAPI.get();
                CustomAdapter myCustomAdapter = new CustomAdapter(this,android.R.layout.simple_list_item_1,branches);
                listBranches.setAdapter(myCustomAdapter);


            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {
        if(view == showMapBtn){
            Intent goTosecondPage = new Intent(this, MapsActivity.class);
            goTosecondPage.putExtra("AllBranches",branches);
            goTosecondPage.putExtra("MyCart",myCart);
            goTosecondPage.putExtra("UserId",loggedUserId);
            startActivity(goTosecondPage);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent goTosecondPage = new Intent(this, MenuActivity.class);
        goTosecondPage.putExtra("BranchId",branches.get(i).getId());
        goTosecondPage.putExtra("MyCart",myCart);
        goTosecondPage.putExtra("UserId",loggedUserId);
        startActivity(goTosecondPage);
    }


    public class CustomAdapter extends ArrayAdapter
    {
        private LayoutInflater inflater;

        public CustomAdapter(Context context, int resource, ArrayList<Branches> objects)
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
                convertView = inflater.inflate(R.layout.custom_list_branches, null);
            }

            TextView branchTV = (TextView) convertView.findViewById(R.id.branch_name_txt);

            branchTV.setText(branches.get(position).getName());

            return convertView;
        }
    }



}

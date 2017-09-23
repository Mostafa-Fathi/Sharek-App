package com.example.khalid.sharektest;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.khalid.sharektest.Utils.AppController;
import com.example.khalid.sharektest.Utils.Proposal;
import com.example.khalid.sharektest.Utils.ProposalCustomAdapter;
import com.example.khalid.sharektest.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Khalid on 7/30/2016.
 */
public class Tab3 extends android.support.v4.app.Fragment implements AdapterView.OnItemClickListener, AdapterView.OnClickListener {

    TextView ownerOk;
    ListView listView;
    TextView noProposals;
    ArrayList<Proposal> proposals = new ArrayList<>();
    //ProposalCustomAdapter proposalCustomAdapter;
    String token, myUserName, price, duration, pieces, posterID, user, startDate,phone;
    JSONObject proposalRequest;
    boolean isAccepted;
    Button showUsersInfo, showPostersInfo;
    String proposalUserName, proposalPosterID,proposalPhone;
    //    LocationManager locationManager;
    public static final int MY_PERMISSIONS_REQUEST_CALL = 1;

    public Tab3() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment


        final View view = inflater.inflate(R.layout.tab3, container, false);
        listView = (ListView) view.findViewById(R.id.myProfile_listView);
        listView.setOnItemClickListener(this);
        listView.setOnItemClickListener(this);
        SharedPreferences mypreference = PreferenceManager.getDefaultSharedPreferences(getContext());
        token = mypreference.getString("token", "value");
        myUserName = mypreference.getString("myUserName", "value");
        Log.i("Token in My proposals", token);
        noProposals = (TextView) view.findViewById(R.id.ProductPage_Proposals_NoProposals);

        String url = "https://api.sharekeg.com/proposals";

//        pDialog = new ProgressDialog(this);
//        pDialog.setMessage("Loading...");
//        pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Response: ", response.toString());
//                        pDialog.hide();
                        if (response.length() == 0) {
                            noProposals.setVisibility(view.VISIBLE);
                            Toast.makeText(getContext(), "You have no proposals", Toast.LENGTH_LONG).show();

                        } else {
                            //Toast.makeText(getContext(), "Check your proposals", Toast.LENGTH_LONG).show();

                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject jsonResponse = response.getJSONObject(i);
//                                  String title = jsonResponse.get("title").toString();
//                                  String description = jsonResponse.get("description").toString();
                                    price = jsonResponse.get("price").toString();
                                    duration = jsonResponse.get("duration").toString();
                                    pieces = jsonResponse.get("pieces").toString();
                                    startDate = jsonResponse.get("from").toString();
                                    posterID = jsonResponse.get("posterId").toString();
                                    user = jsonResponse.get("user").toString();
                                    isAccepted = jsonResponse.getBoolean("accepted");
                                    if (isAccepted == true){
                                        phone = jsonResponse.get("phone").toString();
                                        Proposal proposal = new Proposal(" Some one proposed to you", "Hey, I want to offer you a deal ", price, duration, pieces, startDate, posterID, isAccepted, user,phone);
                                        proposals.add(proposal);
                                    }else {
                                        Proposal proposal = new Proposal(" Some one proposed to you", "Hey, I want to offer you a deal ", price, duration, pieces, startDate, posterID, isAccepted, user,"");
                                        proposals.add(proposal);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            ProposalCustomAdapter proposalCustomAdapter = new ProposalCustomAdapter(getContext(), proposals);
                            listView.setAdapter(proposalCustomAdapter);


                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//               Log.i("Error: ", error.getMessage());
                Toast.makeText(getContext(), "you cannot connect to the internet", Toast.LENGTH_LONG).show();
//                pDialog.hide();
            }
        }) {

            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/json");
//                headers.put("Authorization", "Bearer " + token);
//                return headers;
                Utils utils = new Utils();

                return utils.getRequestHeaders(token);
            }
        };

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);


        return view;


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Proposal proposal = proposals.get(position);
        proposalUserName = proposal.getUser();
        proposalPosterID = proposal.getPosterId();
        proposalPhone = proposal.getPhone();

        if (proposal.isAccepted()) {
            Toast.makeText(getContext(), "This proposal is accepted", Toast.LENGTH_LONG).show();

            //K.A: Disabled for next release
//            Intent intent = new Intent(getContext(),RatingActivity.class);
//            intent.putExtra("poster_user",proposal.getUser());
//            intent.putExtra("poster_id",proposal.getPosterId());
//            startActivity(intent);
            checkLocationPermission();
            String url = "https://api.sharekeg.com/user/" + proposalUserName;
            callOwnerinfo(url, "phone", false);


        } else {

            LayoutInflater inflater = LayoutInflater.from(getContext());
            final View yourCustomView = inflater.inflate(R.layout.proposal_data_dialog, null);
            final TextView start = (TextView) yourCustomView.findViewById(R.id.ProposalInfoDialog_TextView_startDate);
            String startDate = "Start date:     " + proposal.getStartDate().substring(0, proposal.getStartDate().indexOf("T"));
            start.setText(startDate);
            final TextView duration = (TextView) yourCustomView.findViewById(R.id.ProposalInfoDialog_TextView_duration);
            String durationStr = "Duration:     " + proposal.getDuration() + "    Days";
            duration.setText(durationStr);
            final TextView pieces = (TextView) yourCustomView.findViewById(R.id.ProposalInfoDialog_TextView_pieces);
            String piecesStr = "Piece:     " + proposal.getPieces();
            pieces.setText(piecesStr);
            showUsersInfo = (Button) yourCustomView.findViewById(R.id.ProposalInfoDialog_button_userInfo);
            showUsersInfo.setOnClickListener(Tab3.this);
            showPostersInfo = (Button) yourCustomView.findViewById(R.id.ProposalInfoDialog_button_posterInfo);
            showPostersInfo.setOnClickListener(Tab3.this);
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("");
            builder.setView(yourCustomView);
            builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {

                    try {
                        proposalRequest = new JSONObject();
                        proposalRequest.put("accept", true);
                        Log.i("Final_request", proposalRequest.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i("Request_exception", e.toString());
                    }
                    final String URL = "https://api.sharekeg.com/poster/" + proposal.getPosterId() + "/" + proposal.getUser() + "/react";
                    Log.i("Acceptance_URL", URL);

                    final JsonObjectRequest req = new JsonObjectRequest(URL, proposalRequest,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.i("response", response.toString());
                                    Toast.makeText(getContext(), "You Win 5 points , share more ", Toast.LENGTH_LONG).show();
                                    checkLocationPermission();
                                    String url = "https://api.sharekeg.com/user/" + proposalUserName;
                                    callOwnerinfo(url,"phone", true);
//                                    Toast.makeText(getContext(), "You have accepted this proposal, Your contact details wil be sent by email", Toast.LENGTH_LONG).show();


                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // handle error
                            if (error.networkResponse != null) {
                                if (error.networkResponse.statusCode == 400) {
                                    Log.i("Error string", error.networkResponse.data.toString());
                                }
                            }
                            Log.i("error", error.toString());
                        }
                    }) {

                        public String getBodyContentType() {
                            return "application/json";
                        }

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Utils utils = new Utils();

                            return utils.getRequestHeaders(token);
                        }
                    };
                    AppController.getInstance().addToRequestQueue(req);

                }
            });
            builder.setNegativeButton("Ignore", null);
            AlertDialog dialog = builder.create();
            dialog.show();

        }

    }

    @Override
    public void onClick(View v) {
        if (v == showUsersInfo) {
            String url = "https://api.sharekeg.com/user/" + proposalUserName;
            callOwnerinfo(url, "work", false);
        } else if (v == showPostersInfo) {
            Intent intent = new Intent(getContext(), ProductPage.class);
            intent.putExtra("product_id", proposalPosterID);
            startActivity(intent);
        } else if (v == ownerOk) {
            Intent intent = new Intent(getContext(), HomePage.class);
            startActivity(intent);
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + proposalPhone));
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
            try {
                startActivity(callIntent);
            } catch (SecurityException e) {
                Toast.makeText(getContext(), "Please Allow app to make phone calls from settings", Toast.LENGTH_SHORT).show();
            }
        }


    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.CALL_PHONE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getContext())
                        .setTitle("Get Call phone ")
                        .setMessage("we need to  call this number to contact")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{android.Manifest.permission.CALL_PHONE},
                                        MY_PERMISSIONS_REQUEST_CALL);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_CALL);
            }
            return false;
        } else {
            return true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    return;


                }
                // permission was granted, yay! Do the
                // location-related task you need to do.
                if (ContextCompat.checkSelfPermission(getContext(),
                        android.Manifest.permission.CALL_PHONE)
                        == PackageManager.PERMISSION_GRANTED) {

                    //Request location updates:
//                    locationManager.requestLocationUpdates(provider, 400, 1, (android.location.LocationListener) getContext());
                    return;
                }


            }

        }
    }

    public void callOwnerinfo(String url, final String type, final Boolean button) {

        JsonObjectRequest req = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Response: ", response.toString());

                        try {
                            LayoutInflater inflater = LayoutInflater.from(getContext());
                            final View yourCustomView = inflater.inflate(R.layout.owner_info_dialog, null);

                            final TextView ownerName = (TextView) yourCustomView.findViewById(R.id.ownerInfoDialog_name);
                            String userFullName = response.getJSONObject("name").get("first").toString() + " " + response.getJSONObject("name").get("last").toString();
                            ownerName.setText(userFullName);
                            final TextView ownerAddress = (TextView) yourCustomView.findViewById(R.id.ownerInfoDialog_homeAddress);
                            if (response.has("address")) {
                                ownerAddress.setText(response.get("address").toString());
                            } else {
                                ownerAddress.setText("Not provided");
                            }
                            final TextView ownerCall = (TextView) yourCustomView.findViewById(R.id.ownerInfoDialog_Button_call);
                            ownerCall.setVisibility(View.VISIBLE);
                            ownerCall.setOnClickListener(Tab3.this);
                            ownerOk = (TextView) yourCustomView.findViewById(R.id.ownerInfoDialog_Button_ok);

                            final TextView ownerWork = (TextView) yourCustomView.findViewById(R.id.ownerInfoDialog_work);

                            if (type == "phone") {
                                ownerWork.setText(proposalPhone);
                            } else {
                                ownerWork.setText(response.get("work").toString());
                            }
                            final TextView ownerPoints = (TextView) yourCustomView.findViewById(R.id.ownerInfoDialog_points);

                            if (response.has("points")) {
                                ownerPoints.setText(response.get("points").toString());
                            } else {
                                ownerPoints.setText("Not provided");
                            }

                            if (button == true) {
                                AlertDialog dialog = new AlertDialog.Builder(getContext())
                                        .setTitle(null)
                                        .setView(yourCustomView).setCancelable(false).create();
                                ownerOk.setVisibility(View.VISIBLE);
                                ownerOk.setOnClickListener(Tab3.this);
                                dialog.show();
                                // make ok button on center

                            } else {
                                AlertDialog dialog = new AlertDialog.Builder(getContext())
                                        .setTitle(null)
                                        .setView(yourCustomView)
                                        .setPositiveButton(null, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {

                                            }
                                        }).create();
                                dialog.show();


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error: ", error.toString());
                Toast.makeText(getContext(), Utils.GetErrorDescription(error, getContext()), Toast.LENGTH_SHORT).show();
            }
        }) {

            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Utils utils = new Utils();

                return utils.getRequestHeaders(token);
            }
        };
        AppController.getInstance().addToRequestQueue(req);
    }


}
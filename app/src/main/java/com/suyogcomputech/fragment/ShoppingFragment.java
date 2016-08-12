package com.suyogcomputech.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.suyogcomputech.adapter.OnlineShoppingAdapter;
import com.suyogcomputech.helper.ConnectionDetector;
import com.suyogcomputech.helper.Constants;
import com.suyogcomputech.helper.OnlineShopping;
import com.suyogcomputech.sms.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Pintu on 8/12/2016.
 */
public class ShoppingFragment extends Fragment {
    ConnectionDetector detector;
    RecyclerView rcvFacilities;
    OnlineShoppingAdapter adapter;
    ArrayList<OnlineShopping> list;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping, container, false);
        detector=new ConnectionDetector(getActivity());
        rcvFacilities = (RecyclerView) view.findViewById(R.id.rcvFacilities);
        if (detector.isConnectingToInternet()) {
            new FetchFacilities().execute(Constants.URL_FACILITIES);
        } else
            Toast.makeText(getActivity(), Constants.dialog_message, Toast.LENGTH_LONG).show();

        return view;
    }
    private class FetchFacilities extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                // urlConnection.setConnectTimeout(30000);
                urlConnection.setDoInput(true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(urlConnection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return response.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setTitle(Constants.progress_dialog_title);
            dialog.setMessage(Constants.progress_dialog_message);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                dialog.dismiss();
                Log.i("response", s);
                JSONObject objJson = new JSONObject(s);
                JSONArray jsonArray = new JSONArray(objJson.getString(Constants.FACILITIES));
                list = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    OnlineShopping facilities = new OnlineShopping();
                    facilities.setTitle(jsonObject.getString(Constants.TITLE));
                    facilities.setImageUrl(jsonObject.getString(Constants.IMAGE));
                    list.add(facilities);
                }

                adapter = new OnlineShoppingAdapter(list, getActivity());
                rcvFacilities.setAdapter(adapter);
                rcvFacilities.setHasFixedSize(true);
                GridLayoutManager glm = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
                rcvFacilities.setLayoutManager(glm);

            } catch (NullPointerException e) {
                Toast.makeText(getActivity(), Constants.null_pointer_message, Toast.LENGTH_LONG).show();
                getActivity().finish();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException ex) {
            }
        }
    }
}

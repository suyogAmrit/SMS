package com.suyogcomputech.adapter;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.suyogcomputech.helper.AppConstants;
import com.suyogcomputech.helper.AppHelper;
import com.suyogcomputech.helper.Doctor;
import com.suyogcomputech.helper.FileDownloader;
import com.suyogcomputech.sms.R;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Pintu on 9/1/2016.
 */
public class MyReportsAdapter extends RecyclerView.Adapter<MyReportsAdapter.ShowTariffsViewHolder> {
    List<Doctor> myItems;
    Context myContext;
    ImageLoader myImageLoader;
    int focusedItem = 0;
    private int screenWidth;

    public MyReportsAdapter(List<Doctor> myItems, Context myContext) {
        this.myItems = myItems;
        this.myContext = myContext;
        WindowManager wm = (WindowManager) myContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
    }

    @Override
    public MyReportsAdapter.ShowTariffsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_my_report, null);
        MyReportsAdapter.ShowTariffsViewHolder holder = new MyReportsAdapter.ShowTariffsViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyReportsAdapter.ShowTariffsViewHolder holder, int position) {
        final Doctor myItem = myItems.get(position);
        holder.itemView.setSelected(focusedItem == position);
        holder.getLayoutPosition();
        holder.txtReportType.setText(myItem.getType());
        holder.txtDate.setText("Date : "+myItem.getRequestDate());
        holder.btnShowReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppHelper.isConnectingToInternet(myContext)) {
                    new DownloadFile().execute("http://192.168.12.100/apms/PDF_Files/data.pdf", "a.pdf");
                } else
                    Toast.makeText(myContext, AppConstants.dialog_message, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != myItems ? myItems.size() : 0);
    }

    public class ShowTariffsViewHolder extends RecyclerView.ViewHolder {
        TextView txtReportType,txtDate;
        Button btnShowReport;

        public ShowTariffsViewHolder(View itemView) {
            super(itemView);
            this.txtReportType=(TextView)itemView.findViewById(R.id.txtType);
            this.txtDate=(TextView)itemView.findViewById(R.id.txtAppointmentDate);
            this.btnShowReport=(Button)itemView.findViewById(R.id.btnShowReport);

        }
    }

    private class DownloadFile extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=new ProgressDialog(myContext);
            dialog.setTitle("jbsd");
            dialog.setMessage("sjdbnsdj");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            File pdfFile = new File(Environment.getExternalStorageDirectory() + "/testReport/" + "a.pdf");  // -> filename = maven.pdf
            Uri path = Uri.fromFile(pdfFile);
            dialog.dismiss();
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(path, "application/pdf");
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try{
                myContext.startActivity(pdfIntent);
            }catch(ActivityNotFoundException e){
                Toast.makeText(myContext, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "testReport");
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try{
                pdfFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);
            return null;
        }
    }

}
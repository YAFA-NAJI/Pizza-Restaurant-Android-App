package com.example.project;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
import java.util.List;

public class Connection extends AsyncTask<String, Void, String> {
    private final Activity activity;

    public Connection(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ((MainActivity) activity).setButtonText("Connecting");
        ((MainActivity) activity).setProgress(true);
    }

    @Override
    protected String doInBackground(String... params) {
        if (params.length > 0) {
            return HttpManager.getData(params[0]);
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        ((MainActivity) activity).setProgress(false);

        if (result != null) {
            List<String> types = ResturentJasonParser.getObjectFromJson(result);
            if (types != null) {
                ((MainActivity) activity).fillTypes(types);
                Toast.makeText(activity, "Connected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity, Registration.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                activity.startActivity(intent);
                return;
            }
        }
        ((MainActivity) activity).reset(); // Call the reset method on connection error
        ((MainActivity) activity).setButtonText("Connection Error");
    }
}

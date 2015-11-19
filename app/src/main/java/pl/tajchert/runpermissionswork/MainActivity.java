package pl.tajchert.runpermissionswork;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE_CALL = 32;

    @Bind(R.id.main_layout)
    View mLayout;//We will use it later

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //TODO monitor permission with Nammu library, use Nammu.init() to init library
        //TODO and Nammu.permissionCompare() to check if permissions changed - onResume is recommended
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick(R.id.buttonCall)
    public void clickButtCall() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "We don't have any permission for calling");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                //User already refused to give us this permission or removed it
                //Now he/she can mark "never ask again" (sic!), so we better explain why we need it
                Snackbar.make(mLayout, "Here we explain to user why we neeed to use call feature",
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_CALL);
                            }
                        })
                        .show();
            } else {
                // We do not need to explain - first time asking for permission
                // or phone doesn't offer permission
                // or user marked "never ask again"
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_CALL);
            }
        } else {
            callTest();
        }
    }

    private void callTest() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + "11122233"));
        try {
            startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case (REQUEST_CODE_CALL):
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callTest();
                }
                break;
            //Here goes other permissions, for example if you ask for Location permission, here handle as well result
        }
    }
}

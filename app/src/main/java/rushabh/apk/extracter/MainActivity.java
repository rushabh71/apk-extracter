package rushabh.apk.extracter;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 1, Menu.NONE, "Settings");
        menu.add(Menu.NONE, 2, Menu.NONE, "About Me");
        menu.add(Menu.NONE, 3, Menu.NONE, "Exit");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 2:

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);

                builderSingle.setTitle("About Me");
                builderSingle.setMessage("Hey, I am Rushabh Patel and This is a simple app for extracting already installed application. :)");

                builderSingle.show();

                return true;

            case 3:

                finish();

                return true;

            case 1:

                return true;

            default:
                return false;
        }
    }
}

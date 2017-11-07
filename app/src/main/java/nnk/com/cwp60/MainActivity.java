package nnk.com.cwp60;

import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    ListView lv;
    AutoCompleteTextView actv;
    boolean status = true;
    ArrayList al;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView)findViewById(R.id.lv);
        actv = (AutoCompleteTextView)findViewById(R.id.actv);

        showAllFolders();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);

        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.my_options,menu);

        return true;
    }

    public void createNewFolder(MenuItem mi)
    {
        final EditText et = new EditText(this);
        et.setHint("Folder Name");

        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setIcon(R.drawable.folder);
        ad.setTitle("New Folder");
        ad.setView(et);
        ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String folder_name = et.getText().toString().trim();

                String path = Environment.getExternalStorageDirectory()+"/"+folder_name;

                File f = new File(path);
                boolean res = f.mkdir();
                if (res)
                {
                    Toast.makeText(MainActivity.this, "New Folder Added", Toast.LENGTH_SHORT).show();
                    showAllFolders();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Sorry Folder Not Created", Toast.LENGTH_SHORT).show();
                }

            }
        });
        ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        ad.setCancelable(false);
        ad.show();

    }

    public void createNewFile(MenuItem  mi)
    {
        LayoutInflater li = getLayoutInflater();
        View v = li.inflate(R.layout.new_file_layout,null,false);

        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setIcon(R.drawable.archive);
        ad.setTitle("New File");
        ad.setView(v);
        final AlertDialog a = ad.create();
        a.show();


        final EditText et1 = (EditText)v.findViewById(R.id.et1);
        final EditText et2 = (EditText)v.findViewById(R.id.et2);
        Button b = (Button)v.findViewById(R.id.b1);

        b.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String filename = et1.getText().toString().trim();
                String content = et2.getText().toString().trim();

                String path = Environment.getExternalStorageDirectory()+"/"+filename;
                byte b[] = content.getBytes();
                try
                {
                    FileOutputStream fout = new FileOutputStream(path);
                    fout.write(b);
                    fout.close();
                    Toast.makeText(MainActivity.this, "File Created and Content Written", Toast.LENGTH_SHORT).show();
                    showAllFolders();
                    a.dismiss();
                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void search(MenuItem mi)
    {
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,al);
        actv.setAdapter(aa);
        
        if (status)
        {
            actv.setVisibility(View.VISIBLE);
            status = false;
        }
        else
        {
            actv.setVisibility(View.GONE);
            status = true;
        }
    }

    public void deleteFolder(MenuItem mi)
    {

    }

    public void deleteFile(MenuItem mi)
    {

    }

    public void showAllFolders()
    {
       // File f = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

        File f = Environment.getExternalStorageDirectory();
        String path = f.getAbsolutePath();
        Log.e("Path 1 ",path);

        File files[] =  f.listFiles();

        al = new ArrayList();

        // logic to display all files and DIR's
        for (int i=0;i<files.length;i++)
        {
            String name = files[i].getName();
            al.add(name);
        }

       /*//logic to display only files
        for (int i=0;i<files.length;i++)
        {
            if (files[i].isFile())
            {
                String name = files[i].getName();
                al.add(name);
            }
        }*/

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,al);
        lv.setAdapter(aa);
    }
}

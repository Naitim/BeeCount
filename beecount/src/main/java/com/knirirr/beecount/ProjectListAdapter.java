package com.knirirr.beecount;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.knirirr.beecount.database.Project;

public class ProjectListAdapter extends ArrayAdapter<Project>
{
  private static final String TAG = "BeeCountProjectListAdapater";
  Context context;
  int layoutResourceId;
  List<Project> projects = null;
  private Context mContext;
  private BeeCountApplication beeCount;


  // Constructor
  public ProjectListAdapter(Context context, int layoutResourceId, List<Project> projects)
  {
    super(context, layoutResourceId, projects);
    this.layoutResourceId = layoutResourceId;
    this.context = context;
    this.projects = projects;
    mContext = context;
    beeCount = (BeeCountApplication) context.getApplicationContext();
  }

  static class ProjectHolder
  {
    TextView txtTitle;
    TextView txtDate;
    ImageButton deleteProject;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent)
  {
    View row = convertView;
    ProjectHolder holder = null;

    if(row == null)
    {
      LayoutInflater inflater = ((Activity)context).getLayoutInflater();
      row = inflater.inflate(layoutResourceId, parent, false);

      holder = new ProjectHolder();
      holder.txtTitle = (TextView) row.findViewById(R.id.txtTitle);
      holder.txtDate  = (TextView) row.findViewById(R.id.txtDate);
      holder.deleteProject = (ImageButton) row.findViewById(R.id.deleteProject);

      holder.txtTitle.setOnClickListener(mOnTitleClickListener);
      holder.txtDate.setOnClickListener(mOnDateClickListener);
      holder.deleteProject.setOnClickListener(mOnDeleteClickListener);

      holder.txtTitle.setTag(position);
      holder.txtDate.setTag(position);
      holder.deleteProject.setTag(position);

      row.setTag(holder);
    }
    else
    {
      holder = (ProjectHolder)row.getTag();
    }

    Project project = projects.get(position);
    holder.txtTitle.setText(project.name);
    holder.txtDate.setText(project.getDate());

    return row;
  }

  /*
   * Start counting by clicking on date or title, delete by clicking on button.
   */

  private View.OnClickListener mOnTitleClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v)
    {
      Integer position = (Integer) v.getTag();
      //Log.v(TAG, "Title clicked, row: " + position);
      Project p = projects.get(position);
      Intent intent = new Intent(getContext(), CountingActivity.class);
      beeCount.project_id = p.id;
      mContext.startActivity(intent);
    }
  };

  private View.OnClickListener mOnDateClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v)
    {
      Integer position = (Integer) v.getTag();
      //Log.v(TAG, "Date clicked, row: " + v.getTag());
      Project p = projects.get(position);
      Intent intent = new Intent(getContext(), CountingActivity.class);
      beeCount.project_id = p.id;
      mContext.startActivity(intent);
    }
  };

  private View.OnClickListener mOnDeleteClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v)
    {
      final Integer position = (Integer) v.getTag();
      //Log.v(TAG, "Delete clicked, row: " + v.getTag());
      final Project p = projects.get(position);
      // http://developer.android.com/guide/topics/ui/dialogs.html#AlertDialog
      // could make the dialog central in the popup - to do later
      AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
      builder.setIcon(android.R.drawable.ic_dialog_alert);
      builder.setMessage(p.name + ": " + mContext.getString(R.string.confirmDelete)).setCancelable(false).setPositiveButton(R.string.deleteButton, new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface dialog, int id)
        {
          // perform the deleting in the activity

          ((ListProjectActivity) mContext).deleteProject(p,position);
        }
      }).setNegativeButton(R.string.cancelButton, new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface dialog, int id)
        {
          dialog.cancel();
        }
      });
      AlertDialog alert = builder.create();
      alert.show();
    }
  };



}

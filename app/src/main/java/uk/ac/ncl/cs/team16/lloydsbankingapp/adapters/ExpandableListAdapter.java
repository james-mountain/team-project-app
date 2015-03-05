package uk.ac.ncl.cs.team16.lloydsbankingapp.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.content.Context;
import java.util.List;
import java.util.HashMap;
import android.view.LayoutInflater;
import android.widget.TextView;
import uk.ac.ncl.cs.team16.lloydsbankingapp.R;
import android.graphics.Typeface;

/**
 * Created by Roy on 18/02/2015.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter{

    private Context context;
    private HashMap<String, List<String>> listDataItems;
    private List<String> listDataHeaders;

    public ExpandableListAdapter(Context context, HashMap<String, List<String>> listDataItems, List<String> listDataHeaders) {

        this.context = context;
        this.listDataItems = listDataItems;
        this.listDataHeaders = listDataHeaders;
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeaders.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataItems.get(this.listDataHeaders.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeaders.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return this.listDataItems.get(this.listDataHeaders.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.help_list_header, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.helpListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.help_list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.helpListItem);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

package com.cnergee.mypage.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnergee.mypage.obj.PackageList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;


import androidx.cardview.widget.CardView;
import cnergee.sikka.broadband.R;


//public class CustomGridAdapter extends BaseAdapter {
//
//    private Context context;
//    ArrayList<PackageList> arrayList;
//
//    //Constructor to initialize values
//    public CustomGridAdapter(Context context, ArrayList<PackageList> arrayList) {
//        this.context        = context;
//        this.arrayList     = arrayList;
//    }
//
//    @Override
//    public int getCount() {
//
//        // Number of times getView method call depends upon gridValues.length
//        return arrayList.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//
//        return null;
//    }
//
//    @Override
//    public long getItemId(int position) {
//
//        return 0;
//    }
//
//    @Override
//    public int getViewTypeCount() {
//
//        return getCount();
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//
//        return position;
//    }
//
//
//    // Number of times getView method call depends upon gridValues.length
//
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        // LayoutInflator to call external grid_item.xml file
//
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        View gridView;
//
//        if (convertView == null) {
//
//            gridView = new View(context);
//
//            gridView = inflater.inflate( R.layout.horizontal_item_view , null);
//
//            TextView tv_days = (TextView) gridView.findViewById(R.id.tv_days);
//            TextView tv_pkg_name = (TextView) gridView.findViewById(R.id.tv_pkg_name);
//            TextView tv_validity = (TextView) gridView.findViewById(R.id.tv_validity);
//
//            LinearLayout ll_amount =(LinearLayout)gridView.findViewById(R.id.ll_amount);
//
//            TextView tv_desc = (TextView) gridView.findViewById(R.id.tv_desc);
//            tv_desc.setTextColor(context.getResources().getColor(R.color.help_calc));
//
//            tv_days.setText(context.getResources().getString(R.string.rs)+ " "+ Math.round(Double.parseDouble(arrayList.get(position).getPackageRate())));
//            Typeface face = Typeface.createFromAsset(context.getAssets(),
//                    "fonts/UbuntuCondensed-Regular.ttf");
//            tv_days.setTypeface(face);
//
//            tv_pkg_name.setText(arrayList.get(position).getPlanName());
//            Typeface face1 = Typeface.createFromAsset(context.getAssets(),
//                    "fonts/UbuntuCondensed-Regular.ttf");
//            tv_pkg_name.setTypeface(face1);
//
//            Log.e("Offer Desc",":"+arrayList.get(position).getOfferdesc());
//
//            //tv_validity.setText(arrayList.get(position).getPackagevalidity() + " Days");
//            try {
//                if (arrayList.get(position).getOfferdesc().length() > 0 || arrayList.get(position).getOfferdesc() != null || !arrayList.get(position).getOfferdesc().equalsIgnoreCase("null")) {
//                    tv_validity.setText(arrayList.get(position).getOfferdesc());
//                    tv_validity.setVisibility(View.VISIBLE);
//                } else {
//                    tv_validity.setVisibility(View.GONE);
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//
//            tv_desc.setText(arrayList.get(position).getPackagedesc());
//            Typeface face2 = Typeface.createFromAsset(context.getAssets(),
//                    "fonts/Neuton_Regular.ttf");
//            tv_desc.setTypeface(face2);
//            tv_validity.setTypeface(face2);
//            String text = arrayList.get(position).getPackageRate();
//            Double intVal = 0.00;
//
//            try {
//                intVal = Double.parseDouble(text);
//
//                Log.e("intVal",":"+intVal);
//                if(intVal <= 1000){
//                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg1));
//                }else if(intVal > 1000 && intVal<=2000){
//                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg2));
//                }else if(intVal > 2000 && intVal<=5000){
//                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg3));
//                }else if(intVal > 5000 && intVal<=10000){
//                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg4));
//                }else{
//                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg5));
//                }
//            } catch (NumberFormatException e) {
//                intVal = Double.parseDouble(arrayList.get(position).getPackageRate());
//                if(intVal <= 1000){
//                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg1));
//                }else if(intVal > 1000 && intVal<=2000){
//                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg2));
//                }else if(intVal > 2000 && intVal<=5000){
//                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg3));
//                }else if(intVal > 5000 && intVal<=10000){
//                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg4));
//                }else{
//                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg5));
//                }
//            }
//
//        } else {
//            gridView = (View) convertView;
//        }
//
//        return gridView;
//    }
//}

public class CustomGridAdapter extends BaseExpandableListAdapter {

    private List<Integer> expandableListTitle;
    private SortedMap<Integer, ArrayList<PackageList>> expandableListDetail;
    private Context context;

    public CustomGridAdapter(Context context, List<Integer> expandableListTitle,
                             SortedMap<Integer, ArrayList<PackageList>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        PackageList expandedListText = (PackageList) getChild(listPosition, expandedListPosition);
        Log.e("expandedListText",":-"+expandedListText);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.horizontal_item_view, null);
        }
//        TextView expandedListTextView = (TextView) convertView
//                .findViewById(R.id.expandedListItem);
//        expandedListTextView.setText(expandedListText);


//
//            TextView tv_days = (TextView) convertView.findViewById(R.id.tv_days);
//            TextView tv_pkg_name = (TextView) convertView.findViewById(R.id.tv_pkg_name);
//            TextView tv_validity = (TextView) convertView.findViewById(R.id.tv_validity);
//
//            LinearLayout ll_amount =(LinearLayout)convertView.findViewById(R.id.ll_amount);
//
//            TextView tv_desc = (TextView) convertView.findViewById(R.id.tv_desc);
//            tv_desc.setTextColor(context.getResources().getColor(R.color.help_calc));
//
////            tv_days.setText(context.getResources().getString(R.string.rs)+ " "+ Math.round(Double.parseDouble(arrayList.get(position).getPackageRate())));
//            Typeface face = Typeface.createFromAsset(context.getAssets(),
//                    "fonts/UbuntuCondensed-Regular.ttf");
//        Log.e("getChildView", "----" );
//            tv_days.setTypeface(face);

         TextView tv_days = (TextView) convertView.findViewById(R.id.tv_days);
            TextView tv_pkg_name = (TextView) convertView.findViewById(R.id.tv_pkg_name);
            TextView tv_validity = (TextView) convertView.findViewById(R.id.tv_validity);

            LinearLayout ll_amount =(LinearLayout)convertView.findViewById(R.id.ll_amount);

            TextView tv_desc = (TextView) convertView.findViewById(R.id.tv_desc);
            tv_desc.setTextColor(context.getResources().getColor(R.color.help_calc));

            tv_days.setText(context.getResources().getString(R.string.rs)+ " "+ Math.round(Double.parseDouble(expandedListText.getPackageRate())));
            Typeface face = Typeface.createFromAsset(context.getAssets(),
                    "fonts/UbuntuCondensed-Regular.ttf");
            tv_days.setTypeface(face);

            tv_pkg_name.setText(expandedListText.getPlanName());
            Typeface face1 = Typeface.createFromAsset(context.getAssets(),
                    "fonts/UbuntuCondensed-Regular.ttf");
            tv_pkg_name.setTypeface(face1);

            Log.e("Offer Desc",":"+expandedListText.getOfferdesc());

            //tv_validity.setText(arrayList.get(position).getPackagevalidity() + " Days");
            try {
                if (expandedListText.getOfferdesc().length() > 0 || expandedListText.getOfferdesc() != null || !expandedListText.getOfferdesc().equalsIgnoreCase("null")) {
                    tv_validity.setText(expandedListText.getOfferdesc());
                    tv_validity.setVisibility(View.VISIBLE);
                } else {
                    tv_validity.setVisibility(View.GONE);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            tv_desc.setText(expandedListText.getPackagedesc());
            Typeface face2 = Typeface.createFromAsset(context.getAssets(),
                    "fonts/Neuton_Regular.ttf");
            tv_desc.setTypeface(face2);
            tv_validity.setTypeface(face2);
            String text = expandedListText.getPackageRate();
            Double intVal = 0.00;

            try {
                intVal = Double.parseDouble(text);

                Log.e("intVal",":"+intVal);
                if(intVal <= 1000){
                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg1));
                }else if(intVal > 1000 && intVal<=2000){
                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg2));
                }else if(intVal > 2000 && intVal<=5000){
                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg3));
                }else if(intVal > 5000 && intVal<=10000){
                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg4));
                }else{
                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg5));
                }
            } catch (NumberFormatException e) {
                intVal = Double.parseDouble(expandedListText.getPackageRate());
                if(intVal <= 1000){
                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg1));
                }else if(intVal > 1000 && intVal<=2000){
                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg2));
                }else if(intVal > 2000 && intVal<=5000){
                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg3));
                }else if(intVal > 5000 && intVal<=10000){
                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg4));
                }else{
                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg5));
                }
            }

//            tv_pkg_name.setText(arrayList.get(position).getPlanName());
//            Typeface face1 = Typeface.createFromAsset(context.getAssets(),
//                    "fonts/UbuntuCondensed-Regular.ttf");
//            tv_pkg_name.setTypeface(face1);
//
//            Log.e("Offer Desc",":"+arrayList.get(position).getOfferdesc());
//
//            //tv_validity.setText(arrayList.get(position).getPackagevalidity() + " Days");
//            try {
//                if (arrayList.get(position).getOfferdesc().length() > 0 || arrayList.get(position).getOfferdesc() != null || !arrayList.get(position).getOfferdesc().equalsIgnoreCase("null")) {
//                    tv_validity.setText(arrayList.get(position).getOfferdesc());
//                    tv_validity.setVisibility(View.VISIBLE);
//                } else {
//                    tv_validity.setVisibility(View.GONE);
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//
//            tv_desc.setText(arrayList.get(position).getPackagedesc());
//            Typeface face2 = Typeface.createFromAsset(context.getAssets(),
//                    "fonts/Neuton_Regular.ttf");
//            tv_desc.setTypeface(face2);
//            tv_validity.setTypeface(face2);
//            String text = arrayList.get(position).getPackageRate();
//            Double intVal = 0.00;
//
//            try {
//                intVal = Double.parseDouble(text);
//
//                Log.e("intVal",":"+intVal);
//                if(intVal <= 1000){
//                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg1));
//                }else if(intVal > 1000 && intVal<=2000){
//                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg2));
//                }else if(intVal > 2000 && intVal<=5000){
//                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg3));
//                }else if(intVal > 5000 && intVal<=10000){
//                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg4));
//                }else{
//                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg5));
//                }
//            } catch (NumberFormatException e) {
//                intVal = Double.parseDouble(arrayList.get(position).getPackageRate());
//                if(intVal <= 1000){
//                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg1));
//                }else if(intVal > 1000 && intVal<=2000){
//                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg2));
//                }else if(intVal > 2000 && intVal<=5000){
//                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg3));
//                }else if(intVal > 5000 && intVal<=10000){
//                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg4));
//                }else{
//                    ll_amount.setBackgroundColor(context.getResources().getColor(R.color.pkg5));
//                }
//            }
//

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        Log.e("getChildView", "----" +expandableListDetail.get(expandableListTitle.get(listPosition))
                .size());

        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = String.valueOf(getGroup(listPosition));
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.grid_days, null);
        }
        TextView tv_days = (TextView) convertView.findViewById(R.id.tv_days);

//        tv_days.setText(String.valueOf(Math.round(Double.parseDouble(listTitle))));
        tv_days.setText(""+listTitle);

        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "fonts/DroidSerif_Bold.ttf");
        tv_days.setTypeface(face);

        CardView cardView = (CardView) convertView.findViewById(R.id.card_view);
        TextView tv_day = (TextView) convertView.findViewById(R.id.tv_day);

        Typeface face1 = Typeface.createFromAsset(context.getAssets(),
                "fonts/UbuntuCondensed-Regular.ttf");
        tv_day.setTypeface(face1);
        Log.e("getGroupView", "----" );
//        if(listPosition == 0){
//            cardView.setBackgroundColor(context.getResources().getColor(R.color.pkg6));
//        } else {
//            cardView.setBackgroundColor(context.getResources().getColor(R.color.label_white_color));
//        }


        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}
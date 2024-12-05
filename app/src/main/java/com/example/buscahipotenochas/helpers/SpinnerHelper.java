package com.example.buscahipotenochas.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.buscahipotenochas.R;

public class SpinnerHelper extends ArrayAdapter<String> {

    String[] spinnerTitles;
    int[] spinnerImages;
    Context mContext;

    public SpinnerHelper(@NonNull Context context, String[] titles, int[] imagenes) {
        super(context, R.layout.spinner_row);
        this.spinnerTitles = titles;
        this.spinnerImages = imagenes;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return spinnerTitles.length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.spinner_row, parent, false);
            mViewHolder.mFoto = (ImageView) convertView.findViewById(R.id.imagen);
            mViewHolder.mNombre = (TextView) convertView.findViewById(R.id.nombre);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        //mViewHolder.mFoto.setImageResource(spinnerImages[position]);
        mViewHolder.mNombre.setText(spinnerTitles[position]);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    private static class ViewHolder {
        ImageView mFoto;
        TextView mNombre;
    }
}
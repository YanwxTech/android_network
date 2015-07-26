package com.yvan.androidhttpoperation.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yvan.androidhttpoperation.R;
import com.yvan.androidhttpoperation.activity.MultiDownLoadActivity;
import com.yvan.androidhttpoperation.entity.FileInfo;
import com.yvan.androidhttpoperation.services.DownloadService;
import com.yvan.androidhttpoperation.utils.CommonAdapter;
import com.yvan.androidhttpoperation.utils.ViewHolder;

import java.util.List;

/**
 * Created by Yvan on 2015/6/15.
 */
public class DownloadAdapter extends BaseAdapter{
    private Context mContext;
    private List<FileInfo> listData;

    public DownloadAdapter(Context mContext, List<FileInfo> listData) {
        this.mContext = mContext;
        this.listData = listData;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateUI(int position,int progress){
        listData.get(position).setFinished(progress);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final FileInfo fileInfo=listData.get(position);
        ViewHolder holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.multi_thread_download_item,null);
            holder=new ViewHolder();
            holder.dl_progress= (ProgressBar) convertView.findViewById(R.id.id_pb_file_download);
            holder.filename= (TextView) convertView.findViewById(R.id.id_text_filename);
            holder.finish_time= (TextView) convertView.findViewById(R.id.id_text_finish_time);
            holder.file_icon= (ImageView) convertView.findViewById(R.id.id_img_file_icon);
            holder.btn_download= (Button) convertView.findViewById(R.id.id_btn_download_begin);
            holder.btn_pause= (Button) convertView.findViewById(R.id.id_btn_download_pause);

            holder.dl_progress.setMax(100);
            holder.filename.setText(fileInfo.getFilename());
            holder.file_icon.setImageResource(R.mipmap.ic_launcher);
            holder.btn_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext,DownloadService.class);
                    intent.setAction(DownloadService.ACTION_START);
                    intent.putExtra("fileInfo",fileInfo);
                    mContext.startService(intent);
                }
            });
            holder.btn_pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext,DownloadService.class);
                    intent.setAction(DownloadService.ACTION_PAUSE);
                    intent.putExtra("fileInfo",fileInfo);
                    mContext.startService(intent);
                }
            });

            convertView.setTag(holder);
        }
        holder= (ViewHolder) convertView.getTag();
        holder.finish_time.setText("TODO");
        holder.dl_progress.setProgress(fileInfo.getFinished());
        return convertView;
    }

    static class ViewHolder{
        ProgressBar dl_progress;
        TextView filename,finish_time;
        Button btn_download,btn_pause;
        ImageView file_icon;
    }
}

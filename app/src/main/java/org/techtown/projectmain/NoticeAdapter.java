package org.techtown.projectmain;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.techtown.loginactivity.R;
import org.techtown.vote.VoteDPAdapter;

import java.util.ArrayList;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {
    private ArrayList<NoticeData> nlist;

    public class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView notice_title, notice_contents, notice_date;

        public ViewHolder(View itemView) {
            super(itemView);
            this.notice_title = (TextView)itemView.findViewById(R.id.notice_title);
            this.notice_contents = (TextView)itemView.findViewById(R.id.notice_contents);
            this.notice_date = (TextView)itemView.findViewById(R.id.notice_date);

        }

    }

    public NoticeAdapter(ArrayList<NoticeData> list) {this.nlist = list;}

    @Override
    public NoticeAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.notice_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NoticeAdapter.ViewHolder viewHolder, final int position) {
         final NoticeData noticeData = nlist.get(position);

        viewHolder.notice_title.setText("("+noticeData.getNoticePojectInfo()+") "+ noticeData.getNoticeId()+"님이 " +noticeData.getNoticeKind()+"에 글을 올렸습니다.");
        viewHolder.notice_contents.setText("\""+noticeData.getNoticeContents()+"\"");
        viewHolder.notice_date.setText(noticeData.getNoticeDate());

        String classification = noticeData.getNoticeKind();

        Log.e("",classification+"");


        if (classification=="게시판"){
            viewHolder.notice_title.setText("("+noticeData.getNoticePojectInfo()+") "+ noticeData.getNoticeId()+"님이 " +noticeData.getNoticeKind()+"에 글을 올렸습니다.");
            viewHolder.notice_contents.setText("\""+noticeData.getNoticeContents()+"\"");
            viewHolder.notice_date.setText(noticeData.getNoticeDate());
        }
        else if (classification=="투표"){
            viewHolder.notice_title.setText("("+noticeData.getNoticePojectInfo()+") "+ noticeData.getNoticeId()+"님이 " +noticeData.getNoticeKind()+"를 올렸습니다.");
            viewHolder.notice_contents.setText("\""+noticeData.getNoticeContents()+"\"");
            viewHolder.notice_date.setText(noticeData.getNoticeDate());
        }
        else if (classification=="댓글"){
            viewHolder.notice_title.setText("("+noticeData.getNoticePojectInfo()+") "+ noticeData.getNoticeId()+"님이 " +noticeData.getNoticeKind()+"를 달았습니다.");
            viewHolder.notice_contents.setText("\""+noticeData.getNoticeContents()+"\"");
            viewHolder.notice_date.setText(noticeData.getNoticeDate());
        }

        Log.e("","("+noticeData.getNoticePojectInfo()+") "+ noticeData.getNoticeId()+"님이 " +noticeData.getNoticeKind()+"에 글을 올렸습니다.");

    }
    @Override
    public int getItemCount() {
        return (null != nlist ? nlist.size() : 0);

    }
}

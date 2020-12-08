package org.techtown.projectmain;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.techtown.loginactivity.R;

import java.util.ArrayList;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {
    private ArrayList<NoticeData> nlist;

    public class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView notice_title, notice_contents, notice_date;

        public ViewHolder(View itemView) {
            super(itemView);
            this.notice_title = (TextView)itemView.findViewById(R.id.myActivities_project);
            this.notice_contents = (TextView)itemView.findViewById(R.id.myActivities_title);
            this.notice_date = (TextView)itemView.findViewById(R.id.myActivities_date);
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

         String content = noticeData.getNoticeId()+"님이 "+noticeData.getNoticeKind()+"을 올렸습니다 : "+"\""+noticeData.getNoticeContents()+"\"";

        SpannableString spannableString = new SpannableString(content);
        if (noticeData.getNoticeKind().equals("게시판")||noticeData.getNoticeKind().equals("투표")||noticeData.getNoticeKind().equals("댓글")) {

            int start = content.indexOf(noticeData.getNoticeKind());
            int end = start + noticeData.getNoticeKind().length();
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new RelativeSizeSpan(1.1f), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        }
        viewHolder.notice_title.setText("프로젝트 이름 : " + noticeData.getNoticePojectInfo());
        viewHolder.notice_date.setText(noticeData.getNoticeDate());
        viewHolder.notice_contents.setText(spannableString);
        viewHolder.notice_date.setTextColor(Color.parseColor("#c9cacd"));


    }
    @Override
    public int getItemCount() {
        return (null != nlist ? nlist.size() : 0);
    }
}
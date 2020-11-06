package org.techtown.board;

import android.view.View;

//게시물 아이템을 클릭했을 때 실행되는 클릭리스너
public interface BoardItemClickListener {
    public void onItemClick(BoardAdapter.ViewHolder holder, View view, int position);

}

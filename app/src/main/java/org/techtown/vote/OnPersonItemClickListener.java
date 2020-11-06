package org.techtown.vote;

import android.view.View;

public interface OnPersonItemClickListener {
    public void onItemClick(VoteDPAdapter.ViewHolder holder, View view, int position);
}

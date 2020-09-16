package org.techtown.laddergame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import org.techtown.laddergame.LadderGameMain;

public class LadderGameStart extends View {
    public LadderGameStart(Context context) {
        super(context);
    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        canvas.drawColor( Color.WHITE );
        Paint paint = new Paint();
        paint.setColor( Color.BLACK );

        int num = LadderGameMain.GetMemberCount();

        for (int i =0; i<num;i++){
            canvas.drawLine( i * 40, 0, i * 40, 500, paint );

        }
        LadderGameThis ladderGameThis = new LadderGameThis(LadderGameMain.GetMemberCount(),15);
        ladderGameThis.drawLine(LadderGameMain.GetMemberCount(),0);
    }

}

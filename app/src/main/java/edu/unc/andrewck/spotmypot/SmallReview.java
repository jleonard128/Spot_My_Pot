package edu.unc.andrewck.spotmypot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by andrewck on 4/30/17.
 */

public class SmallReview extends View {

    private Bathroom b;

    public SmallReview(Context context, Bathroom bath) {
        super(context);
        b = bath;
    }

    public SmallReview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmallReview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SmallReview(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
    }

    public void onDraw(Canvas c) {
        super.onDraw(c);

        Paint black = new Paint(); black.setColor(Color.BLACK);

        c.drawLine(0, 0, getWidth(), 0, black);
        c.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1, black);

        black.setTextSize(50);

        c.drawText(b.getName() + " (" + b.getGender() + ")", 10, 60, black);

        black.setTextSize(45);

        c.drawText(b.getBuilding() + ", Floor " + b.getFloor(), 10, 140, black);

        c.drawText(b.getStars() + " Stars", getWidth() - 200, 140, black);
    }

    public Bathroom getReview() { return b; }
}

package thuanvo.efs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomListView extends TextView {

  /** Constructors. Each should call init() */
  public CustomListView (Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  public CustomListView (Context context) {
    super(context);
    init();
  }

  public CustomListView (Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }
  private void init() {
	  // Get a reference to our resource table.
	  // Create the paint brushes we will use in the onDraw method.
  };

  @Override
  public void onDraw(Canvas canvas) {
	  // Draw ruled lines
	  this.setTextSize(20);
	  this.setTextColor(Color.BLACK);
	  this.setBackgroundColor(Color.WHITE);
	  canvas.save();
	  // Use the TextView to render the text.
	  super.onDraw(canvas);
	  canvas.restore();
  }
}
package com.chewielouie.textadventure;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.widget.ImageView;

public class MapImageCreator {
    private Resources resources;
    private int[] drawable_masks;
    private int drawable_map;

    public MapImageCreator( Resources resources, int[] drawable_masks, int drawable_map ) {
        this.resources = resources;
        this.drawable_masks = drawable_masks;
        this.drawable_map = drawable_map;
    }

    public Bitmap create() {
        Bitmap original = BitmapFactory.decodeResource( resources, drawable_map );
        return combineAreas( generateMaskedAreas( original ), original.getWidth(), original.getHeight() );
    }

    private List<Bitmap> generateMaskedAreas( Bitmap original ) {
        List<Bitmap> masked_areas = new ArrayList<Bitmap>();
        for( int mask : drawable_masks )
            masked_areas.add( applyMask( mask, original ) );
        return masked_areas;
    }

    private Bitmap applyMask( int drawable_mask, Bitmap original ) {
        Paint paint = new Paint( Paint.ANTI_ALIAS_FLAG );
        paint.setXfermode( new PorterDuffXfermode( PorterDuff.Mode.MULTIPLY ) );

        Bitmap mask = BitmapFactory.decodeResource( resources, drawable_mask );
        Bitmap result = Bitmap.createBitmap( original.getWidth(), original.getHeight(), Bitmap.Config.ARGB_8888 );
        Canvas canvas = new Canvas( result );
        canvas.drawBitmap( original, 0, 0, null );
        canvas.drawBitmap( mask, 0, 0, paint );
        return result;
    }

    private Bitmap combineAreas( List<Bitmap> masked_areas, int width, int height ) {
        Paint paint = new Paint( Paint.ANTI_ALIAS_FLAG );
        // Use LIGHTEN to combine the images because ADD is not available and XOR doesn't work as expected
        paint.setXfermode( new PorterDuffXfermode( PorterDuff.Mode.LIGHTEN ) );
        Bitmap result = Bitmap.createBitmap( width, height, Bitmap.Config.ARGB_8888 );
        Canvas canvas = new Canvas( result );
        for( Bitmap b : masked_areas )
            canvas.drawBitmap( b, 0, 0, paint );
        return result;
    }
}


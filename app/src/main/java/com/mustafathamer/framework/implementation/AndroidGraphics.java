package com.mustafathamer.framework.implementation;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.Log;

import com.mustafathamer.framework.Graphics;
import com.mustafathamer.framework.Image;

import static android.R.attr.format;
import static android.R.attr.x;
import static android.R.attr.y;

/**
 * Created by Mus on 10/29/2016.
 * Three major classes
 * 1. Bitmap just allows you to create image objects.
 * 2. Canvas is really a canvas for your images.
 *      You draw images onto the canvas, which will appear on the screen.
 * 3. Paint is used for styling what you draw to the screen.
 */

public class AndroidGraphics implements Graphics
{
    AssetManager assets;
    Bitmap frameBuffer;
    Canvas canvas;
    Paint paint;
    Rect srcRect = new Rect();
    Rect dstRect = new Rect();

    public AndroidGraphics(AssetManager assets, Bitmap frameBuffer)
    {
        this.assets = assets;
        this.frameBuffer = frameBuffer;
        this.canvas = new Canvas(frameBuffer);
        this.paint = new Paint();

        try
        {
            for (String s : assets.list(""))
                Log.i("MOOSE", "Assets List:" + s);
        }
        catch (IOException e)
        {
            Log.e("MOOSE", e.getMessage());
        }

    }

    @Override
    public Image newImage(String fileName, ImageFormat format)
    {
        Config config;
        if (format == ImageFormat.RGB565)
            config = Config.RGB_565;
        else if (format == ImageFormat.ARGB4444)
            config = Config.ARGB_4444;
        else
            config = Config.ARGB_8888;

        Options options = new Options();
        options.inPreferredConfig = config;

        InputStream in = null;
        Bitmap bitmap = null;
        try
        {
            in = assets.open(fileName);
            bitmap = BitmapFactory.decodeStream(in, null, options);
            if (bitmap == null)
                throw new RuntimeException("Couldn't load bitmap from asset '"
                        + fileName + "'");
        } catch (IOException e)
        {
            throw new RuntimeException("Couldn't load bitmap from asset '"
                    + fileName + "'.  Exception:" + e);
        } finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                } catch (IOException e)
                {
                }
            }
        }

        if (bitmap.getConfig() == Config.RGB_565)
            format = ImageFormat.RGB565;
        else if (bitmap.getConfig() == Config.ARGB_4444)
            format = ImageFormat.ARGB4444;
        else
            format = ImageFormat.ARGB8888;

        return new AndroidImage(bitmap, format);
    }

    @Override
    public void clearScreen(int color)
    {
        canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8, (color & 0xff));
    }


    @Override
    public void drawLine(int x, int y, int x2, int y2, int color)
    {
        paint.setColor(color);
        canvas.drawLine(x, y, x2, y2, paint);
    }

    @Override
    public void drawRect(int x, int y, int width, int height, int color)
    {
        paint.setColor(color);
        paint.setStyle(Style.FILL);
        canvas.drawRect(x, y, x + width - 1, y + height - 1, paint);
    }

    @Override
    public void strokeRect(int x, int y, int width, int height, int color)
    {
        paint.setColor(color);
        paint.setStyle(Style.STROKE);
        canvas.drawRect(x, y, x + width - 1, y + height - 1, paint);
    }

    @Override
    public void drawARGB(int a, int r, int g, int b)
    {
        paint.setStyle(Style.FILL);
        canvas.drawARGB(a, r, g, b);
    }

    @Override
    public void drawString(String text, int x, int y, Paint paint)
    {
        canvas.drawText(text, x, y, paint);
    }

    @Override
    public void drawImage(Image image, int x, int y, int srcX, int srcY,
                          int srcWidth, int srcHeight)
    {
        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + srcWidth;
        srcRect.bottom = srcY + srcHeight;

        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = x + srcWidth;
        dstRect.bottom = y + srcHeight;

        canvas.drawBitmap(((AndroidImage) image).bitmap, srcRect, dstRect, null);
    }

    //
    // draws the image (specified using src rect) using the provided matrix transform
    //
    @Override
    public void drawImage(Image image, int x, int y, float rotationDeg, int srcX, int srcY,
                          int srcWidth, int srcHeight)
    {
        canvas.save();

        // rotate around the object's center
        float px = x + (srcWidth/2.f);
        float py = y + (srcHeight/2.f);
        canvas.rotate(rotationDeg);

        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + srcWidth;
        srcRect.bottom = srcY + srcHeight;

        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = x + srcWidth;
        dstRect.bottom = y + srcHeight;

        canvas.drawBitmap(((AndroidImage) image).bitmap, srcRect, dstRect, null);
        canvas.restore();
    }

    @Override
    public void drawImage(Image image, int x, int y)
    {
        canvas.drawBitmap(((AndroidImage) image).bitmap, x /*left*/, y /* top */, null);
    }

    @Override
    public void drawImage(Image image, Matrix mat)
    {
        canvas.drawBitmap(((AndroidImage) image).bitmap, mat, null);
    }


    public void drawScaledImage(Image image, int x, int y, int width, int height, int srcX, int srcY, int srcWidth, int srcHeight)
    {
        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + srcWidth;
        srcRect.bottom = srcY + srcHeight;

        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = x + width;
        dstRect.bottom = y + height;

        canvas.drawBitmap(((AndroidImage) image).bitmap, srcRect, dstRect, null);
    }

    @Override
    public int getWidth()
    {
        return frameBuffer.getWidth();
    }

    @Override
    public int getHeight()
    {
        return frameBuffer.getHeight();
    }
}

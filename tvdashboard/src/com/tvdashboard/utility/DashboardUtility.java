package com.tvdashboard.utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;

public class DashboardUtility {
	  private static final int FONT_BITMAP_HEIGHT = 100;
	  private static final int FONT_BITMAP_WIDTH = 100;
	  private static final int MAX_QUALITY = 100;
	  private static final String PATH_SDCARD = "/sdcard/";
	  private static final String SUFIXX_PNG = ".png";
	  private static final String TAG = "MstarUtil";
	  private static DashboardUtility instance = new DashboardUtility();
	  private Activity oActivity = null;
	  private HashMap<String, String> oAppLocalValues = new HashMap();
	  
	  private DashboardUtility()
	  {
	    this.oAppLocalValues.put("STORAGE_ROOT", Environment.getExternalStorageDirectory().getAbsolutePath());
	  }
	  
	  public static DashboardUtility getInstance()
	  {
	    return instance;
	  }
	  
	  public Activity getActivity()
	  {
	    return this.oActivity;
	  }
	  
	  public String getAppLocalValue(String paramString)
	  {
	    return (String)this.oAppLocalValues.get(paramString);
	  }
	  
	  public Bitmap getFontBmp(Context paramContext, int paramInt)
	  {
	    Bitmap localBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
	    Canvas localCanvas = new Canvas(localBitmap);
	    localCanvas.drawARGB(0, 0, 0, 0);
	    Typeface localTypeface = Typeface.create("", 2);
	    Paint localPaint = new Paint();
	    localPaint.setColor(-1);
	    localPaint.setTextSize(20.0F);
	    localPaint.setAntiAlias(true);
	    localPaint.setTypeface(localTypeface);
	    localCanvas.drawText(paramContext.getResources().getString(paramInt), 12.0F, 95.0F, localPaint);
	    return localBitmap;
	  }
	  
	  public Bitmap getFontBmp(Context paramContext, int paramInt1, int paramInt2)
	  {
	    Bitmap localBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
	    Canvas localCanvas = new Canvas(localBitmap);
	    localCanvas.drawARGB(0, 0, 0, 0);
	    Typeface localTypeface = Typeface.create("", 2);
	    Paint localPaint = new Paint();
	    localPaint.setColor(-1);
	    localPaint.setTextSize(20.0F);
	    localPaint.setAntiAlias(true);
	    localPaint.setTypeface(localTypeface);
	    localCanvas.drawText(paramContext.getResources().getString(paramInt1), paramInt2, 95.0F, localPaint);
	    return localBitmap;
	  }
	  
	  /* Error */
	  public void getFontImage(Bitmap paramBitmap, String paramString)
	  {
	    // Byte code:
	    //   0: new 51	java/io/File
	    //   3: dup
	    //   4: new 149	java/lang/StringBuilder
	    //   7: dup
	    //   8: ldc 13
	    //   10: invokespecial 152	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
	    //   13: aload_2
	    //   14: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
	    //   17: ldc 16
	    //   19: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
	    //   22: invokevirtual 159	java/lang/StringBuilder:toString	()Ljava/lang/String;
	    //   25: invokespecial 160	java/io/File:<init>	(Ljava/lang/String;)V
	    //   28: astore_3
	    //   29: new 162	java/io/BufferedOutputStream
	    //   32: dup
	    //   33: new 164	java/io/FileOutputStream
	    //   36: dup
	    //   37: aload_3
	    //   38: invokespecial 167	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
	    //   41: invokespecial 170	java/io/BufferedOutputStream:<init>	(Ljava/io/OutputStream;)V
	    //   44: astore 4
	    //   46: aload_1
	    //   47: getstatic 176	android/graphics/Bitmap$CompressFormat:PNG	Landroid/graphics/Bitmap$CompressFormat;
	    //   50: bipush 100
	    //   52: aload 4
	    //   54: invokevirtual 180	android/graphics/Bitmap:compress	(Landroid/graphics/Bitmap$CompressFormat;ILjava/io/OutputStream;)Z
	    //   57: pop
	    //   58: aload 4
	    //   60: invokevirtual 183	java/io/BufferedOutputStream:flush	()V
	    //   63: aload 4
	    //   65: invokevirtual 186	java/io/BufferedOutputStream:close	()V
	    //   68: return
	    //   69: astore 5
	    //   71: aload 5
	    //   73: invokevirtual 189	java/io/FileNotFoundException:printStackTrace	()V
	    //   76: goto -8 -> 68
	    //   79: astore 6
	    //   81: aload 6
	    //   83: invokevirtual 190	java/io/IOException:printStackTrace	()V
	    //   86: goto -18 -> 68
	    //   89: astore 6
	    //   91: goto -10 -> 81
	    //   94: astore 5
	    //   96: goto -25 -> 71
	    // Local variable table:
	    //   start	length	slot	name	signature
	    //   0	99	0	this	MstarUtil
	    //   0	99	1	paramBitmap	Bitmap
	    //   0	99	2	paramString	String
	    //   28	10	3	localFile	File
	    //   44	20	4	localBufferedOutputStream	java.io.BufferedOutputStream
	    //   69	3	5	localFileNotFoundException1	FileNotFoundException
	    //   94	1	5	localFileNotFoundException2	FileNotFoundException
	    //   79	3	6	localIOException1	IOException
	    //   89	1	6	localIOException2	IOException
	    // Exception table:
	    //   from	to	target	type
	    //   29	46	69	java/io/FileNotFoundException
	    //   29	46	79	java/io/IOException
	    //   46	68	89	java/io/IOException
	    //   46	68	94	java/io/FileNotFoundException
	  }
	  
	  public String getParameter(String paramString)
	  {
	    return this.oActivity.getIntent().getStringExtra(paramString);
	  }
	  
	  public boolean hasAppLocalValue(String paramString)
	  {
	    return this.oAppLocalValues.containsKey(paramString);
	  }
	  
	  public boolean isExist(String paramString)
	  {
	    if (new File(paramString).exists()) {}
	    for (boolean bool = true;; bool = false) {
	      return bool;
	    }
	  }
	  
	  public void printKeyCode(int paramInt1, KeyEvent paramKeyEvent, int paramInt2)
	  {
	    String str = "Key Down";
	    if (paramInt2 == 1) {
	      str = "Key Up";
	    }
	    Object[] arrayOfObject = new Object[2];
	    arrayOfObject[0] = str;
	    arrayOfObject[1] = Integer.valueOf(paramKeyEvent.getKeyCode());
	    Log.v("DashboardUtil", String.format("%s keyCode: %d", arrayOfObject));
	  }
	  
	  public void saveToBitmap(Bitmap paramBitmap, String paramString)
	    throws IOException
	  {
	    File localFile = new File("/sdcard/" + paramString + ".png");
	    localFile.createNewFile();
	    FileOutputStream localObject = null;
	    try
	    {
	      FileOutputStream localFileOutputStream = new FileOutputStream(localFile);
	      localObject = localFileOutputStream;
	    }
	    catch (FileNotFoundException localFileNotFoundException)
	    {
	      try
	      {
	        localObject.flush();
	        localObject.close();       
	        Log.v("dashboardUtility", "FileNotFoundException in saveToBitmap");
	        localFileNotFoundException.printStackTrace();
	        return;
	      }
	      catch (IOException localIOException)
	      {
	          Log.v("dashboardutility", "IOException in saveToBitmap");
	          localIOException.printStackTrace();
	      }
	    }
	    paramBitmap.compress(Bitmap.CompressFormat.PNG, 100, localObject);
	  }
	  
	  public void setActivity(Activity paramActivity)
	  {
	    this.oActivity = paramActivity;
	  }
	  
	  public void setAppLocalValue(String paramString1, String paramString2)
	  {
	    this.oAppLocalValues.put(paramString1, paramString2);
	  }
}

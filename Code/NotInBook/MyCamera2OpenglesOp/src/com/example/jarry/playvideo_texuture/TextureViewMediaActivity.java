package com.example.jarry.playvideo_texuture;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

import com.example.jarry.R;

public class TextureViewMediaActivity extends Activity implements TextureView.SurfaceTextureListener,
        MediaPlayer.OnPreparedListener,  SurfaceHolder.Callback{
    private static final String TAG = "GLViewMediaActivity";


    public static final String videoPath = Environment.getExternalStorageDirectory()+"/live.mp4";
    private TextureView textureView;
    private MediaPlayer mediaPlayer;
    private SurfaceView surfaceView;
    private SurfaceTexture  mSurface;
    
    //private TextureSurfaceRenderer videoRenderer;
    private SurfaceRenderer videoRenderer;
    private int surfaceWidth;
    private int surfaceHeight;
    private Button btn_shutter, btn_mirror, btn_color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_0);

        textureView = (TextureView) findViewById(R.id.id_textureview);
        textureView.setSurfaceTextureListener(this);
        
        //surfaceView = (SurfaceView) findViewById(R.id.id_surfaceview);
        //surfaceView.getHolder().addCallback(this);
        

        btn_color = (Button) findViewById(R.id.btn_color);
        btn_shutter = (Button) findViewById(R.id.btn_shutter);
        btn_mirror = (Button) findViewById(R.id.btn_mirror);
        
        btn_shutter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mediaPlayer == null) {
				    playVideo(mSurface);
				}else {
					if (videoRenderer != null) {
			            videoRenderer.onPause();
			            videoRenderer = null;
			        }
			        if (mediaPlayer != null) {
			            mediaPlayer.release();
			            mediaPlayer =null;
			        }
				}
			}
		});
        
        btn_color.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(videoRenderer.mColorFlag == 0) {
					Toast.makeText(TextureViewMediaActivity.this, "Blank/White Color!", Toast.LENGTH_SHORT).show();
				    videoRenderer.mColorFlag = 1;
				}else {
				    videoRenderer.mColorFlag = 0;
				    Toast.makeText(TextureViewMediaActivity.this, "Orignal Color!", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		btn_mirror.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(videoRenderer.xyFlag == 0) {
					Toast.makeText(TextureViewMediaActivity.this, "X Mirror!", Toast.LENGTH_SHORT).show();
				    videoRenderer.xyFlag = 1;
				}else if(videoRenderer.xyFlag == 1){
				    videoRenderer.xyFlag = 2;
				    Toast.makeText(TextureViewMediaActivity.this, "Y Mirror!", Toast.LENGTH_SHORT).show();
				}else if(videoRenderer.xyFlag == 2) {
				    videoRenderer.xyFlag = 0;
				    Toast.makeText(TextureViewMediaActivity.this, "Normal!", Toast.LENGTH_SHORT).show();
				}
			}
		});
    }

    private void playVideo(SurfaceTexture surfaceTexture) {
        //videoRenderer = new VideoTextureSurfaceRenderer(this, surfaceTexture, surfaceWidth, surfaceHeight);
    	videoRenderer = new SurfaceRenderer(this, surfaceTexture, surfaceWidth, surfaceHeight); //SurfaceRenderer
        initMediaPlayer();
    }

    private void initMediaPlayer() {
        try {
            this.mediaPlayer = new MediaPlayer();

            while (videoRenderer.getVideoTexture() == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Surface surface = new Surface(videoRenderer.getVideoTexture());
            mediaPlayer.setDataSource(videoPath);
            mediaPlayer.setSurface(surface);
            //mediaPlayer.setSurface(surfaceView.getHolder().getSurface());
            
            surface.release();

            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setLooping(true);
        } catch (IllegalArgumentException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (SecurityException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IllegalStateException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
    @Override
    public void onPrepared(MediaPlayer mp) {
        try {
            if (mp != null) {
                mp.start();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        Log.v(TAG, "GLViewMediaActivity::onResume()");
        super.onResume();
    }


    @Override protected void onStart()
    {
        Log.v(TAG, "GLViewMediaActivity::onStart()");
        super.onStart();
    }

    @Override
    protected void onPause() {
        Log.v(TAG, "GLViewMediaActivity::onPause()");
        super.onPause();
        if (videoRenderer != null) {
            videoRenderer.onPause();
            videoRenderer = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer =null;
        }
    }

    @Override protected void onStop()
    {
        Log.v(TAG, "GLViewMediaActivity::onStop()");
        super.onStop();
    }

    @Override protected void onDestroy()
    {
        Log.v(TAG, "GLViewMediaActivity::onDestroy()");
        super.onDestroy();
    }


    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Log.v( TAG, "GLViewMediaActivity::onSurfaceTextureAvailable()"+ " tName:" + Thread.currentThread().getName() + "  tid:");

        surfaceWidth = width;
        surfaceHeight = height;
        mSurface = surface;
        //playVideo(surface);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }


    /****************************************************************************************/

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.v( TAG, "GLViewMediaActivity::surfaceCreated()" );
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.v( TAG, "GLViewMediaActivity::surfaceChanged()" );
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.v( TAG, "GLViewMediaActivity::surfaceDestroyed()" );
    }
}

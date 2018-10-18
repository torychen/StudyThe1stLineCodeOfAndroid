package com.example.jarry;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.media.MediaPlayer;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.jarry.playvideo_texuture.HelpUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;

public class GLViewMediaActivity extends Activity implements TextureView.SurfaceTextureListener, GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {
    public static final String videoPath = Environment.getExternalStorageDirectory()+"/live.mp4";
    public static final String TAG = "GLViewMediaActivity";
    
    private static float squareCoords[] = {
            -1.0f,  1.0f,    // top left
            -1.0f, -1.0f,    // bottom left
             1.0f, -1.0f,    // bottom right
             1.0f,  1.0f     // top right
    }; 
    private static short drawOrder[] = {0, 1, 2, 0, 2, 3}; // Texture to be shown in backgrund
    private float textureCoords[] = {
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f
    };
    
    private int[] textures = new int[1];
    private int width, height;
    private int shaderProgram;
    private FloatBuffer vertexBuffer;
    private FloatBuffer textureBuffer;
    private ShortBuffer drawListBuffer;
    private float[] videoTextureTransform = new float[16];
    private SurfaceTexture videoTexture;
    private GLSurfaceView glView;
    
    private Context context;
    private RelativeLayout previewLayout = null;
    
    private boolean frameAvailable = false;
    int textureParamHandle;
    int textureCoordinateHandle;
    int positionHandle;
    int textureTranformHandle;
    	
    public  int mRatio;
    public  float ratio=0.5f;
    public  int mColorFlag=0;
    public  int xyFlag=0;
    TextureView mPreviewView;
    CameraCaptureSession mSession;
	CaptureRequest.Builder mPreviewBuilder;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_1);
        context = this;
        glView = new GLSurfaceView(this);
        
        previewLayout = (RelativeLayout)findViewById(R.id.previewLayout);
        //RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(640,480 );
        previewLayout.addView(glView);//, layoutParams);
        
        //setContentView(previewLayout); //glView
        mPreviewView = (TextureView) findViewById(R.id.id_textureview);
	    mPreviewView.setSurfaceTextureListener(this);
        
        glView.setEGLContextClientVersion(2);
        glView.setRenderer(this);
        // glView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        
        SeekBar seekBar = (SeekBar) findViewById(R.id.id_seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				if(iconObj != null) {
				   iconObj.ratio = progress/100.0f;
				}
				ratio = progress/100.0f;
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
        });
        
        Button btn_color = (Button)findViewById(R.id.btn_color);
        Button btn_mirror = (Button)findViewById(R.id.btn_mirror);
		
        btn_color.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mColorFlag == 0) {
				    mColorFlag = 7;
				    Toast.makeText(GLViewMediaActivity.this, "Saturation adjust!", Toast.LENGTH_SHORT).show();
				}else if(mColorFlag == 7) {
					mColorFlag = 1;
					Toast.makeText(GLViewMediaActivity.this, "Gray Color!", Toast.LENGTH_SHORT).show();
				}else if(mColorFlag == 1) {
				    mColorFlag = 2;
				    Toast.makeText(GLViewMediaActivity.this, "Warm Color!", Toast.LENGTH_SHORT).show();
				}else if(mColorFlag == 2){
				    mColorFlag = 3;
				    Toast.makeText(GLViewMediaActivity.this, "Cool Color!", Toast.LENGTH_SHORT).show();
				}else if(mColorFlag == 3){
				    mColorFlag = 4;
				    Toast.makeText(GLViewMediaActivity.this, "Amplify!", Toast.LENGTH_SHORT).show();
				}else if(mColorFlag == 4){
				    mColorFlag = 5;
				    Toast.makeText(GLViewMediaActivity.this, "Vague!", Toast.LENGTH_SHORT).show();
				}else if(mColorFlag == 5){
				    mColorFlag = 6;
				    Toast.makeText(GLViewMediaActivity.this, "Beauty!", Toast.LENGTH_SHORT).show();
				}else if(mColorFlag ==6){
				    mColorFlag = 0;
				    Toast.makeText(GLViewMediaActivity.this, "Orignal Color!", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		btn_mirror.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(xyFlag == 0) {
					Toast.makeText(GLViewMediaActivity.this, "X Mirror!", Toast.LENGTH_SHORT).show();
				    xyFlag = 1;
				}else if(xyFlag == 1){
				    xyFlag = 2;
				    Toast.makeText(GLViewMediaActivity.this, "Y Mirror!", Toast.LENGTH_SHORT).show();
				}else if(xyFlag == 2) {
				    xyFlag = 0;
				    Toast.makeText(GLViewMediaActivity.this, "Normal!", Toast.LENGTH_SHORT).show();
				}
			}
		});
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        threadFlag = false;
    }

    @Override
	public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
	    CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
	    try {
	    	Log.i(TAG, "onSurfaceTextureAvailable:  width = " + width + ", height = " + height);
	        String[] CameraIdList = cameraManager.getCameraIdList(); 
	        //閼惧嘲褰囬崣顖滄暏閻╁憡婧�鐠佹儳顦崚妤勩��
	        CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(CameraIdList[0]);
	        //閸︺劏绻栭柌灞藉讲娴犮儵锟芥俺绻僀ameraCharacteristics鐠佸墽鐤嗛惄鍛婃簚閻ㄥ嫬濮涢懗锟�,瑜版挾鍔ц箛鍛淬�忓Λ锟介弻銉︽Ц閸氾附鏁幐锟�
	        characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
	        //鐏忓崬鍎氭潻娆愮壉
	        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
	        //startCodec();
	        cameraManager.openCamera(CameraIdList[0], mCameraDeviceStateCallback, null);
	    } catch (CameraAccessException e) {
	        e.printStackTrace();
	    }
	}

	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
	    return false;
	}

	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture surface) {}

	CameraDevice mCameraDevice;
	
	private CameraDevice.StateCallback mCameraDeviceStateCallback = new CameraDevice.StateCallback() {
	    @Override
	    public void onOpened(CameraDevice camera) {
	        Log.i(TAG, "       CameraDevice.StateCallback  onOpened            ");
			try {
				mCameraDevice = camera;
				startPreview(camera);
			} catch (CameraAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	    @Override
	    public void onDisconnected(CameraDevice camera) {
	    	if (null != mCameraDevice) {
                mCameraDevice.close();
                GLViewMediaActivity.this.mCameraDevice = null;
            }
	    }

	    @Override
	    public void onError(CameraDevice camera, int error) {}
	};

	private void startPreview(CameraDevice camera) throws CameraAccessException {
		
	    SurfaceTexture texture = mPreviewView.getSurfaceTexture();
	    texture.setDefaultBufferSize(mPreviewView.getWidth(), mPreviewView.getHeight());
	    Surface surface = new Surface(texture);
	    
		Surface surface0 = new Surface(videoTexture);
		
	    Log.i(TAG, "      startPreview          ");
	    try {
	        mPreviewBuilder = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW); //CameraDevice.TEMPLATE_STILL_CAPTURE
	    } catch (CameraAccessException e) {
	        e.printStackTrace();
	    }
	    mPreviewBuilder.addTarget(surface);
	    mPreviewBuilder.addTarget(surface0);
	    /*
	    //method 2
	    isEncode = true;
	    new Thread() {
	    	public void run() {
	    		MediaCodec.BufferInfo m_BufferInfo = new MediaCodec.BufferInfo();
	    		//鐏忔繆鐦懢宄板絿鏉堟挸鍤弫鐗堝祦閻ㄥ嫪淇婇幁顖ょ礉閸忓厖绨琤ytebuffer閻ㄥ嫪淇婇幁顖氱殺鐏忎浇顥婇崷鈺瀠fferinfo闁插矂娼伴敍宀冪箲閸ョ偠顕歜ytebuffer閸︺劑妲﹂崚妞捐厬閻ㄥ嫪缍呯純锟�
	    		int index = mCodec.dequeueOutputBuffer(m_BufferInfo, 0);
	    		while(isEncode) {
	    		   if (index >= 0) {
	   	    		 ByteBuffer outPutByteBuffer = mCodec.getOutputBuffer(index);
	   	    		 byte[] outDate = new byte[m_BufferInfo.size];
	   	    		 outPutByteBuffer.get(outDate);
	   	    		  
	   	    		 try {
	   	            	Log.d(TAG, " outDate.length : " + outDate.length);
	   					outputStream.write(outDate, 0, outDate.length);
	   				 } catch (IOException e) {
	   					// TODO Auto-generated catch block
	   					e.printStackTrace();
	   				 } 
	   	    	
	   	    		 //闁插﹥鏂侀崚姘灠娴犲钉odec閸欐牕鍤弫鐗堝祦閻ㄥ垺ytebuffer閿涘奔绶礐odec缂佈呯敾閺�鐐殶閹诡喓锟斤拷
	   	    		 mCodec.releaseOutputBuffer(index, false);
	   	    		 //閸愬秴鐨剧拠鏇氱矤Codec娑擃叀骞忛崣鏍翻閸戠儤鏆熼幑顔炬畱娣団剝浼�
	   	    		 index = mCodec.dequeueOutputBuffer(m_BufferInfo, 0);
	    		   }else{
	    		     index = mCodec.dequeueOutputBuffer(m_BufferInfo, 0);
	    		     continue;
	    		   }
	    	    }
	    		mCodec.stop();
	            mCodec.release();
	            mCodec = null;
	    	}
	    	
	    }.start();
	    */
	    //Arrays.asList(surface, surface0)
	    camera.createCaptureSession(Arrays.asList(surface, surface0), mSessionStateCallback, null);
	}

	
	private CameraCaptureSession.StateCallback mSessionStateCallback = new CameraCaptureSession.StateCallback() {
	    @Override
	    public void onConfigured(CameraCaptureSession session) {
	        try {
	    	    Log.i(TAG, "      onConfigured          ");
	            //session.capture(mPreviewBuilder.build(), mSessionCaptureCallback, mHandler);
	        	mSession = session;
	        	// 閼奉亜濮╃�靛湱鍔�
	        	mPreviewBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                // 閹垫挸绱戦梻顏勫帨閻忥拷
	        	mPreviewBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
	        	
	            //int rotation = getWindowManager().getDefaultDisplay().getRotation();
	        	//mPreviewBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
	        	
	            session.setRepeatingRequest(mPreviewBuilder.build(), null, null); //null
	        } catch (CameraAccessException e) {
	            e.printStackTrace();
	        }
	    }

	    @Override
	    public void onConfigureFailed(CameraCaptureSession session) {}
	};

	int callback_time;
    private CameraCaptureSession.CaptureCallback mSessionCaptureCallback =new CameraCaptureSession.CaptureCallback() {
         @Override
         public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
             //Toast.makeText(GLViewMediaActivity.this, "picture success閿涳拷", Toast.LENGTH_SHORT).show();
        	 callback_time++;
        	 Log.i(TAG, "    CaptureCallback =  "+callback_time);
         }

         @Override
         public void onCaptureProgressed(CameraCaptureSession session, CaptureRequest request, CaptureResult partialResult) {
             Toast.makeText(GLViewMediaActivity.this, "picture failed閿涳拷", Toast.LENGTH_SHORT).show();
         }
    };
    
    private int[] fFrame = new int[1];
    private int[] fRender = new int[1];
    private int[] fTexture = new int[1];
    
    
    public  void genTexturesWithParameter(int width, int height){
    	//Bitmap mBitmap=BitmapFactory.decodeStream(context.getAssets().open(texture));
    	Bitmap mBitmap=BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
    			
        GLES20.glGenTextures(1, fTexture, 1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fTexture[0]);
        Log.e("TAG", "  genTexturesWithParameter textures[0] = "+fTexture[0]);
        
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height,
               0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
        //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
        //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
        //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
        //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
        
        //GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmap, 0);
        
        //GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);
    }

    public  void bindFrameBufferWithTexture(int width, int height){  	
        //生成fb
    	GLES20.glGenFramebuffers(1, fFrame, 0);
    	GLES20.glGenRenderbuffers(1, fRender, 0);
    	
    	//生成纹理、绑定纹理
    	genTexturesWithParameter(width, height);
    	//绑定fb
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fFrame[0]);
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, fRender[0]);
        GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16, width,
            height);
        //绑定纹理到fb
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
            GLES20.GL_TEXTURE_2D, fTexture[0], 0);
        GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT,
                GLES20.GL_RENDERBUFFER, fRender[0]);
    }

    public void unBindFrameBuffer(){
    	GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }
    
    private void deleteBuffers() {
        GLES20.glDeleteRenderbuffers(1, fRender, 0);
        GLES20.glDeleteFramebuffers(1, fFrame, 0);
        GLES20.glDeleteTextures(1, fTexture, 0);
    }
    
    int textureIdEarth;//系统分配的地球纹理id
    int textureIdEarthNight;//系统分配的地球夜晚纹理id
    int textureIdMoon;//系统分配的月球纹理id    
    int textureIdCloud;//系统分配的云层纹理id 
    
    Earth earth;//地球
	Moon moon;//月球
	Celestial cSmall;//小星星天球
	Celestial cBig;//大星星天球
	Cloud cloud;//月球
	
	float eAngle=0;//地球自转角度    
    float cAngle=0;//天球自转的角度
    
    boolean threadFlag = false;
    
    public int initTexture(int drawableId)
	{
		//生成纹理ID
		int[] textures = new int[1];
		GLES20.glGenTextures
		(
				1,          //产生的纹理id的数量
				textures,   //纹理id的数组
				0           //偏移量
		);    
		int textureId = textures[0];    
		Log.i(TAG, " initTexture textureId = " + textureId);
		Log.i(TAG, " initTexture textureId = " + textureId);
		
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
        
        //通过输入流加载图片===============begin===================
        InputStream is = this.getResources().openRawResource(drawableId);
        Bitmap bitmapTmp;
        try {
        	bitmapTmp = BitmapFactory.decodeStream(is);
        } finally {
            try {
                is.close();
            } 
            catch(IOException e) {
                e.printStackTrace();
            }
        }
        //通过输入流加载图片===============end=====================  
        //实际加载纹理
        GLUtils.texImage2D
        (
        		GLES20.GL_TEXTURE_2D,   //纹理类型，在OpenGL ES中必须为GL10.GL_TEXTURE_2D
        		0, 					  //纹理的层次，0表示基本图像层，可以理解为直接贴图
        		bitmapTmp, 			  //纹理图像
        		0					  //纹理边框尺寸
        );
        bitmapTmp.recycle(); 		  //纹理加载成功后释放图片 
        return textureId;
	}
    
    IconObj iconObj;
    IconObj2 iconObj2;
    
    int textureIdOne;
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        setupGraphics();
        setupVertexBuffer();
        setupTexture();
        textureIdOne= initTexture(R.drawable.bg);
        
        //iconObj = new IconObj(context);
        //iconObj2 = new IconObj2(context);
        
        /*
        //创建地球对象 
        earth=new Earth(context, 2.0f);
        cloud=new Cloud(context, 2.02f);
        //创建月球对象 
        moon=new Moon(context, 1.0f);
        //创建小星星天球对象
        cSmall=new Celestial(context, 1, 0, 200);
        //创建大星星天球对象
        cBig=new Celestial(context, 2, 0, 80);
        //初始化变换矩阵
        MatrixState.setInitStack();
        */
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        this.width = width;
        this.height = height;
        GLES20.glViewport(0, 0, width, height);
        setSize(width, height);
        GLES20.glUniform1i(gHWidth,width);
        GLES20.glUniform1i(gHHeight,height);
        
        /*
        //计算GLSurfaceView的宽高比
        float ratio= (float) width / height;
        //调用此方法计算产生透视投影矩阵
        MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 1f, 100);
        //调用此方法产生摄像机9参数位置矩阵
        MatrixState.setCamera(0, 0, 2.0f, 0f, 0f, 0f, 0f, 1.0f,0.0f);       
        //打开背面剪裁
        //GLES20.glEnable(GLES20.GL_CULL_FACE);  
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        
        //初始化纹理
        textureIdEarth=initTexture(R.drawable.earth);
        textureIdEarthNight=initTexture(R.drawable.earthn);
        textureIdMoon=initTexture(R.drawable.moon);        
        textureIdCloud=initTexture(R.drawable.cloud);  
        //设置太阳灯光的初始位置
        MatrixState.setLightLocationSun(100,5,0);     
        
        threadFlag = true;
        //启动一个线程定时旋转地球、月球
        new Thread() {
        	public void run(){
        		while(threadFlag){
        			//地球自转角度
        			eAngle=1;//(eAngle+2)%360;
        			//天球自转角度
        			//cAngle=(cAngle+0.2f)%360;
        			try {
						Thread.sleep(10);
					} catch (InterruptedException e) {				  			
						e.printStackTrace();
					}
        		}
        	}
        }.start();
        */
        //bindFrameBufferWithTexture(width, height);
        //GLES20.glEnable(GLES20.GL_DEPTH_TEST); 
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        synchronized (this) {
            if (frameAvailable) {
                videoTexture.updateTexImage();
                videoTexture.getTransformMatrix(videoTextureTransform);
                frameAvailable = false;
            }
        }
        //boolean flag = GLES20.glIsEnabled(GLES20.GL_DEPTH_TEST);
        //GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        //GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE);
        //if(flag){
        //    GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        //}
        
        
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glViewport(0, 0, width, height);
        
        drawTexture();
        
        //iconObj.drawSelf();
        //iconObj2.drawSelf(); 
        
        /*
        //保护现场
        MatrixState.pushMatrix();
        //地球自转
        MatrixState.rotate(eAngle, 0, 1, 0);
    	//绘制纹理圆球
        earth.drawSelf(textureIdEarth,textureIdEarthNight);
        //moon.drawSelf(textureIdMoon);
        
        //开启混合
        GLES20.glEnable(GLES20.GL_BLEND);  
        //设置混合因子
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        cloud.drawSelf(textureIdCloud);//绘制云层
        //关闭混合
        GLES20.glDisable(GLES20.GL_BLEND); 
        
        //推坐标系到月球位置            
        MatrixState.transtate(2f, 0, 0);  
        //月球自转     
        MatrixState.rotate(eAngle, 0, 1, 0);
        //绘制月球
        moon.drawSelf(textureIdMoon);
        //恢复现场
        MatrixState.popMatrix();
        
        //保护现场
        MatrixState.pushMatrix();  
        MatrixState.rotate(cAngle, 0, 1, 0);
        GLES20.glEnable(GLES20.GL_BLEND);  
        //设置混合因子
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        cSmall.drawSelf();
        cBig.drawSelf();
        //恢复现场
        GLES20.glDisable(GLES20.GL_BLEND);   
        MatrixState.popMatrix();
        */
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        synchronized (this) {
            frameAvailable = true;
        }
    }
    
    private float[] mViewMatrix=new float[16];
    private float[] mProjectMatrix=new float[16];
    private float[] mModelMatrix=new float[16];
    private float[] mModelMatrix0=new float[16];
    private float[] matrix=new float[16];
    private float[] matrix0=new float[16];
    private int gHWidth;
    private int gHHeight;
    
    public void setSize(int width,int height){
        float ratio=(float)width/height;
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1, 1, 1, 3);
        //璁剧疆鐩告満浣嶇疆
        Matrix.setLookAtM(mViewMatrix, 0,
        		0.0f, 0.0f, 1.0f, //0f, 0.0f, 0.0f, 
        		0.0f, 0.0f, 0.0f, //0.0f, 0.0f,-1.0f,
        		0f,1.0f, 0.0f);
        //妯″瀷鐭╅樀
        Matrix.setIdentityM(mModelMatrix,0);
        Matrix.setIdentityM(mModelMatrix0,0);
        //Matrix.scaleM(mModelMatrix,0,2,2,2);
        
        Matrix.multiplyMM(matrix,0,mProjectMatrix,0,mViewMatrix,0);
        Matrix.multiplyMM(matrix0,0,mProjectMatrix,0,mViewMatrix,0);
        
        matrix  = flip(mModelMatrix, true, false);
        matrix0 = flip(mModelMatrix0, false, true);
    }

    public static float[] rotate(float[] m,float angle){
        Matrix.rotateM(m,0,angle,0,0,1);
        return m;
    }

    //镜像
    public  float[] flip(float[] m,boolean x,boolean y){
        if(x||y){
            Matrix.scaleM(m,0,x?-1:1,y?-1:1,1);
        }
        return m;
    }

    /*
    NONE(0,new float[]{0.0f,0.0f,0.0f}),
    GRAY(1,new float[]{0.299f,0.587f,0.114f}),
    COOL(2,new float[]{0.0f,0.0f,0.1f}),
    WARM(2,new float[]{0.1f,0.1f,0.0f}),
    BLUR(3,new float[]{0.006f,0.004f,0.002f}),
    MAGN(4,new float[]{0.0f,0.0f,0.4f});
    */
    
    int textureHandle;
    
    private void setupGraphics() {
        //final String vertexShader = HelpUtils.readTextFileFromRawResource(context, R.raw.vetext_sharder);
        //final String fragmentShader = HelpUtils.readTextFileFromRawResource(context, R.raw.fragment_sharder);
        
        final String vertexShader = HelpUtils.readTextFileFromRawResource(context, R.raw.vetext_sharder);
        final String fragmentShader = HelpUtils.readTextFileFromRawResource(context, R.raw.fragment_sharder);

        final int vertexShaderHandle = HelpUtils.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);
        final int fragmentShaderHandle = HelpUtils.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);
        shaderProgram = HelpUtils.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,
                new String[]{"texture", "vPosition", "vTexCoordinate", "textureTransform"});

        GLES20.glUseProgram(shaderProgram);
        textureParamHandle = GLES20.glGetUniformLocation(shaderProgram, "texture");
        textureCoordinateHandle = GLES20.glGetAttribLocation(shaderProgram, "vTexCoordinate");
        positionHandle = GLES20.glGetAttribLocation(shaderProgram, "vPosition");
        textureTranformHandle = GLES20.glGetUniformLocation(shaderProgram, "textureTransform");
        
        textureHandle = GLES20.glGetUniformLocation(shaderProgram, "texture0");
        mRatio = GLES20.glGetUniformLocation(shaderProgram, "mratio");
        
        gHWidth=GLES20.glGetUniformLocation(shaderProgram,"mWidth");
        gHHeight=GLES20.glGetUniformLocation(shaderProgram,"mHeight");
    }

    private void setupVertexBuffer() {
        // Draw list buffer
        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        // Initialize the texture holder
        ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());

        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);
    }

    private void setupTexture() {
        ByteBuffer texturebb = ByteBuffer.allocateDirect(textureCoords.length * 4);
        texturebb.order(ByteOrder.nativeOrder());

        textureBuffer = texturebb.asFloatBuffer();
        textureBuffer.put(textureCoords);
        textureBuffer.position(0);

        // Generate the actual texture
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glGenTextures(1, textures, 0);
        checkGlError("Texture generate");

        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textures[0]);
        checkGlError("Texture bind");

        videoTexture = new SurfaceTexture(textures[0]);
        videoTexture.setOnFrameAvailableListener(this);
        
        /*
        //glView.setEGLWindowSurfaceFactory(factory);
        glView.setEGLWindowSurfaceFactory(new GLSurfaceView.EGLWindowSurfaceFactory() {
			@Override
			public EGLSurface createWindowSurface(EGL10 egl, EGLDisplay display, EGLConfig config,
					Object nativeWindow) {
				// TODO Auto-generated method stub
				SurfaceTexture texture = mPreviewView.getSurfaceTexture();
			    //texture.setDefaultBufferSize(mPreviewView.getWidth(), mPreviewView.getHeight());
			    //Surface surface = new Surface(texture);
				return egl.eglCreateWindowSurface(display,config, texture, null); // surface  mPreviewView.getSurfaceTexture()
				//return null;
			}

			@Override
			public void destroySurface(EGL10 egl, EGLDisplay display, EGLSurface surface) {
				// TODO Auto-generated method stub
				egl.eglDestroySurface(display, surface);
			}
        });
        */
    }

    private void drawTexture() {
        // Draw texture

    	int mHProjMatrix=GLES20.glGetUniformLocation(shaderProgram,"uProjMatrix");
        GLES20.glUniformMatrix4fv(mHProjMatrix,1,false,matrix,0);
        
        int mHProjMatrix0=GLES20.glGetUniformLocation(shaderProgram,"uProjMatrix0");
        GLES20.glUniformMatrix4fv(mHProjMatrix0,1,false,matrix0,0);
        
        int mXyFlag = GLES20.glGetUniformLocation(shaderProgram, "xyFlag");
        GLES20.glUniform1i(mXyFlag, xyFlag);
        
        int mColorFlagHandle = GLES20.glGetUniformLocation(shaderProgram, "colorFlag");
        GLES20.glUniform1i(mColorFlagHandle, mColorFlag);
        
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer);

        /*
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textures[0]);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glUniform1i(textureParamHandle, 0);
        */
        
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textures[0]);
        GLES20.glUniform1i(textureParamHandle, 0);
        
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIdOne);
        GLES20.glUniform1i(textureHandle, 1);
        
        GLES20.glEnableVertexAttribArray(textureCoordinateHandle);
        GLES20.glVertexAttribPointer(textureCoordinateHandle, 4, GLES20.GL_FLOAT, false, 0, textureBuffer);

        GLES20.glUniformMatrix4fv(textureTranformHandle, 1, false, videoTextureTransform, 0);
        GLES20.glUniform1f(mRatio, ratio);

        GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(textureCoordinateHandle);

    }

    public void checkGlError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("SurfaceTest", op + ": glError " + GLUtils.getEGLErrorString(error));
        }
    }

}

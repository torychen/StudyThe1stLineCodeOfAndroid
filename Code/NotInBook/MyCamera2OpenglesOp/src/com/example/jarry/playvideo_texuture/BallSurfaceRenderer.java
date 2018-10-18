package com.example.jarry.playvideo_texuture;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

/**
 * 缁樺埗鍓嶏紝renderer鐨勯厤缃紝鍒濆鍖朎GL锛屽紑濮嬩竴涓粯鍒剁嚎绋�.
 * 杩欎釜绫婚渶瑕佸瓙绫诲幓瀹炵幇鐩稿簲鐨勭粯鍒跺伐浣�.
 *
 * 鍏蜂綋娴佺▼鍙互鍙傝�僪ttp://www.cnblogs.com/kiffa/archive/2013/02/21/2921123.html
 * 鐩稿簲鐨勫嚱鏁板彲浠ユ煡鐪嬶細 https://www.khronos.org/registry/egl/sdk/docs/man/
 */
public  class BallSurfaceRenderer implements Runnable, SurfaceTexture.OnFrameAvailableListener{
    public static String LOG_TAG = BallSurfaceRenderer.class.getSimpleName();
    public static String TAG = BallSurfaceRenderer.class.getSimpleName();
    
    private int[] textures = new int[1];

    private SurfaceTexture videoTexture;
    private float[] videoTextureTransform;
    private boolean frameAvailable = false;

    int textureTranformHandle;
    
    protected final SurfaceTexture surfaceTexture;
    protected int width;
    protected int height;

    private EGL10 egl;
    private EGLContext eglContext;
    private EGLDisplay eglDisplay;
    private EGLSurface eglSurface;

    private Context context;
    /***
     * 鏄惁姝ｅ湪缁樺埗(draw)
     */
    private boolean running = false;
    
    public BallSurfaceRenderer(Context context, SurfaceTexture surfaceTexture, int width, int height) {
        this.surfaceTexture = surfaceTexture;
        Log.e("TAG", "surfaceTexture obj="+ surfaceTexture.toString());
        this.width = width;
        this.height = height;
        this.running = true;
        this.context = context;
        
        videoTextureTransform = new float[16];
        
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        initEGL();
        initGLComponents();
        setSize(width, height);
        Log.d(LOG_TAG, "OpenGL init OK. start draw...");

        while (running) {
            if (draw()) {
                egl.eglSwapBuffers(eglDisplay, eglSurface);
            }
        }

        deinitGLComponents();
        deinitEGL();
    }

    private void initEGL() {
        egl = (EGL10)EGLContext.getEGL();
        //鑾峰彇鏄剧ず璁惧
        eglDisplay = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        //version涓瓨鏀綞GL 鐗堟湰鍙凤紝int[0]涓轰富鐗堟湰鍙凤紝int[1]涓哄瓙鐗堟湰鍙�
        int version[] = new int[2];
        egl.eglInitialize(eglDisplay, version);

        EGLConfig eglConfig = chooseEglConfig();
        //鍒涘缓EGL 鐨剋indow surface 骞朵笖杩斿洖瀹冪殑handles(eslSurface)
        eglSurface = egl.eglCreateWindowSurface(eglDisplay, eglConfig, surfaceTexture, null);

        eglContext = createContext(egl, eglDisplay, eglConfig);

        //璁剧疆褰撳墠鐨勬覆鏌撶幆澧�
        try {
            if (eglSurface == null || eglSurface == EGL10.EGL_NO_SURFACE) {
                throw new RuntimeException("GL error:" + GLUtils.getEGLErrorString(egl.eglGetError()));
            }
            if (!egl.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)) {
                throw new RuntimeException("GL Make current Error"+ GLUtils.getEGLErrorString(egl.eglGetError()));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deinitEGL() {
        egl.eglMakeCurrent(eglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
        egl.eglDestroySurface(eglDisplay, eglSurface);
        egl.eglDestroyContext(eglDisplay, eglContext);
        egl.eglTerminate(eglDisplay);
        Log.d(LOG_TAG, "OpenGL deinit OK.");
    }

    /**
     * 涓哄綋鍓嶆覆鏌撶殑API鍒涘缓涓�涓覆鏌撲笂涓嬫枃
     * @return a handle to the context
     */
    private EGLContext createContext(EGL10 egl, EGLDisplay eglDisplay, EGLConfig eglConfig) {
        int[] attrs = {
                EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
                EGL10.EGL_NONE
        };
        return egl.eglCreateContext(eglDisplay, eglConfig, EGL10.EGL_NO_CONTEXT, attrs);
    }

    /***
     *  refer to https://www.khronos.org/registry/egl/sdk/docs/man/
     * @return a EGL frame buffer configurations that match specified attributes
     */
    private EGLConfig chooseEglConfig() {
        int[] configsCount = new int[1];
        EGLConfig[] configs = new EGLConfig[1];
        int[] attributes = getAttributes();
        int confSize = 1;

        if (!egl.eglChooseConfig(eglDisplay, attributes, configs, confSize, configsCount)) {    //鑾峰彇婊¤冻attributes鐨刢onfig涓暟
            throw new IllegalArgumentException("Failed to choose config:"+ GLUtils.getEGLErrorString(egl.eglGetError()));
        }
        else if (configsCount[0] > 0) {
            return configs[0];
        }

        return null;
    }

    /**
     * 鏋勯�犵粯鍒堕渶瑕佺殑鐗规�у垪琛�,ARGB,DEPTH...
     */
    private int[] getAttributes()
    {
        return new int[] {
                EGL10.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,  //鎸囧畾娓叉煋api绫诲埆
                EGL10.EGL_RED_SIZE, 8,
                EGL10.EGL_GREEN_SIZE, 8,
                EGL10.EGL_BLUE_SIZE, 8,
                EGL10.EGL_ALPHA_SIZE, 8,
                EGL10.EGL_DEPTH_SIZE, 0,
                EGL10.EGL_STENCIL_SIZE, 0,
                EGL10.EGL_NONE      //鎬绘槸浠GL10.EGL_NONE缁撳熬
        };
    }

    /**
     * Call when activity pauses. This stops the rendering thread and deinitializes OpenGL.
     */
    public void onPause()
    {
        running = false;
    }
    
    @Override
    protected  void finalize() throws Throwable {
        super.finalize();
        running = false;
    }
    

    private void setupTexture()
    {
    	Log.v(TAG, "           setupTexture             ");
        // Generate the actual texture
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glGenTextures(1, textures, 0);
        checkGlError("Texture generate");

        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textures[0]);
        checkGlError("Texture bind");

        textureId = textures[0];
        videoTexture = new SurfaceTexture(textures[0]);
        videoTexture.setOnFrameAvailableListener(this);
        
        Log.v(TAG, "           setupTexture 00            ");
    }

    protected boolean draw()
    {
        synchronized (this)
        {
            if (frameAvailable) {
                videoTexture.updateTexImage();
                videoTexture.getTransformMatrix(videoTextureTransform);
                frameAvailable = false;
            }else{
                return false;
            }

        }
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glViewport(0, 0, width, height);
        drawTexture();

        return true;
    }

    protected void deinitGLComponents() {
        GLES20.glDeleteTextures(1, textures, 0);
        GLES20.glDeleteProgram(mHProgram);
        videoTexture.release();
        videoTexture.setOnFrameAvailableListener(null);
    }


    public void checkGlError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("SurfaceTest", op + ": glError " + GLUtils.getEGLErrorString(error));
        }
    }

    public SurfaceTexture getVideoTexture() {
        return videoTexture;
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        synchronized (this)
        {
            frameAvailable = true;
        }
    }
    
    private static final float UNIT_SIZE = 1f;// 鍗曚綅灏哄
    private float r = 2f; // 鐞冪殑鍗婂緞

    private float radius=5f; //2f

    final double angleSpan = Math.PI/90f;// 90f; 灏嗙悆杩涜鍗曚綅鍒囧垎鐨勮搴�
    int vCount = 0;// 椤剁偣涓暟锛屽厛鍒濆鍖栦负0

    private Resources res;

    private int mHProgram;
    private int mHUTexture;
    private int mHProjMatrix;
    private int mHViewMatrix;
    private int mHModelMatrix;
    private int mHRotateMatrix;
    private int mHPosition;
    private int mHCoordinate;

    private int textureId;

    private float[] mViewMatrix=new float[16];
    private float[] mProjectMatrix=new float[16];
    private float[] mModelMatrix=new float[16];
    private float[] mRotateMatrix=new float[16];

    private FloatBuffer posBuffer;
    private FloatBuffer cooBuffer;
    private int vSize;
    private float skyRate= 1.0f;//0.2f;

    private Bitmap mBitmap;
    
    public void initGLComponents() {
    	Log.v(TAG, "           initGLComponents            ");
    	 res=context.getResources();
    	
        mHProgram=Gl2Utils.createGlProgramByRes(res,"vr/skysphere0.vert","vr/skysphere0.frag");
        mHProjMatrix=GLES20.glGetUniformLocation(mHProgram,"uProjMatrix");
        mHViewMatrix=GLES20.glGetUniformLocation(mHProgram,"uViewMatrix");
        mHModelMatrix=GLES20.glGetUniformLocation(mHProgram,"uModelMatrix");
        mHRotateMatrix=GLES20.glGetUniformLocation(mHProgram,"uRotateMatrix");
        mHUTexture=GLES20.glGetUniformLocation(mHProgram,"uTexture");
        mHPosition=GLES20.glGetAttribLocation(mHProgram,"aPosition");
        mHCoordinate=GLES20.glGetAttribLocation(mHProgram,"aCoordinate");
        Log.v(TAG, "           initGLComponents             ");
        
        Matrix.setRotateM(mRotateMatrix, 0, 0, 1, 0, 0);
        
        Log.v(TAG, "           initGLComponents 00            ");
        
        setupTexture();
        calculateAttribute();
    	Log.v(TAG, "           initGLComponents 11             ");
    }

    public void setSize(int width,int height){
    	Log.v(TAG, "           setSize              ");
        //璁＄畻瀹介珮姣�
        float ratio=(float)width/height;
        //璁剧疆閫忚鎶曞奖
        //Matrix.frustumM(mProjectMatrix, 0, -ratio*skyRate, ratio*skyRate, -1*skyRate, 1*skyRate, 1, 300);
        Matrix.frustumM(mProjectMatrix, 0, -ratio*skyRate, ratio*skyRate, -1*skyRate, 1*skyRate, 1, 300);
        
        //閫忚鎶曞奖鐭╅樀/瑙嗛敟
        //MatrixHelper.perspectiveM(mProjectMatrix,0,45,ratio,1f,300);
        
        //璁剧疆鐩告満浣嶇疆
        Matrix.setLookAtM(mViewMatrix, 0,
        		0.0f, 0.0f, 0.0f, //0f, 0.0f, 0.0f, 
        		0.0f, 0.0f, -4.0f, //0.0f, 0.0f,-1.0f,
        		0f, 1.0f, 0.0f);
        //妯″瀷鐭╅樀
        Matrix.setIdentityM(mModelMatrix,0);
        //Matrix.scaleM(mModelMatrix,0,2,2,2);
        Log.v(TAG, "           setSize 11             ");
    }

    public void setMatrix(float[] matrix){
        System.arraycopy(matrix,0,mRotateMatrix,0,16);
    }

    public void drawTexture(){
    	Log.v(TAG, "           drawTexture             ");
    	
        GLES20.glUseProgram(mHProgram);
        GLES20.glUniformMatrix4fv(mHProjMatrix,1,false,mProjectMatrix,0);
        GLES20.glUniformMatrix4fv(mHViewMatrix,1,false,mViewMatrix,0);
        GLES20.glUniformMatrix4fv(mHModelMatrix,1,false,mModelMatrix,0);
        GLES20.glUniformMatrix4fv(mHRotateMatrix,1,false,mRotateMatrix,0);
        
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        //GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureId);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,textureId);

        GLES20.glEnableVertexAttribArray(mHPosition);
        GLES20.glVertexAttribPointer(mHPosition,3,GLES20.GL_FLOAT,false,0,posBuffer);
        GLES20.glEnableVertexAttribArray(mHCoordinate);
        GLES20.glVertexAttribPointer(mHCoordinate,2,GLES20.GL_FLOAT,false,0,cooBuffer);
        
        
        //GLES20.glUniformMatrix4fv(textureTranformHandle, 1, false, videoTextureTransform, 0);
        
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);

        GLES20.glDisableVertexAttribArray(mHPosition);
        Log.v(TAG, "           drawTexture 00            ");	
    }


    private void calculateAttribute(){
        ArrayList<Float> alVertix = new ArrayList<Float>();
        ArrayList<Float> textureVertix = new ArrayList<Float>();
        for (double vAngle = 0; vAngle < Math.PI; vAngle = vAngle + angleSpan){

            for (double hAngle = 0; hAngle < 2*Math.PI; hAngle = hAngle + angleSpan){
                float x0 = (float) (radius* Math.sin(vAngle) * Math.cos(hAngle));
                float y0 = (float) (radius* Math.sin(vAngle) * Math.sin(hAngle));
                float z0 = (float) (radius * Math.cos((vAngle)));

                float x1 = (float) (radius* Math.sin(vAngle) * Math.cos(hAngle + angleSpan));
                float y1 = (float) (radius* Math.sin(vAngle) * Math.sin(hAngle + angleSpan));
                float z1 = (float) (radius * Math.cos(vAngle));

                float x2 = (float) (radius* Math.sin(vAngle + angleSpan) * Math.cos(hAngle + angleSpan));
                float y2 = (float) (radius* Math.sin(vAngle + angleSpan) * Math.sin(hAngle + angleSpan));
                float z2 = (float) (radius * Math.cos(vAngle + angleSpan));

                float x3 = (float) (radius* Math.sin(vAngle + angleSpan) * Math.cos(hAngle));
                float y3 = (float) (radius* Math.sin(vAngle + angleSpan) * Math.sin(hAngle));
                float z3 = (float) (radius * Math.cos(vAngle + angleSpan));

                alVertix.add(x1);
                alVertix.add(y1);
                alVertix.add(z1);
                alVertix.add(x0);
                alVertix.add(y0);
                alVertix.add(z0);
                alVertix.add(x3);
                alVertix.add(y3);
                alVertix.add(z3);

                float s0 = (float) (hAngle / Math.PI/2);
                float s1 = (float) ((hAngle + angleSpan)/Math.PI/2);
                float t0 = (float) (vAngle / Math.PI);
                float t1 = (float) ((vAngle + angleSpan) / Math.PI);

                textureVertix.add(s1);// x1 y1瀵瑰簲绾圭悊鍧愭爣
                textureVertix.add(t0);
                textureVertix.add(s0);// x0 y0瀵瑰簲绾圭悊鍧愭爣
                textureVertix.add(t0);
                textureVertix.add(s0);// x3 y3瀵瑰簲绾圭悊鍧愭爣
                textureVertix.add(t1);

                alVertix.add(x1);
                alVertix.add(y1);
                alVertix.add(z1);
                alVertix.add(x3);
                alVertix.add(y3);
                alVertix.add(z3);
                alVertix.add(x2);
                alVertix.add(y2);
                alVertix.add(z2);

                textureVertix.add(s1);// x1 y1瀵瑰簲绾圭悊鍧愭爣
                textureVertix.add(t0);
                textureVertix.add(s0);// x3 y3瀵瑰簲绾圭悊鍧愭爣
                textureVertix.add(t1);
                textureVertix.add(s1);// x2 y3瀵瑰簲绾圭悊鍧愭爣
                textureVertix.add(t1);
            }
        }
        vCount = alVertix.size() / 3;
        posBuffer = convertToFloatBuffer(alVertix);
        cooBuffer=convertToFloatBuffer(textureVertix);
    }

    private FloatBuffer convertToFloatBuffer(ArrayList<Float> data){
        float[] d=new float[data.size()];
        for (int i=0;i<d.length;i++){
            d[i]=data.get(i);
        }

        ByteBuffer buffer=ByteBuffer.allocateDirect(data.size()*4);
        buffer.order(ByteOrder.nativeOrder());
        FloatBuffer ret=buffer.asFloatBuffer();
        ret.put(d);
        ret.position(0);
        return ret;
    }

    private int createTexture(){
        int[] texture=new int[1];
        try {
            mBitmap=BitmapFactory.decodeStream(context.getAssets().open("vr/360sp.jpg")); //String drawableSrc
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(mBitmap!=null&&!mBitmap.isRecycled()){
            //鐢熸垚绾圭悊
            GLES20.glGenTextures(1,texture,0);
            //鐢熸垚绾圭悊
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture[0]);
            //璁剧疆缂╁皬杩囨护涓轰娇鐢ㄧ汗鐞嗕腑鍧愭爣鏈�鎺ヨ繎鐨勪竴涓儚绱犵殑棰滆壊浣滀负闇�瑕佺粯鍒剁殑鍍忕礌棰滆壊
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
            //璁剧疆鏀惧ぇ杩囨护涓轰娇鐢ㄧ汗鐞嗕腑鍧愭爣鏈�鎺ヨ繎鐨勮嫢骞蹭釜棰滆壊锛岄�氳繃鍔犳潈骞冲潎绠楁硶寰楀埌闇�瑕佺粯鍒剁殑鍍忕礌棰滆壊
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
            //璁剧疆鐜粫鏂瑰悜S锛屾埅鍙栫汗鐞嗗潗鏍囧埌[1/2n,1-1/2n]銆傚皢瀵艰嚧姘歌繙涓嶄細涓巄order铻嶅悎
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
            //璁剧疆鐜粫鏂瑰悜T锛屾埅鍙栫汗鐞嗗潗鏍囧埌[1/2n,1-1/2n]銆傚皢瀵艰嚧姘歌繙涓嶄細涓巄order铻嶅悎
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
            //鏍规嵁浠ヤ笂鎸囧畾鐨勫弬鏁帮紝鐢熸垚涓�涓�2D绾圭悊
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmap, 0);
            return texture[0];
        }
        return 0;
    }
}

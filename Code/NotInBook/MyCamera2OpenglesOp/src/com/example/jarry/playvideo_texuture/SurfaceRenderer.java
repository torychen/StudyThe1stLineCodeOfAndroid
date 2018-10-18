package com.example.jarry.playvideo_texuture;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

import com.example.jarry.R;


/**
 * 缁樺埗鍓嶏紝renderer鐨勯厤缃紝鍒濆鍖朎GL锛屽紑濮嬩竴涓粯鍒剁嚎绋�.
 * 杩欎釜绫婚渶瑕佸瓙绫诲幓瀹炵幇鐩稿簲鐨勭粯鍒跺伐浣�.
 *
 * 鍏蜂綋娴佺▼鍙互鍙傝�僪ttp://www.cnblogs.com/kiffa/archive/2013/02/21/2921123.html
 * 鐩稿簲鐨勫嚱鏁板彲浠ユ煡鐪嬶細 https://www.khronos.org/registry/egl/sdk/docs/man/
 */
public  class SurfaceRenderer implements Runnable, SurfaceTexture.OnFrameAvailableListener{
    public static String LOG_TAG = SurfaceRenderer.class.getSimpleName();

    private static float squareSize = 1.0f;
    private static float squareCoords[] = {
            -squareSize,  squareSize,   // top left
            -squareSize, -squareSize,   // bottom left
            squareSize, -squareSize,    // bottom right
            squareSize,  squareSize }; // top right

    private static short drawOrder[] = { 0, 1, 2, 0, 2, 3};

    private Context context;

    // Texture to be shown in backgrund
    private FloatBuffer textureBuffer;
    private float textureCoords[] = {
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f };
    private int[] textures = new int[1];

    private int shaderProgram;
    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;

    private SurfaceTexture videoTexture;
    private float[] videoTextureTransform;
    private boolean frameAvailable = false;

    int textureParamHandle;
    int textureCoordinateHandle;
    int positionHandle;
    int textureTranformHandle;
    
    protected final SurfaceTexture surfaceTexture;
    protected int width;
    protected int height;

    private EGL10 egl;
    private EGLContext eglContext;
    private EGLDisplay eglDisplay;
    private EGLSurface eglSurface;

    /***
     * 鏄惁姝ｅ湪缁樺埗(draw)
     */
    private boolean running = false;
    
    public SurfaceRenderer(Context context, SurfaceTexture surfaceTexture, int width, int height) {
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
    
    public  int mColorFlag=0;
    public  int xyFlag=0;
    
    private float[] matrix=new float[16];
    private float[] matrix0=new float[16];
    private float[] mModelMatrix=new float[16];
    private float[] mModelMatrix0=new float[16];
    
    //镜像
    public  float[] flip(float[] m,boolean x,boolean y){
        if(x||y){
            Matrix.scaleM(m,0,x?-1:1,y?-1:1,1);
        }
        return m;
    }
    
    public void setSize(){
    	/*
        float ratio=(float)width/height;
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1, 1, 1, 3);
        //
        Matrix.setLookAtM(mViewMatrix, 0,
        		0.0f, 0.0f, 1.0f, //0f, 0.0f, 0.0f, 
        		0.0f, 0.0f, 0.0f, //0.0f, 0.0f,-1.0f,
        		0f,1.0f, 0.0f);
        //
        Matrix.setIdentityM(mModelMatrix,0);
        //Matrix.scaleM(mModelMatrix,0,2,2,2);
        
        Matrix.multiplyMM(matrix,0,mProjectMatrix,0,mViewMatrix,0);
        Matrix.multiplyMM(matrix0,0,mProjectMatrix,0,mViewMatrix,0);
        */
    	Matrix.setIdentityM(mModelMatrix,0);
    	Matrix.setIdentityM(mModelMatrix0,0);
    	
        matrix = flip(mModelMatrix, true, false);
        matrix0 = flip(mModelMatrix0, false, true);
    }
    
    private void setupGraphics()
    {
        final String vertexShader = HelpUtils.readTextFileFromRawResource(context, R.raw.vetext_sharder);
        final String fragmentShader = HelpUtils.readTextFileFromRawResource(context, R.raw.fragment_sharder);

        final int vertexShaderHandle = HelpUtils.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);
        final int fragmentShaderHandle = HelpUtils.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);
        shaderProgram = HelpUtils.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,
                new String[]{"texture","vPosition","vTexCoordinate","textureTransform"});

        GLES20.glUseProgram(shaderProgram);
        textureParamHandle = GLES20.glGetUniformLocation(shaderProgram, "texture");
        textureCoordinateHandle = GLES20.glGetAttribLocation(shaderProgram, "vTexCoordinate");
        positionHandle = GLES20.glGetAttribLocation(shaderProgram, "vPosition");
        textureTranformHandle = GLES20.glGetUniformLocation(shaderProgram, "textureTransform");
        
        setSize();
    }

    private void setupVertexBuffer()
    {
        // Draw list buffer
        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder. length * 2);
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

    private void setupTexture()
    {
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

    }

    protected boolean draw()
    {
        synchronized (this)
        {
            if (frameAvailable) {
                videoTexture.updateTexImage();
                videoTexture.getTransformMatrix(videoTextureTransform);
                frameAvailable = false;
            }
            else{
                return false;
            }

        }
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glViewport(0, 0, width, height);
        this.drawTexture();

        return true;
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

        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textures[0]);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glUniform1i(textureParamHandle, 0);

        GLES20.glEnableVertexAttribArray(textureCoordinateHandle);
        GLES20.glVertexAttribPointer(textureCoordinateHandle, 4, GLES20.GL_FLOAT, false, 0, textureBuffer);

        GLES20.glUniformMatrix4fv(textureTranformHandle, 1, false, videoTextureTransform, 0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(textureCoordinateHandle);
    }


    protected void initGLComponents() {
        setupVertexBuffer();
        setupTexture();
        setupGraphics();
    }

    protected void deinitGLComponents() {
        GLES20.glDeleteTextures(1, textures, 0);
        GLES20.glDeleteProgram(shaderProgram);
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
}

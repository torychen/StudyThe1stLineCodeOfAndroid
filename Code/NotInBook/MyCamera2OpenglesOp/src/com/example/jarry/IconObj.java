
package com.example.jarry;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;
import android.util.SparseArray;

public class IconObj {
    private static final String TAG="Filter";
    protected int mProgram;
    protected int mHPosition;
    protected int mHCoord;
    protected int mMVPMatrix;
    protected int mHTexture;
    protected int mHTexture0;
    protected int mHTexture1;
    protected int mRatio;
    public float ratio=0.5f;
    private float[] matrix=new float[16];
    private float[] mMatrix=new float[16];
    protected FloatBuffer mVerBuffer;

    /**
     * 纹理坐标Buffer
     */
    protected FloatBuffer mTexBuffer;

    /**
     * 索引坐标Buffer
     */
    protected ShortBuffer drawListBuffer;
    private int textureId=-1;
    private int textureId0=-1;
    private int textureId1=-1;
    //顶点坐标
    private float pos[] = {
        -0.9f, 0.9f,
        -0.9f, 0.7f,
        -0.7f, 0.7f,
        -0.7f, 0.9f,
    };

    //纹理坐标
    private float[] coord={
        0.0f,  1.0f,
        0.0f,  0.0f,
        1.0f,  0.0f,
        1.0f,  1.0f,
    };
    
    private final float[] sPos={
        -1.0f,1.0f,    //���Ͻ�
        -1.0f,-1.0f,   //���½�
         1.0f,1.0f,     //���Ͻ�
         1.0f,-1.0f     //���½�
    };
    
    private final float[] sPos0={
        -0.95f,0.95f,   //���Ͻ�
        -0.95f,0.75f,   //���½�
        -0.75f,0.95f,    //���Ͻ�
        -0.75f,0.75f     //���½�
    };
        
    private final float[] sCoord={
         0.0f,0.0f,
         0.0f,1.0f,
         1.0f,0.0f,
         1.0f,1.0f,
    };

    private static short drawOrder[] = {0, 1, 2, 0, 2, 3};
    
    Context context;
    public IconObj(Context context){
        this.context=context;
        initBuffer();
        createProgram();
        textureId = initTexture(R.drawable.earth);
        textureId0 = initTexture(R.drawable.bg);
        textureId1 = initTexture(R.drawable.opengles);
        Matrix.setIdentityM(mMatrix, 0);
        matrix = flip(mMatrix, true, false);
    }
    
    //镜像
    public  float[] flip(float[] m,boolean x,boolean y){
        if(x||y){
            Matrix.scaleM(m,0,x?-1:1,y?-1:1,1);
        }
        return m;
    }

    protected void initBuffer(){
        ByteBuffer a=ByteBuffer.allocateDirect(sPos.length*4);
        a.order(ByteOrder.nativeOrder());
        mVerBuffer=a.asFloatBuffer();
        mVerBuffer.put(sPos);
        mVerBuffer.position(0);
        
        ByteBuffer b=ByteBuffer.allocateDirect(sCoord.length*4);
        b.order(ByteOrder.nativeOrder());
        mTexBuffer=b.asFloatBuffer();
        mTexBuffer.put(sCoord);
        mTexBuffer.position(0);
        
        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);
    }
    
    protected  void createProgram(){
    	String mVertexShader=ShaderUtil.loadFromAssetsFile("base_vertex.sh", context.getResources());
        ShaderUtil.checkGlError("==ss==");   
        //����ƬԪ��ɫ���Ľű�����
        String mFragmentShader=ShaderUtil.loadFromAssetsFile("base_fragment.sh", context.getResources());  
        //���ڶ�����ɫ����ƬԪ��ɫ����������
        ShaderUtil.checkGlError("==ss==");      
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        mHPosition= GLES20.glGetAttribLocation(mProgram, "vPosition");
        mHCoord=GLES20.glGetAttribLocation(mProgram,"vCoord");
        mMVPMatrix=GLES20.glGetUniformLocation(mProgram,"vMatrix");
        mHTexture=GLES20.glGetUniformLocation(mProgram,"vTexture");
        mHTexture0=GLES20.glGetUniformLocation(mProgram,"vTexture0");
        mHTexture1=GLES20.glGetUniformLocation(mProgram,"vTexture1");
        
        mRatio = GLES20.glGetUniformLocation(mProgram,"mratio");
    }
   
    public int initTexture(int drawableId)
	{
		//��������ID
		int[] textures = new int[1];
		GLES20.glGenTextures
		(
				1,          //����������id������
				textures,   //����id������
				0           //ƫ����
		);    
		int textureId = textures[0];    
		Log.i(TAG, " initTexture textureId = " + textureId);
		Log.i(TAG, " initTexture textureId = " + textureId);
		
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
        
        //ͨ������������ͼƬ===============begin===================
        InputStream is = context.getResources().openRawResource(drawableId);
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
        //ͨ������������ͼƬ===============end=====================  
        //ʵ�ʼ�������
        GLUtils.texImage2D
        (
        		GLES20.GL_TEXTURE_2D,   //�������ͣ���OpenGL ES�б���ΪGL10.GL_TEXTURE_2D
        		0, 					  //����Ĳ�Σ�0��ʾ����ͼ��㣬�������Ϊֱ����ͼ
        		bitmapTmp, 			  //����ͼ��
        		0					  //����߿�ߴ�
        );
        bitmapTmp.recycle(); 		  //������سɹ����ͷ�ͼƬ 
        return textureId;
	}

    public void drawSelf() {
    	GLES20.glUseProgram(mProgram);
        GLES20.glEnableVertexAttribArray(mHPosition);
        GLES20.glVertexAttribPointer(mHPosition,2, GLES20.GL_FLOAT, false, 0,mVerBuffer);
        GLES20.glEnableVertexAttribArray(mHCoord);
        GLES20.glVertexAttribPointer(mHCoord, 2, GLES20.GL_FLOAT, false, 0, mTexBuffer);
        
        //GLES20.glUniformMatrix4fv(mMVPMatrix,1,false,matrix,0);
        GLES20.glUniform1f(mRatio, ratio);
        
        //������
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);  
        GLES20.glUniform1i(mHTexture, 0);
        
        //������
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId0);  
        GLES20.glUniform1i(mHTexture0, 1);
        
      //������
        GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId1);  
        GLES20.glUniform1i(mHTexture1, 2);
        
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);
        //GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
        
        GLES20.glDisableVertexAttribArray(mHPosition);
        GLES20.glDisableVertexAttribArray(mHCoord);
    }
}

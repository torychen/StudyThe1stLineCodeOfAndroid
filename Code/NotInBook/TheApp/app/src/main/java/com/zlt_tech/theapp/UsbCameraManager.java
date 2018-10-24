package com.zlt_tech.theapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;


public class UsbCameraManager extends Observable implements TextureView.SurfaceTextureListener {
    private CameraManager mCameraManager;
    private CameraDevice mCamera;
    private CaptureRequest.Builder mCaptureRequestBuilder;
    private CameraCaptureSession mCameraCaptureSession;

    private Context mContext;

    private HandlerThread mChildHandlerThread;
    private Handler mChildHandler;
    private Handler mMainHandler;

    private TextureView mTextureView;

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        if (!openCamera()) {
            Log.e(TAG, "onSurfaceTextureAvailable: fail to open camera.", null);
        }

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        //Inform AICoreManager one frame ready.
        Bitmap bitmap = mTextureView.getBitmap();
        setChanged();
        notifyObservers(bitmap);
    }

    private static final String TAG = "UsbCameraManager";
    UsbCameraManager(Context context, Handler handler, CameraManager cameraManager, TextureView textureView) {
        Log.d(TAG, "UsbCameraManager: ctor");
        mContext = context;
        mCameraManager = cameraManager;
        mMainHandler = handler;
        mTextureView = textureView;

        mTextureView.setSurfaceTextureListener(this);


    }

    private boolean openCamera()
    {
        try {


            mChildHandlerThread = new HandlerThread("UsbCameraManager thread.");
            mChildHandlerThread.start();
            mChildHandler = new Handler(mChildHandlerThread.getLooper());


            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) !=
                    PackageManager.PERMISSION_GRANTED) {

                Log.e(TAG, "onCreate: No permission for Camera", null);
                return false;
            }

            for (String id: mCameraManager.getCameraIdList()
                 ) {
                MyUtility.Toast(mContext, "the camera id is: " + id);
            }

            String cameraID = "" + CameraCharacteristics.LENS_FACING_FRONT;

            mCameraManager.openCamera(cameraID, mCameraDeviceStateCallback, mMainHandler);
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }

    public boolean init(Observer observer) {
        Log.d(TAG, "init: ");


        this.addObserver(observer);

        return true;
    }

    /**
     * Camera StateCallback
     */
    private CameraDevice.StateCallback mCameraDeviceStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            try {
                MyUtility.Toast(mContext, "Camera onOpened");
                mCamera = cameraDevice;
                createCaptureSession(mCamera);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }


        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            MyUtility.Toast(mContext, "Camera onDisconnected");
            quit();

        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int i) {
            MyUtility.Toast(mContext, "Camera onError" + " " + i);
        }
    };


    /**
     * To create capture session.
     *
     */
    private void createCaptureSession(CameraDevice cameraDevice) throws CameraAccessException {
        try {

            mCaptureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

            //The surface of TextureView.
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            Surface surface = new Surface(texture);
            mCaptureRequestBuilder.addTarget(surface);

            //Auto focus
            mCaptureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_VIDEO);
            //Auto exposure
            mCaptureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_OFF);

            mCamera.createCaptureSession(Arrays.asList(surface), m_SessionCallback, mChildHandler);

        } catch (Exception e) {
            e.printStackTrace();
            throw new CameraAccessException(CameraAccessException.CAMERA_ERROR);
        }

    }

    private CameraCaptureSession.StateCallback m_SessionCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
            try {
                mCameraCaptureSession = cameraCaptureSession;

                cameraCaptureSession.setRepeatingRequest(mCaptureRequestBuilder.build(), null, mChildHandler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
            Toast.makeText(mContext, "Session configure failed", Toast.LENGTH_SHORT).show();

        }
    };

    public void quit() {
        Log.d(TAG, "quit: ");

        //Do NOT clean up mContext and mTextureView since MainActivity will handle that.

        quitBackgroundThread();

        closeCamera();
    }

    private void closeCamera() {
        try {
            if (mCameraCaptureSession != null) {
                mCameraCaptureSession.stopRepeating();
                mCameraCaptureSession = null;
            }

            if (mCaptureRequestBuilder != null)
            {
                mCaptureRequestBuilder = null;
            }

            if (mCameraManager != null) {
                mCameraManager = null;
            }

            if (mCamera != null) {

                mCamera.close();
                mCamera = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void quitBackgroundThread() {
        mChildHandlerThread.quitSafely();
        try {
            mChildHandlerThread.join();
            mChildHandlerThread = null;

            mChildHandler = null;

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

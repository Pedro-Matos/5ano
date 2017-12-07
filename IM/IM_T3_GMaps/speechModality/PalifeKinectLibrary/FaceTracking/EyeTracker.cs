

namespace Paelife.KinectFramework.FaceTracking
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Text;
    using Microsoft.Kinect;
    using Microsoft.Kinect.Toolkit.FaceTracking;
    using System.ComponentModel;

    /// <summary>
    /// An implementation of a FaceTracker, that relies on the diference between the Z
    /// coorditates of the eyes of the user. 
    /// </summary>
    class EyeTracker : FaceTracker, IDisposable
    {
        private Microsoft.Kinect.Toolkit.FaceTracking.FaceTracker faceTracker;
        private KinectSensor sensor;
        private byte[] colors;
        private short[] depths;
        private float epsilon;

        private DateTime lastTrackFaceChanged = DateTime.MinValue;
        
        /// <summary>
        /// The default constructor of EyeTracker. The <paramref name="epsilon"/> is the
        /// distance that should be used when comparing the Z coorditates of the eyes 
        /// of the user. If the distance is equal or less than the <paramref name="epsilon"/>, 
        /// it is considered that the user is looking.
        /// </summary>
        public EyeTracker(KinectManager kinectManager, float epsilon = 0.02f)
        {
            this.sensor = kinectManager.KinectSensor;
            this.epsilon = epsilon;
        }

        //! @copydoc FaceTracker::Track((Skeleton[],ColorImageFrame,DepthImageFrame,int)
        public override void Track(Skeleton[] skeletons, ColorImageFrame colorFrame,
            DepthImageFrame depthFrame, int nearestUserId)
        {
            if (faceTracker == null)
            {
                try
                {
                    faceTracker = new Microsoft.Kinect.Toolkit.FaceTracking.FaceTracker(sensor);
                }
                catch (InvalidOperationException)
                {
                    this.faceTracker = null;
                    UpdateFaceTrackingStatusInternally(FaceTracker.FaceTrackingState.UnableToDetectFaces);
                    return;
                }
            }
            
            if (colors == null)
                colors = new byte[sensor.ColorStream.FramePixelDataLength];

            if (colorFrame == null)
            {
                UpdateFaceTrackingStatusInternally(FaceTracker.FaceTrackingState.UnableToDetectFaces);
                return;
            }

            colorFrame.CopyPixelDataTo(colors);


            if (depths == null)
                depths = new short[sensor.DepthStream.FramePixelDataLength];

            if (depthFrame == null)
            {
                UpdateFaceTrackingStatusInternally(FaceTracker.FaceTrackingState.UnableToDetectFaces);
                return;
            }
            depthFrame.CopyPixelDataTo(depths);


            bool? nearUserLooking = null;
            bool? farUserLooking = null;
            int nTrackedUsers = 0;

            foreach (Skeleton skeleton in skeletons)
            {
                if (skeleton.TrackingState == SkeletonTrackingState.Tracked)
                {
                    nTrackedUsers++;

                    var frame = faceTracker.Track(sensor.ColorStream.Format, colors, sensor.DepthStream.Format, depths, skeleton);
                    bool? isLookingToSensor = null;

                    if (frame == null)
                    {
                        if (skeleton.TrackingId == nearestUserId)
                            nearUserLooking = isLookingToSensor;
                        else farUserLooking = isLookingToSensor;
                    }
                    else
                    {
                        var shape = frame.Get3DShape();

                        var leftEyeZ = shape[FeaturePoint.AboveMidUpperLeftEyelid].Z;
                        var rightEyeZ = shape[FeaturePoint.AboveMidUpperRightEyelid].Z;

                        var eyeDistZ = Math.Abs(leftEyeZ - rightEyeZ);

                        if (eyeDistZ == 0.0) //special case where, most of the times, indicates an error
                            isLookingToSensor = null;
                        else
                            isLookingToSensor = eyeDistZ <= epsilon;

                        if (skeleton.TrackingId == nearestUserId)
                            nearUserLooking = isLookingToSensor;
                        else farUserLooking = isLookingToSensor;
                    }
                }
            }

            FaceTracker.FaceTrackingState trackFaceInternalState = FaceTracker.FaceTrackingState.Disabled;
            trackFaceInternalState = getFaceTrackState(nTrackedUsers, nearUserLooking, farUserLooking);
            UpdateFaceTrackingStatusInternally(trackFaceInternalState);
        }

        private void UpdateFaceTrackingStatusInternally(FaceTrackingState trackFaceInternalState)
        {
            if (trackFaceCurrentState != trackFaceInternalState)
            {
                lastTrackFaceChanged = DateTime.Now;
                trackFaceCurrentState = trackFaceInternalState; //assim nao criamos notificacao
            }
            else if (DateTime.Now > lastTrackFaceChanged + lookChangedStabilizingInterval)
                NotifyPropertyChanged("FaceTrackingCurrentState");
        }

        /// <summary>
        /// Disposes the EyeTracker.
        /// </summary>
        public void Dispose()
        {
            faceTracker.Dispose();
        }
        

        private FaceTracker.FaceTrackingState getFaceTrackState(int nTrackedUsers, bool? nearUserLooking, bool? farUserLooking)
        {
            FaceTracker.FaceTrackingState trackFaceInternalState = FaceTracker.FaceTrackingState.Disabled;
            if (nTrackedUsers == 0)
                FaceTrackingCurrentState = FaceTracker.FaceTrackingState.UnableToDetectFaces;
            else
            {
                if (nTrackedUsers > 1)
                {
                    switch (nearUserLooking)
                    {
                        case true:
                            switch (farUserLooking)
                            {
                                case true:
                                    trackFaceInternalState = FaceTracker.FaceTrackingState.BothUsersAreLooking;
                                    break;
                                case false:
                                case null:
                                    trackFaceInternalState = FaceTracker.FaceTrackingState.NearestUserIsLooking;
                                    break;
                            }
                            break;

                        case false:
                        case null:
                            switch (farUserLooking)
                            {
                                case true:
                                    trackFaceInternalState = FaceTracker.FaceTrackingState.FarthestUserIsLooking;
                                    break;
                                case false:
                                    trackFaceInternalState = FaceTracker.FaceTrackingState.NeitherUserIsLooking;
                                    break;
                                case null:
                                    if (nearUserLooking == null)
                                        trackFaceInternalState = FaceTracker.FaceTrackingState.UnableToDetectFaces;
                                    else
                                        trackFaceInternalState = FaceTracker.FaceTrackingState.NeitherUserIsLooking;
                                    break;
                            }
                            break;
                    }
                }
                else
                {
                    switch (nearUserLooking)
                    {
                        case true:
                            trackFaceInternalState = FaceTracker.FaceTrackingState.UserIsLooking;
                            break;
                        case false:
                            trackFaceInternalState = FaceTracker.FaceTrackingState.UserNotLooking;
                            break;
                        case null:
                            trackFaceInternalState = FaceTracker.FaceTrackingState.UnableToDetectFaces;
                            break;
                    }
                }
            }

            return trackFaceInternalState;
        }

    }
}


namespace Paelife.KinectFramework.FaceTracking
{
    using Microsoft.Kinect;
    using System;
    using System.ComponentModel;

    /// <summary>
    /// An abstract class that defines the possible states of the face tracking,
    /// as well as the properties and methods a Face Tracker should have.
    /// </summary>
    public abstract class FaceTracker : INotifyPropertyChanged
    {
        //! @cond
        protected void NotifyPropertyChanged(string caller)
        {
            if (PropertyChanged != null)
                PropertyChanged(this, new PropertyChangedEventArgs(caller));
        }

        protected TimeSpan lookChangedStabilizingInterval = TimeSpan.FromSeconds(0.500);

        protected FaceTracker.FaceTrackingState trackFaceCurrentState = FaceTracker.FaceTrackingState.Disabled;
        //! @endcond


        /// <summary>
        /// Enumerates all the possible states of face tracking.
        /// </summary>
        public enum FaceTrackingState
        {
            Disabled,
            UnableToDetectFaces,
            UserIsLooking,
            UserNotLooking,
            BothUsersAreLooking,
            NearestUserIsLooking,
            FarthestUserIsLooking,
            NeitherUserIsLooking
        };


        /// <summary>
        /// Indicates the current tracking state, which can be one of the values in
        /// <see cref="FaceTrackingState"/>.
        /// </summary>
        public FaceTracker.FaceTrackingState FaceTrackingCurrentState
        {
            get
            {
                return this.trackFaceCurrentState;
            }

            protected set
            {
                if (this.trackFaceCurrentState != value)
                {
                    this.trackFaceCurrentState = value;
                    NotifyPropertyChanged("FaceTrackingCurrentState");
                }
            }
        }

        /// <summary>
        /// Event implementing INotifyPropertyChanged interface.
        /// Subscribe to this event to get notified when properties change.
        /// </summary>
        public event PropertyChangedEventHandler PropertyChanged;

        /// <summary>
        /// When tracking the face of the user, it may occur that, only in a particular frame,
        /// an error occurs and the face can't be detected. This way, by setting a interval
        /// where the result of the face tracking must be always the same, we garantue a
        /// more accurate results. [see more bellow]
        /// 
        /// Note that this class doesn't guarantee this property by itself. It is the implementations
        /// of this abstract class that should check for this value before notifying that
        /// the properties have changed.
        /// 
        /// The value is set to 0.5 seconds, by default.
        /// </summary>
        public TimeSpan StabilizingInterval
        {
            get { return lookChangedStabilizingInterval; }
            set { lookChangedStabilizingInterval = value; }
        }

        /// <summary>
        /// Performs the actual track of users' faces.
        /// </summary>
        public abstract void Track(Skeleton[] skeletons, ColorImageFrame colorFrame,
            DepthImageFrame depthFrame, int nearestUserId);
    }
}

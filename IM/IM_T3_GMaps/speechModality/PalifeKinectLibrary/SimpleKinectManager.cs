
namespace Paelife.KinectFramework
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Text;
    using System.ComponentModel;
    using Paelife.KinectFramework.FaceTracking;
    using Paelife.KinectFramework.Gestures;
    using Paelife.KinectFramework.Postures;

    /// <summary>
    /// This class is the simplest way of using all the available features of
    /// <see cref="KinectManager"/>. It comprises the features
    /// in a more easy to use way, facilitating even more the implementation 
    /// of Kinect based applications.
    /// </summary>
    public class SimpleKinectManager : INotifyPropertyChanged
    {
        private bool isDisconnectedField = true;
        private string disconnectedReasonField;
        private int numberOfDetectedUsersField = 0;
        private bool? isPayingAttention = null;
        private bool? isAlone = null;
        private KinectManager kinectManager;

        /// <summary>
        /// Event implementing INotifyPropertyChanged interface.
        /// Subscribe to this event to get notified when properties change. [see details bellow]
        /// 
        /// The properties that fire the PropertyChanged notification are:
        /// - <see cref="IsDisconnected"/>
        /// - <see cref="DisconnectedReason"/>
        /// - <see cref="IsPayingAttention"/>
        /// - <see cref="IsAlone"/>
        /// - <see cref="NumberOfDetectUsers"/>
        /// </summary>
        public event PropertyChangedEventHandler PropertyChanged;

        private void NotifyPropertyChanged(string caller)
        {
            if (PropertyChanged != null)
                PropertyChanged(this, new PropertyChangedEventArgs(caller));
        }

        /// <summary>
        /// Gets a value indicating whether the KinectSensor is currently disconnected.
        /// </summary>
        public bool IsDisconnected
        {
            get
            {
                return this.isDisconnectedField;
            }

            private set
            {
                if (this.isDisconnectedField != value)
                {
                    this.isDisconnectedField = value;
                    NotifyPropertyChanged("IsDisconnected");
                }
            }
        }

        /// <summary>
        /// Gets any message associated with a failure to connect a KinectSensor.
        /// </summary>
        public string DisconnectedReason
        {
            get
            {
                return this.disconnectedReasonField;
            }

            private set
            {
                if (this.disconnectedReasonField != value)
                {
                    this.disconnectedReasonField = value;
                    NotifyPropertyChanged("DisconnectedReason");
                }
            }
        }

        /// <summary>
        /// Detects if the user is paying attention, by tracking if he is looking
        /// at the screen. If more than one user is present, both must be looking at
        /// the screen in order for this property to be true. This property can be null 
        /// if it wasn't possible to track the user's face, if no user is detected,
        /// or if one user is looking and the second user isn't.
        /// </summary>
        public bool? IsPayingAttention
        {
            get
            {
                return this.isPayingAttention;
            }

            private set
            {
                if (this.isPayingAttention != value)
                {
                    this.isPayingAttention = value;
                    NotifyPropertyChanged("IsPayingAttention");
                }
            }
        }

        /// <summary>
        /// Detects if the user is alone. If no user is detected, this property
        /// is set to null.
        /// </summary>
        public bool? IsAlone
        {
            get
            {
                return this.isAlone;
            }

            private set
            {
                if (this.isAlone != value)
                {
                    this.isAlone = value;
                    NotifyPropertyChanged("IsAlone");
                }
            }
        }

        /// <summary>
        /// Gets the number of users currently detected by KinectSensor.
        /// Note that Kinect may only track the skeleton of 2 users,
        /// but can detect up to 6 users.
        /// </summary>
        public int NumberOfDetectUsers
        {
            get
            {
                return this.numberOfDetectedUsersField;
            }

            private set
            {
                if (this.numberOfDetectedUsersField != value)
                {
                    this.numberOfDetectedUsersField = value;
                    NotifyPropertyChanged("NumberOfDetectUsers");
                    switch (numberOfDetectedUsersField)
                    {
                        case 0:
                            IsAlone = null;
                            break;
                        case 1:
                            IsAlone = true;
                            break;
                        default:
                            IsAlone = false;
                            break;
                    }
                }
            }
        }

        /// <summary>
        /// This event is only fired when the user is alone and motion was detected
        /// on the RGB image. The user pixels are excluded from the motion detection
        /// algorithm.
        /// </summary>
        public event EventHandler HidePrivateInformation;

        /// <summary>
        /// Event that is fired when the nearest user to the Kinect performs
        /// a left swipe gesture.
        /// </summary>
        public event EventHandler LeftSwipeDetected;

        /// <summary>
        /// Event that is fired when the nearest user to the Kinect performs
        /// a right swipe gesture.
        /// </summary>
        public event EventHandler RightSwipeDetected;

        public event EventHandler LeftHandAboveHeadDetected;

        /// <summary>
        /// This class is based on a <see cref="KinectManager"/>. You can configure it
        /// by accessing this property.
        /// </summary>
        public KinectManager KinectManager
        {
            get
            {
                return kinectManager;
            }
            private set
            {
                kinectManager = value;
            }
        }

        /// <summary>
        /// The default constructor.
        /// </summary>
        public SimpleKinectManager()
        {
            KinectManager = new KinectManager();

            KinectManager.PropertyChanged +=
                new System.ComponentModel.PropertyChangedEventHandler(kinectManager_PropertyChanged);

            KinectManager.DetectMotion = true;
            KinectManager.MotionDetector.MotionDetected +=
                new EventHandler(motionDetector_MotionDetected);

            KinectManager.FaceTracking = true;
            KinectManager.FaceTracker.PropertyChanged += new System.ComponentModel.PropertyChangedEventHandler(FaceTracker_PropertyChanged);

            GestureDetector LeftHandSwipeGestureDetector =
                new SwipeGestureDetector(Microsoft.Kinect.JointType.HandLeft);
            LeftHandSwipeGestureDetector.OnGestureDetected +=
                new Action<string>(SwipeGestureDetector_OnGestureDetected);
            KinectManager.GestureDetectors.Add(LeftHandSwipeGestureDetector);

            GestureDetector RightHandSwipeGestureDetector =
                new SwipeGestureDetector(Microsoft.Kinect.JointType.HandRight);
            RightHandSwipeGestureDetector.OnGestureDetected +=
                new Action<string>(SwipeGestureDetector_OnGestureDetected);
            KinectManager.GestureDetectors.Add(RightHandSwipeGestureDetector);


            PostureDetector LeftHandOverHeadPostureDetector = 
                new AlgorithmicPostureDetector();
            LeftHandOverHeadPostureDetector.PostureDetected +=
                new Action<string>(PostureDetector_OnPostureDetected);
            KinectManager.PostureDetectors.Add(LeftHandOverHeadPostureDetector);
        }

        

        private void kinectManager_PropertyChanged(object sender, System.ComponentModel.PropertyChangedEventArgs e)
        {
            switch (e.PropertyName)
            {
                case "IsDisconnected":
                    IsDisconnected = ((KinectManager)sender).IsDisconnected;
                    break;

                case "NumberOfDetectUsers":
                    NumberOfDetectUsers = ((KinectManager)sender).NumberOfDetectUsers;
                    break;
            }
        }

        private void motionDetector_MotionDetected(object sender, EventArgs e)
        {
            if(NumberOfDetectUsers == 1)
                HidePrivateInformation(this, EventArgs.Empty);
        }

        private void FaceTracker_PropertyChanged(object sender, System.ComponentModel.PropertyChangedEventArgs e)
        {
            switch (KinectManager.FaceTracker.FaceTrackingCurrentState)
            {
                case FaceTracker.FaceTrackingState.BothUsersAreLooking:
                case FaceTracker.FaceTrackingState.UserIsLooking:
                    IsPayingAttention = true;
                    break;
                case FaceTracker.FaceTrackingState.NeitherUserIsLooking:
                case FaceTracker.FaceTrackingState.UserNotLooking:
                    IsPayingAttention = false;
                    break;
                default:
                    IsPayingAttention = null;
                    break;
            }
        }

        private void SwipeGestureDetector_OnGestureDetected(string gesture)
        {
            if (gesture == "SwipeToLeft")
                LeftSwipeDetected(this, EventArgs.Empty);
            else if (gesture == "SwipeToRight")
                RightSwipeDetected(this, EventArgs.Empty);
        }

        private void PostureDetector_OnPostureDetected(string posture)
        {
            if (posture == "LeftHandOverHead")
                LeftHandAboveHeadDetected(this, EventArgs.Empty);
        }

        /// <summary>
        /// Detects the faces in the current Kinect frame and saves them in the training 
        /// image set. The <paramref name="username"/> indicates the username of the person
        /// in front of Kinect.
        /// </summary>
        public void DetectAndSaveFace(string username)
        {
            KinectManager.FaceRecognizer.DetectAndSaveFace(username);
        }

        /// <summary>
        /// Detects and recognizes a face in the current Kinect frame. If the
        /// <see cref="IFaceRecognizer.TresholdLevel"/> isn't met, an empty string is returned.
        /// </summary>
        public string RecognizeFace()
        {
            return KinectManager.FaceRecognizer.RecognizeFace();
        }
    }

}

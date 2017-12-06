
/*! \mainpage Index Page
 *
 * \section intro_sec Introduction
 *
 * This framework provides functional modules that allow to access Microsoft's Kinect data in a easy way, facilitating the development of Kinect based applications. It is based on Microsoft's Kinect for Windows software development kit.
 * 
 * The main features provided by this framework are:
 * 
 * - Automatic initialization and uninitialization of the Kinect sensor, providing information when errors occur.
 * - Detection and tracking of the number of users present in the room.
 * - Gesture recognition of the user that is closer to the Kinect.
 * - Tracking of users' faces, allowing to perceive if they are paying attention to the system's screen.
 * - Face recognition, so users can be identified just by sitting in front of the screen.
 * - Motion detection of the environment, excluding the user's movements, which quickly allows to hide private information when someone comes nearby.
 *
 *
 * \section structure_sec Struture of the Framework
 *
 * The main class of this framework is <see cref="Paelife.KinectFramework.KinectManager"/>. This class comprises all the features described in the previous section through a set of properties, events and methods. It allows you to manually enable or disable each functionality, thus guaranteeing optimal performance. Most of the features are configurable, so fell free to change and tweak the parameters to see what works best for you.
 *
 * If you intend to use all the features available in this framework, the simplest approach is using the <see cref="Paelife.KinectFramework.SimpleKinectManager"/> class. It automatically initializes all the features with the default values, allowing you to directly access them in a simple, easy to use way.
 *
 * Each package in this framework corresponds to a feature, whose name is pretty self-explanatory. We have <see cref="Paelife.KinectFramework.FaceRecognition"/>, <see cref="Paelife.KinectFramework.FaceTracking"/>, <see cref="Paelife.KinectFramework.Gestures"/>, and <see cref="Paelife.KinectFramework.MotionDetection"/> packages. For each feature we have developed interfaces, so feel free to use your own implementations of each module with KinectManager.
 *
 * We also included Samples in the framework, to facilitate even further the development of your applications.
 *
 *
 * \section install_sec Installation
 * 
 * In order to use Paelife's Kinect framework, just add a reference to <I><B>PaelifeKinectFramework.dll</B></I> in your project. You may also need to add <I><B>Microsoft.Kinect.dll</B></I> if you want to process the Kinect sensor data, e.g., display the Kinect color image.
 * 
 * All other dlls in the "bin" folder are auxiliary and should be put in same folder of your executable.
 *
 * Since some of the required libraries are only available for x86 architectures, make sure that you set the target platform of your project to x86 architectures.
 * 
 * 
 * \section code_sec Code Samples
 * 
 * The code included in this chapter is transcripted from the funcional WPF demo that is included in this framework, in Paelife.KinectFramework.Samples package. Here we'll briefly explain how to use the functionalities provided by this framework.
 * 
 * Please note that this section is not intended to be exhaustive, it only shows the basic code that is required to use this framework. If you need more information about a particular feature, you should refer to the documentation of each class and/or to the source code of the Samples.
 * 
 * \subsection simpleKinMan_sec Using SimpleKinectManager
 * 
 * If you intend to use all the available features of this framework, the simplest approach is using the <see cref="Paelife.KinectFramework.SimpleKinectManager"/> class. We do not recommend using this class if you are not using all the features, because it would result in a loss of performance.
 * 
 * When you instantiate this class:
 * \code{.java}
 * SimpleKinectManager skm = new SimpleKinectManager();
 * \endcode
 * 
 * It automatically connects and initializes the first available Kinect that is connected to your computer. To register to properties' changes, use:
 * \code{.java}
 * skm.PropertyChanged += new System.ComponentModel.PropertyChangedEventHandler(YourPropertyChangedListenerMethod);
 * \endcode
 * 
 * The properties that fire the PropertyChanged notification are:
 * - IsDisconnected
 * - DisconnectedReason
 * - IsPayingAttention
 * - IsAlone
 * - NumberOfDetectUsers
 * 
 * To proccess swipes, register to the following events:
 * \code{.java}
 * skm.LeftSwipeDetected += new EventHandler(YourLeftSwipeDetectedMethod);
 * skm.RightSwipeDetected += new EventHandler(YourRightSwipeDetectedMethod);
 * \endcode
 * 
 * There's also a event you can register to get notified when you should hide provate information:
 * \code{.java}
 * skm.HidePrivateInformation += new EventHandler(YourHidePrivateInformationMethod);
 * \endcode
 * 
 * 
 * \subsection kinMan_sec Using KinectManager
 * 
 * By using <see cref="Paelife.KinectFramework.KinectManager"/> class, you have more control over the provided functionalities, allowing you to configure them to best suit your needs.
 * 
 * When you instantiate this class:
 * \code{.java}
 * KinectManager km = new KinectManager();
 * \endcode
 * 
 * It automatically connects and initializes the first available Kinect that is connected to your computer. To register to properties' changes, use:
 * \code{.java}
 * km.PropertyChanged += new System.ComponentModel.PropertyChangedEventHandler(YourPropertyChangedListenerMethod);
 * \endcode
 * 
 * The properties that fire the PropertyChanged notification are:
 * - IsDisconnected
 * - DisconnectedReason
 * - NumberOfDetectUsers
 * 
 * To enable and proccess motion detection, use:
 * \code{.java}
 * km.DetectMotion = true;
 * km.MotionDetector.MotionDetected += new EventHandler(YourMotionDetectedMethodHere);
 * \endcode
 * 
 * To enable and proccess face tracking, use:
 * \code{.java}
 * km.FaceTracking = true;
 * km.FaceTracker.PropertyChanged += new System.ComponentModel.PropertyChangedEventHandler(YourFaceTrackerPropertyChangedMethodHere);
 * \endcode
 *
 * To enable and proccess gesture recognition, use:
 * \code{.java}
 * GestureDetector LeftHandSwipeGestureDetector =
 *      new SwipeGestureDetector(Microsoft.Kinect.JointType.HandLeft);
 * LeftHandSwipeGestureDetector.OnGestureDetected +=
 *      new Action<string>(LeftHandSwipeGestureDetector_OnGestureDetected);
 * km.GestureDetectors.Add(LeftHandSwipeGestureDetector);
 * 
 * GestureDetector RightHandSwipeGestureDetector =
 *      new SwipeGestureDetector(Microsoft.Kinect.JointType.HandRight);
 * RightHandSwipeGestureDetector.OnGestureDetected +=
 *      new Action<string>(RightHandSwipeGestureDetector_OnGestureDetected);
 * km.GestureDetectors.Add(RightHandSwipeGestureDetector);
 * \endcode
 * 
 */

namespace Paelife.KinectFramework
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Text;
    using Microsoft.Kinect;
    using System.ComponentModel;
    using System.Diagnostics;
    using System.IO;
    using Paelife.KinectFramework.MotionDetection;
    using Paelife.KinectFramework.FaceRecognition;
    using Paelife.KinectFramework.FaceTracking;
    using Paelife.KinectFramework.Gestures;
    using Paelife.KinectFramework.Postures;

    /// <summary>
    /// A data wrapper for a KinectSensor, which makes it easier to use.
    /// 
    /// This class handles Initing and Uniniting the Sensor, as well as keeping the 
    /// Sensor state in sync with the properties here. It also checks the currently used KinectSensor 
    /// state, allowing to check if it is correctly connected and if not, the reason why it is disconnected.
    /// 
    /// This class also has the functionality of detecting the motion in the area captured
    /// by the Kinect RGB camera. Futhermore, it has the abitity of excluding current user pixels from the 
    /// motion detection. It is usefull for detecting, e.g., when another person enters the room.
    /// 
    /// It can track the face of the users, to tell if they are looking to the screen. Up to two
    /// users can be tracked. The result is stored in <see cref="FaceTracker.FaceTrackingCurrentState"/>.
    ///
    /// Face recognition is also available, which allows to recognize a certain user just by 
    /// staying in Kinect captured area. The user's face is persistently stored for posterior recognition.
    ///
    /// Finally, this class also supports the detection of gestures. Each <see cref="Gestures.GestureDetector"/>
    /// tracks the movements of a particular joint.
    /// </summary>
    public class KinectManager : INotifyPropertyChanged
    {
        private KinectSensor kinectSensorValue;        
        private bool isDisconnectedField = true;
        private string disconnectedReasonField;

        private Skeleton[] skeletons = new Skeleton[0];
        private byte[] colorPixelData;
        private short[] depthPixelData;
        
        private int nearestId = -1;
        private int numberOfDetectedUsersField = 0;

        private bool detectMotion = false;
        private IMotionDetector motionDetector = null;

        private EventHandler<AllFramesReadyEventArgs> onAllFramesReadyEventHandler;

        private bool trackFace = false;
        private FaceTracker faceTracker;

        private IFaceRecognizer faceRecognizer = null;

        bool detectSwipes = false;
        List<GestureDetector> gestureDetectorsValue;
        List<PostureDetector> postureDetectorsValue;

        /// <summary>
        /// Event implementing INotifyPropertyChanged interface.
        /// Subscribe to this event to get notified when properties change. [see details bellow]
        /// 
        /// The properties that fire the PropertyChanged notification are:
        /// - <see cref="IsDisconnected"/>
        /// - <see cref="DisconnectedReason"/>
        /// - <see cref="NumberOfDetectUsers"/>
        /// </summary>
        public event PropertyChangedEventHandler PropertyChanged;

        private void NotifyPropertyChanged(string caller)
        {
            if (PropertyChanged != null)
                PropertyChanged(this, new PropertyChangedEventArgs(caller));
        }


        /// <summary>
        /// Gets the currently connected Kinect sensor.
        /// </summary>
        public KinectSensor KinectSensor
        {
            get
            {
                return this.kinectSensorValue;
            }
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
                }
            }
        }

        /// <summary>
        /// Indicates whether Kinect should track motion or not.
        /// If the field <see cref="KinectManager.MotionDetector"/>
        /// doesn't have a value when this  property is set to true, it is automatically instantiated with
        /// a <see cref="MotionDetection.SimpleMotionDetector"/>.
        /// </summary>
        public bool DetectMotion
        {
            get { return detectMotion; }
            set
            {                
               if (detectMotion == false &&
                    value == true &&
                    motionDetector == null)
                {
                    motionDetector = new SimpleMotionDetector();
                }

                detectMotion = value;
            }
        }

        /// <summary>
        /// The motion detector that fires an event when motion is detected on the RGB 
        /// image. The KinectManager class automatically calls the
        /// <see cref="IMotionDetector.ProcessFrame(ref Bitmap, ref Bitmap)"/>
        /// method everytime a new frame is received from the KinectSensor.
        /// Note that the player pixels can be excluded from the motion detection algorithm, 
        /// making it possible to detect only the changes in environment.
        /// </summary>
        public IMotionDetector MotionDetector
        {
            get { return motionDetector; }
            set { motionDetector = value; }
        }



        /// <summary>
        /// Indicates whether Kinect should track the faces of users or not. If the field
        /// <see cref="KinectManager.FaceTracker"/>
        /// doesn't have a value when this  property is set to true, it is automatically instantiated with
        /// a <see cref="FaceTracking.EyeTracker"/>.
        /// </summary>
        public bool FaceTracking
        {
            get { return trackFace; }
            set {
                if (trackFace == false &&
                    value == true &&
                    faceTracker == null)
                {
                    faceTracker = new EyeTracker(this);
                }
                
                trackFace = value; }
        }

        /// <summary>
        /// The object that performs the face tracking of the users. KinectManager 
        /// class automatically calls the
        /// <see cref="FaceTracker.Track(Skeleton[], ColorImageFrame,DepthImageFrame, int)"/>
        /// method everytime a new frame is received from the KinectSensor. The result 
        /// of face traking is constantly updated in
        /// <see cref="FaceTracker.FaceTrackingCurrentState"/>.
        /// </summary>
        public FaceTracker FaceTracker
        {
            get { return faceTracker; }
            set { faceTracker = value; }
        }


        /// <summary>
        /// The object that performs face recognition of the users. When a KinectManager
        /// is instantiated, this field is instantiated whith a 
        /// <see cref="FaceRecognition.EigenFaceRecognizer"/>.
        /// </summary>
        public IFaceRecognizer FaceRecognizer
        {
            get { return faceRecognizer; }
            set { faceRecognizer = value; }
        }


        /// <summary>
        /// Returns the color pixel data of the last frame that KinectManager had proccessed.
        /// </summary>
        public byte[] KinectColorData
        {
            get
            {
                return this.colorPixelData;
            }
        }
        

        /// <summary>
        /// The list of gesture detectors that KinectManager calls every time a skeleton frame is ready.
        /// Only the nearest user gestures are recognized.
        /// </summary>
        public List<GestureDetector> GestureDetectors
        {
            get { return gestureDetectorsValue; }
            private set { gestureDetectorsValue = value; }
        }

        public List<PostureDetector> PostureDetectors
        {
            get { return postureDetectorsValue; }
            private set { postureDetectorsValue = value; }
        }


        /// <summary>
        /// Initializes a new instance of KinectManager class.
        /// When instantiated, it automatically connects to a Kinect sensor, if available,
        /// by calling the <see cref="KinectManager.InitializeKinectSensor"/> method.
        /// </summary>
        public KinectManager()
        {
            onAllFramesReadyEventHandler = new EventHandler<AllFramesReadyEventArgs>(OnAllFramesReady);
            KinectSensor.KinectSensors.StatusChanged +=
                new EventHandler<StatusChangedEventArgs>(KinectSensors_StatusChanged);

            faceRecognizer = new EigenFaceRecognizer(this);
            gestureDetectorsValue = new List<GestureDetector>();
            postureDetectorsValue = new List<PostureDetector>();

            InitializeKinectSensor();
        }


        private void OnAllFramesReady(object sender, AllFramesReadyEventArgs allFramesReadyEventArgs)
        {
            ColorImageFrame colorImageFrame = null;
            DepthImageFrame depthImageFrame = null;
            SkeletonFrame skeletonFrame = null;

            try
            {
                colorImageFrame = allFramesReadyEventArgs.OpenColorImageFrame();
                depthImageFrame = allFramesReadyEventArgs.OpenDepthImageFrame();
                skeletonFrame = allFramesReadyEventArgs.OpenSkeletonFrame();

                if (colorImageFrame == null || depthImageFrame == null || skeletonFrame == null)
                    return;

                //redimensionar o array dos esqueletos, se necessario
                if (this.skeletons.Length != skeletonFrame.SkeletonArrayLength)
                    this.skeletons = new Skeleton[skeletonFrame.SkeletonArrayLength];

                skeletonFrame.CopySkeletonDataTo(this.skeletons);


                if (this.colorPixelData == null || colorPixelData.Length != colorImageFrame.PixelDataLength)
                    this.colorPixelData = new byte[colorImageFrame.PixelDataLength];

                colorImageFrame.CopyPixelDataTo(this.colorPixelData);

                //detectar o numero de utilizadores e qual o mais proximo
                var newNearestId = -1;
                var nearestDistance2 = double.MaxValue;
                int nUsers = 0;
                int nTrackedUsers = 0;
                foreach (var skeleton in skeletons)
                {
                    if (skeleton.TrackingState == SkeletonTrackingState.Tracked)
                    {
                        // Find the distance squared.
                        var distance2 = (skeleton.Position.X * skeleton.Position.X) +
                            (skeleton.Position.Y * skeleton.Position.Y) +
                            (skeleton.Position.Z * skeleton.Position.Z);

                        // Is the new distance squared closer than the nearest so far?
                        if (distance2 < nearestDistance2)
                        {
                            newNearestId = skeleton.TrackingId;
                            nearestDistance2 = distance2;
                        }
                        nTrackedUsers++;
                    }

                    if (skeleton.TrackingState != SkeletonTrackingState.NotTracked)
                        nUsers++;
                }

                this.nearestId = newNearestId;
                this.NumberOfDetectUsers = nUsers;

                //processar a detecao de movimento
                if (detectMotion && motionDetector != null)
                {
                    if (this.depthPixelData == null || depthPixelData.Length != depthImageFrame.BytesPerPixel)
                        this.depthPixelData = new short[depthImageFrame.PixelDataLength];

                    depthImageFrame.CopyPixelDataTo(depthPixelData);

                    var coloredPixels = new byte[colorImageFrame.PixelDataLength];
                    colorImageFrame.CopyPixelDataTo(coloredPixels);

                    var colorBitmap = ImageProcessingAux.CreateBitmapFromPixelData(coloredPixels, colorImageFrame.Width, colorImageFrame.Height);
                    var playerBitmap = ImageProcessingAux.ProccessPlayerPixels(coloredPixels, depthPixelData, colorImageFrame.Format);

                    motionDetector.ProcessFrame(ref colorBitmap, ref playerBitmap);
                }

                //processar a deteccao da face
                if (trackFace && faceTracker != null)
                {
                    faceTracker.Track(skeletons, colorImageFrame, depthImageFrame, nearestId);
                }

                //processar gestos
                if (gestureDetectorsValue.Count > 0)
                    foreach (Skeleton s in skeletons)
                        if (s.TrackingId == nearestId)
                            foreach (GestureDetector gd in gestureDetectorsValue)
                                gd.Add(s.Joints[gd.TrackedJoint].Position, kinectSensorValue);

                //processar posturas
                if (postureDetectorsValue.Count > 0)
                    foreach (Skeleton s in skeletons)
                        if (s.TrackingId == nearestId)
                            foreach (PostureDetector pd in postureDetectorsValue)
                                pd.Add(s, kinectSensorValue);
            }

            finally
            {
                if (colorImageFrame != null)
                    colorImageFrame.Dispose();

                if (depthImageFrame != null)
                    depthImageFrame.Dispose();

                if (skeletonFrame != null)
                    skeletonFrame.Dispose();
            }
        }

        


        private void KinectSensors_StatusChanged(object sender, StatusChangedEventArgs e)
        {
            switch (e.Status)
            {
                case KinectStatus.Connected:
                    if (kinectSensorValue == null)
                    {
                        Debug.WriteLine("New Kinect connected, initializing...");
                        InitializeKinectSensor();
                    }
                    else
                    {
                        Debug.WriteLine("Existing Kinect signalled connection.");
                    }
                    break;
                default:
                    if (e.Sensor == kinectSensorValue)
                    {
                        Debug.WriteLine("Existing Kinect disconnected, uninitializing...");
                        UninitializeKinectSensor();
                    }
                    else
                    {
                        Debug.WriteLine("Other Kinect event occurred.");
                    }
                    break;
            }
        }


        /// <summary>
        /// Handle insertion of a KinectSensor.
        /// </summary>
        public void InitializeKinectSensor()
        {
            this.UninitializeKinectSensor();

            var index = 0;
            while (this.kinectSensorValue == null && index < KinectSensor.KinectSensors.Count)
            {
                try
                {
                    this.kinectSensorValue = KinectSensor.KinectSensors[index];

                    this.kinectSensorValue.Start();
                    this.kinectSensorValue.SkeletonStream.TrackingMode = SkeletonTrackingMode.Default; // Use Seated Mode
                    this.IsDisconnected = false;
                    this.DisconnectedReason = null;
                }
                catch (IOException ex)
                {
                    this.kinectSensorValue = null;

                    this.DisconnectedReason = ex.Message;
                }
                catch (InvalidOperationException ex)
                {
                    this.kinectSensorValue = null;

                    this.DisconnectedReason = ex.Message;
                }

                index++;
            }



            if (this.kinectSensorValue != null)
            {
                kinectSensorValue.ColorStream.Enable(ColorImageFormat.RgbResolution640x480Fps30);
                kinectSensorValue.DepthStream.Enable(DepthImageFormat.Resolution640x480Fps30);
                kinectSensorValue.SkeletonStream.TrackingMode = SkeletonTrackingMode.Default; // Use Seated Mode
                kinectSensorValue.SkeletonStream.Enable();

                kinectSensorValue.AllFramesReady += onAllFramesReadyEventHandler;
            }
        }

        /// <summary>
        /// Handle removal of the KinectSensor.
        /// </summary>
        public void UninitializeKinectSensor()
        {
            if (this.kinectSensorValue != null)
            {
                this.kinectSensorValue.AllFramesReady -= onAllFramesReadyEventHandler;
                this.kinectSensorValue.Stop();
                this.kinectSensorValue = null;
            }

            this.IsDisconnected = true;
            this.DisconnectedReason = "";
        }
    }
}

using Microsoft.Kinect;
using mmisharp;
using Paelife.KinectFramework;
using Paelife.KinectFramework.FaceRecognition;
using Paelife.KinectFramework.FaceTracking;
using Paelife.KinectFramework.Gestures;
using Paelife.KinectFramework.Postures;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace speechModality
{
    /// <summary>
    /// Lógica interna para MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        KinectManager kinectManager;
        private WriteableBitmap colorBitmap;
        private byte[] colorPixels;

        private DateTime backgroundHighlightTime = DateTime.MinValue;
        private DateTime faceRecoDisplayTime = DateTime.MinValue;
        private DateTime centerMessageDisplayTime = DateTime.MinValue;
        private System.Windows.Media.Brush backgroudBrush;

        bool kinectStartedSucessfully = false;


        private LifeCycleEvents lce;
        private MmiCommunication mmic;

        public MainWindow()
        {
            //init LifeCycleEvents..
            lce = new LifeCycleEvents("ASR", "IM", "speech-1", "acoustic", "command");
            mmic = new MmiCommunication("localhost", 8000, "User1", "ASR");

            mmic.Send(lce.NewContextRequest());
            InitializeComponent();
        }

        private void Window_Loaded(object sender, RoutedEventArgs e)
        {
            backgroudBrush = this.Background;

            kinectManager = new KinectManager();

            InitializeKinectManager();
        }

        private void InitializeKinectManager()
        {
            if (kinectManager.KinectSensor != null && !kinectStartedSucessfully)
            {
                kinectManager.KinectSensor.ColorFrameReady += new EventHandler<ColorImageFrameReadyEventArgs>(KinectSensor_ColorFrameReady);

                this.colorPixels = new byte[kinectManager.KinectSensor.ColorStream.FramePixelDataLength];
                this.colorBitmap = new WriteableBitmap(kinectManager.KinectSensor.ColorStream.FrameWidth, kinectManager.KinectSensor.ColorStream.FrameHeight, 96.0, 96.0, PixelFormats.Bgr32, null);
                this.image1.Source = this.colorBitmap;

                kinectManager.PropertyChanged +=
                    new System.ComponentModel.PropertyChangedEventHandler(kinectManager_PropertyChanged);

                //kinectManager.KinectSensor.ElevationAngle = 15;

                textBoxMessagesCenter.Visibility = System.Windows.Visibility.Collapsed;
                kinectStartedSucessfully = true;

                kinectManager.DetectMotion = true;
                kinectManager.MotionDetector.MotionDetected +=
                    new EventHandler(motionDetector_MotionDetected);


                GestureDetector LeftHandSwipeGestureDetector =
                new SwipeGestureDetector(Microsoft.Kinect.JointType.HandLeft);
                LeftHandSwipeGestureDetector.OnGestureDetected +=
                    new Action<string>(LeftHandSwipeGestureDetector_OnGestureDetected);
                kinectManager.GestureDetectors.Add(LeftHandSwipeGestureDetector);

                GestureDetector RightHandSwipeGestureDetector =
                    new SwipeGestureDetector(Microsoft.Kinect.JointType.HandRight);
                RightHandSwipeGestureDetector.OnGestureDetected +=
                    new Action<string>(RightHandSwipeGestureDetector_OnGestureDetected);
                kinectManager.GestureDetectors.Add(RightHandSwipeGestureDetector);

                PostureDetector postureDetector = new AlgorithmicPostureDetector();
                postureDetector.PostureDetected += new Action<string>(PostureDetector_PostureDetected);
                kinectManager.PostureDetectors.Add(postureDetector);
            }

            if (!kinectStartedSucessfully)
            {
                textBoxMessagesCenter.Text = "DISCONNECTED";
                textBoxMessagesCenter.Visibility = System.Windows.Visibility.Visible;
            }
        }

        void PostureDetector_PostureDetected(string posture)
        {
            string json = "{";

            json += "\"" + "categoria" + "\":\"" + "postura" + "\", ";
            json += "\"" + "gesto" + "\":\"" + posture + "\"";
            json.Substring(0, json.Length - 2);
            json += "}";

            var exNot = lce.ExtensionNotification("", "", 0.0f, json);
            Console.WriteLine(exNot);
            mmic.Send(exNot);

            textBoxPostureCenter.Text = posture;
            textBoxPostureCenter.Visibility = System.Windows.Visibility.Visible;

            centerMessageDisplayTime = DateTime.UtcNow + TimeSpan.FromSeconds(2.5);
            Dispatcher.BeginInvoke(
                System.Windows.Threading.DispatcherPriority.Background,
                new UpdateUIDelegate(UpdateUI));
        }

        void LeftHandSwipeGestureDetector_OnGestureDetected(string gesture)
        {

            string json = "{";

            json += "\"" + "categoria" + "\":\"" + "left-hand" + "\", ";
            json += "\"" + "gesto" + "\":\"" + gesture + "\"";
            json.Substring(0, json.Length - 2);
            json += "}";

            var exNot = lce.ExtensionNotification("", "", 0.0f, json);
            Console.WriteLine(exNot);
            mmic.Send(exNot);
           

            textBoxMessagesCenter.Text = "Left Hand: " + gesture;
            textBoxMessagesCenter.Visibility = System.Windows.Visibility.Visible;

            centerMessageDisplayTime = DateTime.UtcNow + TimeSpan.FromSeconds(2.5);
            Dispatcher.BeginInvoke(
                System.Windows.Threading.DispatcherPriority.Background,
                new UpdateUIDelegate(UpdateUI));
        }

        void RightHandSwipeGestureDetector_OnGestureDetected(string gesture)
        {

            string json = "{";

            json += "\"" + "categoria" + "\":\"" + "right-hand" + "\", ";
            json += "\"" + "gesto" + "\":\"" + gesture + "\"";
            json.Substring(0, json.Length - 2);
            json += "}";

            var exNot = lce.ExtensionNotification("", "", 0.0f, json);
            Console.WriteLine(exNot);
            mmic.Send(exNot);


            textBoxMessagesCenter.Text = "Right Hand: " + gesture;
            textBoxMessagesCenter.Visibility = System.Windows.Visibility.Visible;

            centerMessageDisplayTime = DateTime.UtcNow + TimeSpan.FromSeconds(2.5);
            Dispatcher.BeginInvoke(
                System.Windows.Threading.DispatcherPriority.Background,
                new UpdateUIDelegate(UpdateUI));
        }


        void kinectManager_PropertyChanged(object sender, System.ComponentModel.PropertyChangedEventArgs e)
        {
            if (e.PropertyName == "IsDisconnected")
            {
                if (((KinectManager)sender).IsDisconnected)
                {
                    //aqui devia ser chamada uma funcao de uma funcao de des-inicializacao
                    textBoxMessagesCenter.Text = "DISCONNECTED";
                    textBoxMessagesCenter.Visibility = System.Windows.Visibility.Visible;
                }
                else
                    InitializeKinectManager();
            }

            if (e.PropertyName == "NumberOfDetectUsers")
                textBlockStatus.Text = "Number of Detected Users = " + ((KinectManager)sender).NumberOfDetectUsers;
        }


        void KinectSensor_ColorFrameReady(object sender, ColorImageFrameReadyEventArgs e)
        {
            ColorImageFrame colorImageFrame = null;
            colorImageFrame = e.OpenColorImageFrame();
            if (colorImageFrame == null)
                return;

            colorImageFrame.CopyPixelDataTo(this.colorPixels);
            this.colorBitmap.WritePixels(
                new Int32Rect(0, 0, this.colorBitmap.PixelWidth, this.colorBitmap.PixelHeight),
                this.colorPixels,
                this.colorBitmap.PixelWidth * sizeof(int),
                0);

            if (colorImageFrame != null)
                colorImageFrame.Dispose();
        }


        void motionDetector_MotionDetected(object sender, EventArgs e)
        {
            this.backgroundHighlightTime = DateTime.UtcNow + TimeSpan.FromSeconds(0.5);

            Dispatcher.BeginInvoke(
                System.Windows.Threading.DispatcherPriority.Background,
                new UpdateUIDelegate(UpdateUI));
        }



        private delegate void UpdateUIDelegate();

        private void UpdateUI()
        {
            if (DateTime.UtcNow < this.backgroundHighlightTime)
            {
                Dispatcher.BeginInvoke(
                    System.Windows.Threading.DispatcherPriority.Background,
                    new UpdateUIDelegate(UpdateUI));
            }
            else
                this.Background = backgroudBrush;

            if (DateTime.UtcNow < this.faceRecoDisplayTime)
                Dispatcher.BeginInvoke(
                    System.Windows.Threading.DispatcherPriority.Background,
                    new UpdateUIDelegate(UpdateUI));

            if (DateTime.UtcNow < this.centerMessageDisplayTime)
                Dispatcher.BeginInvoke(
                    System.Windows.Threading.DispatcherPriority.Background,
                    new UpdateUIDelegate(UpdateUI));
            else textBoxMessagesCenter.Text = "";


            if (DateTime.UtcNow < this.centerMessageDisplayTime)
                Dispatcher.BeginInvoke(
                    System.Windows.Threading.DispatcherPriority.Background,
                    new UpdateUIDelegate(UpdateUI));
            else textBoxPostureCenter.Text = "";
        }

    }
}

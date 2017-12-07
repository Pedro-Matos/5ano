
namespace Paelife.KinectFramework.FaceRecognition
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Text;

    using Emgu.CV.UI;
    using Emgu.CV;
    using Emgu.CV.Structure;
    using Emgu.CV.CvEnum;

    using System.IO;
    using System.Xml;
    using System.Xml.Serialization;
    using System.Drawing;

    /// <summary>
    /// An implementation of IFaceRecognizer that uses the Eigen Faces and PCA (Principle 
    /// Component Analysis) to reconize the faces. It only uses Kinect color frame data.
    /// </summary>
    public class EigenFaceRecognizer : IFaceRecognizer
    {
        private List<Image<Gray, byte>> trainingImagesList;
        private List<string> labelsList;
        private int eigenThreshold;
        private bool isTrained;

        private HaarCascade FaceCascade;
        private XmlDocument xmlFaceData;
        private string trainingFolder;

        private KinectManager kinectManager;

        private Image<Gray, byte> currentDetectedFace;
        private int nCurrentlyDetectedFaces = 0;
        private string currentDetectedUsername = null;

        /// <summary>
        /// It is initialized with a value of 3200, which means that the recognizer is more inclined 
        /// to match a face between the current unknown face and the faces in the database, rather than be 
        /// privileging the accuracy. [see details bellow]
        /// 
        /// If you want a good commitment between accuracy and successful recognition, a value around 
        /// 3000 is recommended. Bellow 3000, means that the Face Recognizer will be more accurate, but
        /// there will be more times when a user can't be recognized. If a value over 3000 is provided,
        /// it will recognize users more often, but with less accuracy.
        /// </summary>
        public double TresholdLevel
        {
            get { return (double)eigenThreshold; }
            set { eigenThreshold = (int)value; }
        }

        /// <summary>
        /// Returns true if training data has been located and loaded successfuly. If no training data
        /// was found or an error has occured while loading it, the value false is returned.
        /// </summary>
        public bool IsTrained
        {
            get { return isTrained; }
        }

        
        /// <summary>
        /// Default constructor that saves and looks for training data in the current application directory,
        /// inside "TrainedFaces" folder. It receives a KinectManager as argument, so it can retrieve
        /// the current frame of KinectSensor when needed.
        /// </summary>
        public EigenFaceRecognizer(KinectManager kinectManager)
        {
            this.kinectManager = kinectManager;
            trainingFolder = System.AppDomain.CurrentDomain.BaseDirectory + "\\TrainedFaces";
            init();
        }

        /// <summary>
        /// Constructor that saves and looks for training data in the <paramref name="trainingFolder"/> 
        /// directory, inside "TrainedFaces" folder. It receives a KinectManager as argument, so it can
        /// retrieve the current frame of KinectSensor when needed.
        /// </summary>
        public EigenFaceRecognizer(KinectManager kinectManager, string trainingFolder)
        {
            this.kinectManager = kinectManager;
            this.trainingFolder = trainingFolder + "\\TrainedFaces";
            init();
        }

        private void init()
        {
            xmlFaceData = new XmlDocument();
            FaceCascade = new HaarCascade(System.AppDomain.CurrentDomain.BaseDirectory + "/haarcascade_frontalface_alt2.xml");
            trainingImagesList = new List<Image<Gray, byte>>();
            labelsList = new List<string>();
            TresholdLevel = 3200;

            isTrained = LoadTrainingData();
        }

        //! @copydoc IFaceRecognizer::NumberOfImages(string)
        public int NumberOfImages(string username)
        {
            int n = 0;
            foreach (string user in labelsList)
                if (username.Equals(user))
                    n++;
            return n;
        }

        /// <summary>
        /// It access the current frame of Kinect, detects and saves the face in it. To keep the
        /// Face Recognition as simple as it can be, only one person can be in the captured 
        /// Kinect area. [see details bellow]
        /// 
        /// Therefore, this method can throw a FaceRecognitionException. Check the 
        /// FaceRecognitionException.Message for details on the exception. It can be one of the
        /// following:
        /// - Could not read the current frame from Kinect.
        /// - Unable to detect a face.
        /// - More than one face was detected.
        /// - IO exceptions related to saving the image in the disk.
        /// </summary>
        public void DetectAndSaveFace(string username)
        {
            Bitmap bmap =
                Paelife.KinectFramework.ImageProcessingAux.CreateBitmapFromPixelData(
                kinectManager.KinectColorData,
                kinectManager.KinectSensor.ColorStream.FrameWidth,
                kinectManager.KinectSensor.ColorStream.FrameHeight);

            if (bmap == null)
                throw new FaceRecognitionException("Could not read the current frame from Kinect.");

            Image<Bgr, Byte> imageFrame = new Image<Bgr, byte>(bmap);

            detectFacesInFrame(imageFrame);

            //apenas processamos quando ha exactamente 1 face
            if (nCurrentlyDetectedFaces == 1 &&
                currentDetectedFace != null)
            {
                Bitmap faceBitmap = currentDetectedFace.ToBitmap();
                saveTrainingData(faceBitmap, username);
                trainingImagesList.Add(currentDetectedFace);
                labelsList.Add(username);
                isTrained = true;
                return;
            }
            else
                if (nCurrentlyDetectedFaces == 0)
                    throw new FaceRecognitionException("Unable to detect a face.");
                else
                    throw new FaceRecognitionException(
                        "More than one face was detected. Please use when only one user is in the captured area.");
        }


        /// <summary>
        /// It access the current frame of Kinect, detects and recognizes the face in it. To keep the
        /// Face Recognition as simple as it can be, only one person can be in the captured 
        /// Kinect area. [see details bellow]
        /// 
        /// Therefore, this method can throw a FaceRecognitionException. Check the 
        /// FaceRecognitionException.Message for details on the exception. It can be one of the
        /// following:
        /// - Could not read the current frame from Kinect.
        /// - Unable to detect a face.
        /// - More than one face was detected.
        /// - FaceRecognizer isn't trained.
        /// </summary>
        public string RecognizeFace()
        {
            Bitmap bmap =
                   Paelife.KinectFramework.ImageProcessingAux.CreateBitmapFromPixelData(
                   kinectManager.KinectColorData,
                   kinectManager.KinectSensor.ColorStream.FrameWidth,
                   kinectManager.KinectSensor.ColorStream.FrameHeight);

            if (bmap == null)
                throw new FaceRecognitionException("Could not read the current frame from Kinect.");

            Image<Bgr, byte> currentFrame = new Image<Bgr, byte>(bmap);

            if (isTrained)
            {
                detectFacesInFrame(currentFrame);

                if (nCurrentlyDetectedFaces == 1 &&
                    currentDetectedFace != null)
                {
                    MCvTermCriteria termCrit = new MCvTermCriteria(trainingImagesList.Count, 0.001);
                    EigenObjectRecognizer recognizer = new EigenObjectRecognizer(
                           trainingImagesList.ToArray(),
                           labelsList.ToArray(),
                           eigenThreshold,
                           ref termCrit);

                    //var distances = recognizer.GetEigenDistances(currentDetectedFace);
                    currentDetectedUsername = recognizer.Recognize(currentDetectedFace);
                    return currentDetectedUsername;
                }
                else
                    if (nCurrentlyDetectedFaces == 0)
                        throw new FaceRecognitionException("Unable to detect a face.");
                    else
                        throw new FaceRecognitionException(
                            "More than one face was detected. Please use when only one user is in the captured area.");
            }
            throw new FaceRecognitionException("FaceRecognizer isn't trained.");
        }

        /*! @copydoc IFaceRecognizer::LastRecognitionWasSuccessful()
         * It can throw IO exceptions, related to saving the image in the disk.
         */
        public void LastRecognitionWasSuccessful()
        {
            Bitmap faceBitmap = currentDetectedFace.ToBitmap();
            saveTrainingData(faceBitmap, currentDetectedUsername);
            trainingImagesList.Add(currentDetectedFace);
            labelsList.Add(currentDetectedUsername);
            isTrained = true;
            return;
        }

        /*! @copydoc IFaceRecognizer::LastRecognitionWasWrong(string)
        * It can throw IO exceptions, related to saving the image in the disk.
         */
        public void LastRecognitionWasWrong(string username)
        {
            Bitmap faceBitmap = currentDetectedFace.ToBitmap();
            saveTrainingData(faceBitmap, username);
            trainingImagesList.Add(currentDetectedFace);
            labelsList.Add(username);
            isTrained = true;
            return;
        }


        private void detectFacesInFrame(Image<Bgr, byte> currentFrame)
        {
            currentDetectedFace = null;
            Image<Gray, byte> grayFrame = currentFrame.Convert<Gray, Byte>();

            MCvAvgComp[][] facesDetected = grayFrame.DetectHaarCascade(
                FaceCascade, 1.2, 10, 
                Emgu.CV.CvEnum.HAAR_DETECTION_TYPE.DO_CANNY_PRUNING, 
                new System.Drawing.Size(20, 20));

            nCurrentlyDetectedFaces = facesDetected[0].Length;

            //apenas processamos quando ha exactamente 1 face
            if (nCurrentlyDetectedFaces == 1)
            {
                MCvAvgComp faceFound = facesDetected[0][0];
                currentDetectedFace = currentFrame.Copy(faceFound.rect).Convert<Gray, byte>().Resize(100, 100, Emgu.CV.CvEnum.INTER.CV_INTER_CUBIC);
                currentDetectedFace._EqualizeHist();
            }
        }



        private bool LoadTrainingData()
        {
            if (File.Exists(trainingFolder + "\\TrainedLabels.xml"))
            {
                try
                {
                    labelsList.Clear();
                    trainingImagesList.Clear();
                    FileStream filestream = File.OpenRead(trainingFolder + "\\TrainedLabels.xml");
                    long filelength = filestream.Length;
                    byte[] xmlBytes = new byte[filelength];
                    filestream.Read(xmlBytes, 0, (int)filelength);
                    filestream.Close();

                    MemoryStream xmlStream = new MemoryStream(xmlBytes);

                    using (XmlReader xmlreader = XmlTextReader.Create(xmlStream))
                    {
                        while (xmlreader.Read())
                        {
                            if (xmlreader.IsStartElement())
                            {
                                switch (xmlreader.Name)
                                {
                                    case "NAME":
                                        if (xmlreader.Read())
                                        {
                                            labelsList.Add(xmlreader.Value.Trim());
                                        }
                                        break;
                                    case "FILE":
                                        if (xmlreader.Read())
                                        {
                                            trainingImagesList.Add(new Image<Gray, byte>(trainingFolder + "\\" + xmlreader.Value.Trim()));
                                        }
                                        break;
                                }
                            }
                        }
                    }

                    if (trainingImagesList.ToArray().Length != 0)
                    {
                        return true;
                    }
                    else return false;
                }
                catch (Exception)
                {
                    return false;
                }
            }
            else return false;
        }




        private void saveTrainingData(Image faceImage, string username)
        {
            string faceFileName = username + " " + DateTime.Now.Ticks / TimeSpan.TicksPerMillisecond + ".jpg";

            if (!Directory.Exists(System.AppDomain.CurrentDomain.BaseDirectory + "/TrainedFaces/"))
                Directory.CreateDirectory(System.AppDomain.CurrentDomain.BaseDirectory + "/TrainedFaces/");

            faceImage.Save(System.AppDomain.CurrentDomain.BaseDirectory + "/TrainedFaces/" + faceFileName, System.Drawing.Imaging.ImageFormat.Jpeg);

            if (File.Exists(System.AppDomain.CurrentDomain.BaseDirectory + "/TrainedFaces/TrainedLabels.xml"))
            {
                bool loading = true;
                while (loading)
                {
                    try
                    {
                        xmlFaceData.Load(System.AppDomain.CurrentDomain.BaseDirectory + "/TrainedFaces/TrainedLabels.xml");
                        loading = false;
                    }
                    catch
                    {
                        xmlFaceData = null;
                        xmlFaceData = new XmlDocument();
                        System.Threading.Thread.Sleep(10);
                    }
                }

                XmlElement root = xmlFaceData.DocumentElement;

                XmlElement faceD = xmlFaceData.CreateElement("FACE");
                XmlElement nameD = xmlFaceData.CreateElement("NAME");
                XmlElement fileD = xmlFaceData.CreateElement("FILE");

                nameD.InnerText = username;
                fileD.InnerText = faceFileName;

                faceD.AppendChild(nameD);
                faceD.AppendChild(fileD);

                root.AppendChild(faceD);

                xmlFaceData.Save(System.AppDomain.CurrentDomain.BaseDirectory + "/TrainedFaces/TrainedLabels.xml");
            }
            else
            {
                FileStream fsFace = File.OpenWrite(System.AppDomain.CurrentDomain.BaseDirectory + "/TrainedFaces/TrainedLabels.xml");
                using (XmlWriter writer = XmlWriter.Create(fsFace))
                {
                    writer.WriteStartDocument();
                    writer.WriteStartElement("FacesForTraining");

                    writer.WriteStartElement("FACE");
                    writer.WriteElementString("NAME", username);
                    writer.WriteElementString("FILE", faceFileName);
                    writer.WriteEndElement();

                    writer.WriteEndElement();
                    writer.WriteEndDocument();
                }
                fsFace.Close();
            }
        }
    }
}

using System;
using System.Drawing;

namespace Paelife.KinectFramework.FaceRecognition
{
    /// <summary>
    /// This interface defines the properties and methods that a Face Recognizer should have.
    /// </summary>
    public interface IFaceRecognizer
    {
        /// <summary>
        /// Indicates whether the Face Recognizer is trained or not. It should return true if
        /// there are already some images in the training set, and false if there aren't.
        /// </summary>
        bool IsTrained { get; }

        /// <summary>
        /// The minimum treshold level that is required for a user to be identified. If this
        /// requirement isn't met, a empty string is returned when calling <see cref="RecognizeFace"/> method.
        /// </summary>
        double TresholdLevel { set; get; }

        /// <summary>
        /// Returns the number of images in the training set, of a particular user.
        /// </summary>
        int NumberOfImages(string username);
        
        /// <summary>
        /// Detects the faces in the current Kinect frame and saves them in the training 
        /// image set. The <paramref name="username"/> indicates the username of the person
        /// in front of Kinect.
        /// </summary>
        void DetectAndSaveFace(string username);

        /// <summary>
        /// Detects and recognizes a face in the current Kinect frame. If the <see cref="TresholdLevel"/>
        /// isn't met, an empty string is returned.
        /// </summary>
        string RecognizeFace();

        /// <summary>
        /// Indicates that the last recognition was successful. This way, the Face recognizer
        /// can integrate the previously recognized image in the training image set, increasing the
        /// accuracy of the Face Recognizer.
        /// </summary>
        void LastRecognitionWasSuccessful();

        /// <summary>
        /// Indicates that the last recognition was NOT successful. The Face recognizer
        /// will then save the previously recognized image as the <paramref name="username"/> received in the argument, 
        /// increasing the accuracy of the Face Recognizer.
        /// </summary>
        void LastRecognitionWasWrong(string username);
    }
}

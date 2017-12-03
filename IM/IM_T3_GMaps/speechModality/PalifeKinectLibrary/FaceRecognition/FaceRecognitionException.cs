
namespace Paelife.KinectFramework.FaceRecognition
{
    using System;
    /// <summary>
    /// General class that represents an exception that ocurred while proccessing an image for face recognition.
    /// The details of the exception, can be checked in <see cref="FaceRecognitionException.Message"/>.
    /// </summary> 
    [Serializable()]
    public class FaceRecognitionException : System.Exception
    {
        public FaceRecognitionException() : base() { }
        public FaceRecognitionException(string message) : base(message) { }
        public FaceRecognitionException(string message, System.Exception inner) : base(message, inner) { }

        protected FaceRecognitionException(System.Runtime.Serialization.SerializationInfo info,
            System.Runtime.Serialization.StreamingContext context) { }
    }
}

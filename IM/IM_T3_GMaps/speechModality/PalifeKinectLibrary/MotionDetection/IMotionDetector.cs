using System;
using System.Drawing;

namespace Paelife.KinectFramework.MotionDetection
{
	/// <summary>
	/// Interface to implement a Motion Detector. It has the ability to consider
    /// only some pixels. This way, player pixels can be excluded from the motion
    /// detection algorithm.
    /// When motion is detected, the MotionDetected event is fired.
	/// </summary>
	public interface IMotionDetector
	{
		/// <summary>
		/// Amount of changed pixels, in percents.
		/// </summary>
		double MotionLevel{ get; }

        /// <summary>
        /// The amount of pixels from which we consider to have detected motion.
        /// </summary>
        double AlarmLevel { set; get; }

        /// <summary>
        /// Since image processing requires a lot of CPU time, the motion can be
        /// limited to a number of frames per second.
        /// </summary>
        int FramesPerSecond { set; get; }

        /// <summary>
        /// The event that is fired when the MotionLevel is over the AlarmLevel.
        /// Subscribe to this event to be notified when motion has been detected.
        /// </summary>
        event EventHandler MotionDetected;

        /// <summary>
        /// Process a new frame, calculating the amount of pixels that changed between
        /// the current and the previous frame.
        /// <param name="image">The color image that you want to perform motion detection.</param>
        /// <param name="player">An image where the player pixels are white, while the 
        /// rest of the image is black. The player pixels should be excluded from motion detection algorithm.</param>
        /// </summary>
        void ProcessFrame(ref Bitmap image, ref Bitmap player);

		/// <summary>
		/// Reset detector to initial state.
		/// </summary>
        void Reset();
	}
}

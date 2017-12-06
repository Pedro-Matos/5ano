using System;
using System.Collections.Generic;
using Microsoft.Kinect;
using System.Threading;

namespace Paelife.KinectFramework.Gestures
{
    /// <summary>
    /// An abstract class that already defines some methods useful to
    /// a gesture detector, as well as the methods the subclasses should implement.
    /// </summary>
    public abstract class GestureDetector
    {
        /// <summary>
        /// The minimal time between gestures that should have passed before a second
        /// OnGestureDetected event is raised. This way, false positives are avoided.
        /// This time is in seconds.
        /// </summary>
        public int MinimalPeriodBetweenGestures { get; set; }

        //! @cond
        readonly List<Entry> entries = new List<Entry>();

        protected DateTime lastGestureDate = DateTime.Now;

        readonly int windowSize;

        protected List<Entry> Entries
        {
            get { return entries; }
        }
        //! @endcond

        /// <summary>
        /// The joint that is tracked in the current gesture detector.
        /// </summary>
        public JointType TrackedJoint { get; set; }

        /// <summary>
        /// The event that is raised when a gesture is detected.
        /// </summary>
        public event Action<string> OnGestureDetected;

        /// <summary>
        /// The default constructor of the GestureDetector class.
        /// <param name="trackedJoint">The joint that should be tracked in this gesture detector.</param>
        /// <param name="windowSize">The number of recorded positions that are considered when detecting gestures.</param>
        /// </summary>
        protected GestureDetector(JointType trackedJoint, int windowSize = 20)
        {
            this.windowSize = windowSize;
            this.TrackedJoint = trackedJoint;
            MinimalPeriodBetweenGestures = 0;
        }

        /// <summary>
        /// Gets the number of recorded positions that are considered when detecting gestures.
        /// </summary>
        public int WindowSize
        {
            get { return windowSize; }
        }

        /// <summary>
        /// The method that adds a joint point of a skeleton to the list of considered positions,
        /// so gesture detection can be performed. It should be called in every Kinect frame.
        /// </summary>
        public virtual void Add(SkeletonPoint position, KinectSensor sensor)
        {
            Entry newEntry = new Entry {
                Position = new Vector3(position.X, position.Y, position.Z),
                Time = DateTime.Now };
            Entries.Add(newEntry);

            // Remove too old positions
            if (Entries.Count > WindowSize)
            {
                Entry entryToRemove = Entries[0];                
                Entries.Remove(entryToRemove);
            }

            // Look for gestures
           // Thread.Sleep(50);
            LookForGesture();
        }

        /// <summary>
        /// The method that sublcasses should implement. If a gesture is detected, it should call
        /// the <see cref="GestureDetector.RaiseGestureDetected(string gesture)"/>.
        /// </summary>
        protected abstract void LookForGesture();

        /// <summary>
        /// Checks for <see cref="GestureDetector.MinimalPeriodBetweenGestures"/>
        /// before raising a <see cref="GestureDetector.OnGestureDetected"/>
        /// </summary>
        protected void RaiseGestureDetected(string gesture)
        {
            // Too close?
            if (DateTime.Now.Subtract(lastGestureDate).TotalMilliseconds > MinimalPeriodBetweenGestures)
            {
                if (OnGestureDetected != null)
                    OnGestureDetected(gesture);

                lastGestureDate = DateTime.Now;
            }

            Entries.Clear();
        }
    }
}

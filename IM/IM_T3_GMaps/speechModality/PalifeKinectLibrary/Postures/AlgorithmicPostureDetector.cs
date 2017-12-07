using System;
using System.Collections.Generic;
using Microsoft.Kinect;
using Paelife.KinectFramework.Gestures;

namespace Paelife.KinectFramework.Postures
{
    public class AlgorithmicPostureDetector : PostureDetector
    {

        public float Epsilon {get;set;}
        public float MaxRange { get; set; }

        public AlgorithmicPostureDetector() : base(10)
        {
            Epsilon = 0.1f;
            MaxRange = 0.1f;
        }

        public override async void TrackPostures(Skeleton skeleton)
        {
            if (skeleton.TrackingState != SkeletonTrackingState.Tracked)
                return;

            Vector3? headPosition = null;
            Vector3? leftHandPosition = null;
            Vector3? rightHandPosition = null;
            Vector3? leftElbowPosition = null;
            Vector3? rightElbowPosition = null;
            Vector3? leftHipPosition = null;
            Vector3? rightHipPosition = null;
            Vector3? rightFootPosition = null;
            Vector3? rightKneePosition = null;
            Vector3? leftFootPosition = null;
            Vector3? leftKneePosition = null;

            foreach (Joint joint in skeleton.Joints)
            {
                if (joint.TrackingState != JointTrackingState.Tracked)
                    continue;
                
                switch (joint.JointType)
                {
                    case JointType.Head:
                        headPosition = new Vector3(joint.Position.X, joint.Position.Y, joint.Position.Z);
                        break;
                    case JointType.HandLeft:
                        leftHandPosition = new Vector3(joint.Position.X, joint.Position.Y, joint.Position.Z);
                        break;
                    case JointType.HandRight:
                        rightHandPosition = new Vector3(joint.Position.X, joint.Position.Y, joint.Position.Z);
                        break;
                    case JointType.ElbowLeft:
                        leftElbowPosition = new Vector3(joint.Position.X, joint.Position.Y, joint.Position.Z);
                        break;
                    case JointType.ElbowRight:
                        rightElbowPosition = new Vector3(joint.Position.X, joint.Position.Y, joint.Position.Z);
                        break;
                    case JointType.HipLeft:
                        leftHipPosition = new Vector3(joint.Position.X, joint.Position.Y, joint.Position.Z);
                        break;
                    case JointType.HipRight:
                        rightHipPosition = new Vector3(joint.Position.X, joint.Position.Y, joint.Position.Z);
                        break;
                    case JointType.FootRight:
                        rightFootPosition = new Vector3(joint.Position.X, joint.Position.Y, joint.Position.Z);
                        break;
                    case JointType.FootLeft:
                        leftFootPosition = new Vector3(joint.Position.X, joint.Position.Y, joint.Position.Z);
                        break;
                    case JointType.KneeRight:
                        rightKneePosition = new Vector3(joint.Position.X, joint.Position.Y, joint.Position.Z);
                        break;
                    case JointType.KneeLeft:
                        leftKneePosition = new Vector3(joint.Position.X, joint.Position.Y, joint.Position.Z);
                        break;
                }
            }



            // Posture T
            if(CheckPostureT(leftHandPosition, leftElbowPosition, rightHandPosition, rightElbowPosition))
            {
                RaisePostureDetected("T");
                return;
            }


            // Posture U
            if (CheckPostureU(leftHandPosition, leftElbowPosition, rightHandPosition, rightElbowPosition))
            {
                RaisePostureDetected("U");
                return;
            }


            // Posture A
            if (CheckPostureA(leftHandPosition, leftElbowPosition, rightHandPosition, rightElbowPosition, leftHipPosition, rightHipPosition))
            {
                RaisePostureDetected("A");
                return;
            }

         
            // Posture V
            if (CheckPostureV(leftHandPosition, leftElbowPosition, rightHandPosition, rightElbowPosition, leftHipPosition, rightHipPosition))
            {
                RaisePostureDetected("V");
                return;
            }

            //Pé Direito
            if (CheckRightKick(rightFootPosition, rightKneePosition, rightHipPosition))
            {
                RaisePostureDetected("Right Kick");
                return;
            }

            if(CheckLeftKick(leftFootPosition, leftKneePosition, leftHipPosition))
            {
                RaisePostureDetected("Left Kick");
                return;
            }

            if(CheckHandsJoined(leftHandPosition, rightHandPosition))
            {
                RaisePostureDetected("Maos Juntas");
            }

            Reset();
        }

        bool CheckPostureT(Vector3? leftHand, Vector3? leftElbow, Vector3? rightHand, Vector3? rightElbow)
        {
            if (!leftHand.HasValue || !rightHand.HasValue || !leftElbow.HasValue || !rightElbow.HasValue)
                return false;

            if (Math.Abs(leftHand.Value.Y - leftElbow.Value.Y) < MaxRange)
                if (Math.Abs(rightHand.Value.Y - rightElbow.Value.Y) < MaxRange)
                    return true;

            return false;
        }

        bool CheckPostureU(Vector3? leftHand, Vector3? leftElbow, Vector3? rightHand, Vector3? rightElbow)
        {
            if (!leftHand.HasValue || !rightHand.HasValue || !leftElbow.HasValue || !rightElbow.HasValue)
                return false;

            if (Math.Abs(leftHand.Value.X - leftElbow.Value.X) < MaxRange)
                if (leftHand.Value.Y > leftElbow.Value.Y)
                    if (Math.Abs(rightHand.Value.X - rightElbow.Value.X) < MaxRange)
                        if (rightHand.Value.Y > rightElbow.Value.Y)
                            return true;

            return false;
        }

        bool CheckPostureA(Vector3? leftHand, Vector3? leftElbow, Vector3? rightHand, Vector3? rightElbow, Vector3? leftHip, Vector3? rightHip)
        {
            if (!leftHand.HasValue || !rightHand.HasValue || !leftElbow.HasValue || !rightElbow.HasValue || !leftHip.HasValue || !rightHip.HasValue)
                return false;

            if (leftHand.Value.X < leftElbow.Value.X)
                if(Math.Abs(leftHip.Value.X - leftHand.Value.X) > 0.35f) 
                     if (leftHand.Value.Y < leftElbow.Value.Y)
                         if (rightHand.Value.X > rightElbow.Value.X)
                            if (Math.Abs(rightHip.Value.X - rightHand.Value.X) > 0.35f)
                                if (rightHand.Value.Y < rightElbow.Value.Y)
                            return true;

            return false;
        }

        bool CheckPostureV(Vector3? leftHand, Vector3? leftElbow, Vector3? rightHand, Vector3? rightElbow, Vector3? leftHip, Vector3? rightHip)
        {
            if (!leftHand.HasValue || !rightHand.HasValue || !leftElbow.HasValue || !rightElbow.HasValue)
                return false;

            if (leftHand.Value.X < leftElbow.Value.X)
                if (Math.Abs(leftHip.Value.X - leftHand.Value.X) > 0.35f)
                    if (leftHand.Value.Y > leftElbow.Value.Y)
                        if (rightHand.Value.X > rightElbow.Value.X)
                            if (Math.Abs(rightHip.Value.X - rightHand.Value.X) > 0.35f)
                                if (rightHand.Value.Y > rightElbow.Value.Y)
                            return true;

            return false;
        }

        bool CheckRightKick(Vector3? rightFootPosition, Vector3? rightKneePosition, Vector3? rightHipPosition)
        {
            if (!rightFootPosition.HasValue || !rightKneePosition.HasValue || !rightHipPosition.HasValue)
                return false;

            if (rightFootPosition.Value.X > rightKneePosition.Value.X)
                if (rightKneePosition.Value.X > rightHipPosition.Value.X)
                    if (rightFootPosition.Value.Y < rightKneePosition.Value.Y)
                        if (rightKneePosition.Value.Y < rightHipPosition.Value.Y)
                            if (Math.Abs(rightFootPosition.Value.X - rightHipPosition.Value.X) > 0.45f)
                                return true;

            return false;
        }

        bool CheckLeftKick(Vector3? leftFootPosition, Vector3? leftKneePosition, Vector3? leftHipPosition)
        {
            if (!leftFootPosition.HasValue || !leftKneePosition.HasValue || !leftHipPosition.HasValue)
                return false;

            if (leftFootPosition.Value.X < leftKneePosition.Value.X)
                if (leftKneePosition.Value.X < leftHipPosition.Value.X)
                    if (leftFootPosition.Value.Y < leftKneePosition.Value.Y)
                        if (leftKneePosition.Value.Y < leftHipPosition.Value.Y)
                            if (Math.Abs(leftFootPosition.Value.X - leftHipPosition.Value.X) > 0.45f)
                                return true;

            return false;
        }

        bool CheckHandsJoined(Vector3? leftHandPosition, Vector3? rightHandPosition)
        {
            if (!leftHandPosition.HasValue || !rightHandPosition.HasValue)
                return false;

            float distance = (leftHandPosition.Value - rightHandPosition.Value).Length;

            if (distance > Epsilon)
                return false;

            return true;
        }

    }
}

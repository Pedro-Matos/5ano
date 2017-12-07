namespace Paelife.KinectFramework
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Text;
    using System.Drawing;
    using System.Drawing.Imaging;
    using Microsoft.Kinect;
    using System.Runtime.InteropServices;

    /// <summary>
    /// This static class provides various image related functions that helps
    /// converting between the native format of Kinect color and depth images
    /// to the traditional Bitmap images. It also allows to process player
    /// pixels, usefull for the ambient motion tracking.
    /// </summary>
    public static class ImageProcessingAux
    {
        private const int RedIndex = 2;
        private const int GreenIndex = 1;
        private const int BlueIndex = 0;

        /// <summary>
        /// Creates a bitmap based on the pixel data received.
        /// </summary>
        public static Bitmap CreateBitmapFromPixelData(byte[] pixelData, int width, int height)
        {
            if (pixelData == null)
                return null;

            Bitmap bmap = new Bitmap(width, height,
                         System.Drawing.Imaging.PixelFormat.Format32bppRgb);

            BitmapData bmapdata = bmap.LockBits(
                     new System.Drawing.Rectangle(0, 0, width, height),
                     ImageLockMode.WriteOnly,
                     bmap.PixelFormat);

            IntPtr ptr = bmapdata.Scan0;
            Marshal.Copy(pixelData, 0, ptr, pixelData.Length);

            bmap.UnlockBits(bmapdata);
            return bmap;
        }

        /// <summary>
        /// Convert the native Kinect's ColorImageFrame to a Bitmap.
        /// </summary>
        public static Bitmap ConvertColorImageToBitmap(ColorImageFrame colorImageFrame)
        {
            if (colorImageFrame == null)
                return null;

            byte[] pixeldata = new byte[colorImageFrame.PixelDataLength];
            colorImageFrame.CopyPixelDataTo(pixeldata);

            return CreateBitmapFromPixelData(pixeldata, colorImageFrame.Width, colorImageFrame.Height);
        }


        /// <summary>
        /// Given the color pixel data end the depth pixel data obtained through a
        /// KinectSensor, creates a Bitmap where player pixels are in white and the
        /// rest of the pixels are black. It is usefull for tracking motion on the
        /// environment.
        /// </summary>
        public static Bitmap ProccessPlayerPixels(
            byte[] colorPixelData, 
            short[] depthPixelData,
            ColorImageFormat colorImageFormat)
        {
            if (colorPixelData == null ||
                depthPixelData == null ||
                colorImageFormat == null)
                return null;

            for (int i16 = 0, i32 = 0; i32 < colorPixelData.Length; i16++, i32 += 4)
            {
                int player = depthPixelData[i16] & DepthImageFrame.PlayerIndexBitmask;
                int realDepth = depthPixelData[i16] >> DepthImageFrame.PlayerIndexBitmaskWidth;

                if (player != 0)
                {
                    colorPixelData[i32 + RedIndex] = 255;
                    colorPixelData[i32 + GreenIndex] = 255;
                    colorPixelData[i32 + BlueIndex] = 255;
                }
                else
                {
                    colorPixelData[i32 + RedIndex] = 0;
                    colorPixelData[i32 + GreenIndex] = 0;
                    colorPixelData[i32 + BlueIndex] = 0;
                }
            }

            int width = -1;
            int height = -1;

            switch (colorImageFormat)
            {
                case ColorImageFormat.RawYuvResolution640x480Fps15:
                case ColorImageFormat.RgbResolution640x480Fps30:
                case ColorImageFormat.YuvResolution640x480Fps15:
                    width = 640;
                    height = 480;
                    break;
                case ColorImageFormat.RgbResolution1280x960Fps12:
                    width = 1280;
                    height = 960;
                    break;
            }

            return CreateBitmapFromPixelData(colorPixelData, width, height);
        }
    }
}

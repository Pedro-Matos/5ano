using GoogleMapsAPI.NET.API.Client;
using OpenQA.Selenium;
using OpenQA.Selenium.Chrome;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace IM_T1_GoogleMaps
{
    class Program
    {
        static void Main(string[] args)
        {

            GoogleMapsWorker mapsWorker = new GoogleMapsWorker();
            mapsWorker.searchDirections("Aveiro", "Porto");

        }
    }
}

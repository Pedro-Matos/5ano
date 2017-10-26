using GoogleMapsAPI.NET.API.Client;
using OpenQA.Selenium;
using OpenQA.Selenium.Chrome;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ConsoleApp2
{
    class Program
    {
        static void Main(string[] args)
        {

            String API_KEY = "AIzaSyAEGr6lbb8xTXsVu4VEsA-9N56Dh2VJ1NI";
            IWebDriver driver = new ChromeDriver();
            // Get client
            var client = new MapsAPIClient(API_KEY);

            // Geocoding an address
            var geocodeResult = client.Geocoding.Geocode("1600 Amphitheatre Parkway, Mountain View, CA");

            double latitude = 0.0;
            double longitude = 0.0;
            foreach (var money in geocodeResult.Results)
            {
                latitude = money.Geometry.Location.Latitude;
                longitude = money.Geometry.Location.Longitude;
            }

            String lat = latitude.ToString().Replace(",", ".");
            String lon = longitude.ToString().Replace(",", ".");

            driver.Navigate().GoToUrl("https://www.google.com/maps/?q=-15.623037,18.388672");

        }
    }
}

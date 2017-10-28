using GoogleMapsAPI.NET.API.Client;
using OpenQA.Selenium;
using OpenQA.Selenium.Chrome;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Threading;

namespace IM_T1_GoogleMaps
{
    class GoogleMapsWorker
    {

        String API_KEY = "AIzaSyAEGr6lbb8xTXsVu4VEsA-9N56Dh2VJ1NI";
        String baseMapsUrl = "https://www.google.com/maps/";

        IWebDriver driver = new ChromeDriver();

        MapsAPIClient client;

        public GoogleMapsWorker()
        {
            this.client = new MapsAPIClient(API_KEY);
        }

        public void sampleUrl()
        {
            goToUrl(baseMapsUrl);
        }

        public void goToPlace(String place)
        {

            String url = baseMapsUrl + "place/" + place;
            goToUrl(url);
        }

        public void searchServiceAtLocation(String service, String location)
        {
            String url = baseMapsUrl + "search/" + service + "+" + location;
            goToUrl(url);
        }

        public void goToUrl(String url)
        {
            driver.Manage().Window.Maximize();
            driver.Navigate().GoToUrl(url);

            Thread.Sleep(3000);
            setTransportationMode("bicicleta");
        }

        public void zoomIn()
        {
            try
            {
                IWebElement elemBtn = driver.FindElement(By.Id("widget-zoom-in"));
                elemBtn.Click();
            }
            catch (InvalidOperationException e)
            {
                return;
            }
            catch (NoSuchElementException e)
            {
                return;
            }

        }

        public void zoomOut()
        {
            try
            {
                IWebElement elemBtn = driver.FindElement(By.Id("widget-zoom-out"));
                elemBtn.Click();
            }
            catch (InvalidOperationException e)
            {
                return;
            }
            catch (NoSuchElementException e)
            {
                return;
            }
        }

        public void searchDirections(String start, String end)
        {
            String url = baseMapsUrl + "dir/" + start + "/" + end;
            goToUrl(url);
        }

        public void openMenu()
        {
            try
            {
                IWebElement elembutton = driver.FindElement(By.ClassName("searchbox-hamburger"));
                elembutton.Click();
            }
            catch (InvalidOperationException e)
            {
                return;
            }
            catch (NoSuchElementException e)
            {
                return;
            }
        }


        public void closeMenu()
        {
            try
            {
                IWebElement elembutton = driver.FindElement(By.ClassName("maps-sprite-settings-chevron-left"));
                elembutton.Click();
            }
            catch (InvalidOperationException e)
            {
                return;
            }
            catch (NoSuchElementException e)
            {
                return;
            }
        }

        public void openDirections()
        {
            try
            {
                IWebElement elembutton = driver.FindElement(By.ClassName("searchbox-directions"));
                elembutton.Click();
            }
            catch (InvalidOperationException e)
            {
                return;
            }
            catch (NoSuchElementException e)
            {
                return;
            }

        }

        public void closeDirections()
        {
            try
            {
                IWebElement elembutton = driver.FindElement(By.ClassName("close-directions"));
                elembutton.Click();
            }
            catch (InvalidOperationException e)
            {
                return;
            }
            catch (NoSuchElementException e)
            {
                return;
            }

        }

        public void closeSearchBox()
        {
            try
            {
                IWebElement elembutton = driver.FindElement(By.ClassName("gsst_a"));
                elembutton.Click();
            }
            catch (InvalidOperationException e)
            {
                return;
            }
            catch (NoSuchElementException e)
            {
                return;
            }

        }

        public void togglePanel()
        {
            try
            {
                IWebElement elembutton = driver.FindElement(By.ClassName("widget-pane-toggle-button"));
                elembutton.Click();
            }
            catch (InvalidOperationException e)
            {
                return;
            }
            catch (NoSuchElementException e)
            {
                return;
            }

        }

        public void toggleTraffic()
        {
            

            try
            {
                openMenu();
                Thread.Sleep(1000);

                IWebElement elem = driver.FindElement(By.XPath("//*[@id=\"settings\"]/div/div[2]/div/ul[1]/li[3]/button"));
                elem.Click();
            }
            catch (InvalidOperationException e)
            {
                return;
            }
            catch (NoSuchElementException e)
            {
                return;
            }
        }

        public void toggleSatellite()
        {
            try
            {
                openMenu();
                Thread.Sleep(1000);

                IWebElement elem = driver.FindElement(By.XPath("//*[@id=\"settings\"]/div/div[2]/div/ul[1]/li[2]/div/button[1]"));
                elem.Click();
            }
            catch (InvalidOperationException e)
            {
                return;
            }
            catch (NoSuchElementException e)
            {
                return;
            }
        }

        public void setTransportationMode(String vehicle)
        {
            switch (vehicle)
            {
                case "carro":
                    IWebElement carMode = driver.FindElement(By.XPath("//*[@id=\"omnibox-directions\"]/div/div[2]/div/div/div[1]/div[2]/button"));
                    carMode.Click();

                    break;
                case "pé":
                    IWebElement footMode = driver.FindElement(By.XPath("//*[@id=\"omnibox-directions\"]/div/div[2]/div/div/div[1]/div[4]/button"));
                    footMode.Click();

                    break;
                case "bicicleta":
                    IWebElement bikeMode = driver.FindElement(By.XPath("//*[@id=\"omnibox-directions\"]/div/div[2]/div/div/div[2]/div/div[1]/button"));
                    bikeMode.Click();

                    break;
                case "avião":
                    IWebElement planeMode = driver.FindElement(By.XPath("//*[@id=\"omnibox-directions\"]/div/div[2]/div/div/div[2]/div/div[2]/button"));
                    planeMode.Click();

                    break;
                case "transportes":
                    IWebElement transporMode = driver.FindElement(By.XPath("//*[@id=\"omnibox-directions\"]/div/div[2]/div/div/div[1]/div[3]/button"));
                    transporMode.Click();

                    break;
            }
        }

    }
}

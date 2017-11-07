using OpenQA.Selenium;
using OpenQA.Selenium.Chrome;
using System;
using System.Threading;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using mmisharp;

namespace AppGui
{
    class GoogleMapsWorker
    {

        String API_KEY = "AIzaSyAEGr6lbb8xTXsVu4VEsA-9N56Dh2VJ1NI";
        String baseMapsUrl = "https://www.google.com/maps/";

        private MmiCommunication mmic;
        private LifeCycleEvents lce;

        IWebDriver driver = new ChromeDriver();


        public GoogleMapsWorker()
        {
            lce = new LifeCycleEvents("ASR", "IM", "speech-1", "acoustic", "command");
            mmic = mmic = new MmiCommunication("192.168.1.86", 8000, "User1", "ASR");

            mmic.Send(lce.NewContextRequest());

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
        }

        public void goToMyLocation()
        {
            try
            {
                IWebElement elemBtn = driver.FindElement(By.ClassName("widget-mylocation-button-icon-common"));
                elemBtn.Click();
            }
            catch (InvalidOperationException e)
            {
                Console.WriteLine("1Error");
                return;
            }
            catch (NoSuchElementException e)
            {
                Console.WriteLine("2Error");
                return;
            }
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

            Thread.Sleep(2000);
            String tempo = getTempoPercurso();
            String distancia = getDistanciaPercurso();
            makeDistanceTimeSpeech(tempo, distancia);
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


        public void openMyFavoritePlaces()
        {
            try
            {
                openMenu();
                Thread.Sleep(2000);

                IWebElement elem = driver.FindElement(By.XPath("//*[@id=\"settings\"]/div/div[2]/div/ul[2]/li[3]/button"));
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


        public void toggleTerreno()
        {
            try
            {
                openMenu();
                Thread.Sleep(1000);

                IWebElement elem = driver.FindElement(By.XPath("//*[@id=\"settings\"]/div/div[2]/div/ul[1]/li[6]/button"));
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

                    Thread.Sleep(2000);
                    makeDistanceTimeSpeech(getTempoPercurso(), getDistanciaPercurso());

                    break;
                case "a pé":
                    IWebElement footMode = driver.FindElement(By.XPath("//*[@id=\"omnibox-directions\"]/div/div[2]/div/div/div[1]/div[4]/button"));
                    footMode.Click();

                    Thread.Sleep(2000);
                    makeDistanceTimeSpeech(getTempoPercurso(), getDistanciaPercurso());

                    break;
                case "bicicleta":
                    IWebElement bikeMode = driver.FindElement(By.XPath("//*[@id=\"omnibox-directions\"]/div/div[2]/div/div/div[2]/div/div[1]/button"));
                    bikeMode.Click();

                    Thread.Sleep(2000);
                    makeDistanceTimeSpeech(getTempoPercurso(), getDistanciaPercurso());

                    break;
                case "avião":
                    IWebElement planeMode = driver.FindElement(By.XPath("//*[@id=\"omnibox-directions\"]/div/div[2]/div/div/div[2]/div/div[2]/button"));
                    planeMode.Click();

                    Thread.Sleep(2000);
                    makeDistanceTimeSpeech(getTempoPercurso(), getDistanciaPercurso());


                    break;
                case "publicos":
                    IWebElement transporMode = driver.FindElement(By.XPath("//*[@id=\"omnibox-directions\"]/div/div[2]/div/div/div[1]/div[3]/button"));
                    transporMode.Click();

                    Thread.Sleep(2000);
                    makeDistanceTimeSpeech(getTempoPercurso(), getDistanciaPercurso()); ;

                    break;
            }
        }

        private String getTempoPercurso()
        {
            try
            {
                IWebElement tempo = driver.FindElement(By.XPath("//*[@id=\"pane\"]/div/div[2]/div/div/div[5]/div[1]/div[2]/div[1]/div[1]/div[1]/span[1]"));
                return tempo.Text;
            }
            catch (InvalidOperationException e)
            {
                return "ERROR";
            }
            catch (NoSuchElementException e)
            {
                try
                {
                    IWebElement tempoTry = driver.FindElement(By.XPath("//*[@id=\"pane\"]/div/div[2]/div/div/div[5]/div[1]/div[2]/div[2]/div/div"));
                    return tempoTry.Text;
                }
                catch (NoSuchElementException ex)
                {
                    try
                    {
                        IWebElement tempoTry2 = driver.FindElement(By.XPath("//*[@id=\"pane\"]/div/div[2]/div/div/div[5]/div[1]/div[2]/div[3]/div[1]/div[1]"));
                        return tempoTry2.Text;
                    }
                    catch (NoSuchElementException ex1)
                    {
                        return "ERROR";
                    }
                }
            }

        }

        private String getDistanciaPercurso()
        {
            try
            {
                IWebElement distancia = driver.FindElement(By.XPath("//*[@id=\"pane\"]/div/div[2]/div/div/div[5]/div[1]/div[2]/div[1]/div[1]/div[2]/div"));
                return distancia.Text;
            }
            catch (InvalidOperationException e)
            {
                return "ERROR";
            }
            catch (NoSuchElementException e)
            {
                try
                {
                    IWebElement tempoTry2 = driver.FindElement(By.XPath("//*[@id=\"pane\"]/div/div[2]/div/div/div[5]/div[1]/div[2]/div[3]/div[1]/div[2]"));
                    return tempoTry2.Text;
                }
                catch (NoSuchElementException ex1)
                {
                    return "ERROR";
                }
                return "ERROR";
            }

        }

        private void makeDistanceTimeSpeech(String tempo, String distancia)
        {
            if(distancia.Equals("ERROR"))
            {
                if (tempo.Contains("min") && tempo.Contains("h"))
                {
                    var exNot = lce.ExtensionNotification("", "", 0.0f, "O percurso demora " + tempo);
                    mmic.Send(exNot);
                }
                else
                {
                    if (tempo.Contains("h"))
                    {
                        var exNot = lce.ExtensionNotification("", "", 0.0f, "O percurso demora " + tempo + "oras");
                        mmic.Send(exNot);
                    }
                    else
                    {
                        var exNot = lce.ExtensionNotification("", "", 0.0f, "O percurso demora " + tempo + "utos");
                        mmic.Send(exNot);
                    }
                }
            } else
            {
                if (tempo.Contains("min") && tempo.Contains("h"))
                {
                    var exNot = lce.ExtensionNotification("", "", 0.0f, "A distância a percorrer é de " + distancia + " e demora " + tempo);
                    mmic.Send(exNot);
                }
                else
                {
                    if (tempo.Contains("h"))
                    {
                        var exNot = lce.ExtensionNotification("", "", 0.0f, "A distância a percorrer é de " + distancia + " e demora " + tempo + "oras");
                        mmic.Send(exNot);
                    }
                    else
                    {
                        var exNot = lce.ExtensionNotification("", "", 0.0f, "A distância a percorrer é de " + distancia + " e demora " + tempo + "utos");
                        mmic.Send(exNot);
                    }
                }
            }
        }

    }
}

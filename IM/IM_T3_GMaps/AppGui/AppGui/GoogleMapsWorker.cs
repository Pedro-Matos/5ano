﻿using OpenQA.Selenium;
using OpenQA.Selenium.Chrome;
using System;
using System.Threading;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using mmisharp;
using OpenQA.Selenium.Interactions;

namespace AppGui
{
    class GoogleMapsWorker
    {
        private Tts tts;

        private String baseMapsUrl;

        private IWebDriver driver;

        private Boolean satelite;
        private Boolean terreno;
        private Boolean transito;

        public GoogleMapsWorker()
        {
            tts = new Tts();

            baseMapsUrl = "https://www.google.com/maps/";

            driver = new ChromeDriver();

            satelite = false;
            terreno = false;
            transito = false;
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

            Thread.Sleep(2000);
            getFirstServiceName(service);
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

                if (transito)
                    transito = false;
                else
                    transito = true;

                openMenu();
                Thread.Sleep(1000);

                IWebElement elem = driver.FindElement(By.XPath("//*[@id=\"settings\"]/div/div[2]/div/ul[1]/li[3]/button"));
                elem.Click();

                Thread.Sleep(500);
                if (transito)
                    tts.Speak("Vista de trânsito ativada");
                else
                    tts.Speak("Vista de trânsito desativada");
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

                if(satelite)
                    satelite = false;
                else
                    satelite = true;
                

                openMenu();
                Thread.Sleep(1000);

                IWebElement elem = driver.FindElement(By.XPath("//*[@id=\"settings\"]/div/div[2]/div/ul[1]/li[2]/div/button[1]"));
                elem.Click();

                Thread.Sleep(500);
                if (satelite)
                    tts.Speak("Vista de Satélite ativada");
                else
                    tts.Speak("Vista de Satélite desativada");
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

                if (terreno)
                    terreno = false;
                else
                    terreno = true;

                openMenu();
                Thread.Sleep(1000);

                IWebElement elem = driver.FindElement(By.XPath("//*[@id=\"settings\"]/div/div[2]/div/ul[1]/li[6]/button"));
                elem.Click();

                Thread.Sleep(500);
                if (terreno)
                    tts.Speak("Vista de terreno ativada");
                else
                    tts.Speak("Vista de terreno desativada");
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

        public void swipeRight()
        {
            try
            {
                IWebElement canvas = driver.FindElement(By.XPath("//*[@id=\"scene\"]"));
                Actions interactions = new Actions(driver);
                interactions.SendKeys(canvas, Keys.ArrowRight).Build().Perform();
            } catch(NoSuchElementException e)
            {
                Console.WriteLine("O mapa nao foi encontrado");
            }
        }

        public void swipeLeft()
        {
            try
            {
                IWebElement canvas = driver.FindElement(By.XPath("//*[@id=\"scene\"]"));
                Actions interactions = new Actions(driver);
                interactions.SendKeys(canvas, Keys.ArrowLeft).Build().Perform();
            }
            catch (NoSuchElementException e)
            {
                Console.WriteLine("O mapa nao foi encontrado");
            }
        }

        public void swipeUp()
        {
            try
            {
                IWebElement canvas = driver.FindElement(By.XPath("//*[@id=\"scene\"]"));
                Actions interactions = new Actions(driver);
                interactions.SendKeys(canvas, Keys.ArrowUp).Build().Perform();
            }
            catch (NoSuchElementException e)
            {
                Console.WriteLine("O mapa nao foi encontrado");
            }
        }

        public void swipeDown()
        {
            try
            {
                IWebElement canvas = driver.FindElement(By.XPath("//*[@id=\"scene\"]"));
                Actions interactions = new Actions(driver);
                interactions.SendKeys(canvas, Keys.ArrowDown).Build().Perform();
            }
            catch (NoSuchElementException e)
            {
                Console.WriteLine("O mapa nao foi encontrado");
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
                        try
                        {
                            IWebElement tempoTry3 = driver.FindElement(By.XPath("//*[@id=\"pane\"]/div/div[2]/div/div/div[4]/div[1]/div[2]/div[4]/div[2]/div[1]/span[2]"));
                            return tempoTry3.Text;
                        } 
                        catch
                        {
                            return "ERROR";
                        }
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
            }

        }

        private void makeDistanceTimeSpeech(String tempo, String distancia)
        {
            if(distancia.Equals("ERROR"))
            {
                if (tempo.Contains("min") && tempo.Contains("h"))
                {
                    tts.Speak("O trajeto mais rápido demora " + tempo);
                }
                else
                {
                    if (tempo.Contains("h"))
                    {
                        tts.Speak("O trajeto mais rápido demora " + tempo + "oras");
                    }
                    else
                    {
                        tts.Speak("O trajeto mais rápido demora " + tempo + "utos");
                    }
                }
            } else
            {
                if (tempo.Contains("min") && tempo.Contains("h"))
                {
                    tts.Speak("A distância a percorrer é de " + distancia + " e demora " + tempo);
                }
                else
                {
                    if (tempo.Contains("h"))
                    {
                        tts.Speak("A distância a percorrer é de " + distancia + " e demora " + tempo + "oras");
                    }
                    else
                    {
                        tts.Speak("A distância a percorrer é de " + distancia + " e demora " + tempo + "utos");
                    }
                }
            }
        }

        private void getFirstServiceName(String service)
        {
            try
            {
                IWebElement nome = driver.FindElement(By.XPath("//*[@id=\"pane\"]/div/div[2]/div/div/div[3]/div[1]/div[1]/div[1]/div[1]/h3/span"));
                tts.Speak("O " + service + " mais perto é o " + nome.Text);
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

    }
}

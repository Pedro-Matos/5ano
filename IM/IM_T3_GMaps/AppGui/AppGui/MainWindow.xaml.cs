using System.Linq;
using System.Windows;
using System.Windows.Media;
using System.Windows.Shapes;
using System.Xml.Linq;
using mmisharp;
using System;
using Newtonsoft.Json;
using System.Threading;

namespace AppGui
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        private MmiCommunication mmiC;
        private GoogleMapsWorker worker;
        public MainWindow()
        {
            worker = new GoogleMapsWorker();
            Thread.Sleep(2500);

            worker.goToPlace("Aveiro");
            Thread.Sleep(2500);

            mmiC = new MmiCommunication("localhost", 8000, "User1", "GUI");
            mmiC.Message += MmiC_Message;
            mmiC.Start();

        }

        private void MmiC_Message(object sender, MmiEventArgs e)
        {
            var doc = XDocument.Parse(e.Message);
            var com = doc.Descendants("command").FirstOrDefault().Value;
            dynamic json = JsonConvert.DeserializeObject(com);
            Console.WriteLine(json);
            switch (json.categoria.ToString())
            {
                case "left-hand":
                    switch (json.gesto.ToString())
                    {
                        case "Swipe Up":
                            worker.toggleSatellite();
                            Thread.Sleep(2500);
                            break;
                        case "Swipe Down":
                            worker.toggleTerreno();
                            Thread.Sleep(2500);
                            break;
                        case "Swipe Right":
                            worker.zoomIn();
                            break;
                        case "Swipe Left":
                            worker.zoomOut();
                            break;
                        case "Push":
                            worker.goToMyLocation();
                            Thread.Sleep(2500);
                            break;
                    }
                    break;
                case "right-hand":
                    switch (json.gesto.ToString())
                    {
                        case "Swipe Up":
                            worker.swipeUp();
                            break;
                        case "Swipe Down":
                            worker.swipeDown();
                            break;
                        case "Swipe Right":
                            worker.swipeRight();
                            break;
                        case "Swipe Left":
                            worker.swipeLeft();
                            break;
                        case "Push":
                            worker.goToMyLocation();
                            Thread.Sleep(2500);
                            break;
                    }
                    break;
                case "postura":
                    switch (json.gesto.ToString())
                    {
                        case "T":
                            worker.setTransportationMode("publicos");
                            break;
                        case "V":
                            worker.setTransportationMode("avião");
                            break;
                        case "Right Kick":
                            worker.setTransportationMode("a pé");
                            break;
                        case "Left Kick":
                            worker.setTransportationMode("a pé");
                            break;
                        case "A":
                            worker.setTransportationMode("carro");
                            break;
                        case "Maos Juntas":
                            worker.closeDirections();
                            Thread.Sleep(2500);
                            break;
                    }
                    break;
            }
        }
    }
}

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
            Thread.Sleep(5000);

            worker.goToPlace("Aveiro");
            Thread.Sleep(5000);
            worker.swipeRight();

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
                            worker.zoomIn();
                            break;
                        case "Swipe Down":
                            worker.zoomOut();
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
                    }
                    break;
            }
        }
    }
}

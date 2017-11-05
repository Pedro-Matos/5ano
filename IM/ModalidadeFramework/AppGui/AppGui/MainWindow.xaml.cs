using System.Linq;
using System.Windows;
using System.Windows.Media;
using System.Windows.Shapes;
using System.Xml.Linq;
using mmisharp;
using Newtonsoft.Json;

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

            mmiC = new MmiCommunication("localhost", 8000, "User1", "GUI");
            mmiC.Message += MmiC_Message;
            mmiC.Start();

        }

        private void MmiC_Message(object sender, MmiEventArgs e)
        {
            var doc = XDocument.Parse(e.Message);
            var com = doc.Descendants("command").FirstOrDefault().Value;
            dynamic json = JsonConvert.DeserializeObject(com);

            Shape _s = null;

            switch (json.CidadeDestino.ToString())
            {
                case "Aveiro":
                    worker.goToPlace("Aveiro");
                    break;

            }

            // Dispatcher para ouvir outro reconhecimento
            //App.Current.Dispatcher.Invoke(() =>
            //{
            //    switch (json.color.ToString())
            //    {
            //        case "GREEN":
            //            _s.Fill = Brushes.Green;
            //            break;
            //        case "BLUE":
            //            _s.Fill = Brushes.Blue;
            //            break;
            //        case "RED":
            //            _s.Fill = Brushes.Red;
            //            break;
            //    }
            //});



        }
    }
}

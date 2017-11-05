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

            switch (json.CidadeDestino.ToString())
            {
                case "Aveiro":
                    worker.goToPlace("Aveiro");
                    break;
                case "Porto":
                    worker.goToPlace("Porto");
                        break;
                case "Águeda":
                    worker.goToPlace("Águeda");
                    break;
                case "Albergaria-a-Velha":
                    worker.goToPlace("Albergaria-a-Velha");
                    break;
                case "Lisboa":
                    worker.goToPlace("Lisboa");
                    break;
                case "Viseu":
                    worker.goToPlace("Viseu");
                    break;
                case "Sever do Vouga":
                    worker.goToPlace("Sever do Vouga");
                    break;
                case "Santa Comba Dão":
                    worker.goToPlace("Santa Comba Dão");
                        break;
            }

            switch(json.Zoom.ToString())
            {
                case "mais":
                    worker.zoomIn();
                    break;

                case "menos":
                    worker.zoomOut();
                    break;

            }

            switch(json.Opções.ToString())
            {
                case "painel":
                    worker.togglePanel();
                    break;
                case "sem painel":
                    worker.togglePanel();
                    break;
                case "sem direções":
                    worker.closeDirections();
                    break;
                case "limpar pesquisa":
                    worker.closeSearchBox();
                    break;
            }

            switch(json.EscolherVista.ToString())
            {
                case "mapa":
                    break;
                case "satélite":
                    worker.toggleSatellite();
                    break;
                case "terreno":
                    worker.toggleTerreno();
                    break;

            }

            switch(json.locaisEmCidades.ToString())
            {
                case "hospitais":
                    worker.searchServiceAtLocation("hospitais", json.CidadeDestino.ToString());
                    break;
                case "interesse":
                    worker.searchServiceAtLocation("interesse", json.CidadeDestino.ToString());
                    break;
                case "hoteis":
                    worker.searchServiceAtLocation("hoteis", json.CidadeDestino.ToString());
                    break;
                case "museus":
                    worker.searchServiceAtLocation("museus", json.CidadeDestino.ToString());
                    break;
                case "restaurantes":
                    worker.searchServiceAtLocation("restaurantes", json.CidadeDestino.ToString());
                    break;
                case "aeroportos":
                    worker.searchServiceAtLocation("aeroportos", json.CidadeDestino.ToString());
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

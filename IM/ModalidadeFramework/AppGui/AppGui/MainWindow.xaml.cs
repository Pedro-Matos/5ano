using System.Linq;
using System.Windows;
using System.Windows.Media;
using System.Windows.Shapes;
using System.Xml.Linq;
using mmisharp;
using System;
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

            mmiC = new MmiCommunication("192.168.1.86", 8000, "User1", "GUI");
            mmiC.Message += MmiC_Message;
            mmiC.Start();

        }

        private void MmiC_Message(object sender, MmiEventArgs e)
        {
            var doc = XDocument.Parse(e.Message);
            var com = doc.Descendants("command").FirstOrDefault().Value;
            dynamic json = JsonConvert.DeserializeObject(com);

            switch(json.Categoria.ToString())
            {
                case "cidades":
                    worker.goToPlace(json.CidadeUnica.ToString());
                    break;

                case "zoom":
                    switch (json.Zoom.ToString())
                    {
                        case "mais":
                            worker.zoomIn();
                            break;
                        case "menos":
                            worker.zoomOut();
                            break;
                    }
                    break;

                case "loc":
                    worker.goToMyLocation();
                    break;

                case "locaisEmCidades":
                    worker.searchServiceAtLocation(json.locais.ToString(), json.CidadeLocal.ToString());
                    break;

                case "tipovista":
                    switch (json.EscolherVista.ToString())
                    {
                        case "satélite":
                            worker.toggleSatellite();
                            break;
                        case "terreno":
                            worker.toggleTerreno();
                            break;
                    }
                    break;

                case "Opções":
                    switch (json.Opções.ToString())
                    {
                        case "painel":
                            worker.togglePanel();
                            break;
                        case "sem painel":
                            worker.togglePanel();
                            break;
                        case "abrir menu":
                            worker.openMenu();
                            break;
                        case "fechar menu":
                            worker.closeMenu();
                            break;
                        case "sem direções":
                            worker.closeDirections();
                            break;
                        case "limpar pesquisa":
                            worker.closeSearchBox();
                            break;
                    }
                    break;

                case "direcao":
                    worker.searchDirections(json.CidadeOrigem.ToString(), json.CidadeDestino.ToString());
                    break;
            }
        }
    }
}

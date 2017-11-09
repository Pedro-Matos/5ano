using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using multimodal;

namespace speechModality
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {

        private SpeechMod _sm;
        Tts t;
        public MainWindow()
        {
            InitializeComponent();

            _sm = new SpeechMod();
            _sm.Recognized += _sm_Recognized;
            t = new Tts();
        }

        private void _sm_Recognized(object sender, SpeechEventArg e)
        {
            result.Text = e.Text;
            confidence.Text = e.Confidence+"";
            if(e.Confidence < 0.3)
            {
                t.Speak("Não compreendi.");
                Console.WriteLine("No confidence ");
            }
            if (e.Final && e.Confidence > 0.85) result.FontWeight = FontWeights.Bold;
            else result.FontWeight = FontWeights.Normal;
        }
    }
}

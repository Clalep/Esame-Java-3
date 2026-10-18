import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ascolatore implements ActionListener {

    JProgressBar cpu;
    JProgressBar mem;
    JProgressBar disc;
    JProgressBar net;

    JTextField indirizzo;
    JTextField porta;

    Socket s;
    PrintWriter pw;
    Scanner sc;

    JRadioButton vm1;
    JRadioButton vm2;
    JRadioButton vm3;

    int selez = -1;

    public ascolatore(JProgressBar cpu, JProgressBar mem, JProgressBar disc, JProgressBar net, JTextField indirizzo, JTextField porta, JRadioButton vm1, JRadioButton vm2, JRadioButton vm3) {

        this.cpu = cpu;
        this.disc = disc;
        this.mem = mem;
        this.net = net;

        this.indirizzo = indirizzo;
        this.porta = porta;

        this.vm1 = vm1;
        this.vm2 = vm2;
        this.vm3 = vm3;


    }




    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equals("connect")) {

            try {
                s = new Socket(indirizzo.getText(), Integer.parseInt(porta.getText()));
                sc = new Scanner(s.getInputStream());
                pw = new PrintWriter(s.getOutputStream());
                System.out.println("connessione riuscita");

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }


        }

        if (e.getActionCommand().equals("discnnect")) {

            pw.close();
            sc.close();
            try {
                s.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }


        }

        if(vm1.isSelected()) {

            selez = 1;

        }
        if(vm2.isSelected()) {

            selez = 2;

        }
        if(vm3.isSelected()) {

            selez = 3;

        }

        if (e.getActionCommand().equals("start")) {



            pw.println("start:vm" + selez);
            pw.flush();
            System.out.println("start:vm" + selez);
            azione prv = new azione(cpu,mem,disc,net, sc,pw);
            Thread ciao = new Thread(prv);
            ciao.start();



        }

        if (e.getActionCommand().equals("stop")) {

            pw.println("stop");
            pw.flush();


        }



    }
}

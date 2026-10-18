import javax.swing.*;
import java.io.PrintWriter;
import java.util.Scanner;

public class azione implements Runnable{

    Scanner sc;
    PrintWriter pw;



    JProgressBar cpu;
    JProgressBar mem;
    JProgressBar disc;
    JProgressBar net;

    public azione(JProgressBar cpu, JProgressBar mem, JProgressBar disc, JProgressBar net,  Scanner sc, PrintWriter pw){

        this.cpu = cpu;
        this.disc = disc;
        this.mem = mem;
        this.net = net;

        this.sc = sc;
        this.pw = pw;


    }

    @Override
    public void run() {

        while (sc.hasNextLine()) {

            String riga = sc.nextLine();

            if(riga.equals("Not Found")) {

                System.out.println("Errore1");
                sc.nextLine();
                break;

            }

            if(riga.equals("*")) {

                System.out.println("Errore2");
                break;

            }


            String[] dati = riga.split(":");

            cpu.setValue(Integer.parseInt(dati[0]));
            mem.setValue(Integer.parseInt(dati[1]));
            disc.setValue(Integer.parseInt(dati[2]));
            net.setValue(Integer.parseInt(dati[3]));

        }
    }
}

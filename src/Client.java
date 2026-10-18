import javax.swing.*;
import java.awt.*;

public class Client {

    public static void main(String[] args) {

        JFrame princ = new JFrame("Claudio Leppo 2172201");

        JTextField indirizzo = new JTextField("localhost");
        JTextField porta = new JTextField("4400");

        JButton connect = new JButton("connect");
        JButton disconnect = new JButton("disconnect");

        JPanel su = new JPanel();

        su.add(indirizzo);
        su.add(porta);
        su.add(connect);
        su.add(disconnect);


        JPanel barre = new JPanel();
        barre.setBorder(BorderFactory.createTitledBorder("VM Status"));
        barre.setLayout(new GridLayout(4,1));

        JProgressBar cpu = new JProgressBar();
        cpu.setStringPainted(true);
        JLabel c = new JLabel("CPU Usage");

        JProgressBar mem = new JProgressBar();
        mem.setStringPainted(true);
        JLabel m = new JLabel("Memory Usage");

        JProgressBar disc = new JProgressBar();
        disc.setStringPainted(true);
        JLabel d = new JLabel("Disk Usage");

        JProgressBar net = new JProgressBar();
        net.setStringPainted(true);
        JLabel n = new JLabel("Network Usage");

        JPanel uno = new JPanel();
        uno.setLayout(new GridLayout(1,2));
        uno.add(c);
        uno.add(cpu);

        JPanel due = new JPanel();
        due.setLayout(new GridLayout(1,2));
        due.add(m);
        due.add(mem);

        JPanel tre = new JPanel();
        tre.setLayout(new GridLayout(1,2));
        tre.add(d);
        tre.add(disc);

        JPanel quattro = new JPanel();
        quattro.setLayout(new GridLayout(1,2));
        quattro.add(n);
        quattro.add(net);

        barre.add(uno);
        barre.add(due);
        barre.add(tre);
        barre.add(quattro);

        JPanel scelta = new JPanel();
        scelta.setBorder(BorderFactory.createTitledBorder("VM Status"));
        scelta.setLayout(new GridLayout(1,3));

        ButtonGroup gr = new ButtonGroup();

        JRadioButton vm1 = new JRadioButton("VM 1");
        JRadioButton vm2 = new JRadioButton("VM 2");
        JRadioButton vm3 = new JRadioButton("VM 3");


        gr.add(vm1);
        gr.add(vm2);
        gr.add(vm3);


        scelta.add(vm1);
        scelta.add(vm2);
        scelta.add(vm3);

        JPanel mid = new JPanel();
        mid.add(barre);
        mid.add(scelta);
        mid.setLayout(new GridLayout(2,1));



        JPanel giu = new JPanel();

        JButton start = new JButton("start");
        JButton stop = new JButton("stop");
        giu.add(start);
        giu.add(stop);



        princ.pack();

        princ.setLayout(new BorderLayout());

        princ.add(su, BorderLayout.NORTH);
        princ.add(mid, BorderLayout.CENTER);
        princ.add(giu, BorderLayout.SOUTH);

        princ.setVisible(true);

        princ.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        ascolatore asc = new ascolatore(cpu,mem,disc,net,indirizzo,porta, vm1,vm2,vm3);
        connect.addActionListener(asc);
        disconnect.addActionListener(asc);
        start.addActionListener(asc);
        stop.addActionListener(asc);
        vm1.addActionListener(asc);
        vm2.addActionListener(asc);
        vm3.addActionListener(asc);



    }


}

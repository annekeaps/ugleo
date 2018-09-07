/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ugleo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 *
 * @author anne
 */
public class Ugleo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            new Ugleo().run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //private boolean typingDelay = false;
    //private boolean speech = false;

    /**
     * Runs the main program.
     *
     * @throws IOException primarily on errors initialising the system.
     */
    public void run() throws IOException {
        MegaHAL hal = new MegaHAL();
        maintenanceKB kb = new maintenanceKB();

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out, true);

        while (true) {
            out.print("User> ");
            out.flush();

            String line = in.readLine();
            if (line == null) {
                break;
            }
                        
            String replyLine = hal.formulateReply(line);
         
            if (replyLine == null) {
                replyLine = "Maaf, saat ini saya belum memiliki informasi yang sesuai tentang hal itu.";
            }
            out.println("MegaHAL> " + replyLine);
        }
    }
}
        
        
        
        /*
        JMegaHal hal = new JMegaHal();

        // We need to teach it a few things first...
        hal.add("Hai apa kabar?");
        hal.add("Hai nama saya ugleo");
        hal.add("");
        hal.add("Saya lapar");
        hal.add("Saya suka belajar");
        hal.add("Saya bisa membaca");
        hal.add("Saya sangat suka makan telur");
        hal.add("Saya sangat suka tidur");
        hal.add("Saya sangat bisa makan");
        hal.add("Jurusan favorit di gunadarma adalah teknik informatika");        
        // (The more sentences we add, the more
        //  sense we will get out of it!)

        // Make JMegaHal generate a sentence.        
        for (int i = 0; i<20; i++){
            String sentence1 = hal.getSentence();
            System.out.println("s1: "+sentence1);
        }

        // make JMegaHal generate another sentence,
        // with the word "jibble" in it (if possible).
        
        for (int i = 0; i<20; i++){
            String sentence2 = hal.getSentence("Siapa nama kamu?");
            System.out.println("s2: "+sentence2);
        } */

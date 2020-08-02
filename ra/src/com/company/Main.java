package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main (String[] args) {
        Scanner ulaz = new Scanner(System.in);
        String putanja = "";
        while(true) {
            System.out.println("Unesite putanju datoteke: ");
            putanja = ulaz.nextLine();
            if (readFromFile(putanja) != null) break;
        }
        InstructionSequence delaySlot = new InstructionSequence(readFromFile(putanja));
        boolean nema = false;
        HashMap<Integer, Instruction> map = delaySlot.getDelayInstructions();
        for(Map.Entry<Integer, Instruction> entry : map.entrySet()) {
            if (entry.getValue().getInstructionName().equals("Nema") && entry.getValue().getLabel().equals("zadrske")) nema = true;
        }
        if (map.size() == 0) System.out.println("Zadrška nije potrebna, jer data sekvenca instrukcija nema nijednu instrukciju grananja");
        else if (nema) System.out.println("Nije moguće pronaći zadršku");
        else writeFile(map, putanja);
    }

    public static void writeFile(HashMap<Integer, Instruction> map, String outputFilePath){
        int position = 0;
        for (int i=0; i<outputFilePath.length(); i++) {
            if (outputFilePath.charAt(i) == '\\') {
                position = i;
            }
        }
        outputFilePath = outputFilePath.replace(outputFilePath.substring(position+1), "");
        outputFilePath += "zadrska.txt";
        System.out.println("Putanja do datoteke: " + outputFilePath);

        File file = new File(outputFilePath);
        BufferedWriter bf = null;;
        try{
            bf = new BufferedWriter(new FileWriter(file) );
            for(Map.Entry<Integer, Instruction> entry : map.entrySet()){
                bf.write( "Za " + (entry.getKey() + 1) + ".instrukciju predlozena instr. zadrske: " + entry.getValue());
                bf.newLine();
            }
            bf.flush();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            try{
                bf.close();
            }catch(Exception e){}
        }
    }

    public static Registers findRegister(String number) {
        Registers reg = null;
        for (Registers r : Registers.values()) {
            if (r.getValue() == Integer.valueOf(number)) {
                reg = r;
                break;
            }
        }
        return reg;
    }

    public static ArrayList<Instruction> readFromFile(String putanja) {
        ArrayList<Instruction> niz = new ArrayList<>();
            Scanner file;
            try {
                file = new Scanner(new FileReader(putanja));
                while (file.hasNextLine()) {
                    String line = file.nextLine();
                    String[] temp = line.split(" ");
                    String label = "";
                    String name = "";
                    boolean imaLabela = false;
                    if (temp[0].contains(":")) {
                        label = temp[0].replace(":", "");
                        for (int i = 1; i < temp.length; i++) {
                            temp[i - 1] = temp[i];
                        }
                        imaLabela = true;
                    }
                    name = temp[0];
                    if ((imaLabela && temp.length == 3) || temp.length == 2) {  //j tip
                        Instruction instruction = new Instruction(name, temp[1]);
                        if (imaLabela) instruction.setLabel2(label);
                        niz.add(instruction);
                    } else if ((imaLabela && temp.length == 4) || temp.length == 3) {  // i tip(sw, lw)
                        String reg1 = temp[1].replace("$", "").replace(",", "").replace("r", "").replace("R", "");
                        Registers firstRegister = findRegister(reg1);
                        String reg2 = temp[2].replace(")", "");
                        reg2 = reg2.substring(reg2.length() - 1);
                        Registers secondRegister = findRegister(reg2);
                        Instruction instruction = new Instruction(name, firstRegister, secondRegister);
                        if (imaLabela) instruction.setLabel2(label);
                        niz.add(instruction);
                    } else if ((imaLabela && temp.length == 5) || temp.length == 4) {
                        String reg1 = temp[1].replace("$", "").replace(",", "").replace("r", "").replace("R", "");
                        Registers firstRegister = findRegister(reg1);
                        String reg2 = temp[2].replace("$", "").replace(",", "").replace("r", "").replace("R", "");
                        Registers secondRegister = findRegister(reg2);
                        if (temp[3].contains("$") || (temp[3].contains("R") || temp[3].contains("r") && temp[3].contains("[0-9]+"))) {   //R tip
                            String reg3 = temp[3].replace("$", "").replace("r", "").replace("R", "");
                            Registers thirdRegister = findRegister(reg3);
                            Instruction instruction = new Instruction(name, firstRegister, secondRegister, thirdRegister);
                            if (imaLabela) instruction.setLabel2(label);
                            niz.add(instruction);
                        } else if (temp[3].matches("[0-9]+")) {  //i tip sa konstantom
                            int konstanta = Integer.valueOf(temp[3]);
                            Instruction instruction = new Instruction(name, firstRegister, secondRegister, konstanta);
                            if (imaLabela) instruction.setLabel2(label);
                            niz.add(instruction);
                        } else {  //bne, beq
                            String jumpLabel = temp[3];
                            Instruction instruction = new Instruction(name, firstRegister, secondRegister, jumpLabel);
                            if (imaLabela) instruction.setLabel2(label);
                            niz.add(instruction);
                        }
                    }
                }
                file.close();
                return niz;
            } catch (FileNotFoundException e) {
                System.out.println("Datoteka ne postoji ili se ne moze otvoriti");
                System.out.println("Greska: " + e);
                return null;
            }
    }
}

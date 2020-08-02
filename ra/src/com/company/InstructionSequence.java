package com.company;

import java.util.ArrayList;
import java.util.HashMap;

public class InstructionSequence {
    private ArrayList<Instruction> instructions = new ArrayList<>();


    public InstructionSequence(ArrayList<Instruction> instructions) {
        this.instructions = instructions;
    }

    public ArrayList<Instruction> getInstructions() {
        return instructions;
    }

    public void setInstructions(ArrayList<Instruction> instructions) {
        this.instructions = instructions;
    }


    public boolean checkInstructionsDependencyForBranch (Instruction first,Instruction second) {
        if(first.getInstructionName().toUpperCase().equals("SW"))
            return first.getSecondRegister()==second.getFirstRegister() || first.getSecondRegister()==second.getSecondRegister();
        else
            return first.getFirstRegister() == second.getFirstRegister() || first.getFirstRegister()==second.getSecondRegister();
    }

    public boolean checkInstructionsDependencyDestAndSource(Instruction first,Instruction second) {
        Registers destReg,source1,source2;
        destReg = (Registers) first.getFirstRegister();
        source1 = (Registers) second.getSecondRegister();
        source2 = (Registers) second.getThirdRegister();
        if(first.getInstructionName().toUpperCase().equals("SW")) {
            destReg = (Registers) first.getSecondRegister();
        }
        if(second.getInstructionName().toUpperCase().equals("SW")) {
            source1 = (Registers) second.getFirstRegister();
            source2 = null;
        }
        if(first.getInstructionName().toLowerCase().equals("beq") || first.getInstructionName().equals("bne")) {
            destReg = null;
        }
        if(second.getInstructionName().toLowerCase().equals("beq") || second.getInstructionName().equals("bne")) {
            source1 = (Registers) second.getFirstRegister();
            source2 = (Registers) second.getSecondRegister();
        }

        if(destReg!=null) {
            if(source2!=null) return (destReg == source1 || destReg == source2);
            else return destReg == source1;
        } else return false;
    }

    public boolean checkAFailure(Instruction instruction,Integer pos1,Integer pos2) {
        Integer position = 0;
        for(Instruction i : instructions) {
            if(position>pos1 && position<pos2 && checkInstructionsDependencyDestAndSource(instruction,i)) return true;
            position++;
        }
        return false;
    }

    public boolean checkBFailure(Instruction instruction,Integer positionOfBranchInstruction) {
        Integer pos = 0;
        for(Instruction i : instructions) {
            if(pos>positionOfBranchInstruction && checkInstructionsDependencyDestAndSource(instruction,i)) return true;
            pos++;
        }
        return false;
    }

    public boolean checkCFailure(Instruction instruction,String label) {
        Integer positionOfLabel = 0;
        Integer pos2 = 0;
        for(Instruction i:instructions) {
            if(i.getLabel2()!=null && i.getLabel2().equals(label)) {
                positionOfLabel = pos2;
                break;
            }
            pos2++;
        }
        Integer pos = 0;
        for(Instruction i : instructions) {
            if(pos>=positionOfLabel && checkInstructionsDependencyDestAndSource(instruction,i)) {
                return true;
            }
            pos++;
        }
        return false;
    }

    public Instruction checkForAMethod (Integer position,Instruction instruction) {
        Integer pos = 0;
        Instruction result = null;
        for(Instruction i :instructions) {
            if(pos<position && !checkInstructionsDependencyForBranch(i,instruction) && !checkAFailure(i,pos,position)
                    && !i.getInstructionName().toLowerCase().equals("beq") &&
                    !i.getInstructionName().toLowerCase().equals("bne") &&
                    !i.getInstructionName().toLowerCase().equals("j") && !i.getInstructionName().toLowerCase().equals("jal")
                    && !i.getInstructionName().toLowerCase().equals("jr")) result = i;
            pos++;
        }
        return result;
    }

    public Instruction checkForBMethod (String label,Integer positionOfBranchInstruction) {
        for(Instruction i: instructions) {
            if(i.getLabel2()!=null && i.getLabel2().equals(label) && !checkBFailure(i,positionOfBranchInstruction) &&
                    !i.getInstructionName().toLowerCase().equals("j") && !i.getInstructionName().toLowerCase().equals("jal")
                    && !i.getInstructionName().toLowerCase().equals("jr")) return i;
        }
        return null;
    }

    public Instruction checkForCMethod (Integer position,Instruction instruction,String label) {
        Integer pos=0;
        for(Instruction i: instructions) {
            if(pos>position && !checkCFailure(instruction,label) && !i.getInstructionName().toLowerCase().equals("j") &&
                    !i.getInstructionName().toLowerCase().equals("jal")
                    && !i.getInstructionName().toLowerCase().equals("jr")) {
                return i;
            }
            pos++;
        }
        return null;
    }

    public HashMap<Integer,Instruction> getDelayInstructions () {
        HashMap<Integer,Instruction> result = new HashMap<>();

        Integer position = 0;
        for(Instruction i : instructions) {
            if(i.getInstructionName().toLowerCase().equals("bne") || i.getInstructionName().toLowerCase().equals("beq")) {
                Instruction instruction;
                if((instruction=checkForAMethod(position,i))!=null) {
                    result.put(position,instruction);
                }else if((instruction=checkForBMethod(i.getLabel(),position))!=null) {
                    result.put(position,instruction);
                }else if((instruction=checkForCMethod(position,i,i.getLabel()))!=null) {
                    result.put(position,instruction);
                }else{
                    result.put(position,new Instruction("Nema","zadrske"));
                }
            }
            position++;
        }
        return result;
    }
}
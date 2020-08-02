package com.company;

public class Instruction {
    private String instructionName;
    private String label;
    private String label2;
    private Enum firstRegister;
    private Enum secondRegister;
    private Enum thirdRegister;
    private Integer valueI;

    public Integer getValueI() {
        return valueI;
    }

    //R tip
    public Instruction(String instructionName, Enum firstRegister, Enum secondRegister, Enum thirdRegister) {
        this.instructionName = instructionName;
        this.firstRegister = firstRegister;
        this.secondRegister = secondRegister;
        this.thirdRegister = thirdRegister;
    }

    public Instruction(String label2, String instructionName, Enum firstRegister, Enum secondRegister, Enum thirdRegister) {
        this.instructionName = instructionName;
        this.firstRegister = firstRegister;
        this.secondRegister = secondRegister;
        this.thirdRegister = thirdRegister;
        this.label2 = label2;
    }

    //I tip
    public Instruction(String instructionName, Enum firstRegister, Enum secondRegister, Integer valueI) {
        this.instructionName = instructionName;
        this.firstRegister = firstRegister;
        this.secondRegister = secondRegister;
        this.valueI = valueI;
    }

    public Instruction(String instructionName, Enum firstRegister, Enum secondRegister) {
        this.instructionName = instructionName;
        this.firstRegister = firstRegister;
        this.secondRegister = secondRegister;
    }

    public Instruction(String label2, String instructionName, Enum firstRegister, Enum secondRegister, Integer valueI) {
        this.instructionName = instructionName;
        this.firstRegister = firstRegister;
        this.secondRegister = secondRegister;
        this.valueI = valueI;
        this.label2 = label2;
    }

    public Instruction(String label2, String instructionName, Enum firstRegister, Enum secondRegister) {
        this.instructionName = instructionName;
        this.firstRegister = firstRegister;
        this.secondRegister = secondRegister;
        this.label2 = label2;
    }

    //bne i bqe
    public Instruction(String instructionName,  Enum firstRegister, Enum secondRegister, String label) {
        this.instructionName = instructionName;
        this.label = label;
        this.firstRegister = firstRegister;
        this.secondRegister = secondRegister;
    }

    public Instruction(String label2, String instructionName, Enum firstRegister, Enum secondRegister, String label) {
        this.instructionName = instructionName;
        this.label = label;
        this.label2 = label2;
        this.firstRegister = firstRegister;
        this.secondRegister = secondRegister;
    }

    //J tip
    public Instruction(String instructionName, String label) {
        this.instructionName = instructionName;
        this.label = label;
    }

    public Instruction(String label2, String instructionName, String label) {
        this.instructionName = instructionName;
        this.label = label;
        this.label2 = label2;
    }

    public String getInstructionName() {
        return instructionName;
    }

    public void setInstructionName(String instructionName) {
        this.instructionName = instructionName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Enum getFirstRegister() {
        return firstRegister;
    }

    public void setFirstRegister(Enum firstRegister) {
        this.firstRegister = firstRegister;
    }

    public Enum getSecondRegister() {
        return secondRegister;
    }

    public void setSecondRegister(Enum secondRegister) {
        this.secondRegister = secondRegister;
    }

    public Enum getThirdRegister() {
        return thirdRegister;
    }

    public void setThirdRegister(Enum thirdRegister) {
        this.thirdRegister = thirdRegister;
    }

    public String getLabel2() {
        return label2;
    }

    public void setLabel2(String label2) {
        this.label2 = label2;
    }

    public void setValueI(Integer valueI) {
        this.valueI = valueI;
    }

    @Override
    public String toString() {
        String s = "";
        if (label2 != null) s += label2 + ": ";
        if (thirdRegister == null && label == null && valueI == null){
            s += instructionName + " " + firstRegister + ", " + "offset(" + secondRegister + ")";
        } else if (thirdRegister == null && label == null){
            s += instructionName + " " + firstRegister + ", " + secondRegister + ", " + valueI;
        } else if (firstRegister == null && secondRegister == null && thirdRegister == null && valueI == null) {
            s += instructionName + " " + label;
        } else if (thirdRegister == null && valueI == null) {
            s += instructionName + " " + firstRegister + ", " + secondRegister + ", " + label;
        } else if (valueI == null && label == null) {
            s += instructionName + " " + firstRegister + ", " + secondRegister + ", " + thirdRegister;
        }
        return s;
    }
}
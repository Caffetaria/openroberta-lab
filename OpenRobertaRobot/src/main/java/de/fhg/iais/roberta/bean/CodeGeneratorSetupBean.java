package de.fhg.iais.roberta.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import de.fhg.iais.roberta.codegen.HelperMethodGenerator;
import de.fhg.iais.roberta.factory.AbstractRobotFactory;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.lang.functions.FunctionNames;
import de.fhg.iais.roberta.syntax.lang.methods.Method;
import de.fhg.iais.roberta.util.Util1;

public class CodeGeneratorSetupBean {

    private String helperMethodFile = "";
    private final Set<FunctionNames> usedFunctions = new HashSet<>();
    private List<String> globalVariables = new ArrayList<>();
    private List<String> declaredVariables = new ArrayList<>();
    private List<VarDeclaration<Void>> visitedVars = new ArrayList<>();
    private List<Method<Void>> userDefinedMethods = new ArrayList<>();
    private Set<String> markedVariablesAsGlobal = new HashSet<>();
    private boolean isProgramEmpty = false;
    private boolean isListsUsed = false;
    private Map<Integer, Boolean> loopsLabelContainer = new HashMap<>();

    //TODO: from NXT
    private boolean isVolumeVariableNeeded;

    private HelperMethodGenerator helperMethodGenerator;

    private String fileExtension;

    public HelperMethodGenerator getHelperMethodGenerator() {
        return this.helperMethodGenerator;
    }

    public Set<FunctionNames> getUsedFunctions() {
        return this.usedFunctions;
    }

    public List<String> getGlobalVariables() {
        return this.globalVariables;
    }

    public List<String> getDeclaredVariables() {
        return this.declaredVariables;
    }

    public List<VarDeclaration<Void>> getVisitedVars() {
        return this.visitedVars;
    }

    public List<Method<Void>> getUserDefinedMethods() {
        return this.userDefinedMethods;
    }

    public Set<String> getMarkedVariablesAsGlobal() {
        return this.markedVariablesAsGlobal;
    }

    public boolean isProgramEmpty() {
        return this.isProgramEmpty;
    }

    public boolean isListsUsed() {
        return this.isListsUsed;
    }

    public Map<Integer, Boolean> getLoopsLabelContainer() {
        return this.loopsLabelContainer;
    }

    public boolean isVolumeVariableNeeded() {
        return this.isVolumeVariableNeeded;
    }

    public static class Builder {
        private final CodeGeneratorSetupBean codeGeneratorBean = new CodeGeneratorSetupBean();

        public Builder setHelperMethodFile(String file) {
            this.codeGeneratorBean.helperMethodFile = file;
            return this;
        }

        public Builder addUsedFunction(FunctionNames usedFunction) {
            this.codeGeneratorBean.usedFunctions.add(usedFunction);
            return this;
        }

        public Builder setGlobalVariables(List<String> globalVariables) {
            this.codeGeneratorBean.globalVariables = globalVariables;
            return this;
        }

        public Builder setDeclaredVariables(List<String> declaredVariables) {
            this.codeGeneratorBean.declaredVariables = declaredVariables;
            return this;
        }

        public Builder setVisitedVars(List<VarDeclaration<Void>> visitedVars) {
            this.codeGeneratorBean.visitedVars = visitedVars;
            return this;
        }

        public Builder setUserDefinedMethods(List<Method<Void>> userDefinedMethods) {
            this.codeGeneratorBean.userDefinedMethods = userDefinedMethods;
            return this;
        }

        public Builder setMarkedVariablesAsGlobal(Set<String> markedVariablesAsGlobal) {
            this.codeGeneratorBean.markedVariablesAsGlobal = markedVariablesAsGlobal;
            return this;
        }

        public Builder setProgramEmpty(boolean isProgramEmpty) {
            this.codeGeneratorBean.isProgramEmpty = isProgramEmpty;
            return this;
        }

        public Builder setListsUsed(boolean isListsUsed) {
            this.codeGeneratorBean.isListsUsed = isListsUsed;
            return this;
        }

        public Builder setLoopsLabelContainer(Map<Integer, Boolean> loopsLabelContainer) {
            this.codeGeneratorBean.loopsLabelContainer = loopsLabelContainer;
            return this;
        }

        public Builder setVolumeVariableNeeded(boolean isVolumeVariableNeeded) {
            this.codeGeneratorBean.isVolumeVariableNeeded = isVolumeVariableNeeded;
            return this;
        }

        public Builder setFileExtension(String fileExtension) {
            this.codeGeneratorBean.fileExtension = fileExtension;
            return this;
        }

        public CodeGeneratorSetupBean build() {
            JSONObject helperMethods = new JSONObject();
            Util1.loadYAMLRecursive("", helperMethods, this.codeGeneratorBean.helperMethodFile, true);
            this.codeGeneratorBean.helperMethodGenerator =
                new HelperMethodGenerator(helperMethods, AbstractRobotFactory.getLanguageFromFileExtension(this.codeGeneratorBean.fileExtension));
            return this.codeGeneratorBean;
        }
    }
}

package de.fhg.iais.roberta.javaServer.restServices.all.v2.service;

import de.fhg.iais.roberta.persistence.AbstractProcessor;

public class ProjectResponse {
    private String programSourceCode;
    private String programFileExtension;
    private String programBlockSet;
    private String configurationBlockSet;
    private AbstractProcessor annotations;

    public String getProgramSourceCode() {
        return this.programSourceCode;
    }

    public void setProgramSourceCode(String programSourceCode) {
        this.programSourceCode = programSourceCode;
    }

    public String getProgramFileExtension() {
        return this.programFileExtension;
    }

    public void setProgramFileExtension(String programFileExtension) {
        this.programFileExtension = programFileExtension;
    }

    public String getProgramBlockSet() {
        return this.programBlockSet;
    }

    public void setProgramBlockSet(String programBlockSet) {
        this.programBlockSet = programBlockSet;
    }

    public String getConfigurationBlockSet() {
        return this.configurationBlockSet;
    }

    public void setConfigurationBlockSet(String configurationBlockSet) {
        this.configurationBlockSet = configurationBlockSet;
    }

    public AbstractProcessor getAnnotations() {
        return this.annotations;
    }

    public void setAnnotations(AbstractProcessor annotations) {
        this.annotations = annotations;
    }
}

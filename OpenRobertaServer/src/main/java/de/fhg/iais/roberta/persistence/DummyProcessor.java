package de.fhg.iais.roberta.persistence;

import de.fhg.iais.roberta.javaServer.restServices.all.v1.ClientProgram;

/**
 * this processor can be used to generate messages for the front end. See the usage in {@link ClientProgram} command "showSourceP"
 */
public class DummyProcessor extends AbstractProcessor {

    public DummyProcessor() {
        super(null, null);
    }
}

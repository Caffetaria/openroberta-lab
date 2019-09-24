package de.fhg.iais.roberta.codegen;

import com.google.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.visitor.validate.IWorker;

public class TransferWorker implements IWorker {
    private static final Logger LOG = LoggerFactory.getLogger(TransferWorker.class);

    @Override
    public void execute(Project project) {
        Key run = project.getRobotCommunicator().run(project.getToken(), project.getRobot(), project.getProgramName());
        project.setResult(run);
    }
}

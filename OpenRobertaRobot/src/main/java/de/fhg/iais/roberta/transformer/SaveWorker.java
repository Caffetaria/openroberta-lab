package de.fhg.iais.roberta.transformer;

import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.visitor.validate.IWorker;

public class SaveWorker implements IWorker {
    @Override
    public void execute(Project project) {
        Util1.storeGeneratedProgram(project.getSourceCode().toString(), project.getToken(), project.getProgramName(), "." + project.getFileExtension());
        project.setResult(Key.COMPILERWORKFLOW_SUCCESS);
    }
}

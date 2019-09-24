package de.fhg.iais.roberta.visitor.validate;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean.Builder;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.Key;

public class Ev3BrickValidatorWorker implements IWorker {

    @Override
    public void execute(Project project) {
        UsedHardwareBean.Builder builder = new Builder();
        AbstractProgramValidatorVisitor visitor = new Ev3BrickValidatorVisitor(builder, project.getConfigurationAst());
        visitor.check(project.getProgramAst().getTree());
        final int errorCounter = visitor.getErrorCount();
        if ( errorCounter > 0 ) {
            project.setResult(Key.PROGRAM_INVALID_STATEMETNS);
        }
        project.addWorkerResult("ProgramValidator", builder.build());
    }
}

package de.fhg.iais.roberta.visitor;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractStackMachineGeneratorWorker;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractStackMachineVisitor;
import de.fhg.iais.roberta.visitor.validate.IWorker;

public final class WedoCodeGeneratorWorker extends AbstractStackMachineGeneratorWorker {
    @Override
    protected AbstractStackMachineVisitor<Void> getVisitor(UsedHardwareBean usedHardwareBean,
                                                           Project project) {
        return new WeDoStackMachineVisitor<>(usedHardwareBean, project.getConfigurationAst(), project.getProgramAst().getTree());

    }
}

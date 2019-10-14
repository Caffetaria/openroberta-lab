package de.fhg.iais.roberta.visitor.codegen;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractLanguageGeneratorWorker;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractLanguageVisitor;
import de.fhg.iais.roberta.visitor.validate.IWorker;

public final class RaspberryPiPythonGeneratorWorker extends AbstractLanguageGeneratorWorker {

    @Override
    protected AbstractLanguageVisitor getVisitor(UsedHardwareBean usedHardwareBean,
                                                 CodeGeneratorSetupBean codeGeneratorSetupBean,
                                                 Project project) {
        return new RaspberryPiPythonVisitor(
                usedHardwareBean,
                codeGeneratorSetupBean,
                project.getConfigurationAst(),
                project.getProgramAst().getTree(),
                project.getLanguage());
    }
}

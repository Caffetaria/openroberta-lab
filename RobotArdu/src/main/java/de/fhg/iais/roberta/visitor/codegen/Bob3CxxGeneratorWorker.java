package de.fhg.iais.roberta.visitor.codegen;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractLanguageGeneratorWorker;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractLanguageVisitor;

public class Bob3CxxGeneratorWorker extends AbstractLanguageGeneratorWorker {

    @Override
    protected AbstractLanguageVisitor getVisitor(UsedHardwareBean usedHardwareBean, CodeGeneratorSetupBean codeGeneratorSetupBean, Project project) {
        return new Bob3CppVisitor(usedHardwareBean, codeGeneratorSetupBean, project.getProgramAst().getTree());
    }
}

package de.fhg.iais.roberta.visitor;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.transformer.UsedHardwareBean;
import de.fhg.iais.roberta.visitor.validate.IWorker;

public final class WedoCodeGeneratorWorker implements IWorker {
    private static final Logger LOG = LoggerFactory.getLogger(WedoCodeGeneratorWorker.class);

    @Override
    public void execute(Project project) {
        Object usedHardwareBean = project.getWorkerResult("CollectedHardware");
        WeDoStackMachineVisitor<Void> visitor =
            new WeDoStackMachineVisitor<>((UsedHardwareBean) usedHardwareBean, project.getConfigurationAst(), project.getProgramAst().getTree());
        visitor.generateCodeFromPhrases(project.getProgramAst().getTree());
        JSONObject generatedCode = new JSONObject();
        generatedCode.put(C.OPS, visitor.getOpArray()).put(C.FUNCTION_DECLARATION, visitor.getFctDecls());
        project.setSourceCode(generatedCode.toString(2));
    }
}

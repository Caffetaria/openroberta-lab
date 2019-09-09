package de.fhg.iais.roberta.codegen;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.components.ev3c4ev3.C4Ev3SourceCompiler;
import de.fhg.iais.roberta.components.ev3c4ev3.Uf2Builder;
import de.fhg.iais.roberta.components.ev3c4ev3.Uf2FileContainer;
import de.fhg.iais.roberta.transformer.CompilerSetupBean;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.visitor.validate.IWorker;

public class Ev3C4ev3CompilerWorker implements IWorker {

    private static final Logger LOG = LoggerFactory.getLogger(Ev3C4ev3CompilerWorker.class);

    @Override
    public void execute(Project project) {
        CompilerSetupBean compilerWorkflowBean = (CompilerSetupBean) project.getWorkerResult("CompilerSetup");
        final String compilerResourcesDir = compilerWorkflowBean.getCompilerResourcesDir();
        String programName = project.getProgramName();
        C4Ev3SourceCompiler compiler = new C4Ev3SourceCompiler(compilerResourcesDir);
        Uf2Builder uf2Builder = new Uf2Builder(compilerResourcesDir);
        Key workflowResult = runBuild(project, compiler, uf2Builder);
        project.getErrorMessages().add(workflowResult);
        if ( workflowResult == Key.COMPILERWORKFLOW_SUCCESS ) {
            LOG.info("compile arduino program {} successful", programName);
        } else {
            LOG.error("compile arduino program {} failed with {}", programName, workflowResult);
        }
    }

    public Key runBuild(Project project, C4Ev3SourceCompiler compiler, Uf2Builder uf2Builder) {
        CompilerSetupBean compilerWorkflowBean = (CompilerSetupBean) project.getWorkerResult("CompilerSetup");
        final String tempDir = compilerWorkflowBean.getTempDir();
        String token = project.getToken();
        String programName = project.getProgramName();
        Util1.storeGeneratedProgram(project.getSourceCode().toString(), token, programName, ".cpp");
        String sourceCodeFileName = tempDir + token + "/" + programName + "/source/" + programName + ".cpp";
        String binaryFileName = tempDir + token + "/" + programName + "/target/" + programName + ".elf";
        boolean compilationSuccess = compiler.compile(sourceCodeFileName, binaryFileName);
        if ( !compilationSuccess ) {
            return Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
        }
        Uf2FileContainer uf2;
        try {
            uf2 = uf2Builder.createUf2File(programName, binaryFileName);
        } catch ( IOException e ) {
            e.printStackTrace();
            return Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
        }
        project.setCompiledHex(uf2.toBase64());
        LOG.info("uf2 for program {} generated successfully", programName);
        return Key.COMPILERWORKFLOW_SUCCESS;
    }
}

package de.fhg.iais.roberta.codegen;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.transformer.CompilerSetupBean;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.visitor.validate.IWorker;

/**
 * The workflow for the Edison compiler. Blockly blocks are first converted into EdPy Python2 code and then the code is converted into a WAV audio file.
 * See also: https://github.com/Bdanilko/EdPy
 */
public class EdisonCompilerWorker implements IWorker {

    /**
     * Logger for this class
     */
    private static final Logger LOG = LoggerFactory.getLogger(EdisonCompilerWorker.class);

    @Override
    public void execute(Project project) {
        Util1.storeGeneratedProgram(project.getSourceCode().toString(), project.getToken(), project.getProgramName(), ".py");
        Key workflowResult = runBuild(project);
        project.getErrorMessages().add(workflowResult);
        String programName = project.getProgramName();
        if ( workflowResult == Key.COMPILERWORKFLOW_SUCCESS ) {
            LOG.info("compile arduino program {} successful", programName);
        } else {
            LOG.error("compile arduino program {} failed with {}", programName, workflowResult);
        }
    }

    /**
     * Builds the WAV file from the .py source file using the EdPy Python2 compiler (https://github.com/OpenRoberta/EdPy) by starting an external Python2
     * process.
     * The file will be stored as {@link PluginProperties#getTempDir()}/token/source/XXXX.wav and also in {@link #compiledWav} as a Base64 String.
     *
     * @param token the credential supplied by the user. Needed to provide a unique temporary directory name
     * @param pyFile the source file name
     * @return a Key that gives information about the building process (success, failure, interrupted,...)
     */
    private Key runBuild(Project project) {
        CompilerSetupBean compilerWorkflowBean = (CompilerSetupBean) project.getWorkerResult("CompilerSetup");
        final String compilerBinDir = compilerWorkflowBean.getCompilerBinDir();
        final String compilerResourcesDir = compilerWorkflowBean.getCompilerResourcesDir();
        final String tempDir = compilerWorkflowBean.getTempDir();
        //get all directories
        String token = project.getToken();
        String pyFile = project.getProgramName();
        String sourceFilePath = tempDir + "/" + token + "/" + pyFile + "/source/";
        String targetFilePath = tempDir + "/" + token + "/" + pyFile + "/target/";

        //build and start the Python process
        String[] executableWithParameters =
            new String[] {
                "python2",
                compilerResourcesDir + "EdPy.py",
                compilerResourcesDir + "en_lang.json",
                sourceFilePath + pyFile + ".py",
                "-t",
                targetFilePath + pyFile + ".wav"
            };

        boolean success = AbstractCompilerWorkflow.runCrossCompilerNoResponse(executableWithParameters);
        if ( success ) {
            try {
                byte[] wavBytes = FileUtils.readFileToByteArray(new File(targetFilePath + pyFile + ".wav"));
                project.setCompiledHex(Base64.getEncoder().encodeToString(wavBytes));
                return Key.COMPILERWORKFLOW_SUCCESS;
            } catch ( IOException e ) {
                LOG.error("Compilation successful, but reading WAV file failed (IOException)", e);
                return Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
            }
        } else {
            return Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
        }
    }
}
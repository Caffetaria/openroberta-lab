package de.fhg.iais.roberta.codegen;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.components.ev3lejos.JavaSourceCompiler;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.validate.IWorker;

public class Ev3LejosCompilerWorker implements IWorker {

    private static final Logger LOG = LoggerFactory.getLogger(Ev3LejosCompilerWorker.class);

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void execute(Project project) {
        storeGeneratedProgram(project.getSourceCode().toString(), project.getToken(), project.getProgramName(), ".java");
        final String compilerResourcesDir = System.getenv("robot_crosscompiler_resourcebase") + "/RobotEV3/crossCompilerResources/lejos_v1/";
        String tempDir = "/tmp/";

        JavaSourceCompiler scp = new JavaSourceCompiler(project.getProgramName(), project.getSourceCode().toString(), compilerResourcesDir);
        scp.compileAndPackage(tempDir, project.getToken());
        if ( scp.isSuccess() ) {
            LOG.info("jar for program {} generated successfully", project.getProgramName());
            project.getErrorMessages().add(Key.COMPILERWORKFLOW_SUCCESS);
        } else {
            LOG.error("build exception. Messages from the compiler are:\n" + scp.getCompilerResponse());
            project.getErrorMessages().add(Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED);
        }
    }

    protected final void storeGeneratedProgram(String generatedSourceCode, String token, String programName, String ext) {
        try {
            String tempDir = "/tmp/";
            File sourceFile = new File(tempDir + token + "/" + programName + "/source/" + programName + ext);
            Path path = Paths.get(tempDir + token + "/" + programName + "/target/");
            try {
                Files.createDirectories(path);
                FileUtils.writeStringToFile(sourceFile, generatedSourceCode, StandardCharsets.UTF_8.displayName());
            } catch ( IOException e ) {
                String msg = "could not write source code to file system";
                LOG.error(msg, e);
                throw new DbcException(msg, e);
            }
            LOG.info("stored under: " + sourceFile.getPath());
        } catch ( Exception e ) {
            LOG.error("Storing the generated program " + programName + " into directory " + token + " failed", e);
        }
    }

    @Override
    public Map<String, String> getResult() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Key getResultKey() {
        // TODO Auto-generated method stub
        return null;
    }

}

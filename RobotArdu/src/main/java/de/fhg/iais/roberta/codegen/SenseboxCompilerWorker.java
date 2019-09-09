package de.fhg.iais.roberta.codegen;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.transformer.CompilerSetupBean;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.visitor.validate.IWorker;

public class SenseboxCompilerWorker implements IWorker {

    private static final Logger LOG = LoggerFactory.getLogger(SenseboxCompilerWorker.class);

    @Override
    public void execute(Project project) {
        // TODO Auto-generated method stub

    }

    /**
     * create command to call the cross compiler and execute the call.
     *
     * @return Key.COMPILERWORKFLOW_SUCCESS or Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED
     */
    private Key runBuild(Project project, String token, String mainFile, String mainPackage) {
        CompilerSetupBean compilerWorkflowBean = (CompilerSetupBean) project.getWorkerResult("CompilerSetup");
        final String compilerBinDir = compilerWorkflowBean.getCompilerBinDir();
        final String compilerResourcesDir = compilerWorkflowBean.getCompilerResourcesDir();
        final String tempDir = compilerWorkflowBean.getTempDir();

        String scriptName = "";
        String os = "";
        if ( SystemUtils.IS_OS_LINUX ) {
            if ( System.getProperty("os.arch").contains("arm") ) {
                scriptName = compilerResourcesDir + "arduino-builder/linux-arm/arduino-builder";
                os = "arduino-builder/linux-arm";
            } else {
                scriptName = compilerResourcesDir + "arduino-builder/linux/arduino-builder";
                os = "arduino-builder/linux";
            }
        } else if ( SystemUtils.IS_OS_WINDOWS ) {
            scriptName = compilerResourcesDir + "arduino-builder/windows/arduino-builder.exe";
            os = "arduino-builder/windows";
        } else if ( SystemUtils.IS_OS_MAC ) {
            scriptName = compilerResourcesDir + "arduino-builder/osx/arduino-builder";
            os = "arduino-builder/osx";
        }
        String fqbnArg = "-fqbn=sensebox:samd:sb:power=on";
        Path path = Paths.get(tempDir + token + "/" + mainFile);
        Path base = Paths.get("");

        String[] executableWithParameters =
            new String[] {
                scriptName,
                "-hardware=" + compilerResourcesDir + "hardware/builtin",
                "-hardware=" + compilerResourcesDir + "hardware/additional",
                "-tools=" + compilerResourcesDir + "/" + os + "/tools-builder",
                "-tools=" + compilerResourcesDir + "hardware/additional",
                "-libraries=" + compilerResourcesDir + "/libraries",
                fqbnArg,
                "-prefs=compiler.path=" + compilerBinDir,
                "-vid-pid=0X04D8_0XEF66",
                "-ide-version=10805",
                "-prefs=build.warn_data_percentage=75",
                "-prefs=runtime.tools.arduinoOTA.path=" + compilerResourcesDir + "hardware/additional/arduino/tools/arduinoOTA/1.2.0",
                "-prefs=runtime.tools.CMSIS.path=" + compilerResourcesDir + "hardware/additional/arduino/tools/CMSIS/4.5.0",
                "-prefs=runtime.tools.CMSIS-Atmel.path=" + compilerResourcesDir + "hardware/additional/arduino/tools/CMSIS-Atmel/1.1.0",
                "-prefs=runtime.tools.openocd.path=" + compilerResourcesDir + "hardware/additional/arduino/tools/openocd/0.9.0-arduino6-static",
                "-prefs=runtime.tools.arm-none-eabi-gcc.path=" + compilerResourcesDir + "hardware/additional/arduino/tools/arm-none-eabi-gcc/4.8.3-2014q1",
                "-prefs=runtime.tools.bossac.path=" + compilerResourcesDir + "hardware/additional/arduino/tools/bossac/1.7.0",

                "-build-path=" + base.resolve(path).toAbsolutePath().normalize().toString() + "/target/",
                base.resolve(path).toAbsolutePath().normalize().toString() + "/source/" + mainFile + ".ino"
            };
        boolean success = AbstractCompilerWorkflow.runCrossCompilerNoResponse(executableWithParameters);
        if ( success ) {
            project.setCompiledHex(AbstractCompilerWorkflow.getBase64EncodedBinary(path + "/target/" + project.getProgramName() + ".ino.hex"));
            if ( project.getCompiledHex() != null ) {
                return Key.COMPILERWORKFLOW_SUCCESS;
            } else {
                return Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
            }
        } else {
            return Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
        }
    }
}

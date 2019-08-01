package de.fhg.iais.roberta.codegen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.transformer.mbed.Jaxb2MbedConfigurationAst;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;
import de.fhg.iais.roberta.visitor.codegen.MbedStackMachineVisitor;

public class MbedSimCompilerWorkflow extends AbstractCompilerWorkflow {
    private static final Logger LOG = LoggerFactory.getLogger(MbedSimCompilerWorkflow.class);

    public MbedSimCompilerWorkflow(PluginProperties pluginProperties) {
        super(pluginProperties);
    }

    @Override
    public void generateSourceCode(String token, String programName, Project data, ILanguage language) {
        if ( data.getErrorMessages().isEmpty() ) {
            try {
                this.generatedSourceCode = MbedStackMachineVisitor.generate(data.getConfigurationAst(), data.getProgramAst().getTree());
                this.crosscompilerResponse = "mbed simulation code generated";
                this.workflowResult = Key.COMPILERWORKFLOW_SUCCESS;
            } catch ( Exception e ) {
                this.crosscompilerResponse = "mbed simulation code generation failed";
                LOG.error(this.crosscompilerResponse, e);
                this.workflowResult = Key.COMPILERWORKFLOW_ERROR_PROGRAM_GENERATION_FAILED;
            }
        } else {
            this.crosscompilerResponse = "mbed simulation code generation with key " + data.getErrorMessages();
            this.workflowResult = Key.COMPILERWORKFLOW_ERROR_PROGRAM_GENERATION_FAILED;
        }
    }

    @Override
    public void compileSourceCode(String token, String programName, ILanguage language, Object flagProvider) {
        throw new DbcException("Operation not supported");
    }

    @Override
    public void generateSourceAndCompile(String token, String programName, Project transformer, ILanguage language) {
        throw new DbcException("Operation not supported");
    }

    @Override
    public ConfigurationAst generateConfiguration(IRobotFactory factory, String blocklyXml) throws Exception {
        BlockSet project = JaxbHelper.xml2BlockSet(blocklyXml);
        Jaxb2MbedConfigurationAst transformer = new Jaxb2MbedConfigurationAst(factory);
        return transformer.transform(project);
    }

    @Override
    public String getCompiledCode() {
        return null;
    }
}

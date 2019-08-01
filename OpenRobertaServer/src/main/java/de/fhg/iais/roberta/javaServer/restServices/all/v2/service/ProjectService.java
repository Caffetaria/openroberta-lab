package de.fhg.iais.roberta.javaServer.restServices.all.v2.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.codegen.ICompilerWorkflow;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.persistence.AbstractProcessor;
import de.fhg.iais.roberta.persistence.DummyProcessor;
import de.fhg.iais.roberta.persistence.ProcessorStatus;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.Statistics;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.visitor.validate.IWorker;

public class ProjectService {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectService.class);

    public ProjectResponse executeWorkflow(String workflowName, IRobotFactory robotFactory, Project project) throws JSONException, JAXBException {
        IRobotFactory factory = robotFactory;
        ICompilerWorkflow compilerWorkflow = factory.getRobotCompilerWorkflow();
        final JSONObject response = new JSONObject();
        Map<String, String> validationResults = new HashMap<>();
        final AbstractProcessor annotations = new DummyProcessor();
        String sourceCode = null;
        List<IWorker> workflowPipe = factory.getWorkerPipe(workflowName);

        for ( IWorker worker : workflowPipe ) {
            worker.execute(project);
        }

        Collection<Map<String, String>> results = project.getValidationResults().values();
        for ( Map<String, String> result : results ) {
            validationResults.putAll(result);
        }

        sourceCode = project.getSourceCode();
        ProcessorStatus status;
        if ( sourceCode == null || !validationResults.isEmpty() ) {
            status = ProcessorStatus.FAILED;
        } else {
            status = ProcessorStatus.SUCCEEDED;
        }
        annotations.setStatus(status, compilerWorkflow.getWorkflowResult(), validationResults);
        ProjectResponse projectResponse = new ProjectResponse();
        projectResponse.setProgramSourceCode(sourceCode);
        projectResponse.setProgramFileExtension(factory.getFileExtension());
        projectResponse.setProgramBlockSet(project.getAnnotatedProgramAsXml());
        projectResponse.setConfigurationBlockSet(project.getAnnotatedConfigurationAsXml());
        projectResponse.setAnnotations(annotations);
        Util.addResultInfo(response, annotations);
        Statistics.info("ProgramSource", "success", annotations.succeeded());
        return projectResponse;
    }
}

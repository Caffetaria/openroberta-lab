package de.fhg.iais.roberta.javaServer.restServices.all.v2.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.persistence.AbstractProcessor;
import de.fhg.iais.roberta.persistence.DummyProcessor;
import de.fhg.iais.roberta.persistence.ProcessorStatus;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Statistics;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.visitor.validate.IWorker;

public class ProjectService {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectService.class);

    public static String executeWorkflow(String workflowName, IRobotFactory robotFactory, Project project) throws JSONException, JAXBException {
        IRobotFactory factory = robotFactory;
        final JSONObject response = new JSONObject();
        Map<String, String> validationResults = new HashMap<>();
        final AbstractProcessor annotations = new DummyProcessor();
        String sourceCode = null;
        List<IWorker> workflowPipe = factory.getWorkerPipe(workflowName);

        for ( IWorker worker : workflowPipe ) {
            worker.execute(project);
            //if ( !project.getVisitorResults().isEmpty() ) { // TODO: here separators in the worker list should be used and not such a hard condition
            //    break;
            //}
        }

        /*Collection<Map<String, String>> results = project.getVisitorResults().values();
        for ( Map<String, String> result : results ) {
            validationResults.putAll(result);
        }
        */

        sourceCode = project.getSourceCode().toString();
        ProcessorStatus status;
        if ( sourceCode == null || !validationResults.isEmpty() ) {
            status = ProcessorStatus.FAILED;
            //message = (Key) project.getVisitorResults().keySet().toArray()[0]; //TODO: validator visitor should put errors in project, as well as compiler workflow
        } else {
            status = ProcessorStatus.SUCCEEDED;
        }
        annotations.setStatus(status, Key.COMPILERWORKFLOW_SUCCESS, validationResults);
        Util.addResultInfo(response, annotations);
        Statistics.info("ProgramSource", "success", annotations.succeeded());
        if ( status.equals(ProcessorStatus.FAILED) ) {
            return "error";
        } else {
            return "ok";
        }
    }
}

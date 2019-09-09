package de.fhg.iais.roberta.javaServer.restServices.all.v2.controller;

import java.sql.Timestamp;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.fhg.iais.roberta.javaServer.provider.OraData;
import de.fhg.iais.roberta.javaServer.restServices.all.v2.service.ProjectService;
import de.fhg.iais.roberta.mode.action.Language;
import de.fhg.iais.roberta.persistence.ProgramProcessor;
import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.ServerProperties;
import de.fhg.iais.roberta.util.Statistics;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.dbc.DbcException;

/*
existing commands:

- saveP
- saveAsP
- loadP
- importXML
- shareP
- shareWithGallery
- likeP
- shareDelete
- deleteP
- loadPN
- loadGallery
- loadProgramEntity
- loadEN
- loadPR
- compileN
- compileP
- runPsim
these need to be ported to standalone endpoints and checked for necessity
 */

@Path("/project")
public class ProjectRestController {
    private static final Logger LOG = LoggerFactory.getLogger(ProjectRestController.class);

    private final RobotCommunicator brickCommunicator;
    private final SessionFactoryWrapper sessionFactoryWrapper;

    @Inject
    public ProjectRestController(SessionFactoryWrapper sessionFactoryWrapper, RobotCommunicator brickCommunicator, ServerProperties serverProperties) {
        this.brickCommunicator = brickCommunicator;
        this.sessionFactoryWrapper = sessionFactoryWrapper;
    }

    @POST
    @Path("/source")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSourceCode(@OraData HttpSessionState httpSessionState, JSONObject request) {
        JSONObject dataPart;
        try {
            dataPart = request.getJSONObject("data");
        } catch ( JSONException e1 ) {
            throw new DbcException();
        }
        try {
            String configurationText = httpSessionState.getRobotFactory().getConfigurationDefault();
            Project project =
                new Project.Builder()
                    .setProgramName(dataPart.getString("programName"))
                    .setProgramXml(dataPart.getString("programBlockSet"))
                    .setConfigurationXml(dataPart.optString("configurationBlockSet", configurationText))
                    .setFileExtension(httpSessionState.getRobotFactory().getFileExtension())
                    .setSSID(dataPart.optString("SSID", null))
                    .setPassword(dataPart.optString("password", null))
                    .setLanguage(Language.findByAbbr(dataPart.optString("language")))
                    .setToken(httpSessionState.getToken())
                    .setRobot(dataPart.optString("robot", httpSessionState.getRobotName()))
                    .setFactory(httpSessionState.getRobotFactory())
                    .build();
            String status = ProjectService.executeWorkflow("showsource", httpSessionState.getRobotFactory(), project);
            JSONObject response = new JSONObject();
            // To make this compatible with old frontend we will have to use the old names...
            response.put("cmd", "showSourceP");
            response.put("sourceCode", project.getSourceCode());
            response.put("fileExtension", project.getFileExtension());
            response.put("data", project.getAnnotatedProgramAsXml());
            response.put("configuration", project.getAnnotatedConfigurationAsXml());
            response.put("rc", status);
            response.put("message", "ORA_" + project.getErrorMessage());
            response.put("cause", "ORA_" + project.getErrorMessage());
            response.put("parameters", /*project.getPinValidationResults()*/ "");
            return Response.ok(response).build();
        } catch ( JSONException | JAXBException e ) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @POST
    @Path("/binary")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response runProgram(@OraData HttpSessionState httpSessionState, JSONObject request) {
        JSONObject dataPart;
        try {
            dataPart = request.getJSONObject("data");
        } catch ( JSONException e1 ) {
            throw new DbcException();
        }
        try {
            String configurationText = httpSessionState.getRobotFactory().getConfigurationDefault();
            Project project =
                new Project.Builder()
                    .setProgramName(dataPart.getString("programName"))
                    .setProgramXml(dataPart.getString("programBlockSet"))
                    .setConfigurationXml(dataPart.optString("configurationBlockSet", configurationText))
                    .setFileExtension(httpSessionState.getRobotFactory().getFileExtension())
                    .setSSID(dataPart.optString("SSID", null))
                    .setPassword(dataPart.optString("password", null))
                    .setLanguage(Language.findByAbbr(dataPart.optString("language")))
                    .setToken(httpSessionState.getToken())
                    .setRobot(dataPart.optString("robot", httpSessionState.getRobotName()))
                    .setFactory(httpSessionState.getRobotFactory())
                    .build();
            String status = ProjectService.executeWorkflow("run", httpSessionState.getRobotFactory(), project);
            JSONObject response = new JSONObject();
            response.put("cmd", "runPBack");
            response.put("data", project.getAnnotatedProgramAsXml());
            response.put("errorCounter", 0);
            if ( this.brickCommunicator.getState(project.getToken()) != null ) {
                this.brickCommunicator.setSubtype(httpSessionState.getRobotName());
                this.brickCommunicator.theRunButtonWasPressed(project.getToken(), project.getProgramName());
            } else {
                response.put("compiledCode", project.getCompiledHex());
            }
            response.put("rc", status);
            return Response.ok(response).build();
        } catch ( JSONException | JAXBException e ) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProject(@OraData HttpSessionState httpSessionState, JSONObject request) {
        final int userId = httpSessionState.getUserId();
        final String robot =
            httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup() != ""
                ? httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup()
                : httpSessionState.getRobotName();
        final DbSession dbSession = this.sessionFactoryWrapper.getSession();
        final ProgramProcessor pp = new ProgramProcessor(dbSession, httpSessionState);
        final JSONObject response = new JSONObject();
        String programName = null;
        String programText = null;
        Long timestamp = null;
        Program program;
        try {
            programName = request.getString("programName");
            programText = request.getString("programText");
            timestamp = request.getLong("timestamp");
        } catch ( JSONException e1 ) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        final String configName = request.optString("configName", null);
        final String configText = request.optString("configText", null);
        final Timestamp programTimestamp = new Timestamp(timestamp);
        final boolean isShared = request.optBoolean("shared", false);
        program = pp.persistProgramText(programName, programText, configName, configText, userId, robot, userId, programTimestamp, !isShared);
        if ( pp.succeeded() && program != null ) {
            try {
                response.put("lastChanged", program.getLastChanged().getTime());
                Util.addResultInfo(response, pp);
            } catch ( JSONException e ) {
                e.printStackTrace();
            }
        }
        Statistics.info("ProgramSave", "success", pp.succeeded());
        return Response.ok(response).build();
    }

    @POST
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveProject(@OraData HttpSessionState httpSessionState, JSONObject request) {
        final int userId = httpSessionState.getUserId();
        final String robot =
            httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup() != ""
                ? httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup()
                : httpSessionState.getRobotName();
        final DbSession dbSession = this.sessionFactoryWrapper.getSession();
        final ProgramProcessor pp = new ProgramProcessor(dbSession, httpSessionState);
        final JSONObject response = new JSONObject();
        String programName = null;
        String programText = null;
        try {
            programName = request.getString("programName");
            programText = request.getString("programText");
        } catch ( JSONException e1 ) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        final String configName = request.optString("configName", null);
        final String configText = request.optString("configText", null);

        Program program;
        program = pp.persistProgramText(programName, programText, configName, configText, userId, robot, userId, null, true);
        if ( pp.succeeded() && program != null ) {
            try {
                response.put("lastChanged", program.getLastChanged().getTime());
                Util.addResultInfo(response, pp);
            } catch ( JSONException e ) {
                e.printStackTrace();
            }
        }
        Statistics.info("ProgramSave", "success", pp.succeeded());
        return Response.ok(response).build();
    }

    @POST
    @Path("/check")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAnnotatedProject(@OraData HttpSessionState httpSessionState, JSONObject request) {
        Project project = new Project.Builder().build();
        try {
            String projectResponse = ProjectService.executeWorkflow("annotate", httpSessionState.getRobotFactory(), project);
        } catch ( JSONException | JAXBException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        JSONObject response = new JSONObject();
        return Response.ok(response).build();
    }
}

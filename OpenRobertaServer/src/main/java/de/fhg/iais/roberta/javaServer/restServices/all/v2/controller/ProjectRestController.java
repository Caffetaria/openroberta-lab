package de.fhg.iais.roberta.javaServer.restServices.all.v2.controller;

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
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.ServerProperties;
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
- runP
- compileN
- compileP
- runPBack
- runPsim
these need to be ported to standalone endpoints and checked for necessity
 */

@Path("/project")
public class ProjectRestController {
    private static final Logger LOG = LoggerFactory.getLogger(ProjectRestController.class);

    ProjectService projectService = new ProjectService();

    private final SessionFactoryWrapper sessionFactoryWrapper;
    private final RobotCommunicator brickCommunicator;
    private final boolean isPublicServer;

    @Inject
    public ProjectRestController(SessionFactoryWrapper sessionFactoryWrapper, RobotCommunicator brickCommunicator, ServerProperties serverProperties) {
        this.sessionFactoryWrapper = sessionFactoryWrapper;
        this.brickCommunicator = brickCommunicator;
        this.isPublicServer = serverProperties.getBooleanProperty("server.public");
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
                    .setProgramBlockSet(dataPart.getString("programBlockSet"))
                    .setConfigurationName(dataPart.optString("configurationName", null))
                    .setConfigurationBlockSet(dataPart.optString("configurationBlockSet", configurationText))
                    .setFileExtension(httpSessionState.getRobotFactory().getFileExtension())
                    .setSSID(dataPart.optString("SSID", null))
                    .setPassword(dataPart.optString("password", null))
                    .setLanguage(Language.findByAbbr(dataPart.optString("language")))
                    .setToken(httpSessionState.getToken())
                    .setRobot(dataPart.optString("robot", null))
                    .setFactory(httpSessionState.getRobotFactory())
                    .build();
            String status = this.projectService.executeWorkflow("showsource", httpSessionState.getRobotFactory(), project);
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
            response.put("parameters", project.getPinValidationResults());
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
                    .setProgramBlockSet(dataPart.getString("programBlockSet"))
                    .setConfigurationName(dataPart.optString("configurationName", null))
                    .setConfigurationBlockSet(dataPart.optString("configurationBlockSet", configurationText))
                    .setFileExtension(httpSessionState.getRobotFactory().getFileExtension())
                    .setSSID(dataPart.optString("SSID", null))
                    .setPassword(dataPart.optString("password", null))
                    .setLanguage(Language.findByAbbr(dataPart.optString("language")))
                    .setToken(httpSessionState.getToken())
                    .setRobot(dataPart.optString("robot", null))
                    .setFactory(httpSessionState.getRobotFactory())
                    .build();
            String status = this.projectService.executeWorkflow("run", httpSessionState.getRobotFactory(), project);
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
            response.put("rc", "ok");
            return Response.ok(response).build();
        } catch ( JSONException | JAXBException e ) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @POST
    @Path("/check")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAnnotatedProject(@OraData HttpSessionState httpSessionState, JSONObject request) {
        Project project = new Project.Builder().build();
        try {
            String projectResponse = this.projectService.executeWorkflow("annotate", httpSessionState.getRobotFactory(), project);
        } catch ( JSONException | JAXBException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        JSONObject response = new JSONObject();
        return Response.ok(response).build();
    }
}

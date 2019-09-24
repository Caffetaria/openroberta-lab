package de.fhg.iais.roberta.javaServer.restServices.all.controller;

import com.google.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;

import de.fhg.iais.roberta.javaServer.provider.OraData;
import de.fhg.iais.roberta.javaServer.restServices.all.service.ProjectService;
import de.fhg.iais.roberta.mode.action.Language;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Statistics;
import de.fhg.iais.roberta.util.dbc.DbcException;

@Path("/projectWorkflow")
public class ProjectWorkflowRestController {
    private static final Logger LOG = LoggerFactory.getLogger(ProjectWorkflowRestController.class);

    private final RobotCommunicator brickCommunicator;

    @Inject
    public ProjectWorkflowRestController(RobotCommunicator brickCommunicator) {
        this.brickCommunicator = brickCommunicator;
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
            throw new DbcException("Invalid JSON object: data not found", e1);
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
            ProjectService.executeWorkflow("showsource", httpSessionState.getRobotFactory(), project);
            JSONObject response = new JSONObject();
            // To make this compatible with old frontend we will have to use the old names...
            response.put("cmd", "showSourceP");
            response.put("sourceCode", project.getSourceCode());
            response.put("fileExtension", project.getFileExtension());
            response.put("data", project.getAnnotatedProgramAsXml());
            response.put("configuration", project.getAnnotatedConfigurationAsXml());
            response.put("rc", project.hasSucceeded() ? "ok" : "error");
            response.put("message", project.getResult().getKey());
            response.put("cause", project.getResult().getKey());
            response.put("parameters", project.getResultParams());
            Statistics.info("ProgramSource", "success", project.hasSucceeded());
            return Response.ok(response).build();
        } catch ( JSONException | JAXBException e ) {
            LOG.info("getSourceCode failed", e);
            Statistics.info("ProgramSource", "success", false);
            return Response.serverError().build();
        }
    }

    @POST
    @Path("/source/simulation")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSimulationVMCode(@OraData HttpSessionState httpSessionState, JSONObject request) {
        JSONObject dataPart;
        try {
            dataPart = request.getJSONObject("data");
        } catch ( JSONException e1 ) {
            throw new DbcException("Invalid JSON object: data not found", e1);
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
            ProjectService.executeWorkflow("getsimulationcode", httpSessionState.getRobotFactory(), project);
            JSONObject response = new JSONObject();
            // To make this compatible with old frontend we will have to use the old names...
            response.put("cmd", "runPSim");
            response.put("javaScriptProgram", project.getSourceCode());
            response.put("fileExtension", project.getFileExtension());
            response.put("data", project.getAnnotatedProgramAsXml());
            response.put("configuration", project.getAnnotatedConfigurationAsXml());
            response.put("rc", project.hasSucceeded() ? "ok" : "error");
            response.put("message", project.getResult().getKey());
            response.put("cause", project.getResult().getKey());
            response.put("parameters", project.getResultParams());
            Statistics.info("SimulationRun", "LoggedIn", httpSessionState.isUserLoggedIn(), "success", project.hasSucceeded());
            return Response.ok(response).build();
        } catch ( JSONException | JAXBException e ) {
            LOG.info("getSimulationVMCode failed", e);
            Statistics.info("SimulationRun", "LoggedIn", httpSessionState.isUserLoggedIn(),  "success", true);
            return Response.serverError().build();
        }
    }

    @POST
    @Path("/binary") // TODO rename
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response runProgram(@OraData HttpSessionState httpSessionState, JSONObject request) {
        JSONObject dataPart;
        try {
            dataPart = request.getJSONObject("data");
        } catch ( JSONException e1 ) {
            throw new DbcException("Invalid JSON object: data not found", e1);
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
                    .setRobotCommunicator(this.brickCommunicator)
                    .build();
            ProjectService.executeWorkflow("run", httpSessionState.getRobotFactory(), project);
            JSONObject response = new JSONObject();
            response.put("cmd", "runPBack");
            response.put("data", project.getAnnotatedProgramAsXml());
            response.put("errorCounter", 0); //TODO
            response.put("parameters", project.getResultParams());
            response.put("compiledCode", project.getCompiledHex());
            response.put("message", project.getResult().getKey());
            response.put("cause", project.getResult().getKey());
            response.put("rc", project.hasSucceeded() ? "ok" : "error");
            Statistics.info(
                "ProgramRun",
                "LoggedIn",
                httpSessionState.isUserLoggedIn(),
                "success",
                project.hasSucceeded(),
                "programLength",
                StringUtils.countMatches(project.getAnnotatedProgramAsXml(), "<block "));
            return Response.ok(response).build();
        } catch ( JSONException | JAXBException e ) {
            LOG.info("runProgram failed", e);
            Statistics.info("ProgramRun", "LoggedIn", httpSessionState.isUserLoggedIn(), "success", false);
            return Response.serverError().build();
        }
    }

    @POST
    @Path("/runNative")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response runNative(@OraData HttpSessionState httpSessionState, JSONObject request) {
        JSONObject dataPart;
        try {
            dataPart = request.getJSONObject("data");
        } catch ( JSONException e1 ) {
            throw new DbcException("Invalid JSON object: data not found", e1);
        }
        try {
            Project project =
                new Project.Builder()
                    .setProgramName(dataPart.getString("programName"))
                    .setProgramNativeSource(dataPart.getString("programText"))
                    .setFileExtension(httpSessionState.getRobotFactory().getFileExtension())
                    .setSSID(dataPart.optString("SSID", null))
                    .setPassword(dataPart.optString("password", null))
                    .setLanguage(Language.findByAbbr(dataPart.optString("language")))
                    .setToken(httpSessionState.getToken())
                    .setRobot(dataPart.optString("robot", httpSessionState.getRobotName()))
                    .setFactory(httpSessionState.getRobotFactory())
                    .build();
            ProjectService.executeWorkflow("runnative", httpSessionState.getRobotFactory(), project);
            JSONObject response = new JSONObject();
            response.put("cmd", "runNative");
            response.put("errorCounter", 0);
            response.put("parameters", project.getResultParams());
            response.put("compiledCode", project.getCompiledHex());
            response.put("message", project.getResult().getKey());
            response.put("cause", project.getResult().getKey());
            response.put("rc", project.hasSucceeded() ? "ok" : "error");
            // TODO: rename to ProgramRunNative?
            Statistics.info("ProgramCompileNative", "LoggedIn", httpSessionState.isUserLoggedIn(), "success", project.hasSucceeded());
            return Response.ok(response).build();
        } catch ( JSONException | JAXBException e ) {
            LOG.info("runNative failed", e);
            Statistics.info("ProgramCompileNative", "LoggedIn", httpSessionState.isUserLoggedIn(), "success", false);
            return Response.serverError().build();
        }
    }

    @POST
    @Path("/compileProgram")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response compileProgram(@OraData HttpSessionState httpSessionState, JSONObject request) {
        JSONObject dataPart;
        try {
            dataPart = request.getJSONObject("data");
        } catch ( JSONException e1 ) {
            throw new DbcException("Invalid JSON object: data not found", e1);
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
            ProjectService.executeWorkflow("run", httpSessionState.getRobotFactory(), project);
            JSONObject response = new JSONObject();
            response.put("cmd", "compileP");
            response.put("data", project.getAnnotatedProgramAsXml());
            response.put("errorCounter", 0);
            response.put("message", project.getResult().getKey());
            response.put("cause", project.getResult().getKey());
            response.put("parameters", project.getResultParams());
            response.put("compiledCode", project.getCompiledHex());
            Statistics.info("ProgramCompile", "LoggedIn", httpSessionState.isUserLoggedIn(), "success", project.hasSucceeded(), "programLength",
                            StringUtils.countMatches(project.getAnnotatedProgramAsXml(), "<block "));
            response.put("rc", project.hasSucceeded() ? "ok" : "error");
            return Response.ok(response).build();
        } catch ( JSONException | JAXBException e ) {
            LOG.info("compileProgram failed", e);
            Statistics.info("ProgramCompile", "LoggedIn", httpSessionState.isUserLoggedIn(), "success", false);
            return Response.serverError().build();
        }
    }
}

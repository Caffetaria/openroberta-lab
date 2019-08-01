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

import de.fhg.iais.roberta.javaServer.provider.OraData;
import de.fhg.iais.roberta.javaServer.restServices.all.v2.service.ProjectResponse;
import de.fhg.iais.roberta.javaServer.restServices.all.v2.service.ProjectService;
import de.fhg.iais.roberta.mode.action.Language;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.dbc.DbcException;

@Path("/project")
public class ProjectRestController {

    ProjectService projectService = new ProjectService();

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
                    .setConfigurationBlockSet(dataPart.optString("configurationBlockSet", configurationText))
                    .setConfigurationName(dataPart.optString("configurationName", null))
                    .setSSID(dataPart.optString("SSID", null))
                    .setPassword(dataPart.optString("password", null))
                    .setLanguage(Language.findByAbbr(dataPart.optString("language")))
                    .setToken(httpSessionState.getToken())
                    .setRobot(dataPart.optString("robot", null))
                    .setFactory(httpSessionState.getRobotFactory())
                    .build();
            ProjectResponse projectResponse = this.projectService.executeWorkflow("showsource", httpSessionState.getRobotFactory(), project);
            JSONObject response = new JSONObject();
            // To make this compatible with old frontend we will have to use the old names...
            response.put("cmd", "showSourceP");
            response.put("sourceCode", projectResponse.getProgramSourceCode());
            response.put("fileExtension", projectResponse.getProgramFileExtension());
            response.put("data", projectResponse.getProgramBlockSet());
            response.put("configuration", projectResponse.getConfigurationBlockSet());
            response.put("rc", "ok");
            response.put("message", "ORA_" + projectResponse.getAnnotations().getMessage());
            response.put("cause", "ORA_" + projectResponse.getAnnotations().getMessage());
            response.put("parameters", projectResponse.getAnnotations().getParameters());
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
            ProjectResponse projectResponse = this.projectService.executeWorkflow("annotate", httpSessionState.getRobotFactory(), project);
        } catch ( JSONException | JAXBException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        JSONObject response = new JSONObject();
        return Response.ok(response).build();
    }
}

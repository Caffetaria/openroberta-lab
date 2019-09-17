package de.fhg.iais.roberta.javaServer.restServices.all.v2.controller;

import java.sql.Timestamp;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.fhg.iais.roberta.blockly.generated.Export;
import de.fhg.iais.roberta.javaServer.provider.OraData;
import de.fhg.iais.roberta.javaServer.restServices.all.v2.service.ProjectService;
import de.fhg.iais.roberta.mode.action.Language;
import de.fhg.iais.roberta.persistence.AccessRightProcessor;
import de.fhg.iais.roberta.persistence.ConfigurationProcessor;
import de.fhg.iais.roberta.persistence.LikeProcessor;
import de.fhg.iais.roberta.persistence.ProgramProcessor;
import de.fhg.iais.roberta.persistence.UserProcessor;
import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.dao.ConfigurationDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.ServerProperties;
import de.fhg.iais.roberta.util.Statistics;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;

@Path("/project")
public class ProjectRestController {
    private static final Logger LOG = LoggerFactory.getLogger(ProjectRestController.class);

    private final RobotCommunicator brickCommunicator;
    private final SessionFactoryWrapper sessionFactoryWrapper;
    private final boolean isPublicServer;

    @Inject
    public ProjectRestController(SessionFactoryWrapper sessionFactoryWrapper, RobotCommunicator brickCommunicator, ServerProperties serverProperties) {
        this.brickCommunicator = brickCommunicator;
        this.sessionFactoryWrapper = sessionFactoryWrapper;
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
    @Path("/source/simulation")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSimulationVMCode(@OraData HttpSessionState httpSessionState, JSONObject request) {
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
            String status = ProjectService.executeWorkflow("getsimulationcode", httpSessionState.getRobotFactory(), project);
            JSONObject response = new JSONObject();
            // To make this compatible with old frontend we will have to use the old names...
            response.put("cmd", "runPSim");
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
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProject(@OraData HttpSessionState httpSessionState, JSONObject request) {
        JSONObject dataPart;
        try {
            dataPart = request.getJSONObject("data");
        } catch ( JSONException e1 ) {
            throw new DbcException();
        }
        final int userId = httpSessionState.getUserId();
        final String robot =
            httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup() != ""
                ? httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup()
                : httpSessionState.getRobotName();
        final DbSession dbSession = this.sessionFactoryWrapper.getSession();
        final ProgramProcessor programProcessor = new ProgramProcessor(dbSession, httpSessionState);
        final JSONObject response = new JSONObject();
        String programName = null;
        String programText = null;
        Long timestamp = null;
        Program program;
        Timestamp programTimestamp = null;
        try {
            timestamp = dataPart.getLong("timestamp");
            programTimestamp = new Timestamp(timestamp);
        } catch ( JSONException e2 ) {
            programTimestamp = null;
        }
        try {
            programName = dataPart.getString("programName");
            programText = dataPart.getString("programText");
        } catch ( JSONException e1 ) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        final String configName = dataPart.optString("configName", null);
        final String configText = dataPart.optString("configText", null);
        final boolean isShared = dataPart.optBoolean("shared", false);
        if ( isShared ) {
            programTimestamp = null;
        }
        program = programProcessor.persistProgramText(programName, programText, configName, configText, userId, robot, userId, programTimestamp, !isShared);
        if ( programProcessor.succeeded() && program != null ) {
            try {
                response.put("lastChanged", program.getLastChanged().getTime());
                Util.addResultInfo(response, programProcessor);
            } catch ( JSONException e ) {
                e.printStackTrace();
            }
        }
        dbSession.commit();
        if ( dbSession != null ) {
            dbSession.close();
        }
        Statistics.info("ProgramSave", "success", programProcessor.succeeded());
        return Response.ok(response).build();
    }

    @POST
    @Path("/listing")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProgram(@OraData HttpSessionState httpSessionState, JSONObject request) {
        JSONObject dataPart;
        try {
            dataPart = request.getJSONObject("data");
        } catch ( JSONException e1 ) {
            throw new DbcException();
        }
        final JSONObject response = new JSONObject();
        try {
            if ( !httpSessionState.isUserLoggedIn() && !dataPart.getString("owner").equals("Roberta") && !dataPart.getString("owner").equals("Gallery") ) {
                LOG.info("Unauthorized load request");
                Util.addErrorInfo(response, Key.USER_ERROR_NOT_LOGGED_IN);
            } else {
                final String programName = dataPart.getString("name");
                final String ownerName = dataPart.getString("owner");
                final String authorName = dataPart.getString("authorName");
                final String robot =
                    httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup() != ""
                        ? httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup()
                        : httpSessionState.getRobotName();
                final DbSession dbSession = this.sessionFactoryWrapper.getSession();
                final ProgramProcessor programProcessor = new ProgramProcessor(dbSession, httpSessionState);
                final Program program = programProcessor.getProgram(programName, ownerName, robot, authorName);
                if ( program != null ) {
                    response.put("programText", program.getProgramText());
                    final String configText = programProcessor.getProgramsConfig(program);
                    response.put("configName", program.getConfigName()); // may be null, if an anonymous configuration is used
                    response.put("configText", configText); // may be null, if the default configuration is used
                    response.put("lastChanged", program.getLastChanged().getTime());
                    // count the views if the program is from the gallery!
                    if ( ownerName.equals("Gallery") ) {
                        programProcessor.addOneView(program);
                    }
                }
                Util.addResultInfo(response, programProcessor);
                Statistics.info("ProgramLoad", "success", programProcessor.succeeded());
            }
        } catch ( JSONException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Response.ok(response).build();
    }

    @POST
    @Path("/listing/names")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProgramNames(@OraData HttpSessionState httpSessionState, JSONObject request) {
        final String robot =
            httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup() != ""
                ? httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup()
                : httpSessionState.getRobotName();
        final JSONObject response = new JSONObject();
        if ( !httpSessionState.isUserLoggedIn() ) {
            LOG.error("Unauthorized");
            try {
                Util.addErrorInfo(response, Key.USER_ERROR_NOT_LOGGED_IN);
            } catch ( JSONException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            final DbSession dbSession = this.sessionFactoryWrapper.getSession();
            final ProgramProcessor programProcessor = new ProgramProcessor(dbSession, httpSessionState);
            final int userId = httpSessionState.getUserId();
            final JSONArray programInfo = programProcessor.getProgramInfo(userId, robot, userId);
            try {
                response.put("programNames", programInfo);
                Util.addResultInfo(response, programProcessor);
            } catch ( JSONException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return Response.ok(response).build();
    }

    @POST
    @Path("/examples/names")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProgramExampleNames(@OraData HttpSessionState httpSessionState, JSONObject request) {
        final String robot =
            httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup() != ""
                ? httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup()
                : httpSessionState.getRobotName();
        final JSONObject response = new JSONObject();
        if ( !httpSessionState.isUserLoggedIn() ) {
            LOG.error("Unauthorized");
            try {
                Util.addErrorInfo(response, Key.USER_ERROR_NOT_LOGGED_IN);
            } catch ( JSONException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            final DbSession dbSession = this.sessionFactoryWrapper.getSession();
            final ProgramProcessor programProcessor = new ProgramProcessor(dbSession, httpSessionState);
            final int userId = 1;
            final JSONArray programInfo = programProcessor.getProgramInfo(userId, robot, userId);
            try {
                response.put("programNames", programInfo);
                Util.addResultInfo(response, programProcessor);
            } catch ( JSONException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return Response.ok(response).build();
    }

    @POST
    @Path("/entity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProgramEntity(@OraData HttpSessionState httpSessionState, JSONObject request) {
        final String robot =
            httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup() != ""
                ? httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup()
                : httpSessionState.getRobotName();
        final JSONObject response = new JSONObject();
        if ( !httpSessionState.isUserLoggedIn() ) {
            LOG.error("Unauthorized");
            try {
                Util.addErrorInfo(response, Key.USER_ERROR_NOT_LOGGED_IN);
            } catch ( JSONException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            final DbSession dbSession = this.sessionFactoryWrapper.getSession();
            final ProgramProcessor programProcessor = new ProgramProcessor(dbSession, httpSessionState);
            final UserProcessor up = new UserProcessor(dbSession, httpSessionState);
            String programName;
            try {
                programName = request.getString("name");
                String ownerName = request.getString("owner");
                String authorName = request.getString("author");
                final User owner = up.getUser(ownerName);
                final int ownerID = owner.getId();
                final int authorId = up.getUser(authorName).getId();
                final JSONArray program = programProcessor.getProgramEntity(programName, ownerID, robot, authorId);
                if ( program != null ) {
                    response.put("program", program);
                }
                Util.addResultInfo(response, programProcessor);
            } catch ( JSONException e1 ) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        return Response.ok(response).build();
    }

    @POST
    @Path("/gallery")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGallery(@OraData HttpSessionState httpSessionState, JSONObject request) {
        final DbSession dbSession = this.sessionFactoryWrapper.getSession();
        final ProgramProcessor programProcessor = new ProgramProcessor(dbSession, httpSessionState);
        final int userId = httpSessionState.getUserId();
        final JSONObject response = new JSONObject();
        final JSONArray programInfo = programProcessor.getProgramGallery(userId);
        try {
            response.put("programNames", programInfo);
            Util.addResultInfo(response, programProcessor);
        } catch ( JSONException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Statistics.info("GalleryView", "success", programProcessor.succeeded());
        return Response.ok(response).build();
    }

    @POST
    @Path("/import")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response importProgram(@OraData HttpSessionState httpSessionState, JSONObject request) {
        JSONObject dataPart;
        try {
            dataPart = request.getJSONObject("data");
        } catch ( JSONException e1 ) {
            throw new DbcException();
        }
        final JSONObject response = new JSONObject();
        final String robot =
            httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup() != ""
                ? httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup()
                : httpSessionState.getRobotName();
        String xmlText;
        try {
            xmlText = dataPart.getString("program");
            xmlText = Util.checkProgramTextForXSS(xmlText);
            if ( xmlText.contains("robottype=\"ardu\"") ) {
                xmlText = xmlText.replaceAll("robottype=\"ardu\"", "robottype=\"botnroll\"");
                LOG.warn("Ardu to botnroll renaming on import should be removed in future.");
            }
            String programName = dataPart.getString("name");
            if ( !Util1.isValidJavaIdentifier(programName) ) {
                programName = "NEPOprog";
            }

            Export jaxbImportExport = null;
            try {
                jaxbImportExport = JaxbHelper.xml2Element(xmlText, Export.class);
            } catch ( final UnmarshalException | org.xml.sax.SAXException e ) {
                jaxbImportExport = null;
            }
            if ( jaxbImportExport != null ) {
                final String robotType1 = jaxbImportExport.getProgram().getBlockSet().getRobottype();
                final String robotType2 = jaxbImportExport.getConfig().getBlockSet().getRobottype();
                if ( robotType1.equals(robot) && robotType2.equals(robot) ) {
                    response.put("programName", programName);
                    response.put("programText", JaxbHelper.blockSet2xml(jaxbImportExport.getProgram().getBlockSet()));
                    response.put("configText", JaxbHelper.blockSet2xml(jaxbImportExport.getConfig().getBlockSet()));
                    Util.addSuccessInfo(response, Key.PROGRAM_IMPORT_SUCCESS);
                    Statistics.info("ProgramImport", "success", true);
                } else {
                    Util.addErrorInfo(response, Key.PROGRAM_IMPORT_ERROR_WRONG_ROBOT_TYPE);
                    Statistics.info("ProgramImport", "success", false);
                }
            } else {
                Util.addErrorInfo(response, Key.PROGRAM_IMPORT_ERROR);
            }
        } catch ( Exception e1 ) { //TODO: this is bad, do not catch all exceptions.
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return Response.ok(response).build();
    }

    @POST
    @Path("/share")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response shareProgram(@OraData HttpSessionState httpSessionState, JSONObject request) {
        JSONObject response = new JSONObject();
        final DbSession dbSession = this.sessionFactoryWrapper.getSession();
        final UserProcessor userProcessor = new UserProcessor(dbSession, httpSessionState);
        final AccessRightProcessor accessRightProcessor = new AccessRightProcessor(dbSession, httpSessionState);
        final int userId = httpSessionState.getUserId();
        final String robot =
            httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup() != ""
                ? httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup()
                : httpSessionState.getRobotName();
        try {
            if ( !httpSessionState.isUserLoggedIn() ) {
                LOG.error("Unauthorized");
                Util.addErrorInfo(response, Key.USER_ERROR_NOT_LOGGED_IN);

            } else {
                final User user = userProcessor.getUser(userId);
                if ( !this.isPublicServer || user != null && user.isActivated() ) {
                    final String programName = request.getString("programName");
                    final String userToShareName = request.getString("userToShare");
                    final String right = request.getString("right");
                    accessRightProcessor.shareToUser(userId, robot, programName, userId, userToShareName, right);
                    Util.addResultInfo(response, accessRightProcessor);
                    Statistics.info("ProgramShare", "success", accessRightProcessor.succeeded());
                } else {
                    Util.addErrorInfo(response, Key.ACCOUNT_NOT_ACTIVATED_TO_SHARE);
                }
            }
        } catch ( Exception e ) { //TODO: this is bad, do not catch all exceptions.
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Response.ok(response).build();
    }

    @POST
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteProject(@OraData HttpSessionState httpSessionState, JSONObject request) {
        JSONObject response = new JSONObject();
        final int userId = httpSessionState.getUserId();
        final String robot =
            httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup() != ""
                ? httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup()
                : httpSessionState.getRobotName();
        final DbSession dbSession = this.sessionFactoryWrapper.getSession();
        final ProgramProcessor programProcessor = new ProgramProcessor(dbSession, httpSessionState);
        if ( !httpSessionState.isUserLoggedIn() ) {
            LOG.error("Unauthorized");
            try {
                Util.addErrorInfo(response, Key.USER_ERROR_NOT_LOGGED_IN);
            } catch ( JSONException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Statistics.info("ProgramDelete", "success", false);
        } else {
            String programName;
            String author;
            try {
                programName = request.getString("name");
                author = request.getString("author");
                programProcessor.deleteByName(programName, userId, robot, author);
                Util.addResultInfo(response, programProcessor);
            } catch ( JSONException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Statistics.info("ProgramDelete", "success", programProcessor.succeeded());
        }
        return Response.ok(response).build();
    }

    @POST
    @Path("/like")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response likeProject(@OraData HttpSessionState httpSessionState, JSONObject request) {
        JSONObject response = new JSONObject();
        final DbSession dbSession = this.sessionFactoryWrapper.getSession();
        final LikeProcessor lp = new LikeProcessor(dbSession, httpSessionState);
        if ( !httpSessionState.isUserLoggedIn() ) {
            LOG.error("Unauthorized");
            try {
                Util.addErrorInfo(response, Key.USER_ERROR_NOT_LOGGED_IN);
            } catch ( JSONException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Statistics.info("GalleryLike", "success", false);
        } else {
            String programName;
            String robotName;
            String authorName;
            boolean like;
            try {
                programName = request.getString("programName");
                robotName = request.getString("robotName");
                authorName = request.getString("authorName");
                like = request.getBoolean("like");
                if ( like ) {
                    lp.createLike(programName, robotName, authorName);
                    if ( lp.succeeded() ) {
                        // nothing to do
                        //argument: deleted tracks whether a like was set or taken away
                        Statistics.info("GalleryLike", "success", true, "deleted", false);
                    } else {
                        Util.addErrorInfo(response, Key.LIKE_SAVE_ERROR_EXISTS);
                        Statistics.info("GalleryLike", "success", false);
                    }
                } else {
                    lp.deleteLike(programName, robotName, authorName);
                    Statistics.info("GalleryLike", "success", true, "deleted", true);
                }
                Util.addResultInfo(response, lp);
            } catch ( Exception e ) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return Response.ok(response).build();
    }

    @POST
    @Path("/share/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteProjectShare(@OraData HttpSessionState httpSessionState, JSONObject request) {
        JSONObject response = new JSONObject();
        final DbSession dbSession = this.sessionFactoryWrapper.getSession();
        final int userId = httpSessionState.getUserId();
        final String robot =
            httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup() != ""
                ? httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup()
                : httpSessionState.getRobotName();
        final ProgramProcessor programProcessor = new ProgramProcessor(dbSession, httpSessionState);
        final AccessRightProcessor accessRightProcessor = new AccessRightProcessor(dbSession, httpSessionState);
        final UserProcessor userProcessor = new UserProcessor(dbSession, httpSessionState);
        if ( !httpSessionState.isUserLoggedIn() ) {
            LOG.error("Unauthorized");
            Statistics.info("ProgramShareDelete", "success", false);
            try {
                Util.addErrorInfo(response, Key.USER_ERROR_NOT_LOGGED_IN);
            } catch ( JSONException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            try {
                final String programName = request.getString("programName");
                final String owner = request.getString("owner");
                final String author = request.getString("author");
                accessRightProcessor.shareDelete(owner, robot, programName, author, userId);
                Util.addResultInfo(response, accessRightProcessor);
                // if this program was shared from the gallery we need to delete the copy of it as well
                if ( owner.equals("Gallery") ) {
                    final int ownerId = userProcessor.getUser(owner).getId();
                    programProcessor.deleteByName(programName, ownerId, robot, userId);
                    Statistics.info("ProgramShareDelete", "success", true);
                    Util.addResultInfo(response, programProcessor);
                }
            } catch ( JSONException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return Response.ok(response).build();
    }

    @POST
    @Path("/share/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createProjectShare(@OraData HttpSessionState httpSessionState, JSONObject request) {
        JSONObject response = new JSONObject();
        final DbSession dbSession = this.sessionFactoryWrapper.getSession();
        final int userId = httpSessionState.getUserId();
        final String robot =
            httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup() != ""
                ? httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup()
                : httpSessionState.getRobotName();
        final ProgramProcessor programProcessor = new ProgramProcessor(dbSession, httpSessionState);
        final AccessRightProcessor accessRightProcessor = new AccessRightProcessor(dbSession, httpSessionState);
        final UserProcessor userProcessor = new UserProcessor(dbSession, httpSessionState);
        final ConfigurationProcessor configurationProcessor = new ConfigurationProcessor(dbSession, httpSessionState);
        if ( !httpSessionState.isUserLoggedIn() ) {
            LOG.error("Unauthorized");
            try {
                Util.addErrorInfo(response, Key.USER_ERROR_NOT_LOGGED_IN);
            } catch ( JSONException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            try {
                final String programName = request.getString("programName");
                final int galleryId = userProcessor.getUser("Gallery").getId();
                // generating a unique name for the program owned by the gallery.
                final User user = userProcessor.getUser(userId);
                final String userAccount = user.getAccount();
                if ( !this.isPublicServer || user != null && user.isActivated() ) {
                    // get the program from the origin user to share with the gallery
                    final Program program = programProcessor.getProgram(programName, userAccount, robot, userAccount);
                    String confText;
                    if ( program != null ) {
                        if ( program.getConfigName() == null ) {
                            if ( program.getConfigHash() == null ) {
                                confText = null;
                            } else {
                                final ConfigurationDao confDao = new ConfigurationDao(dbSession);
                                confText = confDao.load(program.getConfigHash()).getConfigurationText();
                            }
                        } else {
                            confText = configurationProcessor.getConfigurationText(program.getConfigName(), userId, robot);
                        }
                        // make a copy of the user program and store it as a gallery owned program
                        final Program programCopy =
                            programProcessor.persistProgramText(programName, program.getProgramText(), null, confText, galleryId, robot, userId, null, true);
                        if ( programProcessor.succeeded() ) {
                            if ( programCopy != null ) {
                                response.put("lastChanged", programCopy.getLastChanged().getTime());
                                // share the copy of the program with the origin user
                                accessRightProcessor.shareToUser(galleryId, robot, programName, userId, userAccount, "X_WRITE");
                            } else {
                                LOG.error("TODO: check potential error: the saved program should never be null");
                            }
                            Util.addSuccessInfo(response, Key.GALLERY_UPLOAD_SUCCESS);
                            Statistics.info("GalleryShare", "success", true);
                        } else {
                            Util.addErrorInfo(response, Key.GALLERY_UPLOAD_ERROR);
                            Statistics.info("GalleryShare", "success", false);
                        }
                    } else {
                        Util.addErrorInfo(response, Key.GALLERY_UPLOAD_ERROR);
                        Statistics.info("GalleryShare", "success", false);
                    }
                } else {
                    Util.addErrorInfo(response, Key.ACCOUNT_NOT_ACTIVATED_TO_SHARE);
                    Statistics.info("GalleryShare", "success", false);
                }
            } catch ( Exception e ) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return Response.ok(response).build();
    }

    @POST
    @Path("/relations")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProjectRelations(@OraData HttpSessionState httpSessionState, JSONObject request) {
        JSONObject response = new JSONObject();
        final DbSession dbSession = this.sessionFactoryWrapper.getSession();
        final ProgramProcessor programProcessor = new ProgramProcessor(dbSession, httpSessionState);
        final int userId = httpSessionState.getUserId();
        final String robot =
            httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup() != ""
                ? httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup()
                : httpSessionState.getRobotName();
        try {
            if ( !httpSessionState.isUserLoggedIn() ) {
                LOG.error("Unauthorized");
                Util.addErrorInfo(response, Key.USER_ERROR_NOT_LOGGED_IN);
            } else {
                final String programName = request.getString("name");
                final JSONArray relations = programProcessor.getProgramRelations(programName, userId, robot, userId);
                response.put("relations", relations);
                Util.addResultInfo(response, programProcessor);
            }
        } catch ( JSONException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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

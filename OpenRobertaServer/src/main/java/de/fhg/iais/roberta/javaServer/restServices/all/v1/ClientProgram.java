package de.fhg.iais.roberta.javaServer.restServices.all.v1;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.UnmarshalException;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.blockly.generated.Export;
import de.fhg.iais.roberta.blockly.generated.Instance;
import de.fhg.iais.roberta.codegen.ICompilerWorkflow;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.javaServer.provider.OraData;
import de.fhg.iais.roberta.mode.action.Language;
import de.fhg.iais.roberta.persistence.AccessRightProcessor;
import de.fhg.iais.roberta.persistence.ConfigurationProcessor;
import de.fhg.iais.roberta.persistence.LikeProcessor;
import de.fhg.iais.roberta.persistence.ProgramProcessor;
import de.fhg.iais.roberta.persistence.UserProcessor;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.persistence.util.SessionFactoryWrapper;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.blocksequence.Location;
import de.fhg.iais.roberta.transformer.ProgramAst;
import de.fhg.iais.roberta.transformer.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.ServerProperties;
import de.fhg.iais.roberta.util.Statistics;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;
import de.fhg.iais.roberta.visitor.validate.AbstractProgramValidatorVisitor;

@Path("/program")
public class ClientProgram {
    private static final Logger LOG = LoggerFactory.getLogger(ClientProgram.class);

    private final SessionFactoryWrapper sessionFactoryWrapper;
    private final RobotCommunicator brickCommunicator;
    private final boolean isPublicServer;

    @Inject
    public ClientProgram(SessionFactoryWrapper sessionFactoryWrapper, RobotCommunicator brickCommunicator, ServerProperties serverProperties) {
        this.sessionFactoryWrapper = sessionFactoryWrapper;
        this.brickCommunicator = brickCommunicator;
        this.isPublicServer = serverProperties.getBooleanProperty("server.public");
    }

    /*
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    */
    public Response command(@OraData HttpSessionState httpSessionState, JSONObject fullRequest) throws Exception {
        Util.handleRequestInit(httpSessionState, LOG, fullRequest);
        Map<String, String> responseParameters = new HashMap<>();
        final int userId = httpSessionState.getUserId();
        final String robot =
            httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup() != ""
                ? httpSessionState.getRobotFactory(httpSessionState.getRobotName()).getGroup()
                : httpSessionState.getRobotName();
        final JSONObject response = new JSONObject();
        final DbSession dbSession = this.sessionFactoryWrapper.getSession();
        try {
            final JSONObject request = fullRequest.getJSONObject("data");
            final String cmd = request.getString("cmd");
            ClientProgram.LOG.info("command is: " + cmd + ", userId is " + userId);
            response.put("cmd", cmd);
            final ProgramProcessor pp = new ProgramProcessor(dbSession, httpSessionState);
            final AccessRightProcessor upp = new AccessRightProcessor(dbSession, httpSessionState);
            final UserProcessor up = new UserProcessor(dbSession, httpSessionState);
            final LikeProcessor lp = new LikeProcessor(dbSession, httpSessionState);
            final ConfigurationProcessor configurationProcessor = new ConfigurationProcessor(dbSession, httpSessionState);

            final IRobotFactory robotFactory = httpSessionState.getRobotFactory();

            if ( cmd.equals("saveP") || cmd.equals("saveAsP") ) {
                // this was moved to ProjectRestController -> saveProject/updateProject and split into 2 methods
            } else {
                ICompilerWorkflow compilerWorkflow = null;
                if ( cmd.equals("showSourceP") ) {
                    // this was moved to ProjectRestController -> getSourceCode
                } else if ( cmd.equals("loadP") ) {
                    // this was moved to ProjectRestController -> getProgram
                } else if ( cmd.equals("importXML") ) {
                    // this was moved to ProjectRestController -> importProgram
                } else if ( cmd.equals("shareP") ) {
                    // this was moved to ProjectRestController -> shareProgram
                } else if ( cmd.equals("shareWithGallery") ) {
                    // this was moved to ProjectRestController -> createProjectShare
                } else if ( cmd.equals("likeP") ) {
                    // this was moved to ProjectRestController -> likeProject
                } else if ( cmd.equals("shareDelete") ) {
                    // this was moved to ProjectRestController -> deleteProjectShare
                } else if ( cmd.equals("deleteP") ) {
                    // this was moved to ProjectRestController -> deleteProject
                } else if ( cmd.equals("loadPN") ) {
                    // this was moved to ProjectRestController -> getProgramNames
                } else if ( cmd.equals("loadGallery") ) {
                    // this was moved to ProjectRestController -> getGallery
                } else if ( cmd.equals("loadProgramEntity") ) {
                    // this was moved to ProjectRestController -> getProgramEntity
                } else if ( cmd.equals("loadEN") ) {
                    // this was moved to ProjectRestController -> getProgramExampleNames
                } else if ( cmd.equals("loadPR") ) {
                    // this was moved to ProjectRestController -> loadProjectRelations
                } else if ( cmd.equals("runP") ) {
                    // this was moved to ProjectRestController -> runProgram
                } else if ( cmd.equals("compileN") ) {
                    final String programName = request.getString("name");
                    final String programText = request.optString("programText");
                    final ILanguage language = Language.findByAbbr(request.optString("language"));
                    LOG.info("compilation of native source started for program {}", programName);
                    compilerWorkflow.setSourceCode(programText);
                    final String token = httpSessionState.getToken();
                    compilerWorkflow.compileSourceCode(token, programName, language, null);
                    final Key messageKey = compilerWorkflow.getWorkflowResult();
                    final String compilerResponse = compilerWorkflow.getCrosscompilerResponse();
                    LOG.info("compile user supplied native program. Result: " + messageKey);
                    if ( messageKey == Key.COMPILERWORKFLOW_SUCCESS ) {
                        Util.addSuccessInfo(response, Key.COMPILERWORKFLOW_SUCCESS);
                        Statistics.info("ProgramCompileNative", "success", true);
                    } else {
                        Util.addErrorInfo(response, messageKey, compilerResponse);
                        Statistics.info("ProgramCompileNative", "success", false);
                    }
                } else if ( cmd.equals("compileP") ) {
                    Key messageKey = null;
                    String compilerResponse = null;

                    String programName = request.getString("name");
                    final String xmlText = request.getString("program");
                    final ILanguage language = Language.findByAbbr(request.optString("language"));
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
                            final String programText = JaxbHelper.blockSet2xml(jaxbImportExport.getProgram().getBlockSet());
                            final String configText = JaxbHelper.blockSet2xml(jaxbImportExport.getConfig().getBlockSet());
                            final String token = httpSessionState.getToken();
                            final Project.Builder builder = Project.setupWithExportXML(robotFactory, programText, configText);
                            final Project programAndConfigTransformer = builder.build();
                            programAndConfigTransformer.getConfigurationAst().setRobotName(httpSessionState.getRobotName());
                            ClientProgram.LOG.info("compiler workflow started for program {}", programName);
                            compilerWorkflow.generateSourceAndCompile(token, programName, programAndConfigTransformer, language);
                            messageKey = compilerWorkflow.getWorkflowResult();
                        } else {
                            messageKey = Key.PROGRAM_IMPORT_ERROR_WRONG_ROBOT_TYPE;
                            Statistics.info("ProgramCompile", "success", false);
                        }
                        ClientProgram.LOG.info("compileP terminated with " + messageKey);
                        if ( messageKey == Key.COMPILERWORKFLOW_SUCCESS ) {
                            Util.addSuccessInfo(response, Key.COMPILERWORKFLOW_SUCCESS);
                            Statistics.info("ProgramCompile", "success", true);
                        } else {
                            compilerResponse = compilerWorkflow.getCrosscompilerResponse();
                            Util.addErrorInfo(response, messageKey, compilerResponse);
                            Statistics.info("ProgramCompile", "success", false);
                        }
                    } else {
                        messageKey = Key.PROGRAM_IMPORT_ERROR;
                        Statistics.info("ProgramCompile", "success", false);
                        Util.addErrorInfo(response, messageKey, compilerResponse);
                    }
                } else if ( cmd.equals("runPBack") ) {
                    // this was moved to ProjectRestController -> runProgram
                } else if ( cmd.equals("runPsim") ) {
                    // this was moved to ProjectRestController -> getSimulationVMCode
                } else {
                    ClientProgram.LOG.error("Invalid command: " + cmd);
                    Util.addErrorInfo(response, Key.COMMAND_INVALID);
                }
            }
            dbSession.commit();
        } catch ( final Exception e ) {
            dbSession.rollback();
            final String errorTicketId = Util1.getErrorTicketId();
            ClientProgram.LOG.error("Exception. Error ticket: " + errorTicketId, e);
            Util.addErrorInfo(response, Key.SERVER_ERROR).append("parameters", errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
        LOG.debug(response.toString());
        return Util.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
    }

    private Key programConfigurationCompatibilityCheck(JSONObject response, Project programAndConfigTransformer, AbstractProgramValidatorVisitor programChecker)
        throws JSONException,
        JAXBException {
        final ProgramAst<Void> data = programAndConfigTransformer.getProgramAst();
        //this will silently ignore the fact, that there is no brick validator for this robot. C'est la vie.
        if ( programChecker == null ) {
            response.put("data", ClientProgram.jaxbToXml(ClientProgram.astToJaxb(data)));
            return null;
        }
        programChecker.check(programAndConfigTransformer.getProgramAst().getTree());
        final int errorCounter = programChecker.getErrorCount();
        response.put("data", ClientProgram.jaxbToXml(ClientProgram.astToJaxb(data)));
        response.put("errorCounter", errorCounter);
        if ( errorCounter > 0 ) {
            return Key.PROGRAM_INVALID_STATEMETNS;
        }
        return null;
    }

    private static String jaxbToXml(BlockSet blockSet) throws JAXBException {
        final JAXBContext jaxbContext = JAXBContext.newInstance(BlockSet.class);
        final Marshaller m = jaxbContext.createMarshaller();
        m.setProperty(Marshaller.JAXB_FRAGMENT, true);
        final StringWriter writer = new StringWriter();
        m.marshal(blockSet, writer);
        return writer.toString();
    }

    private static BlockSet astToJaxb(ProgramAst<Void> data) {
        ArrayList<ArrayList<Phrase<Void>>> astProgram = data.getTree();
        final BlockSet blockSet = new BlockSet();
        blockSet.setDescription(data.getDescription());
        blockSet.setRobottype(data.getRobotType());
        blockSet.setTags(data.getTags());
        blockSet.setXmlversion(data.getXmlVersion());

        for ( final ArrayList<Phrase<Void>> tree : astProgram ) {
            final Instance instance = new Instance();
            blockSet.getInstance().add(instance);
            for ( final Phrase<Void> phrase : tree ) {
                if ( phrase.getKind().hasName("LOCATION") ) {
                    instance.setX(((Location<Void>) phrase).getX());
                    instance.setY(((Location<Void>) phrase).getY());
                }
                instance.getBlock().add(phrase.astToBlock());
            }
        }
        return blockSet;
    }

    private static void handleRunProgramError(JSONObject response, Key messageKey, String token, boolean wasRobotWaiting, String compilerResponse)
        throws JSONException {
        if ( messageKey == Key.COMPILERWORKFLOW_SUCCESS ) {
            if ( token == null ) {
                Util.addErrorInfo(response, Key.ROBOT_NOT_CONNECTED);
            } else {
                if ( wasRobotWaiting ) {
                    Util.addSuccessInfo(response, Key.ROBOT_PUSH_RUN);
                } else {
                    Util.addErrorInfo(response, Key.ROBOT_NOT_WAITING);
                }
            }
        } else {
            Util.addErrorInfo(response, messageKey, compilerResponse);
        }
    }
}

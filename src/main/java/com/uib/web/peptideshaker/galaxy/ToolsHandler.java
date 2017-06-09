package com.uib.web.peptideshaker.galaxy;

import com.compomics.util.experiment.identification.identification_parameters.SearchParameters;
import com.github.jmchilton.blend4j.galaxy.HistoriesClient;
import com.github.jmchilton.blend4j.galaxy.ToolsClient;
import com.github.jmchilton.blend4j.galaxy.WorkflowsClient;
import com.github.jmchilton.blend4j.galaxy.beans.OutputDataset;
import com.github.jmchilton.blend4j.galaxy.beans.Tool;
import com.github.jmchilton.blend4j.galaxy.beans.ToolSection;
import com.github.jmchilton.blend4j.galaxy.beans.Workflow;
import com.github.jmchilton.blend4j.galaxy.beans.WorkflowInputs;
import com.github.jmchilton.blend4j.galaxy.beans.WorkflowOutputs;
import com.github.jmchilton.blend4j.galaxy.beans.collection.request.CollectionDescription;
import com.github.jmchilton.blend4j.galaxy.beans.collection.request.HistoryDatasetElement;
import com.github.jmchilton.blend4j.galaxy.beans.collection.response.CollectionResponse;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * This class responsible for interaction with tools on Galaxy server
 *
 * @author Yehia Farag
 */
public class ToolsHandler {

    private boolean validToolsAvailable;
    /**
     * The main galaxy Work-Flow Client on galaxy server.
     */
    private final WorkflowsClient galaxyWorkFlowClient;
    /**
     * The main galaxy Work-Flow Client on galaxy server.
     */
    private final HistoriesClient galaxyHistoriesClient;
    /**
     * The main galaxy Work-Flow Client on galaxy server.
     */
    private final ToolsClient galaxyToolClient;
    private String search_GUI_Tool_Id = null;
    private String peptideShaker_Tool_Id = null;

    // , 
    /**
     * Constructor to initialize the main data structure and other variables.
     *
     * @param Galaxy_Instance the main Galaxy instance in the system
     *
     */
    public ToolsHandler(ToolsClient galaxyToolClient, WorkflowsClient galaxyWorkFlowClient, HistoriesClient galaxyHistoriesClient) {

        this.galaxyWorkFlowClient = galaxyWorkFlowClient;
        this.galaxyHistoriesClient = galaxyHistoriesClient;
        this.galaxyToolClient = galaxyToolClient;
        /**
         * The SearchGUI tool on galaxy server.
         */
//        String galaxySearchGUIToolId = null;
        /**
         * The PeptideShaker tool on galaxy server.
         */
//        String galaxyPeptideShakerToolId = null;
        try {

            List<ToolSection> toolSections = galaxyToolClient.getTools();
            String PSVersion = "0.0.0";
            String SGVersion = "0.0.0";
            for (ToolSection secion : toolSections) {
                List<Tool> tools = secion.getElems();
                if (tools != null && !validToolsAvailable) {
                    for (Tool tool : tools) {
                        if (tool.getId().contains("toolshed.g2.bx.psu.edu/repos/galaxyp/peptideshaker/search_gui/")) {
                            String version = tool.getId().split("toolshed.g2.bx.psu.edu/repos/galaxyp/peptideshaker/search_gui/")[1];
                            if (isFirmwareNewer(version, SGVersion)) {
                                SGVersion = version;
                                search_GUI_Tool_Id = tool.getId();

                            }
                        } else if (tool.getId().contains("toolshed.g2.bx.psu.edu/repos/galaxyp/peptideshaker/peptide_shaker/")) {
                            String version = tool.getId().split("toolshed.g2.bx.psu.edu/repos/galaxyp/peptideshaker/peptide_shaker/")[1];
                            if (isFirmwareNewer(version, PSVersion)) {
                                PSVersion = version;
                                peptideShaker_Tool_Id = tool.getId();

                            }
                        }

                    }
                }
                if (peptideShaker_Tool_Id != null && search_GUI_Tool_Id != null) {
                    validToolsAvailable = true;
                    break;
                }

            }
        } catch (Exception e) {
            if (e.toString().contains("Service Temporarily Unavailable")) {
                Notification.show("Service Temporarily Unavailable", Notification.Type.ERROR_MESSAGE);
                UI.getCurrent().getSession().close();
                VaadinSession.getCurrent().getSession().invalidate();

            } else {
                System.out.println("at tools are not available");
//                UI.getCurrent().getSession().close();
//                VaadinSession.getCurrent().getSession().invalidate();
//                Page.getCurrent().reload();
            }
        }
    }

    public boolean isValidTools() {
        if (!validToolsAvailable) {
            Notification.show("PeptideShaker tools are not available on this Galaxy Server", Notification.Type.WARNING_MESSAGE);
        }
        return validToolsAvailable;
    }

    /**
     * Re-Index the files (FASTA or MGF files)
     *
     * @param id file id on galaxy server
     * @param historyId the history id that the file belong to
     * @param workHistoryId the history id that the new re-indexed file will be
     * stored in
     *
     * @return new re-indexed file id on galaxy
     *
     */
    public String reIndexFile(String id, String historyId, String workHistoryId) {

        String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
        File file = new File(basepath + "/VAADIN/Galaxy-Workflow-convertMGF.ga");
        String json = readWorkflowFile(file).replace("updated_MGF", id);
        Workflow selectedWf = galaxyWorkFlowClient.importWorkflow(json);

        try {
            WorkflowInputs workflowInputs = new WorkflowInputs();
            workflowInputs.setWorkflowId(selectedWf.getId());
            workflowInputs.setDestination(new WorkflowInputs.ExistingHistory(workHistoryId));
            WorkflowInputs.WorkflowInput input = new WorkflowInputs.WorkflowInput(id, WorkflowInputs.InputSourceType.HDA);
            workflowInputs.setInput("0", input);
            final WorkflowOutputs output = galaxyWorkFlowClient.runWorkflow(workflowInputs);
            galaxyWorkFlowClient.deleteWorkflowRequest(selectedWf.getId());
            return output.getOutputIds().get(0);
        } catch (Exception e) {
            galaxyWorkFlowClient.deleteWorkflowRequest(selectedWf.getId());
        }
        return null;

    }

    /**
     * Save search settings file into galaxy
     *
     * @param fileId search parameters file name
     * @param searchParameters searchParameters .par file
     */
    public Map<String, GalaxyFile> saveSearchGUIParameters(String galaxyURL, File userFolder, Map<String, GalaxyFile> searchSetiingsFilesMap, String workHistoryId, SearchParameters searchParameters, boolean editMode) {

        String fileName = searchParameters.getFastaFile().getName().split("__")[1] + ".par";
        String fileId;
        if (editMode) {
            fileId = searchParameters.getFastaFile().getName().split("__")[3];
            searchSetiingsFilesMap.remove(fileId);
            //delete the file and make new one 
            String userAPIKey = VaadinSession.getCurrent().getSession().getAttribute("ApiKey") + "";
            String cookies = VaadinSession.getCurrent().getSession().getAttribute("cookies") + "";
            this.deleteDataset(galaxyURL, workHistoryId, fileId, userAPIKey, cookies);

        } else {
            fileId = fileName;
        }

        File file = new File(userFolder, fileId);

        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            SearchParameters.saveIdentificationParameters(searchParameters, file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        final ToolsClient.FileUploadRequest request = new ToolsClient.FileUploadRequest(workHistoryId, file);
        request.setDatasetName(fileName);
        List<OutputDataset> excList = galaxyToolClient.upload(request).getOutputs();
        if (excList != null && !excList.isEmpty()) {
            OutputDataset oDs = excList.get(0);
            DataSet ds = new DataSet();
            ds.setName(oDs.getName());
            ds.setHistoryId(workHistoryId);
            ds.setGalaxyId(oDs.getId());
            ds.setDownloadUrl(galaxyURL + "/datasets/" + ds.getGalaxyId() + "/display");
            GalaxyFile userFolderfile = new GalaxyFile(userFolder, ds);
            searchSetiingsFilesMap.put(ds.getGalaxyId(), userFolderfile);
            File updated = new File(userFolder, ds.getGalaxyId());
            try {
                updated.createNewFile();
                FileUtils.copyFile(file, updated);
                file.delete();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return searchSetiingsFilesMap;
    }

    /**
     * Read and convert the work-flow file into string (JSON like string) so the
     * system can execute the work-flow
     *
     * @param file the input file
     * @return the JSON string of the file content
     */
    private String readWorkflowFile(File file) {
        String json = "";
        String line;

        try {
            FileReader fileReader = new FileReader(file);
            try ( // Always wrap FileReader in BufferedReader.
                    BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                while ((line = bufferedReader.readLine()) != null) {
                    json += (line);
                }
                // Always close files.
            }
        } catch (FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" + "'");
        } catch (IOException ex) {
            System.out.println("Error reading file '" + "'");
        }
        return json;
    }

    private void deleteDataset(String galaxyURL, String historyId, String dsId, String userAPIKey, String cookiesRequestProperty) {
        try {

            URL url = new URL(galaxyURL + "/api/histories/" + historyId + "/contents/datasets/" + dsId + "?key=" + userAPIKey);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.addRequestProperty("Cookie", cookiesRequestProperty);
            conn.addRequestProperty("Accept-Encoding", "gzip, deflate, sdch, br");
            conn.addRequestProperty("Accept-Language", "ar,en-US;q=0.8,en;q=0.6,en-GB;q=0.4");
            conn.addRequestProperty("Cache-Control", "no-cache");
            conn.addRequestProperty("Connection", "keep-alive");
            conn.addRequestProperty("DNT", "1");
            conn.addRequestProperty("X-Requested-With", "XMLHttpRequest");
            conn.addRequestProperty("Pragma", "no-cache");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8")) {
                final ObjectMapper mapper = new ObjectMapper();
                HashMap<String, Object> payLoadParamMap = new LinkedHashMap<>();
                payLoadParamMap.put("deleted", Boolean.TRUE);
//                if (purgeSupport) {
                payLoadParamMap.put("purged", Boolean.TRUE);
//                }
                String payload = mapper.writer().writeValueAsString(payLoadParamMap);
                writer.write(payload);
            }
            conn.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                jsonString.append(line);
            }
            br.close();
            conn.disconnect();

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();

        }
    }

    /**
     * Run Online Peptide-Shaker work-flow
     *
     * @param fastaFileId FASTA file dataset id
     * @param mgfIdsList list of MGF file dataset ids
     * @param searchEnginesList List of selected search engine names
     * @param historyId galaxy history id that will store the results
     */
    public void executeWorkFlow(String projectName, String fastaFileId, Set<String> mgfIdsList, Set<String> searchEnginesList, String historyId, SearchParameters searchParameters, Map<String, Boolean> otherSearchParameters) {
        try {
            Workflow selectedWf;
            String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
            File file;
            WorkflowInputs.WorkflowInput input2;
            if (mgfIdsList.size() > 1) {
                file = new File(basepath + "/VAADIN/Galaxy-Workflow-onlinepeptideshaker_collection-updated.ga");
                input2 = prepareWorkflowCollectionList(WorkflowInputs.InputSourceType.HDCA, mgfIdsList, historyId);
            } else {
                file = new File(basepath + "/VAADIN/Galaxy-Workflow-onlinepeptideshaker-updated.ga");
                input2 = new WorkflowInputs.WorkflowInput(mgfIdsList.iterator().next(), WorkflowInputs.InputSourceType.HDA);
            }
            String json = readWorkflowFile(file);
            json = json.replace("toolshed.g2.bx.psu.edu/repos/galaxyp/peptideshaker/search_gui/3.2.11", search_GUI_Tool_Id);
            json = json.replace("toolshed.g2.bx.psu.edu/repos/galaxyp/peptideshaker/peptide_shaker/1.11.0", peptideShaker_Tool_Id);
            json = json.replace("\"create_decoy\\\\\\\": \\\\\\\"true\\\\\\\"", "\"create_decoy\\\\\\\": \\\\\\\"" + (Boolean.valueOf(searchParameters.getFastaFile().getName().split("__")[2])) + "\\\\\\\"");
            json = json.replace("SearchGUI_Label", projectName + " (SearchGUI Results)").replace("CPS_Label", projectName + " (CPS)").replace("PSM_Label", projectName + " (PSM)").replace("Proteins_Label", projectName + " (Proteins)").replace("Peptides_Label", projectName + " (Peptides)");

            selectedWf = galaxyWorkFlowClient.importWorkflow(json);

            WorkflowInputs workflowInputs = new WorkflowInputs();
            workflowInputs.setWorkflowId(selectedWf.getId());
            workflowInputs.setDestination(new WorkflowInputs.ExistingHistory(historyId));

            WorkflowInputs.WorkflowInput input = new WorkflowInputs.WorkflowInput(fastaFileId, WorkflowInputs.InputSourceType.HDA);
            workflowInputs.setInput("0", input);
            workflowInputs.setInput("1", input2);

//            workflowInputs.set(search_GUI_Tool_Id, "create_decoy", String.valueOf(Boolean.FALSE));
//            Map<String, Object> parameters = new HashMap<>();
//        parameters.put("create_decoy", String.valueOf(creatDecoyDB.getSelectedButtonValue().equalsIgnoreCase("Yes")));
//        //create gene mapping
//        parameters.put("use_gene_mapping", String.valueOf(geneMappingBtn.getSelectedButtonValue().equalsIgnoreCase("Yes")));
//        //database search enginxml data type
//        boolean selectAll = false;
//        if (DBSearchEnginsSelect.getValue().toString().equalsIgnoreCase("[]")) {
//            selectAll = true;
//        }
//
//        parameters.put("X!Tandem", String.valueOf(DBSearchEnginsSelect.isSelected("X!Tandem") || selectAll));
//        parameters.put("MSGF", String.valueOf(DBSearchEnginsSelect.isSelected("MS-GF+") || selectAll));
//        parameters.put("OMSSA", String.valueOf(DBSearchEnginsSelect.isSelected("OMSSA") || selectAll));
//        parameters.put("Comet", String.valueOf(DBSearchEnginsSelect.isSelected("Comet") || selectAll));
//            ToolParameter searchGUISearchParam = new ToolParameter(basepath, basepath)
//            workflowInputs.setToolParameter("1", input2);
            Thread t = new Thread(() -> {
                System.out.println("at run workflow thread ");
                galaxyWorkFlowClient.runWorkflow(workflowInputs);
            });
            t.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Prepares a work flow which takes as input a collection list.
     *
     * @param inputSource The type of input source for this work flow.
     * @return A WorkflowInputs describing the work flow.
     * @throws InterruptedException
     */
    private WorkflowInputs.WorkflowInput prepareWorkflowCollectionList(WorkflowInputs.InputSourceType inputSource, Set<String> dsIds, String historyId) {

        CollectionResponse collectionResponse = constructFileCollectionList(historyId, dsIds);
        return new WorkflowInputs.WorkflowInput(collectionResponse.getId(),
                inputSource);
    }

    /**
     * Constructs a list collection from the given files within the given
     * history.
     *
     * @param historyId The id of the history to build the collection within.
     * @param inputIds The IDs of the files to add to the collection.
     * @return A CollectionResponse object for the constructed collection.
     */
    private CollectionResponse constructFileCollectionList(String historyId, Set<String> inputIds) {
        CollectionDescription collectionDescription = new CollectionDescription();
        collectionDescription.setCollectionType("list");
        collectionDescription.setName("collection");
        for (String inputId : inputIds) {
            HistoryDatasetElement element = new HistoryDatasetElement();
            element.setId(inputId);
            element.setName(inputId);
            collectionDescription.addDatasetElement(element);
        }
        return galaxyHistoriesClient.createDatasetCollection(historyId, collectionDescription);
    }

    private boolean isFirmwareNewer(String testFW, String baseFW) {

        int[] testVer = getVersionNumbers(testFW);
        int[] baseVer = getVersionNumbers(baseFW);

        for (int i = 0; i < testVer.length; i++) {
            if (testVer[i] != baseVer[i]) {
                return testVer[i] > baseVer[i];
            }
        }

        return true;
    }

    private int[] getVersionNumbers(String ver) {
        Matcher m = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)(beta(\\d*))?")
                .matcher(ver);
        if (!m.matches()) {
            throw new IllegalArgumentException("Malformed FW version");
        }

        return new int[]{Integer.parseInt(m.group(1)), // major
            Integer.parseInt(m.group(2)), // minor
            Integer.parseInt(m.group(3)), // rev.
            m.group(4) == null ? Integer.MAX_VALUE // no beta suffix
            : m.group(5).isEmpty() ? 1 // "beta"
            : Integer.parseInt(m.group(5)) // "beta3"
    };
    }

}

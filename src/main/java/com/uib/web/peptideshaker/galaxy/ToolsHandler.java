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
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.FileUtils;

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
    private final String search_GUI_Tool_Id="toolshed.g2.bx.psu.edu/repos/galaxyp/peptideshaker/search_gui/3.2.11";

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
        String galaxySearchGUIToolId = null;
        /**
         * The PeptideShaker tool on galaxy server.
         */
        String galaxyPeptideShakerToolId = null;
        try {

            List<ToolSection> toolSections = galaxyToolClient.getTools();
            for (ToolSection secion : toolSections) {
                List<Tool> tools = secion.getElems();
                if (tools != null && !validToolsAvailable) {
                    for (Tool tool : tools) {
                        if (tool.getId().equalsIgnoreCase("toolshed.g2.bx.psu.edu/repos/galaxyp/peptideshaker/peptide_shaker/1.16.3")) {
                            galaxyPeptideShakerToolId = tool.getId();
                            System.out.println("at tool " + tool.getId());
                        } else if (tool.getId().equalsIgnoreCase(search_GUI_Tool_Id)) {
                            galaxySearchGUIToolId = tool.getId();
                            System.out.println("at tool " + tool.getId());
                        }
                        if (galaxyPeptideShakerToolId != null && galaxySearchGUIToolId != null) {
                            validToolsAvailable = true;
                            break;
                        }
                    }
                }
                if (validToolsAvailable) {
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
                UI.getCurrent().getSession().close();
                VaadinSession.getCurrent().getSession().invalidate();
                Page.getCurrent().reload();
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
    public Map<String, GalaxyFile> saveSearchGUIParameters(String galaxyURL, File userFolder, Map<String, GalaxyFile> searchSetiingsFilesMap, String workHistoryId, SearchParameters searchParameters, String fileId) {
        SimpleDateFormat sdfDate = new SimpleDateFormat("MMM dd yyyy HH:mm");
        String info = sdfDate.format(Page.getCurrent().getWebBrowser().getCurrentDate());
        String fileName = "";
        if (searchSetiingsFilesMap.keySet().contains(fileId)) {
            String[] nameArr = searchSetiingsFilesMap.get(fileId).getDataset().getName().split(" - Search settings ");
            fileName = nameArr[0] + " - Search settings ( " + info + " ).par";
        } else {
            fileName = fileId.toUpperCase() + " - Search settings ( " + info + " ).par";
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

    /**
     * Run Online Peptide-Shaker work-flow
     *
     * @param fastaFileId FASTA file dataset id
     * @param mgfIdsList list of MGF file dataset ids
     * @param searchEnginesList List of selected search engine names
     * @param historyId galaxy history id that will store the results
     */
    public void executeWorkFlow(String fastaFileId, Set<String> mgfIdsList, Set<String> searchEnginesList, String historyId, SearchParameters searchParameters, Map<String,Boolean>otherSearchParameters) {
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
            
           
            json = json.replace("\"create_decoy\\\\\\\": \\\\\\\"true\\\\\\\"", "\"create_decoy\\\\\\\": \\\\\\\""+Boolean.FALSE+"\\\\\\\"");
            
            
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

}

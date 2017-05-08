package com.uib.web.peptideshaker.galaxy;

import com.github.jmchilton.blend4j.galaxy.GalaxyInstance;
import com.github.jmchilton.blend4j.galaxy.HistoriesClient;
import com.github.jmchilton.blend4j.galaxy.beans.Dataset;
import com.github.jmchilton.blend4j.galaxy.beans.Tool;
import com.github.jmchilton.blend4j.galaxy.beans.ToolSection;
import com.github.jmchilton.blend4j.galaxy.beans.Workflow;
import com.github.jmchilton.blend4j.galaxy.beans.WorkflowInputs;
import com.github.jmchilton.blend4j.galaxy.beans.WorkflowOutputs;
import com.github.jmchilton.blend4j.galaxy.beans.collection.request.CollectionDescription;
import com.github.jmchilton.blend4j.galaxy.beans.collection.request.HistoryDatasetElement;
import com.github.jmchilton.blend4j.galaxy.beans.collection.response.CollectionResponse;
import com.vaadin.server.VaadinService;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * This class responsible for interaction with tools on Galaxy server
 *
 * @author Yehia Farag
 */
public class ToolsHandler {

    /**
     * The main Galaxy instance in the system.
     */
    private final GalaxyInstance Galaxy_Instance;
    /**
     * The SearchGUI tool on galaxy server.
     */
    private String galaxySearchGUIToolId = null;
    /**
     * The PeptideShaker tool on galaxy server.
     */
    private String galaxyPeptideShakerToolId = null;

    /**
     * Constructor to initialize the main data structure and other variables.
     *
     * @param Galaxy_Instance the main Galaxy instance in the system
     *
     */
    public ToolsHandler(GalaxyInstance Galaxy_Instance) {
        this.Galaxy_Instance = Galaxy_Instance;
        try{
        if (Galaxy_Instance.getToolsClient().showTool("toolshed.g2.bx.psu.edu%2Frepos%2Fgalaxyp%2Fpeptideshaker%2Fpeptide_shaker%2F1.11.0") != null) {
            galaxyPeptideShakerToolId = "toolshed.g2.bx.psu.edu%2Frepos%2Fgalaxyp%2Fpeptideshaker%2Fpeptide_shaker%2F1.11.0";
        }
        if (Galaxy_Instance.getToolsClient().showTool("toolshed.g2.bx.psu.edu%2Frepos%2Fgalaxyp%2Fpeptideshaker%2Fsearch_gui%2F2.9.0") != null) {
            galaxySearchGUIToolId = "toolshed.g2.bx.psu.edu%2Frepos%2Fgalaxyp%2Fpeptideshaker%2Fsearch_gui%2F2.9.0";
        }
        }catch(Exception e){
            System.out.println("at tools are not available");
        }
    }
    
    public boolean isValidTools(){
        return ((galaxyPeptideShakerToolId!=null)&& (galaxySearchGUIToolId!=null));
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
        Workflow selectedWf = Galaxy_Instance.getWorkflowsClient().importWorkflow(json);
        try {
            WorkflowInputs workflowInputs = new WorkflowInputs();
            workflowInputs.setWorkflowId(selectedWf.getId());
            workflowInputs.setDestination(new WorkflowInputs.ExistingHistory(workHistoryId));
            WorkflowInputs.WorkflowInput input = new WorkflowInputs.WorkflowInput(id, WorkflowInputs.InputSourceType.HDA);
            workflowInputs.setInput("0", input);
            final WorkflowOutputs output = Galaxy_Instance.getWorkflowsClient().runWorkflow(workflowInputs);
            Galaxy_Instance.getWorkflowsClient().deleteWorkflowRequest(selectedWf.getId());
            return output.getOutputIds().get(0);
        } catch (Exception e) {
            Galaxy_Instance.getWorkflowsClient().deleteWorkflowRequest(selectedWf.getId());
        }
        return null;

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
    public void executeWorkFlow(String fastaFileId, Set<String> mgfIdsList, Set<String> searchEnginesList, String historyId) {
        Workflow selectedWf;
        String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
        File file;
        WorkflowInputs.WorkflowInput input2;
        if (mgfIdsList.size() > 1) {
            file = new File(basepath + "/VAADIN/Galaxy-Workflow-onlinepeptideshaker_collection.ga");
            input2 = prepareWorkflowCollectionList(WorkflowInputs.InputSourceType.HDCA, mgfIdsList, historyId);
        } else {
            file = new File(basepath + "/VAADIN/Galaxy-Workflow-onlinepeptideshaker.ga");
            input2 = new WorkflowInputs.WorkflowInput(mgfIdsList.iterator().next(), WorkflowInputs.InputSourceType.HDA);
        }
        String json = readWorkflowFile(file);
        selectedWf = Galaxy_Instance.getWorkflowsClient().importWorkflow(json);

        WorkflowInputs workflowInputs = new WorkflowInputs();
        workflowInputs.setWorkflowId(selectedWf.getId());
        workflowInputs.setDestination(new WorkflowInputs.ExistingHistory(historyId));

        WorkflowInputs.WorkflowInput input = new WorkflowInputs.WorkflowInput(fastaFileId, WorkflowInputs.InputSourceType.HDA);

        workflowInputs.setInput("0", input);
        workflowInputs.setInput("1", input2);
//        workflowInputs.setToolParameter(galaxyPeptideShakerToolId, new ToolParameter("outputs", "cps"));
        final WorkflowOutputs output = Galaxy_Instance.getWorkflowsClient().runWorkflow(workflowInputs);
        Galaxy_Instance.getWorkflowsClient().deleteWorkflowRequest(selectedWf.getId());

        List<Dataset> newDss = new ArrayList<>();
        for (String oDs : output.getOutputIds()) {
            newDss.add(Galaxy_Instance.getHistoriesClient().showDataset(historyId, oDs));
        }
//        updateGalaxyHistory(newDss);
//        checkHistory();

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
        HistoriesClient historiesClient = Galaxy_Instance.getHistoriesClient();
        CollectionDescription collectionDescription = new CollectionDescription();
        collectionDescription.setCollectionType("list");
        collectionDescription.setName("collection");
        for (String inputId : inputIds) {
            HistoryDatasetElement element = new HistoryDatasetElement();
            element.setId(inputId);
            element.setName(inputId);

            collectionDescription.addDatasetElement(element);
        }

        return historiesClient.createDatasetCollection(historyId, collectionDescription);
    }

}

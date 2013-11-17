package org.knime.knip.imagej2.interactive.plugins;

/*
 * To the extent possible under law, the ImageJ developers have waived
 * all copyright and related or neighboring rights to this tutorial code.
 *
 * See the CC0 1.0 Universal license for details:
 *     http://creativecommons.org/publicdomain/zero/1.0/
 */

import imagej.command.Command;
import imagej.command.CommandInfo;

import java.io.File;
import java.util.List;

import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeModel;
import org.knime.core.node.workflow.NodeID;
import org.knime.core.node.workflow.WorkflowLoadHelper;
import org.knime.core.node.workflow.WorkflowManager;
import org.knime.core.node.workflow.WorkflowPersistor.WorkflowLoadResult;
import org.knime.knip.imagej2.core.node.IJNodeSetFactory;
import org.knime.knip.io.nodes.imgreader.ImgReaderNodeFactory;
import org.knime.workbench.repository.model.DynamicNodeTemplate;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Tutorial
 */
@Plugin(type = Command.class, headless = false, menuPath = "KNIME>RecordSome", name = "KNIME Workflow Recorder")
public class RecordKNIMEWorkflow implements Command {

    private String path = "\\outputFlow";

    @Parameter
    private KNIMERecorder recorder;

    @Override
    public void run() {
        try {
            List<CommandInfo> moduleInfos = recorder.moduleInfos();
            File emptyFlow = new File("TestFlow");
            File output = new File(path);

            final WorkflowLoadResult result =
                    WorkflowManager.loadProject(emptyFlow, new ExecutionMonitor(), new WorkflowLoadHelper(emptyFlow));

            IJNodeSetFactory nodeSet = new IJNodeSetFactory();

            NodeID sourceId = result.getWorkflowManager().addNode(new ImgReaderNodeFactory());

            for (int i = 0; i < moduleInfos.size(); i++) {

                //                String factoryId = "imagej.core.commands.app.EasterEgg";
                String factoryId = moduleInfos.get(i).getClassName();

                NodeFactory<? extends NodeModel> fac =
                        DynamicNodeTemplate
                                .createFactoryInstance(nodeSet.getNodeFactory(factoryId), nodeSet, factoryId);

                DynamicNodeTemplate dynamicNodeTemplate =
                        new DynamicNodeTemplate(IJNodeSetFactory.class.getSimpleName(), factoryId, nodeSet, moduleInfos
                                .get(i).getName());
                dynamicNodeTemplate.setFactory((Class<? extends NodeFactory<? extends NodeModel>>)fac.getClass());

                NodeID dest = result.getWorkflowManager().addNode(dynamicNodeTemplate.createFactoryInstance());

                result.getWorkflowManager().addConnection(sourceId, 1, dest, 1);
                sourceId = dest;
            }
            result.getWorkflowManager().save(output, new ExecutionMonitor(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package org.knime.knip.imagej2.headless.nodes.writer;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * very simple memory reader factory
 * 
 * @author dietzc
 * 
 */
public class IJMemoryWriterNodeFactory extends NodeFactory<IJMemoryWriterNodeModel> {

        @Override
        public IJMemoryWriterNodeModel createNodeModel() {
                return new IJMemoryWriterNodeModel();
        }

        @Override
        protected int getNrNodeViews() {
                return 0;
        }

        @Override
        public NodeView<IJMemoryWriterNodeModel> createNodeView(int viewIndex, IJMemoryWriterNodeModel nodeModel) {
                return null;
        }

        @Override
        protected boolean hasDialog() {
                return false;
        }

        @Override
        protected NodeDialogPane createNodeDialogPane() {
                return null;
        }

}

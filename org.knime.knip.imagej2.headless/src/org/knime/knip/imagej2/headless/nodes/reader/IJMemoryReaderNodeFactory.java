package org.knime.knip.imagej2.headless.nodes.reader;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * very simple memory reader factory
 * 
 * @author dietzc
 * 
 */
public class IJMemoryReaderNodeFactory extends NodeFactory<IJMemoryReaderNodeModel> {

        @Override
        public IJMemoryReaderNodeModel createNodeModel() {
                return new IJMemoryReaderNodeModel();
        }

        @Override
        protected int getNrNodeViews() {
                return 0;
        }

        @Override
        public NodeView<IJMemoryReaderNodeModel> createNodeView(int viewIndex, IJMemoryReaderNodeModel nodeModel) {
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

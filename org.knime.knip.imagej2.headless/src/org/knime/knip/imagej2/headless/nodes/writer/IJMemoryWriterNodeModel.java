package org.knime.knip.imagej2.headless.nodes.writer;

import java.io.File;
import java.io.IOException;

import net.imglib2.meta.ImgPlus;

import org.knime.core.data.DataTableSpec;
import org.knime.core.data.container.CloseableRowIterator;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.knip.base.data.img.ImgPlusValue;

public class IJMemoryWriterNodeModel extends NodeModel {

        public static ImgPlus remoteImgPlus = null;

        protected IJMemoryWriterNodeModel() {
                super(1, 0);
        }

        @Override
        protected BufferedDataTable[] execute(BufferedDataTable[] inData, ExecutionContext exec) throws Exception {
                // Nothing goes in

                CloseableRowIterator iterator = inData[0].iterator();
                remoteImgPlus = ((ImgPlusValue) iterator.next().getCell(0)).getImgPlus();

                return null;
        }

        @Override
        protected DataTableSpec[] configure(DataTableSpec[] inSpecs) throws InvalidSettingsException {
                return spec();
        }

        private DataTableSpec[] spec() {
                return null;
        }

        @Override
        protected void loadInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException, CanceledExecutionException {
                // Nothing to do here
        }

        @Override
        protected void saveInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException, CanceledExecutionException {
                // TODO Auto-generated method stub

        }

        @Override
        protected void saveSettingsTo(NodeSettingsWO settings) {
                // TODO Auto-generated method stub

        }

        @Override
        protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
                // TODO Auto-generated method stub

        }

        @Override
        protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
                // TODO Auto-generated method stub

        }

        @Override
        protected void reset() {
                remoteImgPlus = null;
        }

}

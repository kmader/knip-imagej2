package org.knime.knip.imagej2.headless.nodes.reader;

import java.io.File;
import java.io.IOException;

import net.imglib2.meta.ImgPlus;

import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.knip.base.data.img.ImgPlusCell;
import org.knime.knip.base.data.img.ImgPlusCellFactory;

public class IJMemoryReaderNodeModel extends NodeModel {

        public static ImgPlus remoteImgPlus = null;

        protected IJMemoryReaderNodeModel() {
                super(0, 1);
        }

        @Override
        protected BufferedDataTable[] execute(BufferedDataTable[] inData, ExecutionContext exec) throws Exception {
                // Nothing goes in

                System.out.println("hello");
                ImgPlusCellFactory imgPlusCellFactory = new ImgPlusCellFactory(exec);

                BufferedDataContainer container = exec.createDataContainer(spec()[0]);
                container.addRowToTable(new DefaultRow(new RowKey("memory_test"), imgPlusCellFactory.createCell(remoteImgPlus)));
                container.close();

                return new BufferedDataTable[] {container.getTable()};
        }

        @Override
        protected DataTableSpec[] configure(DataTableSpec[] inSpecs) throws InvalidSettingsException {
                return spec();
        }

        private DataTableSpec[] spec() {
                return new DataTableSpec[] {new DataTableSpec(DataTableSpec.createColumnSpecs(new String[] {"MEMORY"},
                                new DataType[] {ImgPlusCell.TYPE}))};
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

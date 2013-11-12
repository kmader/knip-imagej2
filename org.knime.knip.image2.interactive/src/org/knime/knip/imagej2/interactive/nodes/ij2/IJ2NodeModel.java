/*
 * ------------------------------------------------------------------------
 *
 *  Copyright (C) 2003 - 2013
 *  University of Konstanz, Germany and
 *  KNIME GmbH, Konstanz, Germany
 *  Website: http://www.knime.org; Email: contact@knime.org
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License, Version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 *  Additional permission under GNU GPL version 3 section 7:
 *
 *  KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs.
 *  Hence, KNIME and ECLIPSE are both independent programs and are not
 *  derived from each other. Should, however, the interpretation of the
 *  GNU GPL Version 3 ("License") under any applicable laws result in
 *  KNIME and ECLIPSE being a combined program, KNIME GMBH herewith grants
 *  you the additional permission to use and propagate KNIME together with
 *  ECLIPSE with only the license terms in place for ECLIPSE applying to
 *  ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the
 *  license terms of ECLIPSE themselves allow for the respective use and
 *  propagation of ECLIPSE together with KNIME.
 *
 *  Additional permission relating to nodes for KNIME that extend the Node
 *  Extension (and in particular that are based on subclasses of NodeModel,
 *  NodeDialog, and NodeView) and that only interoperate with KNIME through
 *  standard APIs ("Nodes"):
 *  Nodes are deemed to be separate and independent programs and to not be
 *  covered works.  Notwithstanding anything to the contrary in the
 *  License, the License does not apply to Nodes, you are not required to
 *  license Nodes under the License, and you are granted a license to
 *  prepare and propagate Nodes, in each case even if such Nodes are
 *  propagated with or for interoperation with KNIME.  The owner of a Node
 *  may freely choose the license terms applicable to such Node, including
 *  when such Node is propagated with or for interoperation with KNIME.
 * --------------------------------------------------------------------- *
 *
 */
package org.knime.knip.imagej2.interactive.nodes.ij2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.imglib2.meta.ImgPlus;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.interactive.InteractiveNode;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.knip.base.data.img.ImgPlusCell;
import org.knime.knip.base.data.img.ImgPlusCellFactory;
import org.knime.knip.base.data.img.ImgPlusValue;
import org.knime.knip.base.node.ValueToCellNodeModel;

/**
 * TODO Auto-generated
 *
 * @author <a href="mailto:dietzc85@googlemail.com">Christian Dietz</a>
 * @author <a href="mailto:horn_martin@gmx.de">Martin Horn</a>
 * @author <a href="mailto:michael.zinsmaier@googlemail.com">Michael Zinsmaier</a>
 */
public class IJ2NodeModel<T extends RealType<T> & NativeType<T>> extends
        ValueToCellNodeModel<ImgPlusValue<T>, ImgPlusCell<T>> implements InteractiveNode<IJ2ViewContent> {

    private ImgPlusCellFactory m_imgPlusCellFactory;

    private DataRow m_currentRow;

    private DataTableSpec m_inSpec;

    private int m_storedIdx;

    private ArrayList<DataRow> m_allRows;

    private Map<RowKey, ImgPlus<? extends RealType>> m_resMap;

    private BufferedDataTable m_inData;

    @Override
    protected void addSettingsModels(final List<SettingsModel> settingsModels) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs) throws InvalidSettingsException {
        m_inSpec = (DataTableSpec)inSpecs[0];
        return super.configure(inSpecs);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PortObject[] execute(final PortObject[] inObjects, final ExecutionContext exec) throws Exception {
        m_inData = (BufferedDataTable)inObjects[0];

        return super.execute(inObjects, exec);
    }

    @Override
    protected void prepareExecute(final ExecutionContext exec) {
        m_imgPlusCellFactory = new ImgPlusCellFactory(exec);
        m_allRows = new ArrayList<DataRow>();
    }

    @Override
    protected void computeDataRow(final DataRow row) {
        m_currentRow = row;
        m_allRows.add(row);

        if (m_storedIdx != -1 && m_storedIdx != m_currentCellIdx) {
            throw new IllegalArgumentException("Only one column can be selected since now");
        } else {
            m_storedIdx = m_currentCellIdx;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ImgPlusCell<T> compute(final ImgPlusValue<T> cellValue) throws Exception {
        // hier: schnappe dir (je nach input = currentRow) den output.

        String rowName = m_currentRow.getKey().getString();
        String colName = m_inSpec.getColumnNames()[m_currentCellIdx];

        ImgPlus<T> res = null;
        if (m_resMap != null) {
            res = (ImgPlus<T>)m_resMap.get(m_currentRow.getKey());
        }

        if (res == null)
            res = cellValue.getImgPlus();

        return m_imgPlusCellFactory.createCell(res);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IJ2ViewContent createViewContent() {
        return new IJ2ViewContent(m_inData, m_storedIdx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadViewContent(final IJ2ViewContent viewContent) {
        // we don't need to care whether something changed, as the expensive processing happens on imagej2 side anyway
        m_resMap = viewContent.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInternalTables(final BufferedDataTable[] tables) {
        m_inData = tables[1];
        super.setInternalTables(tables);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BufferedDataTable[] getInternalTables() {
        return new BufferedDataTable[]{m_data, m_inData};
    }

}
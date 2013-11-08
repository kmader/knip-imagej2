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
package org.knime.knip.imagej2.interactive.nodes.ijinteractive;

import java.util.List;

import net.imglib2.meta.ImgPlus;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTableHolder;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.knip.base.data.img.ImgPlusCell;
import org.knime.knip.base.data.img.ImgPlusCellFactory;
import org.knime.knip.base.data.img.ImgPlusValue;
import org.knime.knip.base.node.ValueToCellNodeModel;
import org.knime.knip.core.types.ImgFactoryTypes;
import org.knime.knip.core.types.NativeTypes;

/**
 * TODO Auto-generated
 *
 * @author <a href="mailto:dietzc85@googlemail.com">Christian Dietz</a>
 * @author <a href="mailto:horn_martin@gmx.de">Martin Horn</a>
 * @author <a href="mailto:michael.zinsmaier@googlemail.com">Michael Zinsmaier</a>
 */
public class InteractiveIIJ2NodeModel<T extends RealType<T> & NativeType<T>> extends
        ValueToCellNodeModel<ImgPlusValue<T>, ImgPlusCell<T>> implements BufferedDataTableHolder {

    static SettingsModelBoolean createWithSegmentidSM() {
        return new SettingsModelBoolean("add_segment_id", true);
    }

    static SettingsModelString creatFactoryTypeSM() {
        return new SettingsModelString("factory_type", ImgFactoryTypes.NTREE_IMG_FACTORY.toString());
    }

    static SettingsModelString createLabelingTypeSM() {
        return new SettingsModelString("labeling_type", NativeTypes.SHORTTYPE.toString());
    }

    private final SettingsModelBoolean m_addSegmentID = createWithSegmentidSM();

    private final SettingsModelString m_factoryType = creatFactoryTypeSM();

    private final SettingsModelString m_labelingType = createLabelingTypeSM();

    private ImgPlusCellFactory m_imgPlusCellFactory;

    private DataRow m_currentRow;

    private DataTableSpec m_inSpec;

    @Override
    protected void addSettingsModels(final List<SettingsModel> settingsModels) {
        settingsModels.add(m_addSegmentID);
        settingsModels.add(m_factoryType);
        settingsModels.add(m_labelingType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs) throws InvalidSettingsException {
        m_inSpec = (DataTableSpec)inSpecs[0];

        return super.configure(inSpecs);
    }

    @Override
    protected void prepareExecute(final ExecutionContext exec) {
        m_imgPlusCellFactory = new ImgPlusCellFactory(exec);
    }

    @Override
    protected void computeDataRow(final DataRow row) {
        m_currentRow = row;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ImgPlusCell<T> compute(final ImgPlusValue<T> cellValue) throws Exception {
        // hier: schnappe dir (je nach input = currentRow) den output.
        ImgPlus<T> res = null;

        return m_imgPlusCellFactory.createCell(res);
    }

}
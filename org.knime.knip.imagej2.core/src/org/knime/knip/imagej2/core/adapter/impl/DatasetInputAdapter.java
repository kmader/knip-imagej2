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
package org.knime.knip.imagej2.core.adapter.impl;

import net.imagej.Dataset;
import net.imagej.DefaultDataset;
import net.imglib2.meta.ImgPlus;

import org.knime.core.data.DataValue;
import org.knime.knip.base.data.img.ImgPlusValue;
import org.knime.knip.imagej2.core.IJGateway;
import org.knime.knip.imagej2.core.adapter.DataValueConfigGuiInfos;
import org.knime.knip.imagej2.core.adapter.IJStandardInputAdapter;
import org.knime.knip.imagej2.core.adapter.ModuleItemDataValueConfig;
import org.scijava.ItemIO;
import org.scijava.module.Module;
import org.scijava.module.ModuleItem;

/**
 * TODO Auto-generated
 *
 * @author <a href="mailto:dietzc85@googlemail.com">Christian Dietz</a>
 * @author <a href="mailto:horn_martin@gmx.de">Martin Horn</a>
 * @author <a href="mailto:michael.zinsmaier@googlemail.com">Michael Zinsmaier</a>
 */
public class DatasetInputAdapter implements IJStandardInputAdapter<Dataset> {

    @Override
    public Class<Dataset> getIJType() {
        return Dataset.class;
    }

    @Override
    public ModuleItemDataValueConfig createModuleItemConfig(final ModuleItem<Dataset> item) {
        return new ModuleItemDataValueConfig() {
            private DataValue[] m_dataValues;

            @Override
            public void resolveHandledModuleItems(final Module module, final boolean preTest) {
                if (preTest || (m_dataValues != null)) {
                    module.setResolved(item.getName(), true);
                }
            }

            @SuppressWarnings({"rawtypes", "unchecked"})
            @Override
            public void configureModuleItem(final Module module) {

                ImgPlus imgPlus;
                final DataValue dv = m_dataValues[0];

                if (item.getIOType() == ItemIO.BOTH) {
                    // make copy if item is in and output
                    imgPlus = ((ImgPlusValue)dv).getImgPlusCopy();
                } else {
                    imgPlus = ((ImgPlusValue)dv).getImgPlus();
                }

                final Dataset input = new DefaultDataset(IJGateway.getImageJContext(), imgPlus);
                module.setInput(item.getName(), input);
            }

            @Override
            public void setConfigurationData(final DataValue[] dataValues) {
                m_dataValues = dataValues;
            }

            @Override
            public DataValueConfigGuiInfos[] getGuiMetaInfo() {
                return new DataValueConfigGuiInfos[]{new DataValueConfigGuiInfos(item.getLabel(), item.getName(),
                        ImgPlusValue.class)};
            }

            @Override
            public ModuleItem<?> getItem() {
                return item;
            }
        };
    }

}

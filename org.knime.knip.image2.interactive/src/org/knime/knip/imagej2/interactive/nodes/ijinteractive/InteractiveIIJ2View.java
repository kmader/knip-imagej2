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

import imagej.data.DefaultDataset;
import imagej.data.autoscale.AutoscaleService;
import imagej.data.display.DefaultOverlayService;
import imagej.display.DisplayService;
import imagej.service.ImageJService;
import imagej.ui.UIService;
import imagej.ui.swing.overlay.JHotDrawService;
import imagej.widget.WidgetService;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.imglib2.meta.ImgPlus;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataTable;
import org.knime.core.node.tableview.TableContentModel;
import org.knime.core.node.tableview.TableContentView;
import org.knime.core.node.tableview.TableView;
import org.knime.knip.base.data.img.ImgPlusValue;
import org.knime.knip.core.ui.event.EventService;
import org.knime.knip.core.ui.imgviewer.annotator.AnnotatorResetEvent;
import org.knime.knip.core.ui.imgviewer.annotator.RowColKey;
import org.scijava.Context;

/**
 * TODO Auto-generated
 *
 * @author <a href="mailto:dietzc85@googlemail.com">Christian Dietz</a>
 * @author <a href="mailto:horn_martin@gmx.de">Martin Horn</a>
 * @author <a href="mailto:michael.zinsmaier@googlemail.com">Michael Zinsmaier</a>
 */
public class InteractiveIIJ2View<T extends RealType<T> & NativeType<T>> implements InteractiveIIJ2Dialog<T>,
        ListSelectionListener {

    private final JPanel m_mainPanel = new JPanel();

    /*
     * does not listen to events of the event service but may trigger them.
     */
    private EventService m_eventService;

    private IJResultManager<T> m_manager;

    private TableContentView m_tableContentView;

    private TableContentModel m_tableContentModel;

    private int m_currentRow = -1;

    private int m_currentCol = -1;

    private Context m_context;

    private KNIPSwingUI m_ui;

    public InteractiveIIJ2View() {
        m_context =
                new Context(ImageJService.class, AutoscaleService.class, JHotDrawService.class,
                        DefaultOverlayService.class, WidgetService.class);
        createIJ2View();
    }

    @Override
    public JPanel getPanel() {
        return m_mainPanel;
    }

    @Override
    public ImgPlus<T> getResult(final RowColKey key) {
        return m_manager.get(key);
    }

    @Override
    public void reset() {
        m_eventService.publish(new AnnotatorResetEvent());
        m_currentRow = -1;
        m_currentCol = -1;
    }

    public List<RowColKey> getRowColKeys() {
        LinkedList<RowColKey> ret = new LinkedList<RowColKey>();
        Map<RowColKey, ImgPlus<T>> map = m_manager.getMap();

        // add all none empty overlays
        for (RowColKey key : map.keySet()) {
            if (map.get(key) != null) {
                ret.add(key);
            }
        }

        return ret;
    }

    @Override
    public void setInputTable(final DataTable inputTable) {
        m_tableContentModel = new TableContentModel(inputTable);
        m_tableContentView.setModel(m_tableContentModel);
        // Scale to thumbnail size
        m_tableContentView.validate();
        m_tableContentView.repaint();
    }

    private void createIJ2View() {
        // table viewer
        m_tableContentView = new TableContentView();
        m_tableContentView.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        m_tableContentView.getSelectionModel().addListSelectionListener(this);
        m_tableContentView.getColumnModel().getSelectionModel().addListSelectionListener(this);
        TableView tableView = new TableView(m_tableContentView);

        // annotator
        m_manager = new IJResultManager<T>();

        // UIService doesn't work
        UIService service = m_context.getService(UIService.class);

        // try to get it
        m_ui = (KNIPSwingUI)service.getUI(KNIPSwingUI.NAME);
        m_ui.show();

        // split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.add(tableView);
        splitPane.add(m_ui.getApplicationFrame());
        splitPane.setDividerLocation(300);

        m_mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        m_mainPanel.add(splitPane, gbc);
    }

    @Override
    public void valueChanged(final ListSelectionEvent e) {

        if(e.getValueIsAdjusting()){
            return;
        }

        final int row = m_tableContentView.getSelectionModel().getLeadSelectionIndex();
        final int col = m_tableContentView.getColumnModel().getSelectionModel().getLeadSelectionIndex();

        System.out.println("value changed lol");

        m_currentRow = row;
        m_currentCol = col;

        try {
            final DataCell currentImgCell = m_tableContentView.getContentModel().getValueAt(m_currentRow, m_currentCol);

            ImgPlus<T> imgPlus = ((ImgPlusValue<T>)currentImgCell).getImgPlus();

            String colKey = m_tableContentModel.getColumnName(m_currentCol);
            String rowKey = m_tableContentModel.getRowKey(m_currentRow).getString();

            //            DatasetService datasetService = m_context.getService(DatasetService.class);
            //            Dataset dataset = datasetService.create(imgPlus);

            m_ui.show(new DefaultDataset(m_context, imgPlus));

            m_context.getService(DisplayService.class).setActiveDisplay(m_context.getService(DisplayService.class)
                                                                                .getDisplays().get(0));
            //            m_eventService.publish(new AnnotatorImgWithMetadataChgEvent<T>(imgPlus.getImg(), imgPlus, new RowColKey(
            //                    rowKey, colKey)));
            //            m_eventService.publish(new ImgRedrawEvent());
        } catch (final IndexOutOfBoundsException e2) {
            e2.printStackTrace();
            return;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RowColKey> getResultKeys() {
        return null;
    }

}
